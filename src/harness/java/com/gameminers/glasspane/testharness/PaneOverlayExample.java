package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.HorzAlignment;
import gminers.glasspane.component.text.PaneLabel;


public class PaneOverlayExample
		extends GlassPane {
	public static int nextId;
	
	public PaneOverlayExample() {
		PaneLabel label = new PaneLabel("Overlay Example #" + nextId);
		label.setAutoResizeWidth(true);
		label.setRelativeWidthOffset(-5);
		label.setY(5 + (nextId * 12));
		label.setAlignmentX(HorzAlignment.RIGHT);
		add(label);
		nextId++;
	}
}
