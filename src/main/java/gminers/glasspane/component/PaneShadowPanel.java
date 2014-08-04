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
	@Getter @Setter private int	shadowDepth	= 3;
	
	public PaneShadowPanel() {
		setShowBorder(false);
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		final int col = 0x88000000;
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		Rendering.drawRect(0, 0, width, height, col);
		final int opacity = ((int) ((2.0f / shadowDepth) * 255)) << 24;
		for (int i = shadowDepth; i > 0; i--) {
			Rendering.drawRect(0, 0, width, i, opacity);
			Rendering.drawRect(0, height, width, height - i, opacity);
		}
		GL11.glPopMatrix();
		super.doRender(mouseX, mouseY, partialTicks);
	}
}
