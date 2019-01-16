package kr.co.tmon.simplex.sample.actions;

import kr.co.tmon.simplex.actions.AbstractActionBinderSet;
import kr.co.tmon.simplex.actions.IActionBinder;
import kr.co.tmon.simplex.actions.IParameterizedActionBinder;
import kr.co.tmon.simplex.reactivex.Unit;

public class MyActionSet extends AbstractActionBinderSet {
	public IActionBinder<SendSignal, Unit> sendSignal() {
		return getOrAdd("SendSignal", SendSignal.class);
	}
	
	public IParameterizedActionBinder<GetBoolean, Boolean, Boolean> getBoolean() {
		return getOrAddParameterized("GetBoolean", GetBoolean.class);
	}
		
	public IParameterizedActionBinder<GetIntegerString, Integer, String> getIntegerString() {
		return getOrAddParameterized("GetIntegerString", GetIntegerString.class);
	}
}
