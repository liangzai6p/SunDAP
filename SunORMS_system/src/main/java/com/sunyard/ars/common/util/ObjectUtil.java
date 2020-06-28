package com.sunyard.ars.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ObjectUtil {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/*
	 * 从一个对象实例复制不为空的属性值到另一个对象实例
	 */
	public void copyNotNullProperty(Object obj4, Object obj2) {
		Class obj4Class = obj4.getClass();
		Field[] fields = obj4Class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			String fieldType = fields[i].getType().toString();
			String simpleFieldType = fieldType.substring(fieldType.lastIndexOf(".") + 1);
			String methodName = this.getGetMethodName(fieldName);
			Object value = getValueByGetMethod(methodName, obj4);
			if ("Boolean".equalsIgnoreCase(simpleFieldType) && value != null) {
				this.setValue(fields[i], value, obj2);
			} else if ("Char".equalsIgnoreCase(simpleFieldType) && value != null) {
				if(!"".equals(value.toString().trim())){
					this.setValue(fields[i], value, obj2);
				}
			} else if ("Byte".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0".equals(value.toString().trim())){
					this.setValue(fields[i], value, obj2);
				}
			} else if ("Short".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0".equals(value.toString().trim())){
					this.setValue(fields[i], value, obj2);
				}
			} else if ("Integer".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0".equals(value.toString().trim())){
					this.setValue(fields[i], value, obj2);
				}
			} else if ("Long".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0".equals(value.toString().trim())){
					this.setValue(fields[i], value, obj2);
				}
			} else if ("Float".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0.0".equals(value.toString().trim())){
					this.setValue(fields[i], value, obj2);
				}
			} else if ("Double".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0.0".equals(value.toString().trim())){
					this.setValue(fields[i], value, obj2);
				}
			} else if ("String".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"".equals(value.toString().trim())){
					this.setValue(fields[i], value, obj2);
				}
			} else if (value != null) {
				this.setValue(fields[i], value, obj2);
			}
		}
	}

	/*
	 * 从一个对象实例复制不为空的属性值到另一个对象实例
	 */
	public void copyProperties(Object obj4, Object obj2) {
		Class obj4Class = obj4.getClass();
		Field[] fields = obj4Class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) 
		{
			String fieldName = fields[i].getName();
			String methodName = this.getGetMethodName(fieldName);
			Object value = getValueByGetMethod(methodName, obj4);
			this.setValue(fields[i], value, obj2);
		}
	}

	/**
	 * 判断一个对象是否为空：对象的所有属性是否为空或null Boolean 布尔型 只有两个值true、false Char 字符型 Byte 8位带符号整数 -128到127之间的任意整数 Short 16位无符号整数 -32768~32767之间的任意整数 Int 32位带符号整数 -231到231-1之间的任意整数 Long 64位带符号整数
	 * -263到263-1之间的任意整数 Float 32位单精度浮点数 根据IEEE754-1985标准 Double 64位双精度浮点数 根据IEEE754-1985标准
	 */
	public boolean isAllValueNull(Object obj) {
		boolean isNull = true;
		Class ownerClass = obj.getClass();
		Field[] fields = ownerClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			String fieldType = fields[i].getType().toString();
			String simpleFieldType = fieldType.substring(fieldType.lastIndexOf(".") + 1);
			String methodName = this.getGetMethodName(fieldName);
			Object value = getValueByGetMethod(methodName, obj);
			if ("Boolean".equalsIgnoreCase(simpleFieldType) && value != null) {
				return false;
			} else if ("Char".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"".equals(value.toString().trim())){
					return false;
				}
			} else if ("Byte".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0".equals(value.toString().trim())){
					return false;
				}
			} else if ("Short".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0".equals(value.toString().trim())){
					return false;
				}
			} else if ("Integer".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0".equals(value.toString().trim())){
					return false;
				}
			} else if ("Long".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0".equals(value.toString().trim())){
					return false;
				}
			} else if ("Float".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0.0".equals(value.toString().trim())){
					return false;
				}
			} else if ("Double".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"0.0".equals(value.toString().trim())){
					return false;
				}
			} else if ("String".equalsIgnoreCase(simpleFieldType) && value != null ) {
				if(!"".equals(value.toString().trim())){
					return false;
				}
			} else if (value != null) {
				return false;
			}
			// System.out.println(simpleFieldType+" "+fieldName+" "+value);
		}
		return isNull;
	}

	/**
	 * 取得属性名 为Key 属性值为Value 存入Hashtable中<fieldName, value>
	 */
	public Hashtable getFieldsAndValue(Object obj) {
		Hashtable table = new Hashtable();
		Class ownerClass = obj.getClass();
		Field[] fields = ownerClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			String methodName = this.getGetMethodName(fieldName);
			Object value = getValueByGetMethod(methodName, obj);
			if (value == null) {
				value = "";
			}
			table.put(fieldName, value);
		}
		return table;
	}

	/**
	 * 取得属性名 为Key 属性值为type 存入Hashtable中<fieldName, fieldType>
	 */
	public Hashtable getFieldsAndType(Object obj) {
		Hashtable table = new Hashtable();
		Class ownerClass = obj.getClass();
		Field[] fields = ownerClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldType = fields[i].getType().toString();
			String fieldName = fields[i].getName();
			table.put(fieldName, fieldType);
		}
		return table;
	}

	/**
	 * constract get method 构造get方法
	 */
	private String getGetMethodName(String proName) {
		char c = (char) proName.charAt(0);
		if (c > 96 && c < 123) {
			c = (char) (proName.charAt(0) - 32);
		}
		String methodName = "get" + c + proName.substring(1, proName.length());
		return methodName;
	}

	/**
	 * constract set method 构造get方法
	 */
	private String getSetMethodName(String proName) {
		char c = (char) proName.charAt(0);
		if (c > 96 && c < 123) {
			c = (char) (proName.charAt(0) - 32);
		}
		String methodName = "set" + c + proName.substring(1, proName.length());
		return methodName;
	}

	/**
	 * get value 动态调用get方法,取得属性值.
	 */
	private Object getValueByGetMethod(String methodName, Object obj) {
		Method method = null;
		Object value = null;
		try {
			method = obj.getClass().getMethod(methodName, null);
			if (method != null) {
				value = method.invoke(obj, null);
			} else {
				logger.info("对象没有这个get方法：" + methodName);
			}
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			logger.error("找不到相应的Get方法: " + methodName, e);
		}
		return value;
	}

	/*
	 * set方法
	 */
	private void setValue(Field field, Object valueObj, Object obj2) {
		// 获得相应属性的getXXX和setXXX方法名称 //获取相应的方法
		try {
			String fieldName = field.getName();
			String stringLetter = fieldName.substring(0, 1).toUpperCase();
			String setName = "set" + stringLetter + fieldName.substring(1);
			Method setMethod = obj2.getClass().getMethod(setName, new Class[] { field.getType() });
			if (setMethod != null) {
				setMethod.invoke(obj2, new Object[] { valueObj });
			} else {
				logger.info("对象没有这个set方法：" + setName);
			}
		} catch (Exception e) {
//			e.printStackTrace();
//			logger.warn(e.getMessage());
		}
	}

}
