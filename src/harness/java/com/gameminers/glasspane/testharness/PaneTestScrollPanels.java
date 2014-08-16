package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.PaneBox;
import gminers.glasspane.component.PaneScrollPanel;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.text.PaneLabel;
import gminers.kitchensink.Hues;


public class PaneTestScrollPanels
		extends GlassPane {
	public PaneTestScrollPanels() {
		setName("Scroll Panels");
		add(PaneButton.createDoneButton());
		PaneButton flipButton = PaneTestHarness.createFlipButton();
		flipButton.setEnabled(false);
		add(PaneTestHarness.createGithubButton("PaneTestScrollPanels.java"), flipButton);
		
		PaneScrollPanel basic = new PaneScrollPanel();
		basic.add(PaneLabel.createTitleLabel("Basic"));
		basic.setAutoPositionX(true);
		basic.setRelativeX(0);
		basic.setRelativeXOffset(10);
		basic.setAutoResizeHeight(true);
		basic.setRelativeHeightOffset(-50);
		basic.setWidth(100);
		basic.setY(10);
		generateContent(basic);
		add(basic);
		
		PaneScrollPanel unshadowed = new PaneScrollPanel();
		unshadowed.add(PaneLabel.createTitleLabel("No Shadow"));
		unshadowed.setAutoPositionX(true);
		unshadowed.setRelativeX(0.5);
		unshadowed.setRelativeXOffset(-50);
		unshadowed.setAutoResizeHeight(true);
		unshadowed.setRelativeHeightOffset(-50);
		unshadowed.setShadowed(false);
		unshadowed.setWidth(100);
		unshadowed.setHeight(80);
		unshadowed.setY(10);
		generateContent(unshadowed);
		add(unshadowed);
		
		PaneScrollPanel deepShadow = new PaneScrollPanel();
		deepShadow.add(PaneLabel.createTitleLabel("Deep Shadow"));
		deepShadow.setAutoPositionX(true);
		deepShadow.setRelativeX(1);
		deepShadow.setRelativeXOffset(-110);
		deepShadow.setAutoResizeHeight(true);
		deepShadow.setRelativeHeightOffset(-50);
		deepShadow.setShadowDepth(40);
		deepShadow.setWidth(100);
		deepShadow.setHeight(80);
		deepShadow.setY(10);
		generateContent(deepShadow);
		add(deepShadow);
	}
	
	private void generateContent(PaneScrollPanel panel) {
		for (int i = 0; i < 360; i++) {
			PaneBox box = new PaneBox(Hues.hueToRGB(i) | 0xFF000000);
			box.setY(20 + (i * 4));
			box.setWidth(80);
			box.setHeight(4);
			box.setX(10);
			panel.add(box);
		}
	}
}
