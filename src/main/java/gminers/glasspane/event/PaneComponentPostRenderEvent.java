package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called after a component has been renderered.<br/>
 * X and Y are absolute, not relative to the component, and will need to be translated.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
@Getter
public class PaneComponentPostRenderEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
			PaneComponent.class,
			int.class,
			int.class,
			float.class
	};
	/**
	 * The absolute X of the mouse.
	 */
	int mouseX;
	/**
	 * The absolute Y of the mouse.
	 */
	int mouseY;
	/**
	 * The amount of the way into the next tick, as frames do not align to ticks. Useful for smoothing animations.
	 */
	float partialTicks;
	
	public PaneComponentPostRenderEvent(final PaneComponent compo, final int mouseX, final int mouseY,
			final float partialTicks) {
		super(compo);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.partialTicks = partialTicks;
	}
}
