package com.gameminers.glasspane.testharness;


import gminers.glasspane.Direction;
import gminers.glasspane.GlassPane;
import gminers.glasspane.component.PaneGradientBox;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.numeric.PaneSlider;
import gminers.glasspane.ease.PaneEaser;
import gminers.glasspane.event.StateChangedEvent;
import gminers.glasspane.listener.PaneEventHandler;
import gminers.kitchensink.Strings;


public class PaneTestEasers
		extends GlassPane {
	private int x = 24;
	private int y = 34;
	
	public PaneTestEasers() {
		setName("Easers");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestEasers.java"), PaneTestHarness.createFlipButton());
		
		final PaneGradientBox box = new PaneGradientBox(0xFFFF5555, 0xFFFFFF55);
		box.setWidth(20);
		box.setHeight(20);
		box.setX(10);
		box.setY(10);
		box.setZIndex(5);
		box.setRotationAllowed(true);
		box.setXRot(0);
		box.setYRot(0);
		box.setZRot(1);
		add(box);
		final PaneEaser boxEaser = new PaneEaser(box);
		final PaneSlider speed = new PaneSlider();
		speed.setDirection(Direction.VERTICAL);
		speed.setMaximum(6400);
		speed.setValue(400);
		speed.setWidth(10);
		speed.setHeight(140);
		speed.setX(10);
		speed.setY(34);
		speed.setText("");
		speed.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChanged(StateChangedEvent e) {
				boxEaser.setSpeed((speed.getValue() + 100) / 100D);
			}
		});
		add(speed);
		createEaserButton(boxEaser, "x", 300);
		createEaserButton(boxEaser, "x", 10);
		createEaserButton(boxEaser, "y", 180);
		createEaserButton(boxEaser, "y", 10);
		createEaserButton(boxEaser, "width", 200);
		createEaserButton(boxEaser, "width", 20);
		y += 12;
		createAndAddButton("Ease to Blue/Green", new Runnable() {
			
			@Override
			public void run() {
				boxEaser.easeColorInt("color", 0xFF5555FF);
				boxEaser.easeColorInt("color2", 0xFF55FF55);
			}
		});
		createAndAddButton("Ease to Red/Yellow", new Runnable() {
			
			@Override
			public void run() {
				boxEaser.easeColorInt("color", 0xFFFF5555);
				boxEaser.easeColorInt("color2", 0xFFFFFF55);
			}
		});
		createEaserButton(boxEaser, "angle", 45f);
		createEaserButton(boxEaser, "angle", 0f);
		createEaserButton(boxEaser, "angle", -45f);
	}
	
	private void createEaserButton(final PaneEaser boxEaser, final String key, final float val) {
		createAndAddButton("Ease to " + Strings.formatTitleCase(key) + " " + val, new Runnable() {
			
			@Override
			public void run() {
				boxEaser.easeFloat(key, val);
			}
		});
	}
	
	private void createEaserButton(final PaneEaser boxEaser, final String key, final int val) {
		createAndAddButton("Ease to " + Strings.formatTitleCase(key) + " " + val, new Runnable() {
			
			@Override
			public void run() {
				boxEaser.easeInteger(key, val);
			}
		});
	}
	
	private void createAndAddButton(String text, Runnable runnable) {
		PaneButton button = new PaneButton(text);
		button.setX(x);
		button.setY(y);
		button.setWidth(120);
		button.registerActivationListener(runnable);
		add(button);
		y += 24;
		if (y >= 160) {
			y = 34;
			x += 124;
		}
	}
}
