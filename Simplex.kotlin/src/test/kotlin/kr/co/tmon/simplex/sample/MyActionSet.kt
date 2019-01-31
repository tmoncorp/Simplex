import kr.co.tmon.simplex.channels.IChannel
import kr.co.tmon.simplex.actions.*
import kr.co.tmon.simplex.annotations.Unsubscribe
import kr.co.tmon.simplex.channels.Channel
import io.reactivex.Observable

class MyActionSet : AbstractActionBinderSet() {

    val sendSignal: IActionBinder<SendSignal, Unit, Unit>
        get() = getOrAddAction("sendSignal")

    val getBoolean: IActionBinder<GetBoolean, Boolean, Boolean>
        get() = getOrAddAction("getBoolean")

    val getInteger: IActionBinder<GetIntegerString, Int, String>
        get() = getOrAddAction("getInteger")
}

class GetBoolean : AbstractAction<Boolean, Boolean>() {
    var ch1: IChannel? = null

    override fun process(param: Boolean?): Observable<Boolean> {
        return Observable.just(param)
    }
}

class GetIntegerString : AbstractAction<Int, String>() {
    val ch1: IChannel by lazy { Channel() }
    val ch2: IChannel by lazy { Channel() }

    override fun process(param: Int?): Observable<String> {
        return Observable.just(param.toString())
    }
}

@Unsubscribe
class SendSignal : AbstractUnitAction<Unit>() {
    override fun process(): Observable<Unit> {
        return Observable.just(Unit)
    }
}


