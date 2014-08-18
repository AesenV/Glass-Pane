package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.HorzAlignment;
import gminers.glasspane.VertAlignment;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.button.PaneCheckBox;
import gminers.glasspane.component.button.PaneImageButton;
import gminers.glasspane.component.button.PaneRadioButton;
import gminers.glasspane.component.button.PaneToggleButton;
import gminers.glasspane.component.button.RadioButtonGroup;
import gminers.glasspane.component.text.PaneLabel;
import gminers.kitchensink.RandomPool;
import net.minecraft.util.ResourceLocation;


public class PaneTestButtons
		extends GlassPane {
	private String[] textures = {
			"textures/items/diamond_sword.png",
			"textures/items/diamond_pickaxe.png",
			"textures/items/diamond_axe.png",
			"textures/items/diamond_shovel.png",
			"textures/items/diamond_helmet.png",
			"textures/items/diamond_chestplate.png",
			"textures/items/diamond_leggings.png",
			"textures/items/diamond_boots.png"
	};
	
	public PaneTestButtons() {
		setName("Buttons");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestButtons.java"), PaneTestHarness.createFlipButton());
		
		final PaneButton basic = new PaneButton("Basic Button");
		basic.setX(10);
		basic.setY(10);
		basic.setWidth(98);
		add(basic);
		
		final PaneButton colored = new PaneButton("Colored Button");
		colored.setButtonColor(0xFF0000);
		colored.setColor(0xFFFF00);
		colored.setHoveredColor(0x0000FF);
		colored.registerActivationListener(new Runnable() {
			
			@Override
			public void run() {
				colored.setButtonColor(RandomPool.nextInt());
				colored.setHoveredColor(RandomPool.nextInt());
				colored.setColor(RandomPool.nextInt());
			}
		});
		colored.setX(110);
		colored.setY(10);
		colored.setWidth(98);
		add(colored);
		
		final PaneImageButton imageButton = new PaneImageButton();
		imageButton.setImage(new ResourceLocation("textures/items/diamond.png"));
		imageButton.setText("Image Button");
		imageButton.setX(10);
		imageButton.setY(34);
		add(imageButton);
		
		PaneToggleButton toggleButton = new PaneToggleButton("Toggle Button");
		toggleButton.setX(10);
		toggleButton.setY(58);
		add(toggleButton);
		
		PaneButton disabledButton = new PaneButton("Disabled Button");
		disabledButton.setX(10);
		disabledButton.setY(82);
		disabledButton.setEnabled(false);
		add(disabledButton);
		
		int x = 10;
		for (String s : textures) {
			if (s.equals("textures/items/diamond_helmet.png")) {
				// alignment hack
				x -= 1;
			}
			final PaneImageButton iconButton = new PaneImageButton();
			iconButton.setImage(new ResourceLocation(s));
			iconButton.setWidth(20);
			iconButton.setText("");
			iconButton.setX(x);
			iconButton.setY(106);
			add(iconButton);
			x += 26;
		}
		
		PaneLabel iconButtonLabel = new PaneLabel("Also Image Buttons");
		iconButtonLabel.setX(10);
		iconButtonLabel.setY(130);
		iconButtonLabel.setHeight(12);
		iconButtonLabel.setWidth(200);
		iconButtonLabel.setAlignmentY(VertAlignment.MIDDLE);
		iconButtonLabel.setAlignmentX(HorzAlignment.MIDDLE);
		add(iconButtonLabel);
		
		
		PaneCheckBox checkBox = new PaneCheckBox("Checkbox");
		checkBox.setX(214);
		checkBox.setY(10);
		add(checkBox);
		
		PaneCheckBox coloredCheckBox = new PaneCheckBox("Colored Checkbox");
		coloredCheckBox.setX(214);
		coloredCheckBox.setY(24);
		coloredCheckBox.setButtonColor(0xFF0000);
		coloredCheckBox.setColor(0xFFFF00);
		coloredCheckBox.setHoveredColor(0x0000FF);
		add(coloredCheckBox);
		
		PaneCheckBox disabledCheckBox = new PaneCheckBox("Disabled Checkbox");
		disabledCheckBox.setX(214);
		disabledCheckBox.setY(38);
		disabledCheckBox.setEnabled(false);
		add(disabledCheckBox);
		
		PaneRadioButton radioButton = new PaneRadioButton("Radio Button");
		radioButton.setX(214);
		radioButton.setY(52);
		add(radioButton);
		
		RadioButtonGroup group = new RadioButtonGroup();
		for (int i = 0; i < 5; i++) {
			PaneRadioButton groupedRadioButton = new PaneRadioButton("Grouped Radio Button (" + i + ")");
			if (i == 4) {
				groupedRadioButton.setText("Grouped Radio Button (4, Disabled)");
				groupedRadioButton.setEnabled(false);
			}
			groupedRadioButton.setX(214);
			groupedRadioButton.setY(66 + (i * 12));
			group.add(groupedRadioButton);
			add(groupedRadioButton);
		}
		
		PaneLabel tabLabel = new PaneLabel(
				"Try pressing Tab and Shift+Tab to change the focus\nPress Enter to send a click");
		tabLabel.setX(10);
		tabLabel.setY(150);
		tabLabel.setHeight(12);
		tabLabel.setAutoResizeWidth(true);
		tabLabel.setAlignmentY(VertAlignment.MIDDLE);
		tabLabel.setAlignmentX(HorzAlignment.MIDDLE);
		add(tabLabel);
	}
}
