package gminers.glasspane.component.progress;


import gminers.glasspane.component.ColorablePaneComponent;
import gminers.kitchensink.ReadableNumbers;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;


/**
 * Base class for all progress indicators.
 * 
 * @author Aesen Vismea
 * @see PaneProgressBar
 * @see PaneProgressRing
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public abstract class PaneProgressIndicator
		extends ColorablePaneComponent {
	/**
	 * The font renderer for this progress indicator.
	 */
	FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
	/**
	 * Whether or not the progress text for this indicator is shown.
	 */
	boolean progressTextShown = false;
	/**
	 * The custom progress text for this indicator. Set to null to use the default, as specified by the progressTextStyle.
	 */
	String progressText = null;
	/**
	 * The current progress of this indicator.
	 */
	@Setter(AccessLevel.NONE) long progress = 0;
	/**
	 * The maximum progress of this indicator.
	 */
	@Setter(AccessLevel.NONE) long maximumProgress = 100;
	/**
	 * Whether or not this indicator should render in Indeterminate mode - in this mode, an indicator shows a repeating animation and
	 * disregards progress and maximumProgress.
	 */
	boolean indeterminate = false;
	/**
	 * Whether or not the progress text should be rendered at half size.
	 */
	boolean smallProgressText = false;
	/**
	 * The color of the filled portion of this indicator.
	 */
	int filledColor = 0x55FF55;
	/**
	 * The color of the filled portion of the indicator, when in Indeterminate mode.
	 */
	int indeterminateColor = 0x5555FF;
	/**
	 * The style to use for the default progress text. Only regarded when progressText is null.
	 */
	ProgressTextStyle progressTextStyle = ProgressTextStyle.PERCENTAGE;
	@Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE) private long lastAmount = 0;
	@Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE) private String timeEstimate = "about 7 eternities remaining";
	@Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE) private long lastTimeEstimateUpdate = 0;
	/**
	 * Whether or not to apply an OpenGL negation blend to the rendered progress text.
	 */
	boolean invertProgressText = true;
	/**
	 * The color to use for the progress text. White (0xFFFFFF) is recommended when invertProgressText is enabled.
	 */
	int progressTextColor = 0xFFFFFF;
	/**
	 * Whether or not to 'smooth' the changing of the time estimate. With this off, the time estimate will constantly change as it tries to
	 * reflect how long it would take if it continued at exactly this pace until completion. With this on, changes in estimate will be
	 * smoothed, making the changes less extreme.
	 */
	boolean smoothTimeEstimate = true;
	
	public PaneProgressIndicator() {
		color = 0x333333;
	}
	
	/**
	 * The current progress of this indicator.
	 */
	public void setProgress(long progress) {
		if (progress < 0) {
			progress = 0;
		}
		if (progress > maximumProgress) {
			progress = maximumProgress;
		}
		this.progress = progress;
	}
	
	/**
	 * The maximum progress of this indicator.
	 */
	public void setMaximumProgress(long maximumProgress) {
		if (maximumProgress < 1) {
			maximumProgress = 1;
		}
		if (progress > maximumProgress) {
			progress = maximumProgress;
		}
		this.maximumProgress = maximumProgress;
	}
	
	/**
	 * The progress of this indicator, divided by it's maximum progress. Tip: multiply this by 100 for a percentage.
	 */
	public double getPercentage() {
		return (double) progress / (double) maximumProgress;
	}
	
	protected void drawProgressText(final float progress) {
		if (progressTextShown) {
			if (invertProgressText) {
				GL11.glPushMatrix();
				GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
				GL11.glEnable(GL11.GL_BLEND);
			}
			String text;
			if (progressText != null) {
				text = progressText;
			} else {
				if (indeterminate) {
					text = "?";
				} else {
					switch (progressTextStyle) {
						case DECIMAL:
							text = progress + "";
							break;
						case DECIMAL_WITH_TIME_ESTIMATE:
							text = progress + ", " + getTimeEstimate();
							break;
						case FRACTION:
							text = (Math.round(progress * maximumProgress)) + "/" + maximumProgress;
							break;
						case FRACTION_WITH_TIME_ESTIMATE:
							text = (Math.round(progress * maximumProgress)) + "/" + maximumProgress + ", "
									+ getTimeEstimate();
							break;
						case PERCENTAGE:
							text = (Math.round(progress * 100f)) + "%";
							break;
						case BARE_PERCENTAGE:
							text = (Math.round(progress * 100f)) + "";
							break;
						case PERCENTAGE_WITH_TIME_ESTIMATE:
							text = (Math.round(progress * 100f)) + "%, " + getTimeEstimate();
							break;
						default:
							text = "???";
							break;
					
					}
				}
			}
			GL11.glPushMatrix();
			final int textX = (width / 2) - renderer.getStringWidth(text) / 2;
			final int textY = (height / 2) - (renderer.FONT_HEIGHT / 2);
			if (smallProgressText) {
				GL11.glScalef(0.5f, 0.5f, 0.0f);
			}
			renderer.drawString(text, textX, textY, progressTextColor);
			GL11.glPopMatrix();
			if (invertProgressText) {
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}
	}
	
	@Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE) private long lastMillisEstimate = -1;
	
	protected long calculateMillisEstimate() {
		return (long) (((maximumProgress - progress) / Math.max(progress - lastAmount, 0.00000000000000001)) * 1000);
	}
	
	/**
	 * Gets the current time estimate - e.g. "about 10 minutes"<br/>
	 * If the progress is the same as the maximumProgress, "done" is returned. If the progress or maximumProgress is equal to 0,
	 * "not yet started" is returned.
	 */
	public String getTimeEstimate() {
		if (progress == maximumProgress) {
			return "done";
		}
		if (progress == 0 || maximumProgress == 0) {
			return "not yet started";
		}
		if (System.currentTimeMillis() - lastTimeEstimateUpdate >= 1000) {
			long estimate = calculateMillisEstimate();
			if (smoothTimeEstimate) {
				if (lastMillisEstimate > 0) {
					estimate = (lastMillisEstimate + lastMillisEstimate + lastMillisEstimate + estimate) / 4;
				}
				lastMillisEstimate = estimate;
			}
			timeEstimate = ("about " + ReadableNumbers.humanReadableMillis(estimate) + " remaining");
			lastAmount = progress;
			lastTimeEstimateUpdate = System.currentTimeMillis();
		}
		return timeEstimate;
	}
	
	/**
	 * The style to use for progress text. Only acknowledged if progressText is null, but progressTextShown is true, causing the default
	 * text to appear.
	 * 
	 * @author Aesen Vismea
	 * 
	 */
	public enum ProgressTextStyle {
		/**
		 * 0%, 20%, 50%, 100%, etc
		 */
		PERCENTAGE,
		/**
		 * 0, 20, 50, 100, etc
		 */
		BARE_PERCENTAGE,
		/**
		 * 0/20, 0/50, 1/20, 40/60, etc
		 */
		FRACTION,
		/**
		 * 0.0, 0.24582, 0.4555, 0.333333333, 1.0, etc
		 */
		DECIMAL,
		/**
		 * '1%, about 2 minutes remaining', '40%, about 1 hour remaining', etc.<br/>
		 * Some more compact progress indicators may reject this value due to it's verbosity.
		 */
		PERCENTAGE_WITH_TIME_ESTIMATE,
		/**
		 * '2/250, about 2 minutes remaining', '50/400, about 1 hour remaining', etc.<br/>
		 * Some more compact progress indicators may reject this value due to it's verbosity.
		 */
		FRACTION_WITH_TIME_ESTIMATE,
		/**
		 * '0.1, about 2 minutes remaining', '0.374623, about 1 hour remaining', etc.<br/>
		 * Some more compact progress indicators may reject this value due to it's verbosity.
		 */
		DECIMAL_WITH_TIME_ESTIMATE
	}
	
}
