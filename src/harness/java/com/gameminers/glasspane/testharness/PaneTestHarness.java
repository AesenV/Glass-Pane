package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.HorzAlignment;
import gminers.glasspane.component.PaneComponent;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.button.PaneImageButton;
import gminers.glasspane.component.text.PaneLabel;
import gminers.glasspane.ease.PaneEaser;
import gminers.glasspane.event.PaneHideEvent;
import gminers.glasspane.listener.PaneEventHandler;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.minecraft.util.ResourceLocation;

import com.gameminers.glasspane.internal.GlassPaneMod;


public class PaneTestHarness
		extends GlassPane {
	private int ticks = 0;
	private GlassPane[] testHarnesses = {
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
			new PaneTestTextFields(),
			new PaneTestShadowboxes(),
			new PaneTestMisc()
	};
	
	private PaneButton flipButton;
	
	public PaneTestHarness() {
		setRevertAllowed(true);
		setScreenClearedBeforeDrawing(true);
		add(PaneLabel
				.createTitleLabel("Glass Pane Test Harness\n\u00A77Useful for texturers or learning what Glass Pane can do."));
		add(PaneTestHarness.createGithubButton("PaneTestHarness.java"), flipButton = PaneTestHarness.createFlipButton());
		add(PaneButton.createDoneButton());
	}
	
	private static final ResourceLocation github = new ResourceLocation("glasspaneharness", "textures/misc/github.png");
	private static final ResourceLocation flip = new ResourceLocation("glasspaneharness",
			"textures/misc/arrow-circle.png");
	
	static PaneButton createFlipButton() {
		final PaneImageButton button = new PaneImageButton();
		button.setText("Flip Screen");
		button.setWidth(85);
		button.setImage(flip);
		button.setAutoPositionX(true);
		button.setRelativeX(0.5);
		button.setRelativeXOffset(-189);
		button.setAutoPositionY(true);
		button.setRelativeY(1.0);
		button.setRelativeYOffset(-30);
		button.setOneBitTransparency(false);
		button.setAlignmentX(HorzAlignment.LEFT);
		button.setImageAlignment(HorzAlignment.RIGHT);
		button.setAngle(180f);
		button.setXRot(0);
		button.setYRot(0);
		button.setZRot(1);
		button.registerActivationListener(new Runnable() {
			
			@Override
			public void run() {
				GlassPane pane = button.getGlassPane();
				pane.setRotationAllowed(true);
				button.setRotationAllowed(true);
				PaneEaser paneEaser = new PaneEaser(pane);
				PaneEaser buttonEaser = new PaneEaser(button);
				buttonEaser.setSpeed(8D);
				paneEaser.setSpeed(8D);
				pane.setXRot(0);
				pane.setYRot(0);
				pane.setZRot(1);
				if (GlassPaneMod.invertMouseCoordinates) {
					buttonEaser.easeFloat("translateX", 0);
					buttonEaser.easeFloat("translateY", 0);
					buttonEaser.easeFloat("angle", 0);
					paneEaser.easeFloat("translateX", 0);
					paneEaser.easeFloat("translateY", 0);
					paneEaser.easeFloat("angle", 0);
					GlassPaneMod.invertMouseCoordinates = false;
				} else {
					buttonEaser.easeFloat("translateX", button.getWidth());
					buttonEaser.easeFloat("translateY", button.getHeight());
					buttonEaser.easeFloat("angle", 180);
					paneEaser.easeFloat("translateX", pane.getWidth());
					paneEaser.easeFloat("translateY", pane.getHeight());
					paneEaser.easeFloat("angle", 180);
					GlassPaneMod.invertMouseCoordinates = true;
				}
				paneEaser.setAutoClose(true);
				buttonEaser.setAutoClose(true);
			}
		});
		return button;
	}
	
	static PaneButton createGithubButton(String link) {
		try {
			final URI url = new URI(
					"http://github.com/AesenV/Glass-Pane/blob/master/src/harness/java/com/gameminers/glasspane/testharness/"
							+ link);
			PaneImageButton button = new PaneImageButton();
			button.setEnabled(Desktop.isDesktopSupported());
			button.setText("View Source");
			button.setWidth(85);
			button.setImage(github);
			button.setAutoPositionX(true);
			button.setRelativeX(0.5);
			button.setRelativeXOffset(104);
			button.setAutoPositionY(true);
			button.setRelativeY(1.0);
			button.setRelativeYOffset(-30);
			button.setOneBitTransparency(false);
			button.setAlignmentX(HorzAlignment.RIGHT);
			button.registerActivationListener(new Runnable() {
				
				@Override
				public void run() {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(url);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			return button;
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		return null;
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
	
	@PaneEventHandler
	public void onHide(PaneHideEvent e) {
		unrotate();
	}
	
	private void unrotate() {
		if (GlassPaneMod.easers.containsKey(this)) {
			GlassPaneMod.easers.get(this).close();
		}
		if (GlassPaneMod.easers.containsKey(flipButton)) {
			GlassPaneMod.easers.get(flipButton).close();
		}
		flipButton.setTranslateX(0);
		flipButton.setTranslateY(0);
		flipButton.setAngle(0);
		setTranslateX(0);
		setTranslateY(0);
		setAngle(0);
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
			if (gp.getComponents().size() <= 3) {
				button.setEnabled(false);
			}
			button.registerActivationListener(new Runnable() {
				
				@Override
				public void run() {
					unrotate();
					gp.modalOverlay();
				}
			});
			add(button);
			x += 100;
		}
	}
}
