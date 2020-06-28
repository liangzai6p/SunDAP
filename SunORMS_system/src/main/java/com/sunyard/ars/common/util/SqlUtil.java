package com.sunyard.ars.common.util;

import java.util.ArrayList;
import java.util.List;

public class SqlUtil {
	/**
	 * 查询条件超过1000处理
	 * @param request
	 * @return
	 */
	public static <T> List<List<T>> getSumArrayList(List<T> list){
		List<List<T>> objectList = new ArrayList<>();
		int iSize = list.size() / 1000;
		int iCount = list.size() % 1000;
		for(int i = 0;i <= iSize;i++){
			List<T> newObjectList = new ArrayList<>();
			if(i == iSize){
				newObjectList.addAll(list.subList(i * 1000, i * 1000 + iCount));
			}else{
				newObjectList.addAll(list.subList(i * 1000, (i + 1) * 1000));
			}
			if(newObjectList.size() > 0){
				objectList.add(newObjectList);
			}
		}
		return objectList;
	}
}
