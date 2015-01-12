package gminers.glasspane.component;


import gminers.kitchensink.Rendering;
import lombok.Getter;
import lombok.Setter;

import org.lwjgl.opengl.GL11;


/**
 * Implements a container that renders a dark background, similar to the way GuiSlot's central area looks.<br>
 * Yes, you can still render borders and text if you want to.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneShadowPanel
		extends PanePanel {
	/**
	 * The depth of the shadow, if enabled.
	 */
	@Getter @Setter private int shadowDepth = 3;
	
	public PaneShadowPanel() {
		setShowBorder(false);
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		final int col = 0x88000000;
		GL11.glPushMatrix();
		Rendering.drawRect(0, 0, width, height, col);
		Rendering.drawGradientRect(0, 0, width, shadowDepth, 0xFF000000, 0x00000000, 0);
		Rendering.drawGradientRect(0, height - shadowDepth, width, height, 0x00000000, 0xFF000000, 0);
		GL11.glPopMatrix();
		super.doRender(mouseX, mouseY, partialTicks);
	}
}
