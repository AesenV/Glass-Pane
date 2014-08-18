package com.gameminers.glasspane.testharness;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.button.PaneCheckBox;
import gminers.glasspane.component.numeric.PaneSlider;
import gminers.glasspane.component.progress.PaneProgressBar;
import gminers.glasspane.component.progress.PaneProgressHueRing;
import gminers.glasspane.event.StateChangedEvent;
import gminers.glasspane.listener.PaneEventHandler;
import gminers.kitchensink.Strings;
import gminers.kitchensink.WaveType;


public class PaneTestProgressBars
		extends GlassPane {
	private WaveType[] waves = {
			WaveType.SINE,
			WaveType.TANGENT,
			WaveType.SECANT,
			WaveType.TRIANGLE
	};
	
	public PaneTestProgressBars() {
		setName("Progress Bars");
		add(PaneButton.createDoneButton());
		add(PaneTestHarness.createGithubButton("PaneTestProgressBars.java"), PaneTestHarness.createFlipButton());
		
		final PaneProgressBar basic = new PaneProgressBar();
		basic.setProgressTextShown(true);
		basic.setProgressText("Basic Bar");
		basic.setX(10);
		basic.setY(10);
		basic.setWidth(200);
		basic.setHeight(14);
		basic.setProgress(0);
		basic.setMaximumProgress(100);
		final PaneSlider slider = new PaneSlider();
		slider.setText("Progress");
		slider.setMaximum(100);
		slider.setX(10);
		slider.setY(28);
		slider.setWidth(200);
		slider.setHeight(14);
		slider.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChange(StateChangedEvent e) {
				basic.setProgress(slider.getValue());
			}
		});
		add(basic, slider);
		
		int y = 46;
		for (WaveType w : waves) {
			final PaneProgressBar indeterminate = new PaneProgressBar();
			indeterminate.setProgressTextShown(true);
			indeterminate.setProgressText("Indeterminate Bar (" + Strings.formatTitleCase(w.name()) + ")");
			indeterminate.setX(10);
			indeterminate.setY(y);
			indeterminate.setIndeterminateWave(w);
			indeterminate.setWidth(200);
			indeterminate.setHeight(14);
			indeterminate.setIndeterminateColor(indeterminate.getColor());
			indeterminate.setIndeterminate(true);
			add(indeterminate);
			// It's pretty busy and distracting with all of them animating at once, so offer a way to turn it off
			final PaneCheckBox animate = new PaneCheckBox("Animate", false);
			animate.setX(214);
			animate.setY(y + 2);
			animate.registerListeners(new Object() {
				@PaneEventHandler
				public void onStateChange(StateChangedEvent e) {
					if (animate.isSelected()) {
						indeterminate.setIndeterminateColor(0x5555FF);
					} else {
						indeterminate.setIndeterminateColor(indeterminate.getColor());
					}
				}
			});
			add(animate);
			y += 18;
		}
		
		final PaneProgressHueRing ring = new PaneProgressHueRing();
		ring.setX(10);
		ring.setY(y);
		ring.setProgressTextShown(true);
		ring.setProgressText("Ring");
		ring.setWidth(40);
		ring.setHeight(40);
		ring.setProgress(0);
		ring.setMaximumProgress(100);
		final PaneSlider ringSlider = new PaneSlider();
		ringSlider.setText("Ring Progress");
		ringSlider.setMaximum(100);
		ringSlider.setX(54);
		ringSlider.setY(y);
		ringSlider.setWidth(156);
		ringSlider.setHeight(20);
		ringSlider.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChange(StateChangedEvent e) {
				ring.setProgress(ringSlider.getValue());
			}
		});
		final PaneSlider hueSlider = new PaneSlider();
		hueSlider.setText("Ring Hue");
		hueSlider.setMaximum(360);
		hueSlider.setX(54);
		hueSlider.setY(y + 20);
		hueSlider.setWidth(156);
		hueSlider.setHeight(20);
		hueSlider.setValue((int) ring.getTargetHue());
		hueSlider.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChange(StateChangedEvent e) {
				ring.setTargetHue(hueSlider.getValue());
			}
		});
		add(ring, ringSlider, hueSlider);
		
		final PaneProgressBar exact = new PaneProgressBar();
		exact.setProgressTextShown(true);
		exact.setProgressText("Exact, Colored Bar");
		exact.setX(214);
		exact.setY(10);
		exact.setColor(0xFFFFFF);
		exact.setProgressTextColor(0xFFFF00);
		exact.setFilledColor(0x0000FF);
		exact.setWidth(200);
		exact.setHeight(14);
		exact.setAccuracy(1.0);
		exact.setProgress(0);
		exact.setMaximumProgress(100);
		final PaneSlider exactSlider = new PaneSlider();
		exactSlider.setText("Exact Progress");
		exactSlider.setMaximum(100);
		exactSlider.setX(214);
		exactSlider.setY(28);
		exactSlider.setWidth(200);
		exactSlider.setHeight(14);
		exactSlider.registerListeners(new Object() {
			@PaneEventHandler
			public void onStateChange(StateChangedEvent e) {
				exact.setProgress(exactSlider.getValue());
			}
		});
		add(exact, exactSlider);
	}
}
