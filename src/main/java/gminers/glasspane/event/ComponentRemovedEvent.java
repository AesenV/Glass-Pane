package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called on a container when a component is removed from it. For an event that is called on a component when it is removed from a
 * container, see {@link ComponentParentChangeEvent}.
 * 
 * @author Aesen Vismea
 * 
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class ComponentRemovedEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
			PaneComponent.class,
			PaneComponent.class
	};
	/**
	 * The component being removed.
	 */
	PaneComponent affected;
	
	public ComponentRemovedEvent(final PaneComponent source, final PaneComponent added) {
		super(source);
		this.affected = added;
	}
}
