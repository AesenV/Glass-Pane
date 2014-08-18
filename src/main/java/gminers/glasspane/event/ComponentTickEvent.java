package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called every tick on all active components.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class ComponentTickEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
		PaneComponent.class
	};
	
	public ComponentTickEvent(final PaneComponent source) {
		super(source);
	}
}
