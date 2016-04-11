package framgia.vn.demoanimation.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;

import framgia.vn.demoanimation.R;

public class BackDialog extends Dialog {
	
	public Button positive;
	public Button negative;

	public BackDialog(Context context) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		this.setContentView(R.layout.dialog_back);
		
		positive = (Button) findViewById(R.id.yes_btn);
		negative = (Button) findViewById(R.id.no_btn);
	}

}
