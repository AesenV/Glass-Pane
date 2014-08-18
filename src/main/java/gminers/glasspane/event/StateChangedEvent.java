package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a component changes state. For the new state, call the relevant method on the component.
 * 
 * @author Aesen Vismea
 * 
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class StateChangedEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
		PaneComponent.class
	};
	
	public StateChangedEvent(final PaneComponent source) {
		super(source);
	}
}
