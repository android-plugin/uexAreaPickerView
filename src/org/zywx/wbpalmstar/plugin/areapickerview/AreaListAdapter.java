package org.zywx.wbpalmstar.plugin.areapickerview;

import java.util.List;

/**
 * The simple Area List Adapter
 * 
 * @param <T>
 *            the element type
 */
public class AreaListAdapter<T> implements BaseAreaAdapter {

	private List<AreaInfo> list;

	/**
	 * Constructor
	 * 
	 * @param list
	 *            the list
	 */
	public AreaListAdapter(List<AreaInfo> list) {
		this.list = list;
	}

	public AreaListAdapter() {
	}

	public boolean add(AreaInfo areaInfo) {
		return list.add(areaInfo);
	}

	public void add(int index, AreaInfo areaInfo) {
		list.add(index, areaInfo);
	}

	public AreaInfo remove(int index) {
		return list.remove(index);
	}

	@Override
	public AreaInfo getAreaInfo(int index) {
		if (index >= 0 && index < list.size()) {
			return list.get(index);
		}
		return null;
	}

	@Override
	public String getAreaInfoId(int index) {
		if (index >= 0 && index < list.size()) {
			return list.get(index).getId();
		}
		return null;
	}

	@Override
	public String getAreaInfoName(int index) {
		if (index >= 0 && index < list.size()) {
			return list.get(index).getName();
		}
		return null;
	}

	@Override
	public int getSize() {
		return list.size();
	}
}
