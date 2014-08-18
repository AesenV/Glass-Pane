package gminers.glasspane.prebaked;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.text.PaneLabel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Implements a confirmation dialog. Designed for use with {@link GlassPane#modalOverlay()}, but works with {@link GlassPane#show()} as
 * well.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class PaneYesNo
		extends GlassPane {
	public interface YesNoCallback {
		void onYes(final PaneYesNo pane);
		
		void onNo(final PaneYesNo pane);
	}
	
	String text;
	String yesText;
	String noText;
	YesNoCallback callback;
	
	PaneButton yesButton;
	PaneButton noButton;
	
	
	public PaneYesNo(final String text, final String yesText, final String noText, final YesNoCallback callback) {
		setRevertAllowed(true);
		this.text = text;
		this.yesText = yesText;
		this.noText = noText;
		this.callback = callback;
		add(PaneLabel.createTitleLabel(text));
		yesButton = new PaneButton(yesText);
		noButton = new PaneButton(noText);
		yesButton.registerActivationListener(new Runnable() {
			
			@Override
			public void run() {
				if (callback != null) {
					callback.onYes(PaneYesNo.this);
				}
				revert();
			}
		});
		noButton.registerActivationListener(new Runnable() {
			
			@Override
			public void run() {
				if (callback != null) {
					callback.onNo(PaneYesNo.this);
				}
				revert();
			}
		});
		yesButton.setWidth(100);
		yesButton.setAutoPosition(true);
		yesButton.setRelativeX(0.5);
		yesButton.setRelativeY(1.0);
		yesButton.setRelativeXOffset(-105);
		yesButton.setRelativeYOffset(-25);
		
		noButton.setWidth(100);
		noButton.setAutoPosition(true);
		noButton.setRelativeX(0.5);
		noButton.setRelativeY(1.0);
		noButton.setRelativeXOffset(5);
		noButton.setRelativeYOffset(-25);
		
		add(yesButton, noButton);
	}
}
