package gminers.glasspane.shadowbox;


import gminers.kitchensink.Rendering;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
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
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        byte b0 = 8;

        for (int k = 0; k < b0 * b0; ++k) {
            GlStateManager.pushMatrix();
            float f1 = ((float)(k % b0) / (float)b0 - 0.5F) / 64.0F;
            float f2 = ((float)(k / b0) / (float)b0 - 0.5F) / 64.0F;
            float f3 = 0.0F;
            GlStateManager.translate(f1, f2, f3);
            GlStateManager.rotate(MathHelper.sin(((float)panoramaTimer + partialTick) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float)panoramaTimer + partialTick) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l) {
                GlStateManager.pushMatrix();

                if (l == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                mc.getTextureManager().bindTexture(overridePaths == null ? titlePanoramaPaths[l] : overridePaths[l]);
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_I(16777215, 255 / (k + 1));
                float f4 = 0.0F;
                worldrenderer.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + f4), (double)(0.0F + f4));
                worldrenderer.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - f4), (double)(0.0F + f4));
                worldrenderer.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - f4), (double)(1.0F - f4));
                worldrenderer.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + f4), (double)(1.0F - f4));
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
	}
	
	private void rotateAndBlurSkybox(final float partialTick) {
		this.mc.getTextureManager().bindTexture(panoramaTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        GlStateManager.disableAlpha();
        byte b0 = 3;

        for (int i = 0; i < b0; ++i) {
            worldrenderer.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(i + 1));
            int j = this.width;
            int k = this.height;
            float f1 = (float)(i - b0 / 2) / 256.0F;
            worldrenderer.addVertexWithUV((double)j, (double)k, (double)0, (double)(0.0F + f1), 1.0D);
            worldrenderer.addVertexWithUV((double)j, 0.0D, (double)0, (double)(1.0F + f1), 1.0D);
            worldrenderer.addVertexWithUV(0.0D, 0.0D, (double)0, (double)(1.0F + f1), 0.0D);
            worldrenderer.addVertexWithUV(0.0D, (double)k, (double)0, (double)(0.0F + f1), 0.0D);
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
	}
	
	private void renderSkybox(final float partialTick) {
		this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        drawPanorama(partialTick);
        rotateAndBlurSkybox(partialTick);
        rotateAndBlurSkybox(partialTick);
        rotateAndBlurSkybox(partialTick);
        rotateAndBlurSkybox(partialTick);
        rotateAndBlurSkybox(partialTick);
        rotateAndBlurSkybox(partialTick);
        rotateAndBlurSkybox(partialTick);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        float f1 = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float f2 = (float)this.height * f1 / 256.0F;
        float f3 = (float)this.width * f1 / 256.0F;
        worldrenderer.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.width;
        int l = this.height;
        worldrenderer.addVertexWithUV(0.0D, (double)l, (double)0, (double)(0.5F - f2), (double)(0.5F + f3));
        worldrenderer.addVertexWithUV((double)k, (double)l, (double)0, (double)(0.5F - f2), (double)(0.5F - f3));
        worldrenderer.addVertexWithUV((double)k, 0.0D, (double)0, (double)(0.5F + f2), (double)(0.5F - f3));
        worldrenderer.addVertexWithUV(0.0D, 0.0D, (double)0, (double)(0.5F + f2), (double)(0.5F + f3));
        tessellator.draw();
	}
	
	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.renderSkybox(partialTicks);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		if (foggy) {
			Rendering.drawGradientRect(0, 0, this.width, this.height, -0x7F000001, 0x00FFFFFF, 0);
			Rendering.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE, 0);
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
