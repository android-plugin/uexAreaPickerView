/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zywx.wbpalmstar.plugin.areapickerview;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EAreaPickerViewUtil {
	public static final String AREAPICKERVIEW_FUN_PARAMS_KEY = "areapickerviewFunParamsKey";
	public static final String AREAPICKERVIEW_ACTIVITY_ID = "areapickerviewActivityID";
	public final static String AREAPICKERVIEW_EXTRA_UEXBASE_OBJ = "org.zywx.wbpalmstar.plugin.uexareapickerview.AREAPICKERVIEW_EXTRA_UEXBASE_OBJ";
	public final static String AREAPICKERVIEW_FUN_ON_CONFIRMCLICK = "uexAreaPickerView.onConfirmClick";
	public final static String AREAPICKERVIEW_PARAMS_JSON_KEY_CITY = "city";

	/**
	 * 读取文本数据
	 * 
	 * @param context
	 *            程序上下文
	 * @return String, 读取到的文本内容，失败返回null
	 */
	public static String readAreaJSON(Context context) {
		InputStream is = null;
		String content = null;
		try {
			is = context.getResources().openRawResource(
					CRes.plugin_areapickerview_raw_acearea);
			if (is != null) {
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				while (true) {
					int readLength = is.read(buffer);
					if (readLength == -1)
						break;
					arrayOutputStream.write(buffer, 0, readLength);
				}
				is.close();
				arrayOutputStream.close();
				content = new String(arrayOutputStream.toByteArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
			content = null;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return content;
	}

	public static List<AreaInfo> getJSONParserResult(String JSONString,
			String key) {
		List<AreaInfo> list = new ArrayList<AreaInfo>();
		JsonObject result = new JsonParser().parse(JSONString)
				.getAsJsonObject().getAsJsonObject(key);
		Iterator iterator = result.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
					.next();
			AreaInfo areaInfo = new AreaInfo();
			areaInfo.setName(entry.getValue().getAsString());
			areaInfo.setId(entry.getKey());
			list.add(areaInfo);
		}
		return list;
	}

	public static HashMap<String, List<AreaInfo>> getJSONParserResultArray(
			String JSONString, String key) {
		HashMap<String, List<AreaInfo>> hashMap = new HashMap<String, List<AreaInfo>>();
		JsonObject result = new JsonParser().parse(JSONString)
				.getAsJsonObject().getAsJsonObject(key);

		Iterator iterator = result.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
					.next();
			List<AreaInfo> list = new ArrayList<AreaInfo>();
			JsonArray array = entry.getValue().getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				AreaInfo areaInfo = new AreaInfo();
				areaInfo.setName(array.get(i).getAsJsonArray().get(0)
						.getAsString());
				areaInfo.setId(array.get(i).getAsJsonArray().get(1)
						.getAsString());
				list.add(areaInfo);
			}
			hashMap.put(entry.getKey(), list);
		}
		return hashMap;
	}
}