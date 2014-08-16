package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.numeric.PaneSlider;
import gminers.glasspane.event.StateChangedEvent;
import gminers.glasspane.listener.PaneEventHandler;
import gminers.kitchensink.Hues;


public class PaneTestSliders
		extends GlassPane {
	public PaneTestSliders() {
		setName("Sliders");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestSliders.java"), PaneTestHarness.createFlipButton());
		
		final PaneSlider basic = new PaneSlider();
		basic.setText("Basic Slider: 0/200");
		basic.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChanged(StateChangedEvent e) {
				basic.setText("Basic Slider: " + basic.getValue() + "/200");
			}
		});
		basic.setWidth(200);
		basic.setMaximum(200);
		basic.setX(10);
		basic.setY(10);
		add(basic);
		
		final PaneSlider low = new PaneSlider();
		low.setText("Low Maximum Slider: 0/8");
		low.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChanged(StateChangedEvent e) {
				low.setText("Low Maximum Slider: " + low.getValue() + "/8");
			}
		});
		low.setWidth(200);
		low.setMaximum(8);
		low.setX(10);
		low.setY(34);
		add(low);
		
		final PaneSlider colored = new PaneSlider();
		colored.setText("Colored Slider: 0/360");
		colored.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChanged(StateChangedEvent e) {
				colored.setText("Colored Slider: " + colored.getValue() + "/360");
				colored.setKnobColor(Hues.hueToRGB(colored.getValue()));
			}
		});
		colored.setWidth(200);
		colored.setMaximum(360);
		colored.setKnobColor(0xFF0000);
		colored.setX(10);
		colored.setY(58);
		add(colored);
		
		final PaneSlider wide = new PaneSlider();
		wide.setText("Wide Slider: 0/100");
		wide.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChanged(StateChangedEvent e) {
				wide.setText("Wide Slider: " + wide.getValue() + "/100");
			}
		});
		wide.setWidth(200);
		wide.setMaximum(100);
		wide.setKnobLength(30);
		wide.setX(214);
		wide.setY(10);
		add(wide);
		
		final PaneSlider twoState = new PaneSlider();
		twoState.setText("Switch: 0/1");
		twoState.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChanged(StateChangedEvent e) {
				twoState.setText("Switch: " + twoState.getValue() + "/1");
			}
		});
		twoState.setWidth(200);
		twoState.setMaximum(1);
		twoState.setKnobLength(100);
		twoState.setX(214);
		twoState.setY(34);
		add(twoState);
		
		final PaneSlider transparent = new PaneSlider();
		transparent.setText("Transparent Slider: 0/100");
		transparent.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChanged(StateChangedEvent e) {
				transparent.setText("Transparent Slider: " + transparent.getValue() + "/100");
			}
		});
		transparent.setWidth(200);
		transparent.setMaximum(100);
		transparent.setRenderBackground(false);
		transparent.setX(214);
		transparent.setY(58);
		transparent.setTooltip("You could put this in front of a progress\nbar for a seek bar...");
		add(transparent);
	}
}
