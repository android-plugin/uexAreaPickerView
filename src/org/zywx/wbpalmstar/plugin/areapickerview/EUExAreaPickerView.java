package org.zywx.wbpalmstar.plugin.areapickerview;

import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class EUExAreaPickerView extends EUExBase{

	public static final String TAG = "EUExAreaPickerView";

	public static final int AREAPICKERVIEW_MSG_OPEN = 0;
	public static final int AREAPICKERVIEW_MSG_CLOSE = 1;
    private ACEAreaPickerView mView;

	public EUExAreaPickerView(Context context, EBrowserView inParent) {
		super(context, inParent);
	}

	private void sendMessageWithType(int msgType, String[] params) {
		if (mHandler == null) {
			return;
		}
		Message msg = new Message();
		msg.what = msgType;
		msg.obj = this;
		Bundle b = new Bundle();
		b.putStringArray(EAreaPickerViewUtil.AREAPICKERVIEW_FUN_PARAMS_KEY,
				params);
		msg.setData(b);
		mHandler.sendMessage(msg);
	}

	@Override
	public void onHandleMessage(Message msg) {
		if (msg.what == AREAPICKERVIEW_MSG_OPEN) {
			handleOpen(msg);
		} else {
			handleMessageInAreaPickerView(msg);
		}
	}

	private void handleMessageInAreaPickerView(Message msg) {
        switch (msg.what) {
            case AREAPICKERVIEW_MSG_CLOSE:
                handleClose();
                break;
        }
	}

	private void handleClose() {
        if (mView == null) return;
		mBrwView.removeViewFromCurrentWindow(mView);
        mView = null;
	}

	private void handleOpen(Message msg) {
		Log.i(TAG, " handleOpen");
		String[] params = msg.getData().getStringArray(
				EAreaPickerViewUtil.AREAPICKERVIEW_FUN_PARAMS_KEY);
		try {
			if (mView != null) {
				return;
			}
			mView = new ACEAreaPickerView(mContext, this);
			DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					dm.widthPixels, RelativeLayout.LayoutParams.WRAP_CONTENT);
			addView2CurrentWindow(mView, lp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addView2CurrentWindow(final View child,
			RelativeLayout.LayoutParams parms) {
		int l = (int) (parms.leftMargin);
		int t = (int) (parms.topMargin);
		int w = parms.width;
		int h = parms.height;
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
		lp.gravity = Gravity.BOTTOM;
		lp.leftMargin = l;
		lp.topMargin = t;
		adptLayoutParams(parms, lp);
		mBrwView.addViewToCurrentWindow(child, lp);

		mBrwView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
					float h = child.getHeight();
					float y = event.getY();
					if (dm.heightPixels - Math.abs(y) > h) {
						close(null);
					}
				}
				return false;
			}
		});
	}

	public void open(String[] params) {
		sendMessageWithType(AREAPICKERVIEW_MSG_OPEN, params);
	}

	public void close(String[] params) {
		sendMessageWithType(AREAPICKERVIEW_MSG_CLOSE, params);
	}

	@Override
	protected boolean clean() {
		close(null);
		return false;
	}
}
