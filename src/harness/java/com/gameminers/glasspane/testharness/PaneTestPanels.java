package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestPanels
		extends GlassPane {
	public PaneTestPanels() {
		setName("Panels");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestPanels.java"));
	}
}
