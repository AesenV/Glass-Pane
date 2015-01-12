package gminers.glasspane.component.text;


import gminers.glasspane.HorzAlignment;
import gminers.glasspane.VertAlignment;
import gminers.glasspane.component.ColorablePaneComponent;
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
 * Implements a simple component that displays some text.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class PaneLabel
		extends ColorablePaneComponent {
	/**
	 * The text to render for this label. Allows newlines.
	 */
	@Setter(AccessLevel.NONE) String text = "";
	/**
	 * The font renderer to use for the text of this label.
	 * 
	 * @see Minecraft#fontRenderer
	 * @see Minecraft#standardGalacticFontRenderer
	 */
	FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
	/**
	 * Whether or not to render a shadow for this text.
	 */
	boolean shadow = true;
	/**
	 * Whether or not to outline this text.
	 */
	boolean outlined = false;
	/**
	 * Whether or not to put a more vibrant color for the outline, instead of the default of a darker color.
	 */
	boolean invertedOutline = false;
	/**
	 * The X alignment of this label.
	 */
	HorzAlignment alignmentX = HorzAlignment.LEFT;
	/**
	 * The Y alignment of this label.
	 */
	VertAlignment alignmentY = VertAlignment.TOP;
	/**
	 * Whether or not to render this label at 50% size.
	 */
	boolean small = false;
	/**
	 * The amount of pixels between lines of text.
	 */
	int lineSpacing = 4;
	
	public PaneLabel() {
		recalculateSize();
	}
	
	public PaneLabel(final String text) {
		this.text = text;
		recalculateSize();
	}
	
	public PaneLabel(final String text, final int color) {
		this.text = text;
		recalculateSize();
		this.color = color;
	}
	
	public PaneLabel(final String text, final int color, final FontRenderer renderer) {
		this.text = text;
		recalculateSize();
		this.color = color;
		this.renderer = renderer;
	}
	
	public int getLineCount() {
		if (text.trim().isEmpty()) return 0;
		if (text.contains("\n"))
			return text.split("\n").length;
		else
			return 1;
	}
	
	public int getLongestLineWidth() {
		if (text.contains("\n")) {
			int work = 0;
			for (final String s : text.split("\n")) {
				work = Math.max(renderer.getStringWidth(s), work);
			}
			return work;
		} else
			return renderer.getStringWidth(text);
	}
	
	
	/**
	 * The text to render for this label. Allows newlines.
	 */
	public void setText(final String text) {
		this.text = text;
		recalculateSize();
	}
	
	protected void recalculateSize() {
		int addX = 0;
		int addY = 0;
		final boolean addToX = alignmentX == HorzAlignment.LEFT || alignmentX == HorzAlignment.MIDDLE;
		final boolean addToY = alignmentY == VertAlignment.TOP || alignmentY == VertAlignment.MIDDLE;
		if (outlined) {
			addX += 2;
			addY += 2;
		}
		if (shadow) {
			addX += 1;
			addY += 1;
		}
		if (alignmentY == VertAlignment.TOP) {
			height = ((getLineCount() * (renderer.FONT_HEIGHT + lineSpacing)) - lineSpacing) + (addToX ? addX : 0);
		}
		if (alignmentX == HorzAlignment.LEFT) {
			width = getLongestLineWidth() + (addToY ? addY : 0);
		}
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		if (small) {
			GL11.glScalef(0.5f, 0.5f, 1.0f);
		}
		if (text.contains("\n")) {
			int offset = 0;
			for (final String s : text.split("\n")) {
				draw(s, offset);
				offset += renderer.FONT_HEIGHT + lineSpacing;
			}
		} else {
			draw(text, 0);
		}
	}
	
	private void draw(final String s, final int offset) {
		int adjX = 0;
		int adjY = 0;
		if (alignmentX == HorzAlignment.MIDDLE) {
			adjX = width / 2 - renderer.getStringWidth(s) / (small ? 4 : 2);
			if (outlined) {
				adjX++;
			}
		} else if (alignmentX == HorzAlignment.RIGHT) {
			adjX = width - renderer.getStringWidth(s) / (small ? 2 : 1);
			if (outlined) {
				adjX--;
			}
		} else {
			if (outlined) {
				adjX++;
			}
		}
		if (alignmentY == VertAlignment.MIDDLE) {
			adjY = height / 2 - renderer.FONT_HEIGHT / 2;
			if (outlined) {
				adjY--;
			}
		} else if (alignmentY == VertAlignment.BOTTOM) {
			adjY = height - ((getLineCount() * (renderer.FONT_HEIGHT + lineSpacing)) - lineSpacing);
			if (outlined) {
				adjY--;
			}
		} else {
			if (outlined) {
				adjY++;
			}
		}
		if (small) {
			adjX *= 2;
			adjY *= 2;
		}
		adjY += offset;
		if (outlined) {
			if (shadow) {
				final int shadowColor = (color & 16579836) >> 2 | color & -16777216;
				Rendering.drawOutlinedString(renderer, s, adjX + 1, adjY + 1, shadowColor, invertedOutline);
			}
			Rendering.drawOutlinedString(renderer, s, adjX, adjY, color, invertedOutline);
		} else {
			renderer.drawString(s, adjX, adjY, color, shadow);
		}
	}
	
	/**
	 * Creates and returns a 'title label' - a label that has width set to 100% and Y set to 3, and a MIDDLE X alignment.
	 * 
	 * @param text
	 *            The text the label should have
	 * @return A newly created 'title label'
	 */
	public static PaneLabel createTitleLabel(final String text) {
		final PaneLabel label = new PaneLabel(text);
		label.setAutoResizeWidth(true);
		label.setY(3);
		label.setAlignmentX(HorzAlignment.MIDDLE);
		return label;
	}
	
	/**
	 * Creates and returns a 'center label' - a label that has width and height set to 100% and MIDDLE X and Y alignments.
	 * 
	 * @param text
	 *            The text the label should have
	 * @return A newly created 'center label'
	 */
	public static PaneLabel createCenterLabel(final String text) {
		final PaneLabel label = new PaneLabel(text);
		label.setAutoResize(true);
		label.setAlignmentY(VertAlignment.MIDDLE);
		label.setAlignmentX(HorzAlignment.MIDDLE);
		return label;
	}
	
}
