package gminers.glasspane.component.text;


import gminers.glasspane.event.KeyTypedEvent;
import gminers.glasspane.listener.PaneEventHandler;
import joptsimple.internal.Strings;
import lombok.Getter;

import org.lwjgl.input.Keyboard;


/**
 * A variant of TextField that hides it's contents, and doesn't allow copying or cutting.
 * 
 * @author Aesen Vismea
 * 
 */
public class PanePasswordField
		extends PaneTextField {
	@Getter private String text;
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		final String pureText = text = super.text;
		super.text = Strings.repeat('\u00D7', text.length());
		super.doRender(mouseX, mouseY, partialTicks);
		super.text = pureText;
	}
	
	@PaneEventHandler
	@Override
	public void onKeyType(final KeyTypedEvent e) {
		// precalc the ctrl and shift values
		final boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		// shift is included for IBM-style shortcuts instead of Windows-style
		final boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		if ((ctrl && e.getKeyCode() == Keyboard.KEY_C) || (ctrl && e.getKeyCode() == Keyboard.KEY_INSERT)) {
			// don't allow copying in a password field
			blinkColor = 0xFFFFFF;
			blink = 0.45f;
		} else if ((ctrl && e.getKeyCode() == Keyboard.KEY_X) || (shift && e.getKeyCode() == Keyboard.KEY_DELETE)) {
			// don't allow cutting in a password field
			blinkColor = 0xFFFFFF;
			blink = 0.45f;
		} else {
			super.onKeyType(e);
		}
	}
}
