package gminers.glasspane.component.progress;


import gminers.kitchensink.Rendering;

import org.lwjgl.opengl.GL11;


/**
 * Implements a progress ring, used for displaying how complete an asynchronous long-running operation is. Or whatever other twisted uses
 * you may have for it.
 * 
 * @author Aesen Vismea
 * 
 */
public class PaneProgressRing
		extends PaneProgressBar {
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		int percievedWidth = width;
		int percievedHeight = height;
		if (outlined) {
			GL11.glTranslatef(1, 1, 0);
			percievedHeight -= 2;
			percievedWidth -= 2;
		}
		Rendering.drawRect(0, 0, percievedWidth, 2, color | 0xFF000000);
		Rendering.drawRect(percievedWidth - 2, 2, percievedWidth, percievedHeight, color | 0xFF000000);
		Rendering.drawRect(0, percievedHeight - 2, percievedWidth - 2, percievedHeight, color | 0xFF000000);
		Rendering.drawRect(0, 2, 2, percievedHeight - 2, color | 0xFF000000);
		float lagg = lag;
		if (lag < target) {
			lagg += getAdjustment(target, lag) * partialTicks;
		} else if (lag > target) {
			lagg -= getAdjustment(lag, target) * partialTicks;
		}
		if (indeterminate) {
			// TODO: make this look less awful
			final double appl = (step + partialTicks) / (indeterminateSegmentDivisor * 1.5);
			
			final int segmentLength = (int) (percievedWidth / indeterminateSegmentDivisor);
			final int segmentX = segmentOffset(appl, percievedWidth, segmentLength);
			
			final int segmentHeight = (int) (percievedHeight / indeterminateSegmentDivisor);
			final int segmentY = segmentOffset(appl, percievedHeight, segmentHeight);
			
			// top
			Rendering.drawRect(segmentX, 0, segmentX + segmentLength, 2, indeterminateColor | 0xFF000000);
			// right
			Rendering.drawRect(percievedWidth - 2, segmentY, percievedWidth, segmentY + segmentHeight,
					indeterminateColor | 0xFF000000);
			// bottom
			Rendering.drawRect(segmentX, percievedHeight - 2, segmentX + segmentLength, percievedHeight,
					indeterminateColor | 0xFF000000);
			// left
			Rendering.drawRect(0, segmentY, 2, segmentY + segmentHeight, indeterminateColor | 0xFF000000);
		} else {
			final float total = lagg * 4f;
			final float seg1 = (total >= 1 ? 1 : total);
			final float seg2 = (total >= 2 ? 1 : (total <= 1 ? 0 : total - 1));
			final float seg3 = (total >= 3 ? 1 : (total <= 2 ? 0 : total - 2));
			final float seg4 = (total >= 4 ? 1 : (total <= 3 ? 0 : total - 3));
			Rendering.drawRect(0, 0, (int) (percievedWidth * seg1), 2, filledColor | 0xFF000000);
			if (seg2 > 0) {
				Rendering.drawRect(percievedWidth - 2, 2, percievedWidth, (int) (percievedHeight * seg2),
						filledColor | 0xFF000000);
				if (seg3 > 0) {
					Rendering.drawRect((int) (percievedWidth - (percievedWidth * seg3)), percievedHeight - 2,
							percievedWidth - 2, percievedHeight, filledColor | 0xFF000000);
					if (seg4 > 0) {
						Rendering.drawRect(0, (int) (percievedHeight - ((percievedHeight - 2) * seg4)), 2,
								percievedHeight - 2, filledColor | 0xFF000000);
					}
				}
			}
		}
		drawProgressText(useLagValueForProgressText ? lagg : target);
		if (outlined) {
			GL11.glTranslatef(-1, -1, 0);
			percievedWidth = width;
			percievedHeight = height;
			final int shadowColor = (color & 16579836) >> 2 | color & -16777216;
			Rendering.drawRect(0, 0, percievedWidth, 1, shadowColor | 0xFF000000);
			Rendering.drawRect(percievedWidth - 1, 1, percievedWidth, percievedHeight, shadowColor | 0xFF000000);
			Rendering.drawRect(0, percievedHeight - 1, percievedWidth - 1, percievedHeight, shadowColor | 0xFF000000);
			Rendering.drawRect(0, 0, 1, percievedHeight - 1, shadowColor | 0xFF000000);
			GL11.glTranslatef(3, 3, 0);
			percievedWidth -= 6;
			percievedHeight -= 6;
			Rendering.drawRect(0, 0, percievedWidth, 1, shadowColor | 0xFF000000);
			Rendering.drawRect(percievedWidth - 1, 1, percievedWidth, percievedHeight, shadowColor | 0xFF000000);
			Rendering.drawRect(0, percievedHeight - 1, percievedWidth - 1, percievedHeight, shadowColor | 0xFF000000);
			Rendering.drawRect(0, 0, 1, percievedHeight - 1, shadowColor | 0xFF000000);
		}
	}
	
}
