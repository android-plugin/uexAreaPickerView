package org.zywx.wbpalmstar.plugin.areapickerview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.zywx.wbpalmstar.plugin.areapickerview.PickerView.OnWheelChangedListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ACEAreaPickerViewActivity extends Activity {

	private EUExAreaPickerView mUexBaseObj;
	private Button mCompleteBttn;
	private PickerView mProvinceView;
	private PickerView mCityView;
	private PickerView mDistrictView;
	private List<AreaInfo> mProvinceDate = new ArrayList<AreaInfo>();
	private HashMap<String, List<AreaInfo>> mCityDate = new HashMap<String, List<AreaInfo>>();
	private HashMap<String, List<AreaInfo>> mDistrictDate = new HashMap<String, List<AreaInfo>>();
	private AreaListAdapter<List<AreaInfo>> mProvinceAdapter = new AreaListAdapter<List<AreaInfo>>();
	private AreaListAdapter<List<AreaInfo>> mCityAdapter = new AreaListAdapter<List<AreaInfo>>();
	private AreaListAdapter<List<AreaInfo>> mDistrictAdapter = new AreaListAdapter<List<AreaInfo>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CRes.init(getApplication());
		Intent intent = getIntent();
		if (intent
				.hasExtra(EAreaPickerViewUtil.AREAPICKERVIEW_EXTRA_UEXBASE_OBJ)) {
			mUexBaseObj = (EUExAreaPickerView) intent
					.getParcelableExtra(EAreaPickerViewUtil.AREAPICKERVIEW_EXTRA_UEXBASE_OBJ);
		}
		setContentView(CRes.plugin_areapickerview_layout);
		mCompleteBttn = (Button) findViewById(CRes.plugin_areapickerview_complete);
		mCompleteBttn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mUexBaseObj != null) {
					JSONObject jsonObject = new JSONObject();
					try {
						String city = mProvinceView.getAreaInfoName()+" "
								+ mCityView.getAreaInfoName()+" "
								+ mDistrictView.getAreaInfoName();
						jsonObject
								.put(EAreaPickerViewUtil.AREAPICKERVIEW_PARAMS_JSON_KEY_CITY,
										city);
						String js = EUExAreaPickerView.SCRIPT_HEADER
								+ "if("
								+ EAreaPickerViewUtil.AREAPICKERVIEW_FUN_ON_CONFIRMCLICK
								+ "){"
								+ EAreaPickerViewUtil.AREAPICKERVIEW_FUN_ON_CONFIRMCLICK
								+ "('" + jsonObject.toString() + "');}";
						mUexBaseObj.onCallback(js);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		mProvinceView = (PickerView) findViewById(CRes.plugin_areapickerview_province);
		mCityView = (PickerView) findViewById(CRes.plugin_areapickerview_city);
		mDistrictView = (PickerView) findViewById(CRes.plugin_areapickerview_district);
		initAreaData();
	}

	private void initAreaData() {
		String areaString = EAreaPickerViewUtil.readAreaJSON(this);
		mProvinceDate = EAreaPickerViewUtil.getJSONParserResult(areaString,
				"area0");
		mCityDate = EAreaPickerViewUtil.getJSONParserResultArray(areaString,
				"area1");
		mDistrictDate = EAreaPickerViewUtil.getJSONParserResultArray(
				areaString, "area2");

		mProvinceAdapter = new AreaListAdapter<List<AreaInfo>>(mProvinceDate);
		mProvinceView.setAdapter(mProvinceAdapter);
		mProvinceView.setCyclic(false);
		mProvinceView.setCurrentItem(0);
		mProvinceView.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(PickerView wheel, int oldValue, int newValue) {
				setCityAdapter(mProvinceAdapter.getAreaInfoId(newValue));
				setDistrictAdapter(mCityDate
						.get(mProvinceAdapter.getAreaInfoId(newValue)).get(0)
						.getId());
			}
		});

		setCityAdapter(mProvinceAdapter.getAreaInfoId(0));
		mCityView.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(PickerView wheel, int oldValue, int newValue) {
				setDistrictAdapter(mCityAdapter.getAreaInfoId(newValue));
			}
		});

		setDistrictAdapter(mCityAdapter.getAreaInfoId(0));
		mDistrictView.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(PickerView wheel, int oldValue, int newValue) {
			}
		});
	}

	private void setCityAdapter(String infoId) {
		List<AreaInfo> cityAreaInfos = mCityDate.get(infoId);
		mCityAdapter = new AreaListAdapter<List<AreaInfo>>(cityAreaInfos);
		mCityView.setAdapter(mCityAdapter);
		mCityView.setCurrentItem(0);
	}

	private void setDistrictAdapter(String infoId) {
		List<AreaInfo> districtAreaInfos = mDistrictDate.get(infoId);
		if (districtAreaInfos == null || districtAreaInfos.size() == 0) {
			districtAreaInfos = new ArrayList<AreaInfo>();
			AreaInfo areaInfo = new AreaInfo();
			areaInfo.setName("");
			areaInfo.setId("");
			districtAreaInfos.add(areaInfo);
		}
		mDistrictAdapter = new AreaListAdapter<List<AreaInfo>>(
				districtAreaInfos);
		mDistrictView.setAdapter(mDistrictAdapter);
		mDistrictView.setCurrentItem(0);
	}
}
