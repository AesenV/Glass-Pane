package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a listener object is unregistered. See comments in {@link PaneComponent} for why this is not called per-method like
 * {@link PaneEventListenerRegisterEvent}.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@Getter
public class PaneEventListenerUnregisterEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
			PaneComponent.class,
			Object.class
	};
	/**
	 * The object being unregistered.
	 */
	Object listener;
	
	public PaneEventListenerUnregisterEvent(final PaneComponent c, final Object o) {
		super(c);
		listener = o;
	}
}
