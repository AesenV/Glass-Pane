package gminers.glasspane.ease;

import gminers.kitchensink.Strings;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AggressiveFieldAccessor<T> implements FieldAccessor<T> {
	private Object access;
	private Class<?> clazz;
	private String field;
	public AggressiveFieldAccessor(Object access, Class<?> clazz, String field) {
		this.access = access;
		this.clazz = clazz;
		this.field = field;
	}
	@Override
	public T get() {
		try {
			return (T) accessible(clazz.getDeclaredField(field)).get(access);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not get "+field+" from "+clazz.getName());
		}
	}

	@Override
	public void set(T val) {
		try {
			accessible(clazz.getDeclaredField(field)).set(access, val);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not set "+field+" in "+clazz.getName());
		}
	}
	
	private <X extends AccessibleObject> X accessible(X x) {
		x.setAccessible(true);
		return x;
	}

}
