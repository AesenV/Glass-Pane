package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestShadowboxes
		extends GlassPane {
	public PaneTestShadowboxes() {
		setName("Shadowboxes");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestShadowboxes.java"), PaneTestHarness.createFlipButton());
	}
}
