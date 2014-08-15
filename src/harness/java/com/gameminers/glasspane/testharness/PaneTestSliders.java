package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestSliders
		extends GlassPane {
	public PaneTestSliders() {
		setName("Sliders");
		add(PaneButton.createDoneButton());
	}
}
