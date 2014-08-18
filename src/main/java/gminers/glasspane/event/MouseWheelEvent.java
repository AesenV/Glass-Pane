package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when the mouse wheel is spun, on the component that has the focus (or a GlassPane that is currently being displayed, either with
 * show() or overlay())<br/>
 * X and Y are absolute, not relative to the component, and will need to be translated.
 * 
 * @author Aesen Vismea
 * @see PaneComponent#getChainX()
 * @see PaneComponent#getChainY()
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@Getter
public class MouseWheelEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
			PaneComponent.class,
			int.class,
			int.class,
			int.class
	};
	/**
	 * The absolute X coordinate of the mouse.
	 */
	int mouseX;
	/**
	 * The absolute Y coordinate of the mouse.
	 */
	int mouseY;
	/**
	 * The distance the mouse wheel moved since the last event.<br>
	 * Positive numbers mean up, negative mean down.
	 */
	int distance;
	
	public MouseWheelEvent(final PaneComponent source, final int mouseX, final int mouseY, final int mouseButton) {
		super(source);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.distance = mouseButton;
	}
}
