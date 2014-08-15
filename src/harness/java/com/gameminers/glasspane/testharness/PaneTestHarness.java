package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.PaneComponent;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.text.PaneLabel;
import gminers.glasspane.ease.PaneEaser;

import com.gameminers.glasspane.internal.GlassPaneMod;


public class PaneTestHarness
		extends GlassPane {
	private int			ticks			= 0;
	private GlassPane[]	testHarnesses	= {
			new PaneTestButtons(),
			new PaneTestProgressBars(),
			new PaneTestSliders(),
			new PaneTestLabels(),
			new PaneTestOverlays(),
			new PaneTestScrollPanels(),
			new PaneTestSpinners(),
			new PaneTestPanels(),
			new PaneTestEasers(),
			new PaneTestTooltips(),
			new PaneTestMisc()
										};
	
	public PaneTestHarness() {
		setRevertAllowed(true);
		add(PaneLabel
				.createTitleLabel("Glass Pane Test Harness\n\u00A77Useful for texturers or learning what Glass Pane can do."));
		add(PaneButton.createDoneButton());
	}
	
	@Override
	protected void doTick() {
		if (mouseX == -10) {
			ticks = 0;
			for (PaneComponent c : getComponents()) {
				if ("entry".equals(c.getName())) {
					((PaneButton) c).setButtonColor(0xAAAAAA);
					((PaneButton) c).setColor(0xFFFFFF);
					PaneEaser easer = GlassPaneMod.easers.get(c);
					if (easer != null) {
						easer.close();
					}
				}
			}
			return;
		}
		ticks++;
		if (ticks > 400) {
			ticks = 0;
		}
		if (ticks % 15 == 0) {
			int idx = ticks / 15;
			if (idx < components.size()) {
				if (idx > 0) {
					PaneComponent prev = components.get(idx - 1);
					if ("entry".equals(prev.getName())) {
						ease(prev, 0xAAAAAA, 0xFFFFFF);
					}
				}
				PaneComponent c = components.get(idx);
				if ("entry".equals(c.getName())) {
					ease(c, 0x55AAFF, 0x55FFFF);
				}
			} else if (idx == components.size()) {
				PaneComponent last = components.get(idx - 1);
				if ("entry".equals(last.getName())) {
					ease(last, 0xAAAAAA, 0xFFFFFF);
				}
			}
		}
	}
	
	@SuppressWarnings("resource")
	private void ease(PaneComponent c, int buttonColor, int color) {
		PaneEaser easer = new PaneEaser(c);
		easer.easeColorInt("buttonColor", buttonColor);
		easer.easeColorInt("color", color);
		easer.setAutoClose(true);
	}
	
	// overridden for the buttons to reflow when the window is resized
	// normally your buttons and such should be created in the constructor, not here
	@Override
	protected void winch(int oldWidth, int oldHeight, int newWidth, int newHeight) {
		super.winch(oldWidth, oldHeight, newWidth, newHeight);
		for (PaneComponent c : getComponents()) {
			if ("entry".equals(c.getName())) {
				remove(c);
			}
		}
		int x = 10;
		int y = 32;
		for (final GlassPane gp : testHarnesses) {
			if (x + 98 >= getWidth() - 10) {
				x = 10;
				y += 24;
			}
			PaneButton button = new PaneButton(gp.getName());
			button.setName("entry");
			button.setWidth(98);
			button.setX(x);
			button.setY(y);
			button.setButtonColor(0xAAAAAA);
			if (gp.getComponents().size() <= 1) {
				button.setEnabled(false);
			}
			button.registerActivationListener(new Runnable() {
				
				@Override
				public void run() {
					gp.modalOverlay();
				}
			});
			add(button);
			x += 100;
		}
	}
}
