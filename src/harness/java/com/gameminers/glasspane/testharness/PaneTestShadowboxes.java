package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.shadowbox.ImageTileShadowbox;
import gminers.glasspane.shadowbox.PanoramaShadowbox;
import gminers.glasspane.shadowbox.SolidShadowbox;
import net.minecraft.util.ResourceLocation;


public class PaneTestShadowboxes
		extends GlassPane {
	private int x = 10;
	private int y = 10;
	
	public PaneTestShadowboxes() {
		setName("Shadowboxes");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestShadowboxes.java"), PaneTestHarness.createFlipButton());
		
		createAndAddButton("Default", new Runnable() {
			
			@Override
			public void run() {
				new PaneShadowboxExample(new ImageTileShadowbox(defaultShadowboxTex)).show();
			}
		});
		createAndAddButton("Custom Tiled", new Runnable() {
			
			@Override
			public void run() {
				new PaneShadowboxExample(new ImageTileShadowbox(new ResourceLocation("textures/blocks/brick.png")))
						.show();
			}
		});
		createAndAddButton("Custom Tiled Full Brightness", new Runnable() {
			
			@Override
			public void run() {
				ImageTileShadowbox pan = new ImageTileShadowbox(new ResourceLocation("textures/blocks/lapis_block.png"));
				pan.setDarkened(false);
				new PaneShadowboxExample(pan).show();
			}
		});
		y += 10;
		createAndAddButton("Panorama", new Runnable() {
			
			@Override
			public void run() {
				new PaneShadowboxExample(new PanoramaShadowbox()).show();
			}
		});
		createAndAddButton("Non-Foggy Panorama", new Runnable() {
			
			@Override
			public void run() {
				PanoramaShadowbox pan = new PanoramaShadowbox();
				pan.setFoggy(false);
				new PaneShadowboxExample(pan).show();
			}
		});
		createAndAddButton("Custom Panorama", new Runnable() {
			
			@Override
			public void run() {
				PanoramaShadowbox pan = new PanoramaShadowbox();
				pan.setFoggy(false);
				pan.setOverridePaths(new ResourceLocation[] {
						new ResourceLocation("textures/blocks/diamond_block.png"),
						new ResourceLocation("textures/blocks/gold_block.png"),
						new ResourceLocation("textures/blocks/iron_block.png"),
						new ResourceLocation("textures/blocks/emerald_block.png"),
						new ResourceLocation("textures/blocks/brick.png"),
						new ResourceLocation("textures/blocks/lapis_block.png"),
				});
				new PaneShadowboxExample(pan).show();
			}
		});
		y += 10;
		createAndAddButton("No Shadowbox", new Runnable() {
			
			@Override
			public void run() {
				new PaneShadowboxExample(null).show();
			}
		}).setTooltip(
				"This is not recommended, and will not properly work\nin some environments. Use overlays instead.");
		createAndAddButton("Translucent", new Runnable() {
			
			@Override
			public void run() {
				new PaneShadowboxExample(new SolidShadowbox(0x1100AA00)).show();
			}
		});
		createAndAddButton("Solid", new Runnable() {
			
			@Override
			public void run() {
				new PaneShadowboxExample(new SolidShadowbox(0xFFAA0000)).show();
			}
		});
	}
	
	private PaneButton createAndAddButton(String text, Runnable runnable) {
		PaneButton button = new PaneButton(text);
		button.setX(x);
		button.setY(y);
		button.setWidth(150);
		button.registerActivationListener(runnable);
		add(button);
		y += 24;
		if (y >= 180) {
			x += 154;
			y = 10;
		}
		return button;
	}
}
