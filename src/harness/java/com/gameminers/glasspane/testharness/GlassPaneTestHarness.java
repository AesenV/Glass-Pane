package com.gameminers.glasspane.testharness;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.event.PaneDisplayEvent;
import gminers.glasspane.event.PaneOverlayEvent;
import gminers.glasspane.listener.PaneEventHandler;
import gminers.kitchensink.RandomPool;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(name="Glass Pane Test Harness", version="0.1", modid="GlassPaneTH", dependencies="required-after:GlassPane")
public class GlassPaneTestHarness extends GlassPane {
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		autoOverlay(GuiOptions.class);
	}
	@PaneEventHandler
	public void onDisplay(PaneOverlayEvent e) {
		PaneButton button = new PaneButton(RandomStringUtils.randomAlphanumeric(16));
		button.setAutoPosition(true);
		button.setRelativeX(0.5);
		button.setRelativeY(0.5);
		button.setRelativeXOffset((int) RandomPool.nextGaussian(0, 200));
		button.setRelativeYOffset((int) RandomPool.nextGaussian(0, 200));
		button.setButtonColor(RandomPool.nextInt());
		add(button);
	}
}
