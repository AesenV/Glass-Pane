package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestSpinners
		extends GlassPane {
	public PaneTestSpinners() {
		setName("Spinners");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestSpinners.java"));
	}
}
