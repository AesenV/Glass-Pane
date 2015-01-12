package gminers.glasspane.component.button;


import gminers.glasspane.HorzAlignment;
import gminers.kitchensink.Rendering;
import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;


/**
 * Implements a checkbox. It's a box, and when it's selected, it has a check in it. Wow.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneCheckBox
		extends PaneToggleButton {
	protected int u = 220;
	
	public PaneCheckBox() {
		this("Checkbox");
	}
	
	public PaneCheckBox(final String text) {
		this(text, false);
	}
	
	public PaneCheckBox(final String text, final boolean selected) {
		alignmentX = HorzAlignment.LEFT;
		this.text = text;
		this.selected = selected;
		lineSpacing = 2;
	}
	
	@Override
	protected void doTick() {
		super.doTick();
		height = Math.max(10, getLineCount() * (renderer.FONT_HEIGHT + lineSpacing));
		width = getLongestLineWidth() + 12;
	}
	
	/**
	 * Ignored since Checkbox needs a specific width
	 */
	@Override
	public void setWidth(final int width) {
		// no
	}
	
	/**
	 * Ignored since Checkbox needs a specific height
	 */
	@Override
	public void setHeight(final int height) {
		// go away
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		// determine the u and v offsets we want
		int v = 0;
		final boolean hover = withinBounds(mouseX, mouseY);
		if (enabled) {
			v += 10;
			if (buttonColor != 0xFFFFFF) {
				v += 20;
			}
			if (hover) {
				v += 10;
			}
		}
		// bind the widgets file
		Minecraft.getMinecraft().renderEngine.bindTexture(RESOURCE);
		
		// unpack the button color
		final int r = buttonColor >> 16 & 255;
		final int g = buttonColor >> 8 & 255;
		final int b = buttonColor & 255;
		
		// apply the button color
		GL11.glColor3f(r / 255f, g / 255f, b / 255f);
		// render the button
		Rendering.drawTexturedModalRect(0, 0, u, v, 10, 10, 0);
		// if we're selected, draw the selected overlay
		if (selected) {
			Rendering.drawTexturedModalRect(0, 0, u, v + 50, 10, 10, 0);
		}
		
		// if we're focused, draw a blue border over the normal black one
		GL11.glTranslatef(0, 0, 0.001f);
		if (getParent() != null) {
			if (getParent().getFocusedComponent() == this) {
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				Rendering.drawTexturedModalRect(0, 0, u, v + 100, 10, 10, 0);
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
		GL11.glTranslatef(12f, 0, 0.001f);
		labelRender(mouseX, mouseY, partialTicks);
		color = trueColor;
	}
}
