package gminers.glasspane;


import gminers.glasspane.event.KeyTypedEvent;
import gminers.glasspane.event.MouseDownEvent;
import gminers.glasspane.event.PaneDisplayEvent;
import gminers.glasspane.event.PaneHideEvent;
import gminers.kitchensink.Rendering;

import java.util.List;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.gameminers.glasspane.internal.GlassPaneMod;


/**
 * Simple class that wraps a GlassPane with a GuiScreen.
 * All operations on the GuiScreen pass through to the underlying GlassPane, converting where necessary.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public final class GlassPaneMirror
		extends GuiScreen {
	/**
	 * The GlassPane that this GlassPaneMirror is mirroring.
	 */
	@Getter GlassPane																	mirrored;
	@NonFinal boolean																	shown	= false;
	@Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE) @NonFinal List<GlassPane>	modalUnderlays;
	@Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE) @NonFinal GuiScreen		modal;
	
	@Override
	public boolean doesGuiPauseGame() {
		return !(mirrored instanceof NonPausingGlassPane);
	}
	
	@Override
	public void initGui() {
		if (isModal()) {
			modal.setWorldAndResolution(mc, width, height);
		}
		if (!shown) {
			shown = true;
			mirrored.fireEvent(PaneDisplayEvent.class, mirrored);
		}
		mirrored.setWidth(width);
		mirrored.setHeight(height);
		if (mirrored.shadowbox != null) {
			mirrored.shadowbox.setWidth(width);
			mirrored.shadowbox.setHeight(height);
			mirrored.shadowbox.winch();
		}
		for (final GlassPane pane : GlassPaneMod.inst.currentOverlays) {
			pane.setWidth(width);
			pane.setHeight(height);
		}
	}
	
	@Override
	public void updateScreen() {
		if (isModal()) {
			modal.updateScreen();
		}
		mirrored.tick();
		if (mirrored.shadowbox != null) {
			mirrored.shadowbox.tick();
		}
	}
	
	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		if (isModal()) {
			modal.drawScreen(-10, -10, par3);
			for (final GlassPane gp : modalUnderlays) {
				if (gp == null) {
					continue;
				}
				gp.render(-10, -10, par3);
			}
			GL11.glTranslatef(0, 0, 20);
			Rendering.drawGradientRect(0, 0, width, height, 0xC0101010, 0xD0101010);
		}
		mirrored.render(par1, par2, par3);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	@Override
	protected void keyTyped(final char keyChar, final int keyCode) {
		final KeyTypedEvent e = mirrored.fireEvent(KeyTypedEvent.class, mirrored, keyChar, keyCode);
		if (e != null) {
			if (e.isConsumed()) {
				return;
			}
		}
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
		}
	}
	
	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int button) {
		mirrored.fireEvent(MouseDownEvent.class, mirrored, mouseX, mouseY, button);
	}
	
	@Override
	public void onGuiClosed() {
		shown = false;
		mirrored.fireEvent(PaneHideEvent.class, mirrored);
		mirrored.setFocusedComponent(null);
		mirrored.setWidth(1);
		mirrored.setHeight(1);
	}
	
	public boolean isModal() {
		return modal != null;
	}
}
