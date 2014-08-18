package gminers.glasspane.ease;


public class FriendlyFieldAccessor<T>
		implements FieldAccessor<T> {
	private Object access;
	private Class<?> clazz;
	private String field;
	private Class<?> setterClass;
	private String getterName;
	private String setterName;
	
	public FriendlyFieldAccessor(Object access, Class<?> clazz, Class<?> setterClass, String field) {
		this.access = access;
		this.clazz = clazz;
		this.field = field;
		this.setterClass = setterClass;
		
		getterName = "get" + firstCharUppercase(field);
		setterName = "set" + firstCharUppercase(field);
	}
	
	private String firstCharUppercase(String str) {
		if (str.isEmpty()) return str;
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T get() {
		try {
			return (T) clazz.getMethod(getterName).invoke(access);
		} catch (Exception e) {
			throw new RuntimeException("Could not get " + field + " from " + clazz.getName(), e);
		}
	}
	
	@Override
	public void set(T val) {
		try {
			clazz.getMethod(setterName, setterClass).invoke(access, val);
		} catch (Exception e) {
			throw new RuntimeException("Could not set " + field + " in " + clazz.getName(), e);
		}
	}
	
}
