package org.zywx.wbpalmstar.plugin.areapickerview;

public interface BaseAreaAdapter {
	/**
	 * Gets items count
	 * 
	 * @return the count of area items
	 */
	public int getSize();

	/**
	 * Gets a area item by index.
	 * 
	 * @param index
	 *            the item index
	 * @return the area item id or null
	 */
	public String getAreaInfoId(int index);

	/**
	 * Gets a area item by index.
	 * 
	 * @param index
	 *            the item index
	 * @return the area item text or null
	 */
	public String getAreaInfoName(int index);

	/**
	 * Gets a area item by index.
	 * 
	 * @param index
	 *            the item index
	 * @return the area item or null
	 */
	public AreaInfo getAreaInfo(int index);

}
