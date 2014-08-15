package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestMisc
		extends GlassPane {
	public PaneTestMisc() {
		setName("Miscellaneous");
		add(PaneButton.createDoneButton());
	}
}
