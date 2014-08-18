package gminers.glasspane.component.progress;


import gminers.kitchensink.Rendering;
import gminers.kitchensink.WaveType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import org.lwjgl.opengl.GL11;


/**
 * Implements a progress bar, used for displaying how complete an asynchronous long-running operation is. Or whatever other twisted uses you
 * may have for it.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
public class PaneProgressBar
		extends PaneProgressIndicator {
	/**
	 * Whether or not to draw an outline around this bar.
	 */
	@Getter @Setter protected boolean outlined = true;
	/**
	 * The wave to use for the indeterminate animation.
	 */
	@Getter @Setter protected WaveType indeterminateWave = WaveType.SINE;
	/**
	 * Whether or not to allow the indeterminate bar to leave the bounds of the bar. Turning this off is useful for some waves to make them
	 * look better, such as tangent.<br/>
	 * When turning this off, it is usually a good idea to make sure clipToSize is enabled.
	 */
	@Getter @Setter protected boolean indeterminateWaveKeptInBounds = true;
	/**
	 * Whether or not to use the 'lag' value for progress text display.
	 */
	@Getter @Setter protected boolean useLagValueForProgressText = true;
	/**
	 * Defines the width of the indeterminate animation's segment. (width / indeterminateSegmentDivisor = segmentLength)
	 */
	@Getter @Setter protected double indeterminateSegmentDivisor = 5D;
	/**
	 * How accurate this bar should be - 1.0 means it will always follow the value exactly, values less than 1.0 make it animate smoothly
	 * towards the value, with lower values being slower.
	 */
	@Getter @Setter protected double accuracy = 0.25;
	
	protected int step = 0;
	protected float lag = 0;
	protected float target = 0;
	
	
	@Override
	protected void doTick() {
		target = ((float) progress / (float) maximumProgress);
		if (Float.isNaN(lag)) {
			lag = 0f;
		}
		if (!indeterminate) {
			if (lag < target) {
				lag += getAdjustment(target, lag);
			} else if (lag > target) {
				lag -= getAdjustment(lag, target);
			}
		} else {
			step++;
		}
	}
	
	protected float getAdjustment(final float big, final float small) {
		final float diff = (big - small);
		return (float) (diff > 0.000001 ? (diff * accuracy) : diff);
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		int perceivedWidth = width;
		int perceivedHeight = height;
		if (outlined) {
			perceivedWidth -= 2;
			perceivedHeight -= 2;
			GL11.glTranslatef(1, 1, 0);
		}
		Rendering.drawRect(0, 0, perceivedWidth, perceivedHeight, color | 0xFF000000);
		if (indeterminate) {
			final int segmentLength = (int) (perceivedWidth / indeterminateSegmentDivisor);
			final double appl = (step + partialTicks) / indeterminateSegmentDivisor;
			final int segmentX = segmentOffset(appl, perceivedWidth, segmentLength);
			if (outlined) {
				final int shadowColor = (indeterminateColor & 16579836) >> 2 | indeterminateColor & -16777216;
				Rendering.drawRect(segmentX, -1, segmentX + segmentLength, perceivedHeight + 1,
						shadowColor | 0xFF000000);
			}
			Rendering.drawRect(segmentX, 0, segmentX + segmentLength, perceivedHeight, indeterminateColor | 0xFF000000);
		} else {
			float lagg = lag;
			if (lag < target) {
				lagg += getAdjustment(target, lag) * partialTicks;
			} else if (lag > target) {
				lagg -= getAdjustment(lag, target) * partialTicks;
			}
			final int segmentLength = (int) (lagg * perceivedWidth);
			if (outlined && segmentLength > 0) {
				final int shadowColor = (filledColor & 16579836) >> 2 | filledColor & -16777216;
				Rendering.drawRect(-1, -1, segmentLength + (lagg == 1.0 ? 1 : 0), perceivedHeight + 1,
						shadowColor | 0xFF000000);
			}
			Rendering.drawRect(0, 0, segmentLength, perceivedHeight, filledColor | 0xFF000000);
		}
		drawProgressText(useLagValueForProgressText ? lag : target);
		if (outlined) {
			GL11.glTranslatef(-1, -1, 0);
			final int shadowColor = (color & 16579836) >> 2 | color & -16777216;
			Rendering.drawRect(0, 0, width, 1, shadowColor | 0xFF000000);
			Rendering.drawRect(width - 1, 1, width, height, shadowColor | 0xFF000000);
			Rendering.drawRect(0, height - 1, width - 1, height, shadowColor | 0xFF000000);
			Rendering.drawRect(0, 0, 1, height - 1, shadowColor | 0xFF000000);
		}
	}
	
	public float getLagPercentage() {
		return lag;
	}
	
	protected int segmentOffset(final double appl, final int max, final int segmentLength) {
		if (indeterminateWaveKeptInBounds) {
			return (int) (indeterminateWave.calculate(appl) * (max - segmentLength));
		} else {
			return (int) ((indeterminateWave.calculate(appl) * (max + segmentLength))) - segmentLength;
		}
	}
	
}
