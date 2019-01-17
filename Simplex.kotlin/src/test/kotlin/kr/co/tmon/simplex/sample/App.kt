import kr.co.tmon.simplex.SimpleLogger
import kr.co.tmon.simplex.Simplex

fun main() {

    // 초기화
    Simplex.initialize(logger = SimpleLogger{x:String -> System.out.println(x)})

    // DisposableStore 생성
    val store = Simplex.getStore<MyActionSet>().toDisposableStore("subscription-id")

    // getInteger ch1과 ch2를 구독한다.
    store.subscribe(
        action = { set -> set.getInteger },
        channel = { act -> act.zip(act.ch2, act.ch1) },
        observable = { ob -> ob.distinct() },
        onNext = { r -> System.out.println("get Integer Ch1 & Ch2 : $r") })

    // getInteger ch2를 구독한다.
    store.subscribe(
        action =  { set -> set.getInteger },
        channel = { act -> act.ch2 },
        onNext = { r -> System.out.println("get Integer       Ch2 : $r") })

    // getInteger ch1을 구독한다.
    store.subscribe(
        action = { set -> set.getInteger },
        channel = {act -> act.ch1},
        onNext = { r -> System.out.println("get Integer Ch1       : $r") })

    // sendSignal을 구독한다.
    store.subscribe(
        action = { set -> set.sendSignal },
        onNext = { System.out.println("signal") })

    // getBoolean을 구독한다.
    store.subscribe(
        action = { set -> set.getBoolean },
        onNext = { r -> System.out.println("get Boolean : $r") })

    // @Unsubscribe로 설정되어 구독자는 값을 받지 않는다.
    store.dispatch({set -> set.sendSignal})

    // getBoolean로 등록한 구독자가 값을 받는다.
    store.dispatch(
        action =  {set -> set.getBoolean},
        parameters = true)

    // getBoolean ch1으로 등록한 구독자만 값을 받는다.
    store.dispatch(
        action =  {set -> set.getBoolean},
        channel = { act -> act.ch1!! },
        parameters = false)

    // getInteger ch1으로 등록한 구독자만 값을 받는다.
    store.dispatch(
        action = { set -> set.getInteger },
        channel =  { act -> act.ch1 },
        parameters =  1)

    // getInteger ch1이나 ch2로 등록한 구독자만 값을 받는다.
    store.dispatch(
        action = {set -> set.getInteger},
        channel = {act -> act.zip(act.ch2, act.ch1)},
        parameters = 2)

    // getInteger ch2로 등록한 구독자만 값을 받는다.
    store.dispatch(
        action = { set -> set.getInteger },
        channel =  { act -> act.ch2 },
        parameters =  3)

    store.dispose()
}