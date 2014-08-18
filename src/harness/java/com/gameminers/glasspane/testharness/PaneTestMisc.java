package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.PaneBlinker;
import gminers.glasspane.component.PaneBox;
import gminers.glasspane.component.PaneGradientBox;
import gminers.glasspane.component.PaneImage;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.button.PaneToggleButton;
import gminers.glasspane.component.text.PaneLabel;
import gminers.kitchensink.Strings;
import gminers.kitchensink.WaveType;
import net.minecraft.util.ResourceLocation;


public class PaneTestMisc
		extends GlassPane {
	private int x = 20;
	private int y = 10;
	
	public PaneTestMisc() {
		setName("Miscellaneous");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestMisc.java"), PaneTestHarness.createFlipButton());
		
		PaneBlinker blinker = new PaneBlinker();
		setup("Blinker", blinker);
		
		WaveType[] waves = {
				WaveType.SINE,
				WaveType.TRIANGLE,
				WaveType.ABSOLUTE_SINE
		};
		for (WaveType wave : waves) {
			PaneBlinker waveBlinker = new PaneBlinker();
			waveBlinker.setWave(wave);
			setup(Strings.formatTitleCase(wave.name()) + " Wave Blinker", waveBlinker);
		}
		
		PaneBlinker fastBlinker = new PaneBlinker();
		fastBlinker.setSpeed(2);
		setup("Fast Blinker", fastBlinker);
		
		PaneBlinker slowBlinker = new PaneBlinker();
		slowBlinker.setSpeed(10);
		setup("Slow Blinker", slowBlinker);
		
		PaneImage image = new PaneImage(new ResourceLocation("textures/items/iron_ingot.png"));
		image.setX(224);
		image.setY(10);
		image.setWidth(16);
		image.setHeight(16);
		add(image);
		PaneLabel imageLabel = new PaneLabel("Image");
		imageLabel.setX(244);
		imageLabel.setY(14);
		add(imageLabel);
		
		PaneBox box = new PaneBox(0xFFFF5555);
		box.setX(224);
		box.setY(30);
		box.setWidth(16);
		box.setHeight(16);
		add(box);
		PaneLabel boxLabel = new PaneLabel("Box");
		boxLabel.setX(244);
		boxLabel.setY(34);
		add(boxLabel);
		
		PaneGradientBox gradientBox = new PaneGradientBox(0xFF5555FF, 0xFF55FF55);
		gradientBox.setX(224);
		gradientBox.setY(50);
		gradientBox.setWidth(16);
		gradientBox.setHeight(16);
		add(gradientBox);
		PaneLabel gradientBoxLabel = new PaneLabel("Gradient Box");
		gradientBoxLabel.setX(244);
		gradientBoxLabel.setY(54);
		add(gradientBoxLabel);
	}
	
	private PaneButton setup(String text, final PaneBlinker blinker) {
		final PaneToggleButton button = new PaneToggleButton(text);
		button.setX(x);
		button.setY(y);
		button.setWidth(160);
		button.registerActivationListener(new Runnable() {
			
			@Override
			public void run() {
				blinker.setBlinking(button.isSelected());
			}
		});
		blinker.target(button);
		blinker.setBlinking(false);
		add(button, blinker);
		y += 30;
		return button;
	}
}
