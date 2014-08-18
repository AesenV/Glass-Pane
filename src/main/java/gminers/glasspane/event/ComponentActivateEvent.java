package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a focused component is activated, either by clicking it or pressing Enter while it has the focus.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class ComponentActivateEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
		PaneComponent.class
	};
	
	public ComponentActivateEvent(final PaneComponent source) {
		super(source);
	}
}
