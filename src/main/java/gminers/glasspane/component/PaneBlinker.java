package gminers.glasspane.component;


import gminers.kitchensink.Rendering;
import gminers.kitchensink.WaveType;
import lombok.Getter;
import lombok.Setter;


/**
 * A pulsing component, to bring attention. May be useful to make buttons more apparent in a "first run" scenario.<br>
 * The bounds of the component are used as the 'core' area to pulsate around. The minimum distance the ring will pulsate to is around the
 * inner edge of the bounds of this component. When created, this component will default clipToSize to false due to this behavior.<br>
 * <br>
 * Illustration:<br>
 * <img src="http://dl.gameminers.com/blinker.png"/>
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneBlinker
		extends ColorablePaneComponent {
	/**
	 * Whether or not this component is currently blinking.
	 */
	@Getter @Setter private boolean		blinking	= true;
	/**
	 * The maximum distance to pulsate to.
	 */
	@Getter @Setter private int			distance	= 10;
	/**
	 * The wave this component will use for pulsating.
	 */
	@Getter @Setter private WaveType	wave		= WaveType.ABSOLUTE_TANGENT;
	/**
	 * The speed that this component will pulsate at.
	 */
	@Getter @Setter private double		speed		= 4;
	
	private int							tickCounter	= 0;
	
	public PaneBlinker() {
		setClipToSize(false);
		setColor(0xFF0000);
	}
	
	@Override
	protected void doTick() {
		if (blinking) {
			tickCounter++;
		}
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		if (blinking) {
			final double wv = wave.calculate((tickCounter + partialTicks) / speed);
			final float dist = (float) (wv * distance);
			final int col = color | (((int) ((1 - wv) * 255D) & 0xFF) << 24);
			Rendering.drawRect(-dist, -dist, width + dist, (-dist) + 1, col, 0.0);
			Rendering.drawRect(-dist, (-dist) + 1, (-dist) + 1, height + dist, col, 0.0);
			Rendering.drawRect((-dist) + 1, height + dist, width + dist, (height + dist) - 1, col, 0.0);
			Rendering.drawRect(width + dist, (-dist) + 1, (width + dist) - 1, (height + dist) - 1, col, 0.0);
		}
	}
	
}
