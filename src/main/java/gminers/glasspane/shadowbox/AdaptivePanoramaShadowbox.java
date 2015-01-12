package gminers.glasspane.shadowbox;


import gminers.kitchensink.Rendering;
import net.minecraft.client.Minecraft;


/**
 * A Shadowbox that acts similar to PanoramaShadowbox, but will render a translucent background instead of the panorama if the player is in
 * a world, similar to a BasicShadowbox with transparentWhenInWorld set to true.
 * 
 * @author Aesen Vismea
 * 
 */
public class AdaptivePanoramaShadowbox
		extends PanoramaShadowbox {
	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		if (Minecraft.getMinecraft().theWorld != null) {
			Rendering.drawGradientRect(0, 0, width, height, -1072689136, -804253680, 0);
		} else {
			super.render(mouseX, mouseY, partialTicks);
		}
	}
}
