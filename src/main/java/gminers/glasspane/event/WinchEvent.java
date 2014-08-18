package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Fired when the width or height of a component (or pane) changes.
 * 
 * @author Aesen Vismea
 * 
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class WinchEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
			PaneComponent.class,
			int.class,
			int.class,
			int.class,
			int.class
	};
	/**
	 * The old width of the component.
	 */
	int oldWidth;
	/**
	 * The old height of the component.
	 */
	int oldHeight;
	/**
	 * The new width of the component.
	 */
	int newWidth;
	/**
	 * The new height of the component.
	 */
	int newHeight;
	
	public WinchEvent(final PaneComponent source, final int oldWidth, final int oldHeight, final int newWidth,
			final int newHeight) {
		super(source);
		this.oldWidth = oldWidth;
		this.oldHeight = oldHeight;
		this.newWidth = newWidth;
		this.newHeight = newHeight;
	}
}
