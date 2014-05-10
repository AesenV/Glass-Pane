package gminers.glasspane.component;


import gminers.kitchensink.Rendering;


/**
 * A colored box. Supports transparency.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneBox
		extends ColorablePaneComponent {
	public PaneBox(final int color) {
		this.color = color;
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		Rendering.drawRect(0, 0, width, height, color);
	}
	
}
