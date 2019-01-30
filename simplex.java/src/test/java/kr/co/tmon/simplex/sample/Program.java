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
		
		store.subscribe(descBuilder -> descBuilder
				.from(set -> set.sendSignal())
				.onNext(() -> {
					System.out.println("결과 수신");
				}));
		
		store.subscribe(descBuilder -> descBuilder
				.from(set -> set.getIntegerString())
				.onNext(result -> {
					System.out.println("GetInteger Ch1 & Ch2 : " + result);
				})
				.observable(ob -> ob.distinct())
				.selectChannel(act -> act.zip(act.ExCh1, act.ExCh2)));
		
		store.subscribe(descBuilder -> descBuilder
				.from(set -> set.getIntegerString())
				.onNext(result -> {
					System.out.println("GetInteger       Ch2 : " + result);
				})
				.selectChannel(act -> act.ExCh2));
		
		store.subscribe(descBuilder -> descBuilder
				.from(set -> set.getIntegerString())
				.onNext(result -> {
					System.out.println("GetInteger Ch1       : " + result);
				})
				.selectChannel(act -> act.ExCh1));
		
		store.dispatch(descBuilder -> descBuilder
				.to(set -> set.sendSignal())
				.onBegin(() -> System.out.println("begin..."))
				.onEnd(() -> System.out.println("....end")));
		
		store.dispatch(descBuilder -> descBuilder
				.to(set -> set.getBoolean())
				.setParameter(true)
				.onBegin(() -> System.out.println("begin..."))
				.onEnd(() -> System.out.println("....end")));

		store.dispatch(
				descBuilder -> descBuilder
					.to(set -> set.getIntegerString())
					.setParameter(1)
					.selectChannel(act -> act.ExCh1));
		
		store.dispatch(
				descBuilder -> descBuilder
					.to(set -> set.getIntegerString())
					.setParameter(1)
					.selectChannel(act -> act.zip(act.ExCh1, act.ExCh2)));
		
		store.dispatch(
				descBuilder -> descBuilder
					.to(set -> set.getIntegerString())
					.setParameter(1)
					.selectChannel(act -> act.ExCh2));
					
		
		store.dispose();
		
	}
}
