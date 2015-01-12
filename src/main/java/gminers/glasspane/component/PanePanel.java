package gminers.glasspane.component;


import gminers.glasspane.HorzAlignment;
import gminers.glasspane.VertAlignment;
import gminers.kitchensink.Rendering;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;


/**
 * Implements a basic container that can have a border (with text!) displayed around it's contents.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class PanePanel
		extends PaneContainer {
	/**
	 * The X alignment of the text in the border.
	 */
	HorzAlignment borderAlignmentX = HorzAlignment.LEFT;
	/**
	 * The Y alignment of the text in the border. Does not allow MIDDLE.
	 */
	VertAlignment borderAlignmentY = VertAlignment.TOP;
	/**
	 * The font renderer to use for the text in the border.
	 * 
	 * @see Minecraft#fontRendererObj
	 * @see Minecraft#standardGalacticFontRenderer
	 */
	FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
	/**
	 * The text to render in the border. <code>null</code> for a solid border with no text.
	 */
	String borderText = null;
	/**
	 * The thickness of the border, in 'big' pixels.
	 */
	int borderThickness = 1;
	/**
	 * The color to use for the border and the border text.
	 */
	int borderColor = 0xFFFFFF;
	/**
	 * Whether or not to render a shadow for the border.
	 */
	boolean borderShadow = true;
	/**
	 * Whether or not to render a shadow for the border text.
	 */
	boolean borderTextShadow = true;
	/**
	 * Whether or not to actually render a border - setting this to false makes this a dumb container that just renders it's children.
	 */
	boolean showBorder = true;
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		// if we're drawing a border, do a bunch of super complicated awesome
		if (showBorder) {
			// precalc the border offsets
			final int borderOffsetY = Math.max(borderThickness + 1, (borderText == null ? 0 : renderer.FONT_HEIGHT));
			final int borderOffsetX = borderThickness + 1;
			// make new vars for width and height to use since we're clipped to our size
			int percievedWidth = width;
			if (borderShadow) {
				percievedWidth--;
			}
			int percievedHeight = height;
			if (borderShadow) {
				percievedHeight--;
			}
			// precalc borderOffsetY's halved value
			final int borderOffsetYH = borderOffsetY / 2;
			if (borderText != null) {
				// if we have text, we'll render a different border
				
				// first, precalc the text's width
				final int textWidth = renderer.getStringWidth(borderText);
				// calculate the X offset of the text based off the alignment
				int textX = 8;
				if (borderAlignmentX == HorzAlignment.MIDDLE) {
					textX = (width / 2) - (textWidth / 2);
				} else if (borderAlignmentX == HorzAlignment.RIGHT) {
					textX = (width - 8) - textWidth;
				}
				if (borderAlignmentY == VertAlignment.TOP) {
					// if it's the top, draw a border like so
					
					// top & text
					drawRect(0, borderOffsetYH, textX - 2, (borderOffsetYH) + borderThickness, borderColor | 0xFF000000);
					drawRect((textX + textWidth) + 2, borderOffsetYH, percievedWidth, (borderOffsetYH)
							+ borderThickness, borderColor | 0xFF000000);
					// draw the other 3 sides
					drawDefaultBorder(percievedWidth, percievedHeight, borderOffsetYH, false, true, true, true);
					// draw the text
					renderer.drawString(borderText, textX, 0, borderColor, borderTextShadow);
				} else if (borderAlignmentY == VertAlignment.BOTTOM) {
					// if it's the bottom, draw a border like so
					
					// draw the other 3 sides
					drawDefaultBorder(percievedWidth, percievedHeight - borderOffsetYH, 0, true, true, true, false);
					// bottom & text
					drawRect(0, percievedHeight - (borderOffsetYH), textX - 2, (percievedHeight - (borderOffsetYH))
							+ borderThickness, borderColor | 0xFF000000);
					drawRect((textX + textWidth) + 2, percievedHeight - (borderOffsetYH), percievedWidth,
							(percievedHeight - (borderOffsetYH)) + borderThickness, borderColor | 0xFF000000);
					// draw the text
					renderer.drawString(borderText, textX, percievedHeight - renderer.FONT_HEIGHT, borderColor,
							borderTextShadow);
				} else {
					// we can't draw a middle-aligned Y, so just draw the default as a fallback
					// TODO - draw 90-degree rotated text on X align LEFT or RIGHT?
					drawDefaultBorder(percievedWidth, percievedHeight, borderOffsetYH, true, true, true, true);
				}
			} else {
				// no text, a solid border will do
				drawDefaultBorder(percievedWidth, percievedHeight, borderOffsetYH, true, true, true, true);
			}
			// perform the translation
			GL11.glTranslatef(borderOffsetX, borderOffsetY, 0);
			// render the components
			final int pX = getPX();
			final int pY = getPY();
			for (final PaneComponent pc : components) {
				pc.render(mouseX - pX, mouseY - pY, partialTicks);
			}
		} else {
			// otherwise, just call super
			super.doRender(mouseX, mouseY, partialTicks);
		}
	}
	
	private void drawDefaultBorder(final int percievedWidth, final int percievedHeight, final int borderOffsetYH,
			final boolean top, final boolean right, final boolean left, final boolean bottom) {
		if (top) {
			drawRect(0, 0, percievedWidth, borderThickness, borderColor | 0xFF000000);
		}
		if (right) {
			drawRect(percievedWidth - borderThickness, borderOffsetYH, percievedWidth, percievedHeight,
					borderColor | 0xFF000000);
		}
		if (left) {
			drawRect(0, borderOffsetYH, borderThickness, percievedHeight, borderColor | 0xFF000000);
		}
		if (bottom) {
			drawRect(0, percievedHeight - borderThickness, percievedWidth, percievedHeight, borderColor | 0xFF000000);
		}
	}
	
	private void drawRect(final int x1, final int y1, final int x2, final int y2, final int color) {
		if (borderShadow) {
			final int shadowColor = (color & 16579836) >> 2 | color & -16777216;
			Rendering.drawRect(x1 + 1, y1 + 1, x2 + 1, y2 + 1, shadowColor);
		}
		Rendering.drawRect(x1, y1, x2, y2, color);
	}
	
	@Override
	protected int getPX() {
		final int borderOffsetX = borderThickness + 1;
		return super.getPX() + borderOffsetX;
	}
	
	@Override
	protected int getPY() {
		final int borderOffsetY = Math.max(borderThickness + 1, (borderText == null ? 0 : renderer.FONT_HEIGHT));
		final int translateY = borderAlignmentY == VertAlignment.TOP ? borderOffsetY : borderThickness + 1;
		return super.getPY() + translateY;
	}
}
