import kr.co.tmon.simplex.SimpleLogger
import kr.co.tmon.simplex.Simplex

fun main() {

    Simplex.initialize(logger = SimpleLogger{x:String -> System.out.println(x)})
    val store = Simplex.getStore<MyActionSet>().toDisposableStore("subscription-id")

    store.subscribe(
        action = { set -> set.getInteger },
        channel = { act -> act.zip(act.ch2, act.ch1) },
        observable = { ob -> ob.distinct() },
        onNext = { r -> System.out.println("getInteger Ch1 & Ch2 : $r") })

    store.subscribe(
        action =  { set -> set.getInteger },
        channel = { act -> act.ch2 },
        onNext = { r -> System.out.println("getInteger       Ch2 : $r") })

    store.subscribe(
        action = { set -> set.getInteger },
        channel = {act -> act.ch1},
        onNext = { r -> System.out.println("getInteger Ch1       : $r") })

    store.subscribe(
        action = { set -> set.sendSignal },
        onNext = { System.out.println("signal") })

    store.dispatch(
        action = { set -> set.getInteger },
        channel =  { act -> act.ch1 },
        parameters =  1)

    store.dispatch(
        action =  {set -> set.sendSignal},
        channel = { act -> act.ch1!! })

    store.dispatch(
        action = {set -> set.getInteger},
        channel = {act -> act.zip(act.ch2, act.ch1)},
        parameters = 2)

    store.dispatch({set -> set.sendSignal})

    store.dispatch(
        action = { set -> set.getInteger },
        channel =  { act -> act.ch2 },
        parameters =  3)

    store.dispatch({set -> set.sendSignal})

    store.dispose()
}