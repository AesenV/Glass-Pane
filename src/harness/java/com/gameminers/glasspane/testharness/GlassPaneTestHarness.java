package com.gameminers.glasspane.testharness;


import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;


@Mod(name = "Glass Pane Test Harness", version = "0.1", modid = "GlassPaneTH", dependencies = "required-after:GlassPane")
public class GlassPaneTestHarness {
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		new PaneHarnessOptionsOverlay().autoOverlay(GuiOptions.class);
	}
}
