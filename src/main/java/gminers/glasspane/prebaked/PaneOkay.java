package gminers.glasspane.prebaked;


import gminers.glasspane.GlassPane;
import gminers.glasspane.component.button.PaneButton;
import gminers.glasspane.component.text.PaneLabel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Implements a message dialog. Designed for use with {@link GlassPane#modalOverlay()}, but works with {@link GlassPane#show()} as
 * well.
 * 
 * @author Aesen Vismea
 * 
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@Getter
public class PaneOkay
		extends GlassPane {
	public interface OkayCallback {
		void onOkay(final PaneOkay pane);
	}
	
	String text;
	String okText;
	OkayCallback callback;
	
	PaneButton okButton;
	
	public PaneOkay(final String text, final String okText, final OkayCallback callback) {
		setRevertAllowed(true);
		this.text = text;
		this.okText = okText;
		this.callback = callback;
		add(PaneLabel.createTitleLabel(text));
		okButton = new PaneButton(okText);
		okButton.registerActivationListener(new Runnable() {
			
			@Override
			public void run() {
				if (callback != null) {
					callback.onOkay(PaneOkay.this);
				}
				revert();
			}
		});
		okButton.setAutoPosition(true);
		okButton.setRelativeX(0.5);
		okButton.setRelativeY(1.0);
		okButton.setRelativeXOffset(-100);
		okButton.setRelativeYOffset(-25);
		
		add(okButton);
	}
}
