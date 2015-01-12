package gminers.glasspane.component;


import gminers.kitchensink.Rendering;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;


/**
 * Implements a simple component that displays an image.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class PaneImage
		extends ColorablePaneComponent {
	/**
	 * The image to render.
	 */
	ResourceLocation image;
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
	int imageWidth = 256;
	/**
	 * The height of the portion of the image to use - 256 for the entire image.
	 */
	int imageHeight = 256;
	/**
	 * The alpha transparency of this image - 0.0 is completely transparent, 1.0 is opaque
	 */
	float alpha = 1.0f;
	/**
	 * Whether or not to use one-bit transparency for this image. One-bit transparency is faster, but if your image is partially
	 * transparent, it will render as fully opaque.
	 */
	boolean oneBitTransparency = true;
	
	public PaneImage(final ResourceLocation image) {
		this.image = image;
	}
	
	@Override
	protected void doRender(final int mouseX, final int mouseY, final float partialTicks) {
		render(image, 0, 0, u, v, width, height, imageWidth, imageHeight, color, alpha, oneBitTransparency);
	}
	
	public static void render(ResourceLocation image, int x, int y, int u, int v, int width, int height,
			int imageWidth, int imageHeight, int color, float alpha, boolean oneBitTransparency) {
		if (image == null) return;
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		// bind the image texture
		Minecraft.getMinecraft().renderEngine.bindTexture(image);
		// apply a scale because for some asinine reason all textures in minecraft are 256x256
		// there's probably a really good reason for it, but I don't fully understand OpenGL
		GL11.glScalef(width / ((float) imageWidth), height / ((float) imageHeight), 0.0f);
		// if we want full transparency, enable blending
		if (!oneBitTransparency) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		// apply the tint
		final int r = color >> 16 & 255;
		final int g = color >> 8 & 255;
		final int b = color & 255;
		GL11.glColor4f(r / 255f, g / 255f, b / 255f, alpha);
		// and finally render it
		Rendering.drawTexturedModalRect(0, 0, u, v, imageWidth, imageHeight, 0);
		// then disable blending if we enabled it for full transparency
		if (!oneBitTransparency) {
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
	}
	
}
