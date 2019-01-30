package kr.co.tmon.simplex.sample.actions;

import kr.co.tmon.simplex.actions.AbstractActionBinderSet;
import kr.co.tmon.simplex.actions.IActionBinder;
import kr.co.tmon.simplex.reactivex.Unit;

public class MyActionSet extends AbstractActionBinderSet {
	public IActionBinder<SendSignal, Unit, Unit> sendSignal() {
		return getOrAdd("SendSignal", SendSignal.class);
	}
	
	public IActionBinder<GetBoolean, Boolean, Boolean> getBoolean() {
		return getOrAdd("GetBoolean", GetBoolean.class);
	}
		
	public IActionBinder<GetIntegerString, Integer, String> getIntegerString() {
		return getOrAdd("GetIntegerString", GetIntegerString.class);
	}
}
