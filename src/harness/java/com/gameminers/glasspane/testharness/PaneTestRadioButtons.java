package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestRadioButtons
		extends GlassPane {
	public PaneTestRadioButtons() {
		setName("Radio Buttons");
		add(PaneButton.createDoneButton());
	}
}
