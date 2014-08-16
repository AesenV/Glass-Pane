package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestTextFields
		extends GlassPane {
	public PaneTestTextFields() {
		setName("Text Fields");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestTextFields.java"), PaneTestHarness.createFlipButton());
	}
}
