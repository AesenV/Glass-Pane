package gminers.glasspane.component;


import gminers.kitchensink.Rendering;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


/**
 * A box with a gradient. Supports transparency.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
public class PaneGradientBox
		extends ColorablePaneComponent {
	/**
	 * The second color of this component.
	 */
	int color2 = 0xFFFFFF;
	
	public PaneGradientBox(final int color, final int color2) {
		this.color = color;
		this.color2 = color2;
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		Rendering.drawGradientRect(0, 0, width, height, color, color2, 0);
	}
	
	/**
	 * Returns a PaneGradientBox that looks like the background of the pause menu.
	 */
	public static PaneGradientBox createPauseBackgroundBox() {
		final PaneGradientBox pgb = new PaneGradientBox(0xC0101010, 0xD0101010);
		pgb.setAutoResize(true);
		return pgb;
	}
	
}
