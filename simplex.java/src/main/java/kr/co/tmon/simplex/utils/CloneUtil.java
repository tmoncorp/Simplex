package kr.co.tmon.simplex.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Stack;

import kr.co.tmon.simplex.Simplex;
import kr.co.tmon.simplex.exceptions.UnclonableTypeException;

public class CloneUtil {

	@SuppressWarnings("unchecked")
	public static <T> T deepClone(T source) throws UnclonableTypeException {
		try {
			return (T)deepCloneObject(source);
		} catch(Exception ex) {
			throw new UnclonableTypeException(
					ex.getMessage() 
					+ "\n깊은 복사에 실패하였습니다. 원본을 배출 하려면 prenventClone 옵션을 설정하세요.\n"
					+ source.getClass().getName());
		}
	}
		
	public static Object deepCloneObject(Object source) {
		if (source == null)
			return null;
		
		Class<?> clazz = source.getClass();
		
		if (clazz.isPrimitive() || clazz.isEnum()) {
			//1. 원시자료형, 열거형이면 할당
			return source;
		} else if (isValueType(clazz)) {
			//2. 값 타입
			return source;
		} else if (clazz.isArray()) {
			return copyArray(clazz, source);
		} else {
			try {
				
				Constructor<?> constructor = source.getClass().getDeclaredConstructor();
				constructor.setAccessible(true);
				Object target = constructor.newInstance();
				copyInstance(clazz, source, target);
				return target;
			} catch (Exception ex) {
				Simplex.getLogger().write(clazz.getName() + " 필드 복사가 skip 되었습니다. " + ex.getMessage());
				return null;
			}
		}
	}
	
	private static boolean isValueType(Class<?> clazz) {
		return (clazz == String.class
				|| clazz == Boolean.class
				|| clazz == Character.class
				|| clazz == Byte.class
				|| clazz == Short.class
				|| clazz == Integer.class
				|| clazz == Long.class
				|| clazz == Float.class
				|| clazz == Double.class);
	}
	
	private static Object copyArray(Class<?> clazz, Object source) {
		
		Class<?> componentType = source.getClass().getComponentType();
		Object[] array = (Object[])source;
		Object[] objs = (Object[])Array.newInstance(componentType, array.length);
		
		for(int i = 0; i < array.length; i++) {
			objs[i] = deepCloneObject(array[i]);
		}
		
		return (Object)objs;
	}
	
	private static <T> void copyInstance(Class<T> clazz, Object source, Object target) {
		Stack<Class<T>> stack = new Stack<Class<T>>();
		stack.push(clazz);
		
		while (!stack.isEmpty()) {
			Class<T> cls = stack.pop();
			
			//필드 copy
			Field[] fields = cls.getDeclaredFields();
			for(Field field : fields) {
				try {
					field.setAccessible(true);
					Object value = field.get(source);
					if (value != null) {
						field.set(target, deepCloneObject(value));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					Simplex.getLogger().write(field.getName() + " 필드의 값 복사가 실패하였습니다.");
				}
				
			}
		}
	}
}
