package gminers.glasspane.component.text;


import gminers.glasspane.VertAlignment;
import gminers.glasspane.component.Focusable;
import gminers.glasspane.component.PaneImage;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.event.KeyTypedEvent;
import gminers.glasspane.listener.PaneEventHandler;
import gminers.kitchensink.Rendering;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
public class PaneTextField
		extends PaneLabel
		implements Focusable {
	int counter = 0;
	/**
	 * The current position of the carat.
	 */
	@Getter @Setter int cursorPos = 0;
	/**
	 * The current opacity of the visual bell.
	 */
	@Getter @Setter float blink = 0.0f;
	/**
	 * The current color of the visual bell.
	 */
	@Getter @Setter int blinkColor = 0xFFFFFF;
	/**
	 * The current offset of the view of the text.
	 */
	@Getter @Setter int viewPos = 0;
	final StringBuilder str = new StringBuilder();
	int trimmedLength = 0;
	/**
	 * The text to show when the TextField is empty.
	 */
	@Getter @Setter String blankText = "";
	/**
	 * The color to use when showing blankText.
	 */
	@Getter @Setter int blankColor = 0x888888;
	/**
	 * Whether or not to blink the text field when something happens.<br/>
	 * The following colors are used:
	 * <ul>
	 * <li>Green - Copy succeeded</li>
	 * <li>Yellow - Cut succeeded</li>
	 * <li>Red - Paste succeeded</li>
	 * <li>White - Cannot currently do that</li>
	 * </ul>
	 * Other, custom, colors can also be used, but these four are the only ones used by default.
	 */
	@Getter @Setter boolean visualBellEnabled = true;
	/**
	 * An icon to put to the left of the text. Can be used for identification purposes or decoration.<br/>
	 * Null means 'do not show'.
	 */
	@Getter @Setter ResourceLocation icon = null;
	/**
	 * The U (X texture offset) to use when rendering the icon
	 */
	@Getter @Setter int iconU = 0;
	/**
	 * The V (Y texture offset) to use when rendering the icon
	 */
	@Getter @Setter int iconV = 0;
	/**
	 * The width of the portion of the icon's image to use - 256 for the entire image
	 */
	@Getter @Setter int iconImageWidth = 256;
	/**
	 * The height of the portion of the icon's image to use - 256 for the entire image.
	 */
	@Getter @Setter int iconImageHeight = 256;
	/**
	 * A 24-bit packed color to use for the icon. (first 8 bits are ignored, see {@link #alpha})
	 */
	@Getter @Setter int iconColor = 0xFFFFFF;
	/**
	 * The alpha transparency of the icon - 0.0 is completely transparent, 1.0 is opaque
	 */
	@Getter @Setter float alpha = 1.0f;
	/**
	 * Whether or not to use one-bit transparency for the icon. One-bit transparency is faster, but if your image is partially
	 * transparent, it will render as fully opaque.
	 */
	@Getter @Setter boolean oneBitTransparency = true;
	
	public PaneTextField() {
		alignmentY = VertAlignment.MIDDLE;
		width = 200;
		height = 20;
		setActivatedOnClick(false);
	}
	
	public PaneTextField(final String text) {
		this();
		setText(text);
	}
	
	@Override
	protected void doTick() {
		// add to the counter
		counter++;
		// if we're allowing visual bells, and we need to decrement the bell opacity, do so
		if (visualBellEnabled && blink > 0) {
			// make sure we don't go negative
			blink -= Math.min(blink, 0.1);
		}
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		// clamp cursor pos
		if (cursorPos < 0) {
			cursorPos = 0;
		} else if (cursorPos > str.length()) {
			cursorPos = str.length() - 1;
		}
		// bind the widgets
		Minecraft.getMinecraft().renderEngine.bindTexture(RESOURCE);
		
		// u and v, for convenient changing
		final int u = 0;
		final int v = 0;
		
		// set color
		GL11.glColor3f(0.6f, 0.6f, 0.6f);
		PaneButton.renderStretchyTexturedRect(0, 0, u, v, width, height, 220, 40);
		
		// translate to the right to make text less stupid looking
		GL11.glTranslatef(4f, 0f, 0f);
		if (icon != null) {
			GL11.glTranslatef(getHeight() - 4, 0, 0);
		}
		// trim the text to the component width
		final String oldText = text;
		final int oldColor = color;
		if (text.isEmpty() || text == blankText) {
			text = blankText;
			color = blankColor;
		}
		String trimmedText;
		if (viewPos > text.length()) {
			viewPos = text.length() - trimmedLength;
		}
		text = trimmedText = renderer.trimStringToWidth(text.substring(viewPos), getWidth() - 8
				- (icon != null ? getHeight() - 4 : 0));
		trimmedLength = text.length();
		// shift the view pos if needed
		if (cursorPos > viewPos + trimmedLength) {
			viewPos = cursorPos - trimmedLength;
		} else if (cursorPos < viewPos) {
			viewPos = cursorPos;
		}
		// render the label
		super.doRender(mouseX, mouseY, partialTicks);
		text = oldText;
		color = oldColor;
		// if we're focused, draw focus-y things
		if (getParent() != null && getParent().getFocusedComponent() == this) {
			final int llw = renderer.getStringWidth(trimmedText.substring(0,
					Math.min(trimmedLength, Math.max(0, cursorPos - viewPos))));
			final int opacity = 255 - ((int) ((counter + partialTicks) * 15) % 255);
			int hHeight = height / 2;
			// such as a carat (if the window is also focused)
			if (Display.isActive()) {
				Rendering.drawRect(llw - 1, hHeight - ((renderer.FONT_HEIGHT / 2) + 1), llw, hHeight
						+ (renderer.FONT_HEIGHT / 2), 0x00FFFFFF | opacity << 24);
			}
			if (icon != null) {
				GL11.glTranslatef(-(getHeight() - 4), 0, 0);
			}
			GL11.glTranslatef(-4f, 0f, 0f);
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			// and ye olde blue outline
			Minecraft.getMinecraft().renderEngine.bindTexture(RESOURCE);
			final int fv = 200;
			PaneButton.renderStretchyTexturedRect(0, 0, u, fv, width, height, 220, 40);
		} else {
			// if we aren't focused, just undo the translate
			GL11.glTranslatef(-4f, 0f, 0f);
			if (icon != null) {
				GL11.glTranslatef(-(getHeight() - 4), 0, 0);
			}
		}
		// draw some indications there's more text if we're not showing it all
		if (viewPos > 0) {
			Rendering.drawHorzGradientRect(1, 1, 2 + (icon != null ? getHeight() - 4 : 0), height - 1, 0x99FFFFFF,
					0x33FFFFFF, 0);
		}
		if (viewPos + trimmedLength < str.length()) {
			Rendering.drawHorzGradientRect(width - 1, height - 1, width - 2, 1, 0x99FFFFFF, 0x33FFFFFF, 0);
		}
		// if we are allowing visual bell, draw the current visual bell values
		if (visualBellEnabled) {
			final int blinkOpacity = (int) ((blink - (Math.min(blink, 0.1) * partialTicks)) * 255);
			Rendering.drawRect(1, 1, width - 1, height - 1, blinkColor | (blinkOpacity << 24));
		}
		
		// render the icon
		if (icon != null) {
			PaneImage.render(icon, 2, 2, iconU, iconV, getHeight() - 4, getHeight() - 4, iconImageWidth,
					iconImageHeight, iconColor, 1.0f, false);
		}
	}
	
	@Override
	public void setText(final String text) {
		viewPos = 0;
		cursorPos = 0;
		str.delete(0, str.length());
		str.append(text);
		this.text = text;
	}
	
	@PaneEventHandler
	public void onKeyType(final KeyTypedEvent e) {
		if (getParent() == null || getParent().getFocusedComponent() != this) return;
		// precalc the ctrl and shift values
		final boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		// shift is included for IBM-style shortcuts instead of Windows-style
		final boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		if ((ctrl && e.getKeyCode() == Keyboard.KEY_C) || (ctrl && e.getKeyCode() == Keyboard.KEY_INSERT)) {
			// if they're holding ^C or ^Ins...
			// copy to the clipboard
			final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			clip.setContents(new StringSelection(text), null);
			// 'sound' the visual bell in a green color for feedback
			blinkColor = 0x55FF55;
			blink = 0.5f;
		} else if ((ctrl && e.getKeyCode() == Keyboard.KEY_X) || (shift && e.getKeyCode() == Keyboard.KEY_DELETE)) {
			// if they're holding ^X or _Del...
			// copy to the clipboard
			final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			clip.setContents(new StringSelection(text), null);
			// clear the text
			str.delete(0, str.length());
			text = "";
			// reset the cursor and view positions
			cursorPos = 0;
			viewPos = 0;
			// 'sound' the visual bell in a yellow color for feedback
			blinkColor = 0xFFFF55;
			blink = 0.75f;
		} else if ((ctrl && e.getKeyCode() == Keyboard.KEY_V) || (shift && e.getKeyCode() == Keyboard.KEY_INSERT)) {
			// if they're holding ^V or _Ins...
			// paste from the clipboard
			final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			// cache the contents as it's recreated every time this is called - with large payloads, this is very costly
			final Transferable cont = clip.getContents(null);
			// make sure it can be represented by a string
			if (cont.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					final String data = String.valueOf(cont.getTransferData(DataFlavor.stringFlavor));
					// finally, put it into the string buffer
					str.insert(cursorPos, data);
					text = str.toString();
					cursorPos += data.length();
					// and 'sound' a red visual bell for feedback.
					blinkColor = 0xFF5555;
					blink = 0.5f;
				} catch (final UnsupportedFlavorException e1) {
					e1.printStackTrace();
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() > 31 && e.getKeyChar() < 127) {
			// if it's a letter/digit, or a printable ASCII character, insert it
			// this won't fit all cases for international text input, but should be adequate for US and Euro layouts (for the most part)
			str.insert(cursorPos, e.getKeyChar());
			text = str.toString();
			cursorPos++;
		} else if (e.getKeyCode() == Keyboard.KEY_BACK) {
			// if it's backspace, and we actually have something to backspace
			if (str.length() > 0 && cursorPos > 0) {
				// then delete it
				str.deleteCharAt(cursorPos - 1);
				text = str.toString();
				cursorPos--;
			} else {
				// otherwise, 'sound' a quick white visual bell
				blink = 0.45f;
				blinkColor = 0xFFFFFF;
			}
		} else if (e.getKeyCode() == Keyboard.KEY_DELETE) {
			// if it's delete, and we actually have something to delete
			if (cursorPos < str.length()) {
				// then delete it
				str.deleteCharAt(cursorPos);
				text = str.toString();
			} else {
				// otherwise, 'sound' a quick white visual bell
				blink = 0.45f;
				blinkColor = 0xFFFFFF;
			}
		} else if (e.getKeyCode() == Keyboard.KEY_LEFT) {
			// if we're pressing left and actually have space to go left
			if (cursorPos > 0) {
				// then go left
				cursorPos--;
			} else {
				// otherwise, 'sound' a quick white visual bell
				blink = 0.45f;
				blinkColor = 0xFFFFFF;
			}
		} else if (e.getKeyCode() == Keyboard.KEY_RIGHT) {
			// if we're pressing right and actually have space to go right
			if (cursorPos < text.length()) {
				// then go right
				cursorPos++;
			} else {
				// otherwise, 'sound' a quick white visual bell
				blink = 0.45f;
				blinkColor = 0xFFFFFF;
			}
		} else if (e.getKeyCode() == Keyboard.KEY_END) {
			// just jump straight to the ending char when End is pressed
			cursorPos = text.length();
		} else if (e.getKeyCode() == Keyboard.KEY_HOME) {
			// just jump straight to the beginning char when Home is pressed
			cursorPos = 0;
		}
	}
}
