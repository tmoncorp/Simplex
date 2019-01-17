package kr.co.tmon.simplex

import kr.co.tmon.simplex.store.*
import kr.co.tmon.simplex.actions.*

import java.util.concurrent.TimeUnit
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class Simplex {
    companion object {
        /**
         * Store 인스턴스
         */
        @JvmField
        var Store: IActionStore<*>? = null

        /**
         * 초기화 여부
         */
        @JvmField
        var IsInitialized: Boolean = false

        /**
         * 액션 처리가 지연될때 예외를 발생시킬 시간으로 기본값은 10초 입니다.
         */
        @JvmField
        internal var DefaultActionTimeout: Long = 10000

        /**
         * 예외를 배출시킬 주체
         */
        @JvmField
        internal var ExceptionSubject: PublishSubject<Throwable>? = null

        /**
         * 메인 쓰레드 스케줄러로 플랫폼별 구현되어야 합니다.
         */
        @JvmField
        var MainThreadScheduler: Scheduler? = null

        /**
         * 로그기록 인터페이스
         */
        @JvmField
        internal var Logger: ILogger? = null

        /**
         * Simplex를 초기화 합니다.
         * 초기화는 한번만 호출 되어야 하며, 중복으로 초기화가 된 경우 InitializationException이 발생됩니다.
         * @param mainThreadScheduler 메인 스레드 스케줄러 (UI가 존재하는 경우 UI 스레드가 일반적으로 메인 스레드가 됩니다.)
         * @param exceptionHandler 예외가 발생되었을때 처리할 예외 처리기
         * @param exceptionThrottleMilliseconds 중복된 예외를 제거하기 위한 시간 버퍼로 기본값은 0.5초 입니다. 중복된 예외를 허용하려면 0을 설정하세요.
         * @param actionTimeoutMilliseconds 액션 처리가 지연될때 예외를 발생시킬 시간으로 기본값은 10초 입니다.
         * @param logger 로그기록 인터페이스
         */
        @JvmStatic
        fun initialize(
            mainThreadScheduler: Scheduler? = null,
            exceptionHandler: Function1<Throwable, Unit>? = null,
            exceptionThrottleMilliseconds: Long = 500,
            actionTimeoutMilliseconds: Long = 10000,
            logger: ILogger? = null) {

            synchronized(this) {
                if (IsInitialized) {
                    throw Exception("Store는 이미 초기화 되어 있습니다.")
                }

                MainThreadScheduler = mainThreadScheduler
                DefaultActionTimeout = actionTimeoutMilliseconds
                Logger = logger

                //등록된 메인스레드 스케쥴러가 존재하지 않는 경우 Rx기본 스케줄러 사용
		        if (MainThreadScheduler == null)
		            MainThreadScheduler = Schedulers.trampoline()

                //등록된 예외 처리기가 존재하지 않으면 예외를 발생시킴
                val simplexExceptionHandler: Function1<Throwable, Unit> = exceptionHandler ?: {exception: Throwable ->
                    throw Exception("IAction.Execute를 구현하는 객체가 오류를 발생시켰습니다. IAction<T>.Transform을 재정의하면 에러를 정상 데이터로 변환하여 구독할 수 있습니다. $exception")
                }

                //예외를 배출할 주체 생성
                if (ExceptionSubject == null) {
                    val exceptionSource = PublishSubject.create<Throwable>()
                    exceptionSource
                        .buffer(exceptionThrottleMilliseconds, TimeUnit.MILLISECONDS)
                        .filter {x -> x.count() > 0}
                        .flatMap {x -> x.toObservable().distinct{ex -> ex.message}}
                        .subscribe {x ->
                            MainThreadScheduler.run { simplexExceptionHandler(x) }
                        }

                    ExceptionSubject = exceptionSource
                }

                //초기화 완료
                IsInitialized = true
            }
        }

        @JvmStatic
        inline fun <reified TActionSet: IActionBinderSet> getStore(): Store<TActionSet> {
            synchronized(this) {
                if (!IsInitialized)
                    throw IllegalAccessException("Initialize 메소드가 먼저 호출되어야 합니다.")

                if (Store == null)
                    Store = Store<TActionSet>()

                @Suppress("UNCHECKED_CAST")
                return Store as? Store<TActionSet> ?: throw IllegalAccessException("ActionStore를 ActionSet으로 형변환 할 수 없습니다.")
            }
        }
    }
}