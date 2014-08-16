package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestTooltips
		extends GlassPane {
	public PaneTestTooltips() {
		setName("Tooltips");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestTooltips.java"), PaneTestHarness.createFlipButton());
	}
}
