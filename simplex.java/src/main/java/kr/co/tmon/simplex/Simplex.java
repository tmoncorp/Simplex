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

public class Simplex {

	public static int DefaultActionTimeoutMilliseconds = 10000;
	
	public static Scheduler MainThreadScheduler;

	public static ILogger Logger;

	public static Subject<Exception> ExceptionSubject;

	@SuppressWarnings("rawtypes")
	private static IActionStore store;

	private static boolean isInitialized;

	public static boolean isInitialized() {
		return isInitialized;
	}

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
			ExceptionSubject.buffer(exceptionThrottleMilliseconds, TimeUnit.MILLISECONDS).filter(x -> x.size() > 0)
					.flatMapIterable(x -> x).distinct(x -> x.getMessage()).subscribe(x -> {
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
				Logger.write(e);
			}
		}
		
		return (Store<TActionBinderSet>)store;
	}
}
