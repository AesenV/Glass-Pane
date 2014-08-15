package gminers.glasspane.ease;


public interface FieldAccessor<T> {
	public T get();
	
	public void set(T val);
}
