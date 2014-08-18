package gminers.glasspane.event;


import gminers.glasspane.component.Focusable;
import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a {@link Focusable} component loses the focus of it's current parent.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class FocusLostEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
		PaneComponent.class
	};
	
	public FocusLostEvent(final PaneComponent source) {
		super(source);
	}
}
