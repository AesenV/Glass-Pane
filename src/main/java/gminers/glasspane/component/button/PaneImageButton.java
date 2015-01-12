package gminers.glasspane.component.button;


import gminers.glasspane.HorzAlignment;
import gminers.kitchensink.Rendering;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


/**
 * Implements a button with an icon on it.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class PaneImageButton
		extends PaneButton {
	/**
	 * The image to render.
	 */
	ResourceLocation image;
	/**
	 * The image to render, when the button is in hovered state. If null, defaults to image.
	 */
	ResourceLocation imageHover;
	/**
	 * The image to render, when the button is in disabled state. If null, defaults to image.
	 */
	ResourceLocation imageDisabled;
	/**
	 * The U (X texture offset) to use when rendering
	 */
	int u = 0;
	/**
	 * The V (Y texture offset) to use when rendering
	 */
	int v = 0;
	/**
	 * The width of the portion of the image to use - 256 for the entire image
	 */
	int imagePortionWidth = 256;
	/**
	 * The height of the portion of the image to use - 256 for the entire image.
	 */
	int imagePortionHeight = 256;
	/**
	 * The alpha transparency of this image - 0.0 is completely transparent, 1.0 is opaque
	 */
	float alpha = 1.0f;
	/**
	 * Whether or not to use one-bit transparency for this image. One-bit transparency is faster, but if your image is partially
	 * transparent, it will render as fully opaque.
	 */
	boolean oneBitTransparency = true;
	/**
	 * The tint to use for the image in it's normal state.
	 */
	int imageColor = 0xFFFFFF;
	/**
	 * The tint to use for the image in it's hovered state.
	 */
	int imageHoverColor = 0xFFFFA0;
	/**
	 * The tint to use for the image in it's disabled state.
	 */
	int imageDisabledColor = 0xA0A0A0;
	/**
	 * The width of the image on the button.
	 */
	int imageWidth = 16;
	/**
	 * The width of the image on the button.
	 */
	int imageHeight = 16;
	/**
	 * The side to put the image on.
	 */
	HorzAlignment imageAlignment = HorzAlignment.LEFT;
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		super.doRender(mouseX, mouseY, partialTicks);
		if (image == null) return;
		// apply a transform
		int imgX = 2;
		if (imageAlignment == HorzAlignment.MIDDLE) {
			imgX = (width / 2) - (imageWidth / 2);
		} else if (imageAlignment == HorzAlignment.RIGHT) {
			imgX = (width - imageWidth) - 2;
		}
		GL11.glTranslatef(imgX, 2, 0);
		// apply a scale because for some asinine reason all textures in minecraft are 256x256
		GL11.glScalef(imageWidth / ((float) imagePortionWidth), imageHeight / ((float) imagePortionHeight), 0.0f);
		// if we want full transparency, enable blending
		if (!oneBitTransparency) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		int color;
		final boolean hover = Mouse.isInsideWindow() && withinBounds(mouseX, mouseY);
		ResourceLocation image;
		if (enabled) {
			if (hover) {
				color = imageHoverColor;
				image = imageHover;
			} else {
				color = imageColor;
				image = this.image;
			}
		} else {
			color = imageDisabledColor;
			image = imageDisabled;
		}
		if (image == null) {
			image = this.image;
		}
		// bind the image texture
		Minecraft.getMinecraft().renderEngine.bindTexture(image);
		// apply the tint
		final int r = color >> 16 & 255;
		final int g = color >> 8 & 255;
		final int b = color & 255;
		GL11.glColor4f(r / 255f, g / 255f, b / 255f, alpha);
		// and finally render it
		Rendering.drawTexturedModalRect(0, 0, u, v, imagePortionWidth, imagePortionHeight, 0);
		// then disable blending if we enabled it for full transparency
		if (!oneBitTransparency) {
			GL11.glDisable(GL11.GL_BLEND);
		}
	}
}
