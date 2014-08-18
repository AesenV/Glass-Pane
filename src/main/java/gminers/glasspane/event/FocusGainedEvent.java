package gminers.glasspane.event;


import gminers.glasspane.component.Focusable;
import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a {@link Focusable} component gains the focus of it's current parent. Focused components receive mouse/keyboard input events.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class FocusGainedEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
		PaneComponent.class
	};
	
	public FocusGainedEvent(final PaneComponent source) {
		super(source);
	}
}
