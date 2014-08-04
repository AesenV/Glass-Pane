package gminers.glasspane.component.progress;


import gminers.kitchensink.ReadableNumbers;


/**
 * Implements a bar that displays the JVM's memory usage.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneMemoryBar
		extends PaneProgressHueBar {
	
	public PaneMemoryBar() {
		progressTextShown = true;
		doTick();
		lagHue = targetHue;
	}
	
	@Override
	protected void doTick() {
		maximumProgress = Runtime.getRuntime().maxMemory();
		progress = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory());
		setProgressText(((int) (target * 100f) + "% in use - "
				+ ReadableNumbers.humanReadableByteCount(progress, false) + "/"
				+ ReadableNumbers.humanReadableByteCount(maximumProgress, false) + " - "
				+ ReadableNumbers.humanReadableByteCount(Runtime.getRuntime().freeMemory(), false) + " free"));
		if (target < 0.1) {
			targetHue = 240;
		} else if (target < 0.2) {
			targetHue = 175;
		} else if (target < 0.4) {
			targetHue = 120;
		} else if (target < 0.6) {
			targetHue = 60;
		} else if (target < 0.8) {
			targetHue = 30;
		} else if (target < 0.9) {
			targetHue = 10;
		} else {
			targetHue = (float) ((Math.sin(counter / 10D) / 2D + 0.5) * 10f);
		}
		super.doTick();
	}
}
