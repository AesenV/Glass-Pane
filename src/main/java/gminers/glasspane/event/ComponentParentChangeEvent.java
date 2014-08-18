package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import gminers.glasspane.component.PaneContainer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when the parent of a component changes. For the component's current parent, call PaneComponent.getParent(). For it's old parent,
 * use ComponentParentChangeEvent.getOldParent().
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
@Getter
public class ComponentParentChangeEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
			PaneComponent.class,
			PaneContainer.class
	};
	/**
	 * The previous parent of this component. For the new parent, call PaneComponent.getParent().
	 */
	PaneContainer oldParent;
	
	public ComponentParentChangeEvent(final PaneComponent source, final PaneContainer oldParent) {
		super(source);
		this.oldParent = oldParent;
	}
}
