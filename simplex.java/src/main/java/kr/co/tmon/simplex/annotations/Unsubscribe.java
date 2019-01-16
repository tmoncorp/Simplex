package kr.co.tmon.simplex.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 액션을 실행만 하고 구독하지 않게 설정합니다. 
 * @author yookjy
 *
 */
@Target(ElementType.TYPE)
public @interface Unsubscribe {

}
