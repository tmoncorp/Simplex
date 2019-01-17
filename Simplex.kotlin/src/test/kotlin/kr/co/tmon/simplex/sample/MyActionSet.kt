import kr.co.tmon.simplex.channels.IChannel
import kr.co.tmon.simplex.actions.*
import kr.co.tmon.simplex.annotations.Unsubscribe
import kr.co.tmon.simplex.channels.Channel
import io.reactivex.Observable

class MyActionSet : AbstractActionBinderSet() {

    val sendSignal: IActionBinder<SendSignal, Unit>
        get() = getOrAddAction("sendSignal")

    val getBoolean: IParameterizedActionBinder<GetBoolean, Boolean, Boolean>
        get() = getOrAddParameterizedAction("getBoolean")

    val getInteger: IParameterizedActionBinder<GetIntegerString, Int, String>
        get() = getOrAddParameterizedAction("getInteger")
}

class GetBoolean : AbstractParameterizedAction<Boolean, Boolean>() {
    var ch1: IChannel? = null

    override fun process(param: Boolean?): Observable<Boolean> {
        return Observable.just(param)
    }
}

class GetIntegerString : AbstractParameterizedAction<Int, String>() {
    val ch1: IChannel by lazy { Channel() }
    val ch2: IChannel by lazy { Channel() }

    override fun process(param: Int?): Observable<String> {
        return Observable.just(param.toString())
    }
}

@Unsubscribe
class SendSignal : AbstractAction<Unit>() {
    override fun process(): Observable<Unit> {
        return Observable.just(Unit)
    }
}


