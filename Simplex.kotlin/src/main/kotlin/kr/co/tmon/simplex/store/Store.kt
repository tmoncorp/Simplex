package kr.co.tmon.simplex.store

import kr.co.tmon.simplex.Simplex
import kr.co.tmon.simplex.actions.*
import kr.co.tmon.simplex.annotations.*
import kr.co.tmon.simplex.channels.*
import kr.co.tmon.simplex.extensions.*
import java.util.concurrent.ConcurrentHashMap
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*

class Store<TActionSet>(factory: () -> TActionSet) : IActionStore<TActionSet> {
	companion object {
        inline operator fun <reified TActionSet : Any> invoke() = Store {
            TActionSet::class.java.newInstance()
        }
    }

    private val actions: TActionSet = factory()
    private val observableSources: ConcurrentHashMap<String, Any> = ConcurrentHashMap()

    override fun <TAction : IAction<TResult>, TResult> dispatch(
        action: Function1<TActionSet, IActionBinder<TAction, TResult>>,
        begin: Action?,
        end: Action?,
        channel: Function1<TAction, IChannel>?) {
        dispatchInner(action, null, channel, begin, end)
	}

    override fun <TAction : IParameterizedAction<TParam, TResult>, TParam, TResult> dispatch(
        action: Function1<TActionSet, IParameterizedActionBinder<TAction, TParam, TResult>>,
        parameters: TParam,
        begin: Action?,
        end: Action?,
        channel: Function1<TAction, IChannel>?) {
        dispatchInner(action, parameters, channel, begin, end)
    }

    override fun <TAction : IAction<TResult>, TResult>subscribe(
        action: Function1<TActionSet, IActionBinder<TAction, TResult>>,
        onNext: (TResult) -> Unit,
        observeOnMainThread: Boolean,
        observable : Function1<Observable<TResult>, Observable<TResult>>?,
        channel: Function1<TAction, IChannel>?,
        preventClone: Boolean
    ) : Disposable? {
        return subscribeInner(action, onNext, observable, channel, observeOnMainThread)
    }

    private fun <TAction : IAction<TResult>, TResult> subscribeInner(
        action: Function1<TActionSet, IActionBinder<TAction, TResult>>,
        onNext: (TResult) -> Unit,
        observable: Function1<Observable<TResult>, Observable<TResult>>? = null,
        channel: Function1<TAction, IChannel>? = null,
        observeOnMainThread: Boolean = false) : Disposable {

        val binder = action.invoke(actions)
        //채널이 생성되지 않았으면 기본 Channel클래스로 생성
        createChannelIfNull(binder.action)
        //채널 선택
        val ch = channel?.invoke(binder.action) ?: getDefaultChannel(binder.action)
        //액션에 해당하는 source 선택
        val source = this.getOrAddObservableSource(binder.action)
            .filter { it.first.contains(ch) }
            .unwrap()

        var o = observable?.invoke(source) ?: source
        if (observeOnMainThread)
            o = o.observeOn(Simplex.MainThreadScheduler)

        return o.subscribe{ onNext(it) }
    }

    private fun <TAction : IAction<TResult>, TParam, TResult> dispatchInner(
        action: Function1<TActionSet, IActionBinder<TAction, TResult>>,
        param: TParam,
        channel: Function1<TAction, IChannel>?  = null,
        begin: Action? = null,
        end: Action? = null) {

        Simplex.MainThreadScheduler.run { begin?.run() }
        val doCompleted = Action { Simplex.MainThreadScheduler.run { end?.run() } }

        try {
            val binder = action(actions)
            val resultObservable = binder.action.toObservable(param)
                .timeout(Simplex.DefaultActionTimeout, TimeUnit.MILLISECONDS)
                .onErrorResumeNext { exception: Throwable ->
                    val transform = binder.action.transform(exception)
                    if (transform.isTransformed) {
                        Simplex.Logger?.write?.invoke("예외가 발생되지 않고 데이터로 트랜스폼되어 배출되었습니다.")
                        Observable.just(transform.result)
                    }
                    else {
                        Simplex.Logger?.write?.invoke(exception.message ?: "")
                        Simplex.ExceptionSubject?.onNext(exception)
                        Observable.empty()
                    }
                }
                .doFinally(doCompleted)

            if (binder.action::class.findAnnotation<Unsubscribe>() != null) {
                    createChannelIfNull(binder.action)
                    //더미
                    resultObservable.subscribe()
            }
            else {
                //채널이 생성되지 않았으면 기본 Channel클래스로 생성
                createChannelIfNull(binder.action)
                //채널 선택
                val ch = channel?.invoke(binder.action) ?: getDefaultChannel(binder.action)
                //액션에 해당하는 source 선택
                val observer = this.getOrAddObservableSource(binder.action)
                //채널 추가
                resultObservable.wrap(ch)
                    .subscribe { observer.onNext(it) }
            }
        }
        catch (ex: Exception) {
            Simplex.ExceptionSubject?.onNext(ex)
            Simplex.Logger?.write?.invoke(ex.stackTrace.toString())
            doCompleted.run()
        }
    }


    fun toDisposableStore(subscriberId: String): DisposableStore<TActionSet> {
        return DisposableStore(this, subscriberId)
    }

    private fun <TResult> getDefaultChannel(action: IAction<TResult>): IChannel {
        val abstractAction = action as? AbstractAction<TResult>
        return abstractAction?.default ?: throw IllegalAccessException("${action.javaClass.name}에서 기본 채널을 가져올 수 있는 방법이 구현되지 않았습니다.")
    }

    private fun <TResult> getOrAddObservableSource(action: IAction<TResult>) : PublishSubject<Pair<IChannel, TResult>> {
        // Default채널의 Id를 키로 등록한다.
        val key = getDefaultChannel(action).id

        if (!observableSources.containsKey(key)) {
            val subject:  PublishSubject<Any> = PublishSubject.create()
            observableSources.put(key, subject)
        }

        @Suppress("UNCHECKED_CAST")
        return observableSources.get(key) as PublishSubject<Pair<IChannel, TResult>>
    }

    private fun <TResult> createChannelIfNull(action: IAction<TResult>) {
        for (property in action::class.declaredMemberProperties) {
            @Suppress("UNCHECKED_CAST")
            val channelProperty = property as? KProperty1<IAction<TResult>, IChannel>
            if (channelProperty?.get(action) == null) {
                // 필드 생성
                if (property is KMutableProperty<*>)
                    property.setter.call(action, Channel())
                else throw IllegalAccessException("${action.javaClass.name}액션의 채널 ${property.name}의 생성자에 접근 할 수 없습니다. " +
                            "생성자를 추가하거나 객체를 생성하세요.")
            }
        }
    }

}