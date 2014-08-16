package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.HorzAlignment;
import gminers.glasspane.VertAlignment;
import gminers.glasspane.component.PaneComponent;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.numeric.PaneSpinner;
import gminers.glasspane.component.text.PaneLabel;

import java.text.NumberFormat;


public class PaneTestSpinners
		extends GlassPane {
	public PaneTestSpinners() {
		setName("Spinners");
		add(PaneButton.createDoneButton());
		PaneButton flipButton = PaneTestHarness.createFlipButton();
		flipButton.setEnabled(false);
		add(PaneTestHarness.createGithubButton("PaneTestSpinners.java"), flipButton);
		
		PaneSpinner integer = new PaneSpinner();
		integer.setIncrement(1);
		integer.setWidth(50);
		integer.setHeight(12);
		integer.setX(10);
		integer.setY(10);
		add(integer);
		addLabel(integer, "Boundless Integral");
		
		PaneSpinner floating = new PaneSpinner();
		floating.setIncrement(0.25f);
		floating.setWidth(50);
		floating.setHeight(12);
		floating.setX(10);
		floating.setY(24);
		add(floating);
		addLabel(floating, "Boundless Floating Point");
		
		PaneSpinner boundedFloat = new PaneSpinner();
		boundedFloat.setIncrement(0.1f);
		boundedFloat.setMinimum(0);
		boundedFloat.setMaximum(1);
		boundedFloat.setWidth(50);
		boundedFloat.setHeight(12);
		boundedFloat.setX(10);
		boundedFloat.setY(38);
		add(boundedFloat);
		addLabel(boundedFloat, "Bounded Floating Point");
		
		PaneSpinner colored = new PaneSpinner();
		colored.setFormat(NumberFormat.getCurrencyInstance());
		colored.setIncrement(0.01f);
		colored.setMinimum(0);
		colored.setMaximum(100);
		colored.setColor(0x55FF55);
		colored.setWidth(50);
		colored.setHeight(12);
		colored.setX(10);
		colored.setY(52);
		add(colored);
		addLabel(colored, "Colored Bounded Floating Point with Custom Number Format");
		
		PaneLabel shiftLabel = new PaneLabel(
				"Use the scroll wheel or arrow keys to control the spinner.\nHold Shift, Control, or both to move in larger increments.\n\u00A7cThis component needs a bit of work to be more usable.");
		shiftLabel.setY(150);
		shiftLabel.setHeight(12);
		shiftLabel.setAutoResizeWidth(true);
		shiftLabel.setAlignmentY(VertAlignment.MIDDLE);
		shiftLabel.setAlignmentX(HorzAlignment.MIDDLE);
		add(shiftLabel);
	}
	
	private void addLabel(PaneComponent compo, String text) {
		PaneLabel label = new PaneLabel(text);
		label.setX(compo.getEdgeX() + 4);
		label.setY(compo.getY() + 2);
		add(label);
	}
}
