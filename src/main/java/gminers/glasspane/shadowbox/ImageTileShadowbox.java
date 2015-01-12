package gminers.glasspane.shadowbox;


import gminers.kitchensink.Rendering;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;


/**
 * A Shadowbox that acts similar to drawDefaultBackground in a GuiScreen.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ImageTileShadowbox
		extends PaneShadowbox {
	public ImageTileShadowbox(final ResourceLocation texture) {
		this.texture = texture;
	}
	
	/**
	 * Whether or not this shadowbox should render as a partially transparent gradient when the player is logged into a world, instead of
	 * the texture.
	 */
	boolean transparentWhenInWorld = true;
	/**
	 * Whether or not this texture should be darkened when rendered, similar to drawDefaultBackground.
	 */
	boolean darkened = true;
	/**
	 * The texture to tile for the background.
	 */
	ResourceLocation texture;
	
	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		if (transparentWhenInWorld && Minecraft.getMinecraft().theWorld != null) {
			Rendering.drawGradientRect(0, 0, width, height, -1072689136, -804253680, 0);
		} else {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_FOG);
			final Tessellator tess = Tessellator.getInstance();
			final WorldRenderer wr = tess.getWorldRenderer();
			Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			final float divisor = 32.0F;
			wr.startDrawingQuads();
			if (darkened) {
				wr.setColorOpaque_I(0x404040);
			} else {
				wr.setColorOpaque_I(0xFFFFFF);
			}
			wr.addVertexWithUV(0.0D, this.height, 0.0D, 0.0D, this.height / divisor);
			wr.addVertexWithUV(this.width, this.height, 0.0D, this.width / divisor, this.height / divisor);
			wr.addVertexWithUV(this.width, 0.0D, 0.0D, this.width / divisor, 0);
			wr.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0);
			tess.draw();
		}
	}
	
	@Override
	public void tick() {}
	
	@Override
	public void winch() {}
	
}
