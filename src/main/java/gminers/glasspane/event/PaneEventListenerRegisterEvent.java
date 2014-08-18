package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;

import java.lang.reflect.Method;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a new listener method is registered.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@Getter
public class PaneEventListenerRegisterEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
			PaneComponent.class,
			Object.class,
			Method.class
	};
	/**
	 * The object being registered as a listener.
	 */
	Object listener;
	/**
	 * The method on the listener object being registered as a handler.
	 */
	Method handler;
	
	public PaneEventListenerRegisterEvent(final PaneComponent c, final Object o, final Method m) {
		super(c);
		listener = o;
		handler = m;
	}
}
