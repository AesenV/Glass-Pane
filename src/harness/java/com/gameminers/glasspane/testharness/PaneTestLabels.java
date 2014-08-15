package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestLabels
		extends GlassPane {
	public PaneTestLabels() {
		setName("Labels");
		add(PaneButton.createDoneButton());
	}
}
