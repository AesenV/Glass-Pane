package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.shadowbox.PaneShadowbox;


public class PaneShadowboxExample
		extends GlassPane {
	public PaneShadowboxExample(PaneShadowbox shadowbox) {
		setRevertAllowed(true);
		setShadowbox(shadowbox);
		add(PaneButton.createDoneButton());
	}
}
