package org.zywx.wbpalmstar.plugin.areapickerview;

import android.content.Context;
import android.content.res.Resources;

public class CRes{
	public static int app_name;
	private static boolean init;
	public static int plugin_areapickerview_layout;
	public static int plugin_areapickerview_complete;
	public static int plugin_areapickerview_cancel;
	public static int plugin_areapickerview_province;
	public static int plugin_areapickerview_city;
	public static int plugin_areapickerview_district;
	public static int plugin_areapickerview_center_drawable;
	public static int plugin_areapickerview_raw_acearea;

	public static boolean init(Context context){
		if(init){
			return init;
		}
		String packg = context.getPackageName();
		Resources res = context.getResources();
		plugin_areapickerview_layout=res.getIdentifier("plugin_areapickerview_layout", "layout", packg);
		
		plugin_areapickerview_complete=res.getIdentifier("plugin_areapickerview_complete", "id", packg);
		plugin_areapickerview_cancel=res.getIdentifier("plugin_areapickerview_cancel", "id", packg);
		plugin_areapickerview_province=res.getIdentifier("plugin_areapickerview_province", "id", packg);
		plugin_areapickerview_city=res.getIdentifier("plugin_areapickerview_city", "id", packg);
		plugin_areapickerview_district=res.getIdentifier("plugin_areapickerview_district", "id", packg);
		
		plugin_areapickerview_center_drawable=res.getIdentifier("plugin_areapickerview_center_drawable", "drawable", packg);

		plugin_areapickerview_raw_acearea=res.getIdentifier("acearea", "raw", packg);
		app_name = res.getIdentifier("app_name", "string", packg);
		init = true;
		return true;
	}
}
