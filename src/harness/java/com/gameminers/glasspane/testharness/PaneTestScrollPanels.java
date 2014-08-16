package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestScrollPanels
		extends GlassPane {
	public PaneTestScrollPanels() {
		setName("Scroll Panels");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestScrollPanels.java"));
	}
}
