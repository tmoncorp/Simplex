package kr.co.tmon.simplex.sample;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import kr.co.tmon.simplex.SimpleLogger;
import kr.co.tmon.simplex.Simplex;
import kr.co.tmon.simplex.exceptions.UnclonableTypeException;
import kr.co.tmon.simplex.sample.actions.MyActionSet;
import kr.co.tmon.simplex.store.DisposableStore;

public class Program {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, IOException, UnclonableTypeException{
				
		
		ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(10);		
		Scheduler scheduler = Schedulers.from(pool) ;
		
		Simplex.Initialize(
				scheduler,
				null,
				500,
				10000,
				new SimpleLogger(new PrintWriter(System.out)));
				
		DisposableStore<MyActionSet> store = Simplex.getStore(MyActionSet.class).toDisposableStore("subscriber-id");
		
		store.subscribe(
				set -> set.sendSignal(),
				() -> {
					System.out.println("결과 수신");
				},
				false,
				null,
				null);
		
		store.subscribe(
				set -> set.getIntegerString(),
				result -> {
					System.out.println("GetInteger Ch1 & Ch2 : " + result);
				},
				false,
				ob -> ob.distinct(), 
				act -> act.zip(act.ExCh1, act.ExCh2), 
				false);
		
		store.subscribe(
				set -> set.getIntegerString(),
				result -> {
					System.out.println("GetInteger       Ch2 : " + result);
				},
				false,
				null, 
				act -> act.ExCh2, 
				false);
		
		store.subscribe(
				set -> set.getIntegerString(),
				result -> {
					System.out.println("GetInteger Ch1       : " + result);
				},
				act -> act.ExCh1,
				false);
		
		
		store.dispatch(
				set -> set.sendSignal(),
				() -> System.out.println("begin..."),
				() -> System.out.println("....end"));
		
		store.dispatch(
				set -> set.getBoolean(),
				true,
				() -> System.out.println("begin..."),
				() -> System.out.println("....end"));
		

		store.dispatch(
				set -> set.getIntegerString(),
				1,
				act -> act.ExCh1);
		
		store.dispatch(
				set -> set.getIntegerString(),
				1,
				act -> act.zip(act.ExCh1, act.ExCh2));
		
		store.dispatch(
				set -> set.getIntegerString(),
				1,
				act -> act.ExCh2);
		
		store.dispose();
		
	}
}
