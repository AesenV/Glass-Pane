package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Called when a mouse button is lifted, on the component that has the focus (or a GlassPane that is currently being displayed, either with
 * show() or overlay())<br/>
 * X and Y are absolute, not relative to the component, and will need to be translated.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@Getter
public class MouseUpEvent
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
	 * The button being pressed. Known values:
	 * <ol start="0">
	 * <li>Left Mouse Button (LMB, Left Click)</li>
	 * <li>Right Mouse Button (RMB, Right Click)</li>
	 * <li>Middle Mouse Button (MMB, Scroll Wheel, Middle Click)</li>
	 * <li>(only some mice) Bottom/Left Thumb Button (LTB, BTB, Back)</li>
	 * <li>(only some mice) Top/Right Thumb Button (RTB, TTB, Forward)</li>
	 * <li>(only some mice) Scroll Wheel Tilt Left (SWTL, Wheel Tilt Left, Scroll Left)</li>
	 * <li>(only some mice) Scroll Wheel Tilt Right (SWTR, Wheel Tilt Right, Scroll Right)</li>
	 * </ol>
	 * Avoid using any values marked "only some mice" unless it's a user option or a non-essential feature, as many mice don't have those
	 * buttons.
	 */
	int mouseButton;
	
	public MouseUpEvent(final PaneComponent source, final int mouseX, final int mouseY, final int mouseButton) {
		super(source);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.mouseButton = mouseButton;
	}
}
