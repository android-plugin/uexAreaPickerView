package org.zywx.wbpalmstar.plugin.areapickerview;

import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class EUExAreaPickerView extends EUExBase implements Parcelable {

	public static final String TAG = "EUExAreaPickerView";

	public static final int AREAPICKERVIEW_MSG_OPEN = 0;
	public static final int AREAPICKERVIEW_MSG_CLOSE = 1;
	private static LocalActivityManager mgr;

	public EUExAreaPickerView(Context context, EBrowserView inParent) {
		super(context, inParent);
		mgr = ((ActivityGroup) mContext).getLocalActivityManager();
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
		String activityId = EAreaPickerViewUtil.AREAPICKERVIEW_ACTIVITY_ID
				+ EUExAreaPickerView.this.hashCode();
		Activity activity = mgr.getActivity(activityId);

		if (activity != null && activity instanceof ACEAreaPickerViewActivity) {
			String[] params = msg.getData().getStringArray(
					EAreaPickerViewUtil.AREAPICKERVIEW_FUN_PARAMS_KEY);
			ACEAreaPickerViewActivity aActivity = ((ACEAreaPickerViewActivity) activity);

			switch (msg.what) {
			case AREAPICKERVIEW_MSG_CLOSE:
				handleClose(aActivity, mgr);
				break;
			}
		}
	}

	private void handleClose(ACEAreaPickerViewActivity aActivity,
			LocalActivityManager mgr) {
		Log.i(TAG, " handleClose");
		View decorView = aActivity.getWindow().getDecorView();
		mBrwView.removeViewFromCurrentWindow(decorView);
		String activityId = EAreaPickerViewUtil.AREAPICKERVIEW_ACTIVITY_ID
				+ EUExAreaPickerView.this.hashCode();
		mgr.destroyActivity(activityId, true);
	}

	private void handleOpen(Message msg) {
		Log.i(TAG, " handleOpen");
		String[] params = msg.getData().getStringArray(
				EAreaPickerViewUtil.AREAPICKERVIEW_FUN_PARAMS_KEY);
		try {
			String activityId = EAreaPickerViewUtil.AREAPICKERVIEW_ACTIVITY_ID
					+ EUExAreaPickerView.this.hashCode();
			ACEAreaPickerViewActivity aActivity = (ACEAreaPickerViewActivity) mgr
					.getActivity(activityId);
			if (aActivity != null) {
				return;
			}
			Intent intent = new Intent(mContext,
					ACEAreaPickerViewActivity.class);
			intent.putExtra(
					EAreaPickerViewUtil.AREAPICKERVIEW_EXTRA_UEXBASE_OBJ, this);
			Window window = mgr.startActivity(activityId, intent);
			final View decorView = window.getDecorView();
			DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					dm.widthPixels, RelativeLayout.LayoutParams.WRAP_CONTENT);
			addView2CurrentWindow(decorView, lp);
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}
}
