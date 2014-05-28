package gminers.glasspane.ease;

import gminers.kitchensink.Strings;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FriendlyFieldAccessor<T> implements FieldAccessor<T> {
	private Object access;
	private Class<?> clazz;
	private String field;
	private String getterName;
	private String setterName;
	public FriendlyFieldAccessor(Object access, Class<?> clazz, String field) {
		this.access = access;
		this.clazz = clazz;
		this.field = field;
		
		getterName = "get"+Strings.formatTitleCase(field);
		setterName = "set"+Strings.formatTitleCase(field);
	}
	@Override
	public T get() {
		try {
			return (T) clazz.getMethod(getterName).invoke(access);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not get "+field+" from "+clazz.getName());
		}
	}

	@Override
	public void set(T val) {
		try {
			clazz.getMethod(setterName, val.getClass()).invoke(access, val);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not set "+field+" in "+clazz.getName());
		}
	}

}
