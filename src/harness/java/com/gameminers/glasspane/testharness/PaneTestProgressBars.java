package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestProgressBars
		extends GlassPane {
	public PaneTestProgressBars() {
		setName("Progress Bars");
		add(PaneButton.createDoneButton());
	}
}
