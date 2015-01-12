package gminers.glasspane.component.button;


import gminers.glasspane.HorzAlignment;
import gminers.glasspane.VertAlignment;
import gminers.glasspane.component.Focusable;
import gminers.glasspane.component.text.PaneLabel;
import gminers.glasspane.event.ComponentActivateEvent;
import gminers.glasspane.event.FocusGainedEvent;
import gminers.glasspane.listener.PaneEventHandler;
import gminers.kitchensink.Rendering;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;


/**
 * Implements a clickable button.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class PaneButton
		extends PaneLabel
		implements Focusable {
	/**
	 * Whether or not this button should render as enabled, and should be allowed to be clicked and have the focus.
	 */
	boolean enabled = true;
	/**
	 * The color of this button. If set to 0xFFFFFF, renders the same as a default Minecraft button.
	 */
	int buttonColor = 0xFFFFFF;
	/**
	 * The color of the text of this button when it is disabled.
	 */
	int disabledColor = 0xA0A0A0;
	/**
	 * The color of the text of this button when hovering.
	 */
	int hoveredColor = 0xFFFFA0;
	
	public PaneButton() {
		this("Button");
	}
	
	public PaneButton(final String text) {
		this.text = text;
		color = 0xE0E0E0;
		alignmentX = HorzAlignment.MIDDLE;
		alignmentY = VertAlignment.MIDDLE;
		width = 200;
		height = 20;
	}
	
	@Override
	protected void doTick() {
		super.doTick();
		// if we're disabled and focused, drop the focus
		if (!enabled) {
			if (getParent() != null) {
				if (getParent().getFocusedComponent() == this) {
					getParent().setFocusedComponent(null);
				}
			}
		}
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		// determine the u and v offsets we want
		final int u = 0;
		int v = 0;
		final boolean hover = Mouse.isInsideWindow() && withinBounds(mouseX, mouseY);
		if (enabled) {
			v += 40;
			if (buttonColor != 0xFFFFFF) {
				v += 80;
			}
			if (hover || (this instanceof PaneToggleButton && ((PaneToggleButton) this).isSelected())) {
				v += 40;
			}
		}
		// bind the widgets file
		Minecraft.getMinecraft().renderEngine.bindTexture(RESOURCE);
		
		// unpack the button color
		int r = buttonColor >> 16 & 255;
		int g = buttonColor >> 8 & 255;
		final int b = buttonColor & 255;
		
		if (this instanceof PaneToggleButton && ((PaneToggleButton) this).isSelected()) {
			r -= hover ? 32 : 64;
			g -= hover ? 32 : 64;
		}
		
		// apply the button color
		GL11.glColor3f(r / 255f, g / 255f, b / 255f);
		renderStretchyTexturedRect(0, 0, u, v, width, height, 220, 40);
		
		// if we're focused, draw a blue border over the normal black one
		GL11.glTranslatef(0, 0, 0.001f);
		if (getParent() != null) {
			if (getParent().getFocusedComponent() == this) {
				final int fv = 200;
				renderStretchyTexturedRect(0, 0, u, fv, width, height, 220, 40);
			}
		}
		// change the label's color, if needed
		final int trueColor = color;
		if (!enabled) {
			color = disabledColor;
		} else if (hover) {
			color = hoveredColor;
		}
		// render the label
		GL11.glTranslatef(0, 0, 0.001f);
		if (alignmentX == HorzAlignment.RIGHT && !text.endsWith(" ")) {
			text = text + " ";
		} else if (alignmentX == HorzAlignment.LEFT && !text.startsWith(" ")) {
			text = " " + text;
		}
		labelRender(mouseX, mouseY, partialTicks);
		color = trueColor;
	}
	
	@Override
	public String getText() {
		if (alignmentX == HorzAlignment.RIGHT && text.endsWith(" "))
			return text.substring(0, text.length() - 1);
		else if (alignmentX == HorzAlignment.LEFT && text.startsWith(" ")) return text.substring(1);
		return text;
	}
	
	protected void labelRender(final int mouseX, final int mouseY, final float partialTicks) {
		super.doRender(mouseX, mouseY, partialTicks);
	}
	
	private List<Runnable> activationListeners = Lists.newArrayList();
	
	/**
	 * Convenience method to easily register click listeners.
	 */
	public void registerActivationListener(final Runnable r) {
		Validate.notNull(r, "Runnable cannot be null");
		activationListeners.add(r);
	}
	
	/**
	 * Convenience method to easily unregister click listeners.
	 */
	public void unregisterActivationListener(final Runnable r) {
		activationListeners.remove(r);
	}
	
	@PaneEventHandler
	public void onActivate(final ComponentActivateEvent e) {
		if (enabled) {
			for (final Runnable r : activationListeners) {
				r.run();
			}
			Minecraft.getMinecraft().getSoundHandler()
					.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
		} else {
			e.consume();
		}
	}
	
	@PaneEventHandler
	public void onFocus(final FocusGainedEvent e) {
		if (!enabled) {
			e.consume(); // refuse focus if we're disabled
		}
	}
	
	public static void renderStretchyTexturedRect(int x, int y, int u, int v, int width, int height, int texWidth,
			int texHeight) {
		int hWidth = width / 2;
		int hHeight = height / 2;
		// render the button - this method of rendering gives buttons a max sensible size of 436x84 (for a 220x40 texture)
		// for comparison, a GuiButton can only go up to 390x30
		
		// rendering nine-patch style with a tiled center would allow theoretically infinite button sizes, but that's unnecessary
		Rendering.drawTexturedModalRect(x, y, u, v, hWidth, hHeight, 0);
		Rendering.drawTexturedModalRect(x + hWidth, y, u + (texWidth - hWidth), v, hWidth, hHeight, 0);
		
		Rendering.drawTexturedModalRect(x, y + hHeight, u, v + (texHeight - hHeight), hWidth, hHeight, 0);
		Rendering.drawTexturedModalRect(x + hWidth, y + hHeight, u + (texWidth - hWidth), v + (texHeight - hHeight),
				hWidth, hHeight, 0);
	}
	
	/**
	 * Creates a 'done' button - a button which will call revert() on it's parent GlassPane when clicked, and appears centered near the
	 * bottom of the screen.
	 * 
	 * @return A newly created 'done' button.
	 */
	public static PaneButton createDoneButton() {
		final PaneButton done = new PaneButton("Done");
		done.setAutoPositionX(true);
		done.setRelativeX(0.5);
		done.setRelativeXOffset(-100);
		done.setAutoPositionY(true);
		done.setRelativeY(1.0);
		done.setRelativeYOffset(-30);
		done.registerActivationListener(new Runnable() {
			
			@Override
			public void run() {
				done.getGlassPane().revert();
			}
		});
		return done;
	}
}
