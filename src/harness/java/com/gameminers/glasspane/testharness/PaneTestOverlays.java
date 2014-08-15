package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;


public class PaneTestOverlays
		extends GlassPane {
	public PaneTestOverlays() {
		setName("Overlays");
		add(PaneButton.createDoneButton());
	}
}
