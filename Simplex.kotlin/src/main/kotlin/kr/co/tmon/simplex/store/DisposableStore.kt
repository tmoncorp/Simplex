package kr.co.tmon.simplex.store

import kr.co.tmon.simplex.actions.*
import kr.co.tmon.simplex.channels.IChannel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import java.util.concurrent.ConcurrentHashMap

class DisposableStore<TActionSet>(private val store: IActionStore<TActionSet>, private val subscriberId: String) : IActionStore<TActionSet> {

    private val managedDisposables: ConcurrentHashMap<String, CompositeDisposable> = ConcurrentHashMap()

    override fun <TAction : IAction<TParam, TResult>, TParam, TResult> dispatch(
        action: Function1<TActionSet, IActionBinder<TAction, TParam, TResult>>,
        parameters: TParam?,
        begin: Action?,
        end: Action?,
        channel: Function1<TAction, IChannel>? ) {
        store.dispatch(action, parameters, begin, end, channel)
    }

    override fun <TAction : IAction<TParam, TResult>, TParam, TResult> subscribe(
        action: Function1<TActionSet, IActionBinder<TAction, TParam, TResult>>,
        onNext: (TResult) -> Unit,
        observeOnMainThread: Boolean,
        observable: Function1<Observable<TResult>, Observable<TResult>>?,
        channel: Function1<TAction, IChannel>?,
        preventClone: Boolean
    ): Disposable? {
        return addDisposable(store.subscribe(action, onNext, observeOnMainThread, observable, channel, preventClone))
    }

    /**
     * 구독이 등록될때 발생하는 삭제패턴(Disposable)객체를 관리 목록에 등록합니다.
     * 관리목록은 모든 관리형 스토어에 공유되며, 삭제 패턴객체는 관리 스토어 식별자 별로 관리됩니다.
     * @param disposable 삭제패턴 객체
     * @return 삭제패턴 객체
     */
    private fun addDisposable(disposable: Disposable?) : Disposable? {
        if (disposable == null)
            return null

        val list = managedDisposables.get(subscriberId)
        if (list != null) {
            if (!list.isDisposed) {
                list.add(disposable)
            }
        } else {
            managedDisposables.put(subscriberId, CompositeDisposable(disposable))
        }

        return disposable
    }

    fun dispose() {
        //관리형 스토어별로 등록된 구독을 모두 해제 합니다.
        val value = managedDisposables.remove(subscriberId)
        if (value != null) {
            if (!value.isDisposed)
                value.dispose()
        }
    }
}