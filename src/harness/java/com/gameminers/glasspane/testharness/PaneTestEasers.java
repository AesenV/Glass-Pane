package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestEasers
		extends GlassPane {
	public PaneTestEasers() {
		setName("Easers");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestEasers.java"));
	}
}
