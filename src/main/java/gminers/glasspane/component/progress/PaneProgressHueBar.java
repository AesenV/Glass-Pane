package gminers.glasspane.component.progress;


import gminers.kitchensink.Hues;
import lombok.Getter;
import lombok.Setter;


/**
 * Implements a progress bar that sets it's filled color through a (smoothly animated) hue value rather than exact RGB.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneProgressHueBar
		extends PaneProgressBar {
	@Getter @Setter protected float targetHue = 100;
	protected float lagHue = 100;
	protected int counter = 0;
	@Getter @Setter protected int brightnessModifier = 64;
	@Getter @Setter protected int hueChangeSpeed = 12;
	
	public PaneProgressHueBar() {
		doTick();
		lagHue = targetHue;
	}
	
	@Override
	protected void doTick() {
		counter++;
		if (lagHue > targetHue) {
			lagHue -= Math.min(hueChangeSpeed, lagHue - targetHue);
		} else if (lagHue < targetHue) {
			lagHue += Math.min(hueChangeSpeed, targetHue - lagHue);
		}
		int brighterCol = Hues.hueToRGB((int) lagHue);
		int r = brighterCol >> 16 & 0xFF;
		int g = brighterCol >> 8 & 0xFF;
		int b = brighterCol & 0xFF;
		r += brightnessModifier;
		g += brightnessModifier;
		b += brightnessModifier;
		r = Math.min(r, 255);
		g = Math.min(g, 255);
		b = Math.min(b, 255);
		brighterCol = r << 16 | g << 8 | b;
		filledColor = brighterCol;
		super.doTick();
	}
}
