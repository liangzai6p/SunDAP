package com.sunyard.ars.common.util;

import java.util.ArrayList;
import java.util.List;



import com.sunyard.ars.common.pojo.FormFieldModel;

/**
 * 排列组合工具类
 * @date   2018年9月19日
 * @Description CombinationUtil.java
 */
public class CombinationUtil {
	
	 @SuppressWarnings({ "rawtypes", "unchecked" })  
	    public static ArrayList combinationOcr(List<List> list) { 
		 if(list.size() == 0){
			 return (ArrayList)list;
		 }else if(list.size() == 1){
			 //list.size为1时，只将list.get(0)处理
			 ArrayList temp = new ArrayList(); 
			 ArrayList cut = null;
			 ArrayList a0 = (ArrayList) list.get(0);// l1  
			 for(int i=0;i<a0.size();i++){
				 cut = new ArrayList();
				 cut.add(a0.get(i));
				 temp.add(cut);
			 }			 
			 return temp;
		 }
	        ArrayList a0 = (ArrayList) list.get(0);// l1  
	        ArrayList result = new ArrayList();// 组合的结果  
	        for (int i = 1; i < list.size(); i++) {  
	            ArrayList a1 = (ArrayList) list.get(i);  
	            ArrayList temp = new ArrayList();  
	            // 每次先计算两个集合的笛卡尔积，然后用其结果再与下一个计算  
	            for (int j = 0; j < a0.size(); j++) {  
	                for (int k = 0; k < a1.size(); k++) {  
	                    ArrayList cut = new ArrayList();  
	  
	                    if (a0.get(j) instanceof ArrayList) {  
	                        cut.addAll((ArrayList) a0.get(j));  
	                    } else {  
	                        cut.add(a0.get(j));  
	                    }  
	                    if (a1.get(k) instanceof ArrayList) {  
	                        cut.addAll((ArrayList) a1.get(k));  
	                    } else {  
	                        cut.add(a1.get(k));  
	                    }  
	                    temp.add(cut);  
	                }  
	            }  
	            a0 = temp;  
	            if (i == list.size() - 1) {  
	                result = temp;  
	            }  
	        }  
	        return result;  
	    }  
	 
	 public static void main(String[] args) {
		List<FormFieldModel> l1 = new ArrayList<FormFieldModel>();
		FormFieldModel f1 = new FormFieldModel("name1", "value1");
		FormFieldModel f11 = new FormFieldModel("name1", "value11");
		l1.add(f1);
		l1.add(f11);
		
		List<FormFieldModel> l2 = new ArrayList<FormFieldModel>();
		FormFieldModel f2 = new FormFieldModel("name2", "value2");
		FormFieldModel f22 = new FormFieldModel("name2", "value22");
		l2.add(f2);
		l2.add(f22);
		
		List<List> list = new ArrayList<List>();
		list.add(l1);
		list.add(l2);
		List a = combinationOcr(list);
		for(int i=0;i<a.size();i++){
			System.out.println(a.get(i));
		}
	}
}
