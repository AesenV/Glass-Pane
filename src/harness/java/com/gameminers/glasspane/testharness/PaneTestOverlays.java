package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;

import com.gameminers.glasspane.internal.GlassPaneMod;


public class PaneTestOverlays
		extends GlassPane {
	private int x = 10;
	private int y = 10;
	
	public PaneTestOverlays() {
		setName("Overlays");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestOverlays.java"), PaneTestHarness.createFlipButton());
		
		createAndAddButton("Normal Overlay", new Runnable() {
			
			@Override
			public void run() {
				new PaneOverlayExample().overlay();
			}
		});
		createAndAddButton("Sticky Overlay", new Runnable() {
			
			@Override
			public void run() {
				new PaneOverlayExample().stickyOverlay();
			}
		});
		createAndAddButton("Modal Overlay", new Runnable() {
			
			@Override
			public void run() {
				PaneOverlayExample example = new PaneOverlayExample();
				example.add(PaneButton.createDoneButton());
				example.modalOverlay();
			}
		});
		y += 10;
		createAndAddButton("Hide All Overlays", new Runnable() {
			
			@Override
			public void run() {
				for (GlassPane pane : GlassPaneMod.inst.currentOverlays) {
					pane.hide();
				}
				for (GlassPane pane : GlassPaneMod.inst.currentStickyOverlays) {
					pane.hide();
				}
				PaneOverlayExample.nextId = 0;
			}
		});
		createAndAddButton("Reset ID", new Runnable() {
			
			@Override
			public void run() {
				PaneOverlayExample.nextId = 0;
			}
		});
	}
	
	private void createAndAddButton(String text, Runnable runnable) {
		PaneButton button = new PaneButton(text);
		button.setX(x);
		button.setY(y);
		button.setWidth(100);
		button.registerActivationListener(runnable);
		add(button);
		y += 24;
	}
}
