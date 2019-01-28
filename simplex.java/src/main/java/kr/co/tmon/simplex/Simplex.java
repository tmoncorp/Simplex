package kr.co.tmon.simplex;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import kr.co.tmon.simplex.actions.IActionBinderSet;
import kr.co.tmon.simplex.exceptions.InitializationException;
import kr.co.tmon.simplex.exceptions.UnhandledErrorException;
import kr.co.tmon.simplex.store.IActionStore;
import kr.co.tmon.simplex.store.Store;

/**
 * 
 *   
 *  
 * @author yookjy
 *
 */
public class Simplex {

	/**
	 * 액션 처리가 지연될때 예외를 발생시킬 시간으로 기본값은 10초 
	 */
	public static int DefaultActionTimeoutMilliseconds = 10000;
	
	/**
	 * 메인 스레드 (플랫폼별로 구현되어야 함) 
	 */
	public static Scheduler MainThreadScheduler;

	/**
	 * 로그 기록 인터페이스
	 */
	public static ILogger Logger;

	/**
	 * 예외를 배출시킬 주체
	 */
	public static Subject<Exception> ExceptionSubject;

	/**
	 * 스토어 인스턴스
	 */
	@SuppressWarnings("rawtypes")
	private static IActionStore store;

	/**
	 * 초기화 여부
	 */
	private static boolean isInitialized;

	/**
	 * 초기화 여부
	 */
	public static boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * Simplex를 초기화 합니다.
	 * 초기화는 한번만 호출 되어야 하며, 중복으로 초기화가 된 경우 InitializationException이 발생됩니다.
	 * 
	 * @param mainThreadScheduler 메인 스레드 스케줄러 (UI가 존재하는 경우 UI 스레드가 일반적으로 메인 스레드가 됩니다.) 
	 * @param exceptionHandler 예외가 발생되었을때 처리할 예외 처리기
	 * @param exceptionThrottleMilliseconds 중복된 예외를 제거하기 위한 시간 버퍼로 기본값은 0.5초 입니다. 중복된 예외를 허용하려면 0을 설정하세요.
	 * @param actionTimeoutMilliseconds 액션 처리가 지연될때 예외를 발생시킬 시간으로 기본값은 10초 입니다.
	 * @param logger 로그기록 인터페이스
	 */
	public synchronized static void Initialize(Scheduler mainThreadScheduler, Consumer<Exception> exceptionHandler,
			int exceptionThrottleMilliseconds, int actionTimeoutMilliseconds, ILogger logger) {

		if (isInitialized()) {
			throw new InitializationException(store.getClass().getName() + "타입용으로 이미 초기화 되어 있습니다.");
		}

		MainThreadScheduler = mainThreadScheduler;
		DefaultActionTimeoutMilliseconds = actionTimeoutMilliseconds;
		Logger = logger;

		if (MainThreadScheduler == null) {
			MainThreadScheduler = Schedulers.trampoline();
		}

		if (exceptionHandler == null) {
			exceptionHandler = ex -> {

				MainThreadScheduler.scheduleDirect(() -> {
					throw new UnhandledErrorException(
							"IAction.Execute를 구현하는 객체가 오류를 발생시켰습니다. IAction<T>.Transform을 재정의하면 에러를 정상 데이터로 변환하여 구독할 수 있습니다.",
							ex);
				});
			};
		}

		if (ExceptionSubject == null) {
			final Consumer<Exception> exHandler = exceptionHandler;
			ExceptionSubject = PublishSubject.create();
			ExceptionSubject
				.buffer(exceptionThrottleMilliseconds, TimeUnit.MILLISECONDS)
				.filter(x -> x.size() > 0)
				.flatMapIterable(x -> x)
				.distinct(x -> x.getMessage())
				.subscribe(x -> {
					MainThreadScheduler.scheduleDirect(() -> {
						try {
							exHandler.accept(x);
						} catch (Exception e) {
							Logger.write(e);
						}
					});
				});
		}

		//초기화 완료
		isInitialized = true;
	}
	
	/**
	 * 스토어 인스턴스를 조회합니다.
	 * *주의 : 이 메소드 호출 전에 반드시 Simplex.Initialize() 메소드가 호출되어야만 합니다.
	 * 
	 * @param clazz 스토어에 등록할 액션 바인더의 타입
	 * @return 스토어 인스턴스
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <TActionBinderSet extends IActionBinderSet> Store<TActionBinderSet> getStore(Class<TActionBinderSet> clazz) {
		
		if (!isInitialized) {
			throw new InitializationException("Simplex.Initialize()를 호출하여 Simplex를 초기화 하십시오.");
		}
		
		if (store == null) {
			
			try {
				Constructor<?> constructor = Store.class.getConstructor(Class.class);
				constructor.setAccessible(true);
				store = (Store<TActionBinderSet>)constructor.newInstance(clazz);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
				ExceptionSubject.onNext(e);
				Logger.write(e);
			}
		}
		
		return (Store<TActionBinderSet>)store;
	}
}
