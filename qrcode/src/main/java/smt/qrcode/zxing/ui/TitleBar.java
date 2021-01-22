package smt.qrcode.zxing.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import smt.qrcode.R;

public class TitleBar extends RelativeLayout {

	public TextView titleBar_right_text;
	public TextView titleBar_centre_text;
	public ImageView titleBar_left_image;

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	public TitleBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_titlebar, this);
		titleBar_right_text = (TextView) view
				.findViewById(R.id.titleBar_right_text);
		titleBar_centre_text = (TextView) view
				.findViewById(R.id.titleBar_centre_text);
		titleBar_left_image = (ImageView) view
				.findViewById(R.id.titleBar_left_text);
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.TitleBar);
		
		titleBar_right_text.setText(array
				.getString(R.styleable.TitleBar_titleBar_right_text));
		boolean right_isvisible = array.getBoolean(
				R.styleable.TitleBar_titleBar_right_visible, true);
		if (right_isvisible) {
			titleBar_right_text.setVisibility(View.VISIBLE);
//			titleBar_right_text.setBackground(array
//					.getDrawable(R.styleable.TitleBar_titleBar_right_image));
		} else {
			titleBar_right_text.setVisibility(View.GONE);
		}
		titleBar_centre_text.setText(array
				.getString(R.styleable.TitleBar_titleBar_centre_text));

//		titleBar_left_text.setText(array
//				.getString(R.styleable.TitleBar_titleBar_left_text));
//		titleBar_left_text.setBackground(array
//				.getDrawable(R.styleable.TitleBar_titleBar_left_image));
		boolean left_visible = array.getBoolean(
				R.styleable.TitleBar_titleBar_left_visible, true);
		if (left_visible) {
			titleBar_left_image.setVisibility(View.VISIBLE);
		} else {
			titleBar_left_image.setVisibility(View.GONE);
		}
		array.recycle();
	}

}
