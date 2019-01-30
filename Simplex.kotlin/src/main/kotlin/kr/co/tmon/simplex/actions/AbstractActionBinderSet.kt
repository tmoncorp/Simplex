package kr.co.tmon.simplex.actions

import org.jetbrains.annotations.NotNull
import java.util.concurrent.ConcurrentHashMap
import java.lang.IllegalArgumentException

/**
 * 단위작업의 묶음 기능을 제공합니다.
 * 단위작업 또는 묶을을 중첩하여 넣을 수 있습니다.
 * Store의 dispatch 또는 subscribe에 "단위작업의 묶음"을 노출 시키기 위해서는 접근 가능한 프로퍼티를 구현해야 합니다.
 */
abstract class AbstractActionBinderSet : IActionBinderSet {

    /**
     * 단위작업 목록
     */
    val actionBinders: ConcurrentHashMap<String, IActionBinder<*,*,*>> = ConcurrentHashMap()

    /**
     * 단위작업의 묶음 목록
     */
    val actionSets: MutableList<IActionBinderSet> = ArrayList()

    /**
     * 단위작업 바인더를 조회합니다. 단위작업이 등록되어 있지 않으면 새롭게 등록하고 해당 객체를 리턴합니다.
     * @param actionBinderKey 단위작업의 등록 키
     * @return 단위작업 바인더 인스턴스
     */
    inline fun <reified TAction : IAction<TParam, TResult>, TParam, TResult> getOrAddAction(@NotNull actionBinderKey: String)
		: IActionBinder<TAction, TParam, TResult> {
        @Suppress("UNCHECKED_CAST")
		return getOrAddInner(actionBinderKey) { ActionBinder<TAction, TParam, TResult>() } as IActionBinder<TAction, TParam, TResult>
	}

    inline fun <reified TActionBinder : IActionBinder<*,*,*>> getOrAddInner(@NotNull actionBinderKey: String, valueFactory: (String) -> TActionBinder)
            : IActionBinder<*,*,*> {
        if (actionBinderKey.isEmpty())
            throw IllegalArgumentException("$actionBinderKey 값이 없습니다.")

        if (!actionBinders.containsKey(actionBinderKey))
            actionBinders.put(actionBinderKey, valueFactory(actionBinderKey) as IActionBinder<*,*,*>)

        return actionBinders.get(actionBinderKey) as TActionBinder
    }

    /**
     * 단위작업의 묶음을 조회합니다. 단위작업 묶음이 등록되어 있지 않으면 새롭게 등록하고 해당 객체를 리턴합니다.
     * @return 단위작업 묶음 인스턴스
     */
    inline fun <reified TActionSet : IActionBinderSet> getOrAdd() : TActionSet {
        var actionSet = actionSets.firstOrNull {x -> x is TActionSet} as? TActionSet
        if (actionSet != null) {
            actionSet = TActionSet::class.java.newInstance()
            actionSets.add(actionSet)
        }

        return actionSet!!
    }
}