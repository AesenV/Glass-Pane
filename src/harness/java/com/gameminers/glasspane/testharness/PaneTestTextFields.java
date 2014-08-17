package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.text.PanePasswordField;
import gminers.glasspane.component.text.PaneTextField;
import net.minecraft.util.ResourceLocation;


public class PaneTestTextFields
		extends GlassPane {
	public PaneTestTextFields() {
		setName("Text Fields");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestTextFields.java"), PaneTestHarness.createFlipButton());
		
		PaneTextField basic = new PaneTextField("Basic Text Field");
		basic.setX(10);
		basic.setY(10);
		add(basic);
		
		PaneTextField placeholder = new PaneTextField();
		placeholder.setBlankText("Text Field with Placeholder");
		placeholder.setX(10);
		placeholder.setY(34);
		add(placeholder);
		
		PanePasswordField password = new PanePasswordField();
		password.setBlankText("Password Field");
		password.setX(10);
		password.setY(56);
		add(password);
		
		PanePasswordField passwordWithIcon = new PanePasswordField();
		passwordWithIcon.setBlankText("Password Field with Icon");
		passwordWithIcon.setX(10);
		passwordWithIcon.setY(78);
		passwordWithIcon.setIcon(new ResourceLocation("textures/items/diamond.png"));
		add(passwordWithIcon);
		
		PaneTextField normalWithIcon = new PaneTextField();
		normalWithIcon.setBlankText("Text Field with Icon");
		normalWithIcon.setX(10);
		normalWithIcon.setY(102);
		normalWithIcon.setIcon(new ResourceLocation("textures/items/carrot.png"));
		add(normalWithIcon);
	}
}
