package kr.co.tmon.simplex;


import java.io.PrintWriter;
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
 * Simplex를 초기화하고 스토어의 인스턴스를 발행하는 클래스
 * getStore()를 통해 Store 인스턴스를 취득할 수 있으며, getStore() 호출전에는 
 * 반드시 initialize() 메소드를 호출하여야 합니다.
 * 이 메소드는 한번만 호출되어야 하며, 중복 호출된 경우에 예외가 발생됩니다.
 * 
 * @author yookjy
 */
public class Simplex {

	/**
	 * 액션 처리가 지연될때 예외를 발생시킬 시간으로 기본값은 10초 
	 */
	private static int defaultActionTimeoutMilliseconds = 10000;
	
	/**
	 * 메인 스레드 (플랫폼별로 구현되어야 함) 
	 */
	private static Scheduler mainThreadScheduler;

	/**
	 * 로그 기록 인터페이스
	 */
	private static ILogger logger;

	/**
	 * 예외를 배출시킬 주체
	 */
	private static Subject<Exception> exceptionSubject;

	/**
	 * 스토어 인스턴스
	 */
	@SuppressWarnings("rawtypes")
	private static IActionStore store;

	/**
	 * 초기화 여부
	 */
	private static boolean isInitialized;

	public static int getDefaultActionTimeoutMilliseconds() {
		return defaultActionTimeoutMilliseconds;
	}
	
	public static Scheduler getMainThreadScheduler() {
		return mainThreadScheduler;
	}
	
	public static ILogger getLogger() {
		return logger;
	}
	
	public static Subject<Exception> getExceptionSubject() {
		return exceptionSubject;
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
	public synchronized static void Initialize(
			final Scheduler mainThreadScheduler, 
			final Consumer<Exception> exceptionHandler,
			final int exceptionThrottleMilliseconds, 
			final int actionTimeoutMilliseconds, 
			final ILogger logger) {

		if (isInitialized) {
			throw new InitializationException(store.getClass().getName() + "타입용으로 이미 초기화 되어 있습니다.");
		}

		Simplex.mainThreadScheduler = mainThreadScheduler;
		Simplex.defaultActionTimeoutMilliseconds = actionTimeoutMilliseconds;
		Simplex.logger = logger;
		
		if (Simplex.mainThreadScheduler == null) {
			Simplex.mainThreadScheduler = Schedulers.trampoline();
		}
		
		if (Simplex.logger == null) {
			Simplex.logger = new SimpleLogger(new PrintWriter(System.out));
		}

		final Consumer<Exception> exHandler = 
				exceptionHandler != null 
					? exceptionHandler
					: ex -> {

						Simplex.mainThreadScheduler.scheduleDirect(() -> {
							throw new UnhandledErrorException(
									"IAction.Execute를 구현하는 객체가 오류를 발생시켰습니다. IAction<T>.Transform을 재정의하면 에러를 정상 데이터로 변환하여 구독할 수 있습니다.",
									ex);
						});
					};
 
		exceptionSubject = PublishSubject.create();
		exceptionSubject
			.buffer(exceptionThrottleMilliseconds, TimeUnit.MILLISECONDS)
			.filter(x -> x.size() > 0)
			.flatMapIterable(x -> x)
			.distinct(x -> x.getMessage())
			.subscribe(x -> {
				Simplex.mainThreadScheduler.scheduleDirect(() -> {
					try {
						exHandler.accept(x);
					} catch (Exception e) {
						logger.write(e);
					}
				});
			});


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
				exceptionSubject.onNext(e);
				logger.write(e);
			}
		}
		
		return (Store<TActionBinderSet>)store;
	}
}
