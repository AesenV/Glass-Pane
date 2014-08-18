package gminers.glasspane.shadowbox;


import gminers.kitchensink.Rendering;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * Implements a Shadowbox that draws a solid color.
 * 
 * @author Aesen Vismea
 * 
 */
@AllArgsConstructor
public class SolidShadowbox
		extends PaneShadowbox {
	@Getter @Setter protected int color = 0xFF000000;
	
	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		Rendering.drawRect(0, 0, width, height, color);
	}
	
	@Override
	public void tick() {}
	
	@Override
	public void winch() {}
	
}
