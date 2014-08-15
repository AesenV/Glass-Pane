package gminers.glasspane.shadowbox;


import gminers.glasspane.PaneBB;


/**
 * A Shadowbox is a background for a GlassPane, and is rendered before any components.
 * 
 * @author Aesen Vismea
 * 
 */
public abstract class PaneShadowbox
		extends PaneBB {
	/**
	 * Called every frame to render this Shadowbox.
	 * 
	 * @param mouseX
	 *            The X coordinate of the mouse, in 'big' pixels.
	 * @param mouseY
	 *            The Y coordinate of the mouse, in 'big' pixels.
	 * @param partialTicks
	 *            The amount of the way into the next tick we are, since frames do not align with ticks.
	 */
	public abstract void render(final int mouseX, final int mouseY, final float partialTicks);
	
	/**
	 * Called every tick to allow this Shadowbox to animate.
	 */
	public abstract void tick();
	
	/**
	 * Called whenever the display is resized, or this shadowbox is being displayed for the first time.
	 */
	public abstract void winch();
}
