package gminers.glasspane.shadowbox;


import gminers.kitchensink.Rendering;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;


/**
 * A Shadowbox that acts like the panorama shown in the background of the Minecraft main menu.
 * 
 * @author Aesen Vismea
 * 
 */
public class PanoramaShadowbox
		extends PaneShadowbox {
	private static int panoramaTimer;
	private final Minecraft mc = Minecraft.getMinecraft();
	private ResourceLocation panoramaTexture;
	private DynamicTexture viewportTexture;
	/**
	 * Whether or not this shadowbox should render with a layer of "fog" over it.
	 */
	@Getter @Setter private boolean foggy = true;
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png")
	};
	private ResourceLocation[] overridePaths = null;
	
	/**
	 * Returns a <b>clone</b> of the paths currently being used to override the default panorama.
	 */
	public ResourceLocation[] getOverridePaths() {
		return overridePaths == null ? null : overridePaths.clone();
	}
	
	/**
	 * Sets the overrides for this panorama. Passing null resets it to the default used by the main menu.
	 * 
	 * @param overridePaths
	 *            The array of paths to use as an override. Must be 6 elements long, and contain no null elements.
	 */
	public void setOverridePaths(final ResourceLocation[] overridePaths) {
		if (overridePaths.length != 6)
			throw new IllegalArgumentException("Override paths array is incorrectly sized!");
		for (int i = 0; i < overridePaths.length; i++) {
			if (overridePaths[i] == null) throw new NullPointerException("overridePaths[" + i + "]");
		}
		this.overridePaths = overridePaths.clone();
	}
	
	private void drawPanorama(final float partialTick) {
		final Tessellator tessellator = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		final byte b0 = 8;
		
		for (int k = 0; k < b0 * b0; ++k) {
			GL11.glPushMatrix();
			final float f1 = ((float) (k % b0) / (float) b0 - 0.5F) / 64.0F;
			final float f2 = ((float) (k / b0) / (float) b0 - 0.5F) / 64.0F;
			final float f3 = 0.0F;
			GL11.glTranslatef(f1, f2, f3);
			GL11.glRotatef(MathHelper.sin((panoramaTimer + partialTick) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-(panoramaTimer + partialTick) * 0.1F, 0.0F, 1.0F, 0.0F);
			
			for (int l = 0; l < 6; ++l) {
				GL11.glPushMatrix();
				
				if (l == 1) {
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				}
				
				if (l == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}
				
				if (l == 3) {
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}
				
				if (l == 4) {
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}
				
				if (l == 5) {
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}
				
				mc.getTextureManager().bindTexture(overridePaths == null ? titlePanoramaPaths[l] : overridePaths[l]);
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
				final float f4 = 0.0F;
				tessellator.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + f4, 0.0F + f4);
				tessellator.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - f4, 0.0F + f4);
				tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - f4, 1.0F - f4);
				tessellator.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + f4, 1.0F - f4);
				tessellator.draw();
				GL11.glPopMatrix();
			}
			
			GL11.glPopMatrix();
			GL11.glColorMask(true, true, true, false);
		}
		
		tessellator.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	private void rotateAndBlurSkybox(final float partialTick) {
		mc.getTextureManager().bindTexture(panoramaTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		GL11.glColorMask(true, true, true, false);
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		final byte b0 = 3;
		
		for (int i = 0; i < b0; ++i) {
			tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (i + 1));
			final int j = this.width;
			final int k = this.height;
			final float f1 = (i - b0 / 2) / 256.0F;
			tessellator.addVertexWithUV(j, k, 0, 0.0F + f1, 1.0D);
			tessellator.addVertexWithUV(j, 0.0D, 0, 1.0F + f1, 1.0D);
			tessellator.addVertexWithUV(0.0D, 0.0D, 0, 1.0F + f1, 0.0D);
			tessellator.addVertexWithUV(0.0D, k, 0, 0.0F + f1, 0.0D);
		}
		
		tessellator.draw();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColorMask(true, true, true, true);
	}
	
	private void renderSkybox(final float partialTick) {
		this.mc.getFramebuffer().unbindFramebuffer();
		GL11.glViewport(0, 0, 256, 256);
		this.drawPanorama(partialTick);
		this.rotateAndBlurSkybox(partialTick);
		this.rotateAndBlurSkybox(partialTick);
		this.rotateAndBlurSkybox(partialTick);
		this.rotateAndBlurSkybox(partialTick);
		this.rotateAndBlurSkybox(partialTick);
		this.rotateAndBlurSkybox(partialTick);
		this.rotateAndBlurSkybox(partialTick);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		final float f1 = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
		final float f2 = this.height * f1 / 256.0F;
		final float f3 = this.width * f1 / 256.0F;
		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		final int k = this.width;
		final int l = this.height;
		tessellator.addVertexWithUV(0.0D, l, 0, 0.5F - f2, 0.5F + f3);
		tessellator.addVertexWithUV(k, l, 0, 0.5F - f2, 0.5F - f3);
		tessellator.addVertexWithUV(k, 0.0D, 0, 0.5F + f2, 0.5F - f3);
		tessellator.addVertexWithUV(0.0D, 0.0D, 0, 0.5F + f2, 0.5F + f3);
		tessellator.draw();
	}
	
	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.renderSkybox(partialTicks);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		if (foggy) {
			Rendering.drawGradientRect(0, 0, this.width, this.height, -0x7F000001, 0x00FFFFFF);
			Rendering.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		}
	}
	
	@Override
	public void tick() {
		panoramaTimer++;
	}
	
	@Override
	public void winch() {
		viewportTexture = new DynamicTexture(256, 256);
		panoramaTexture = mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
	}
	
}
