package com.gameminers.glasspane.testharness;


import gminers.glasspane.Direction;
import gminers.glasspane.GlassPane;
import gminers.glasspane.component.numeric.PaneSlider;
import gminers.glasspane.event.PaneOverlayEvent;
import gminers.glasspane.listener.PaneEventHandler;
import net.minecraft.client.gui.GuiOptions;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;


@Mod(
		name = "Glass Pane Test Harness",
		version = "0.1",
		modid = "GlassPaneTH",
		dependencies = "required-after:GlassPane")
public class GlassPaneTestHarness
		extends GlassPane {
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		autoOverlay(GuiOptions.class);
	}
	
	@PaneEventHandler
	public void onOverlay(PaneOverlayEvent e) {
		clear();
		PaneSlider button = new PaneSlider();
		button.setAutoPosition(true);
		button.setRelativeX(0.5);
		button.setRelativeY(0.5);
		button.setRelativeXOffset(-100);
		button.setRelativeYOffset(-10);
		button.setDirection(Direction.VERTICAL);
		button.setWidth(20);
		button.setHeight(100);
		add(button);
	}
}
