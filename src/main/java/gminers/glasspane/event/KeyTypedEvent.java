package gminers.glasspane.event;


import gminers.glasspane.component.PaneComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import org.lwjgl.input.Keyboard;


/**
 * Called when a key is pressed, on the component that has the focus (or a GlassPane that is currently being displayed, either with show()
 * or overlay())
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
@Getter
public class KeyTypedEvent
		extends PaneEvent {
	public static final Class<?>[] SIGNATURE = {
			PaneComponent.class,
			char.class,
			int.class
	};
	/**
	 * A char representing the key being pressed.
	 */
	char keyChar;
	/**
	 * The {@link Keyboard} key code being pressed, or KEY_NONE if only keyChar can represent it properly.
	 */
	int keyCode;
	
	public KeyTypedEvent(final PaneComponent source, final char keyChar, final int keyCode) {
		super(source);
		this.keyChar = keyChar;
		this.keyCode = keyCode;
	}
}
