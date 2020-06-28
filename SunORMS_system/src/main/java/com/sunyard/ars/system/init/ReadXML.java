package com.sunyard.ars.system.init;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunyard.ars.system.bean.sc.ShortcutKey;






/**
 * 
 * @ClassName: ReadXML 
 * @Description: TODO 读取配置文件信息
 * @Author：sui.xf
 * @Date： 2012-4-8 上午11:06:54 (创建文件的精确时间)
 */
public class ReadXML {
	protected final static Logger logger = LoggerFactory.getLogger(ReadXML.class);
	
	/**
	 * 
	 * @Title: readFunctions 
	 * @Description: 获取模块功能列表 
	 * @return
	 * @return Map
	 */
	public static Map readFunctions(String path, String model_name){
		
		Map functionsMap = new TreeMap();
		List list = new ArrayList();
		Map map = null;
		String functionId = null;
		String desc = null;
		String type = null;
		String purview= null;
		
		String filePath = (path + File.separator + "functions.xml").replaceAll("%20", " ");
		Document doc = createDocument(filePath);
		if(doc == null ) {
			return functionsMap;
		}
		Element root = doc.getRootElement();
		List fieldsList  = root.getChildren("functions");
		String model = null;
		for(int i =0;i<fieldsList.size();i++) {
			Element elements = (Element)fieldsList.get(i);
			model = elements.getAttributeValue("modelName");
			if(model.equals(model_name)){
				List fieldList  = elements.getChildren("function");
				for(int j=0;j<fieldList.size();j++){
					Element element = (Element)fieldList.get(j);
					ShortcutKey shortcutKey = new ShortcutKey();
					functionId = element.getAttributeValue("id");
					desc = element.getAttributeValue("desc");
					purview = element.getAttributeValue("purview");
					shortcutKey.setFunctionId(functionId);
					shortcutKey.setDesc(desc);
					shortcutKey.setPurview(purview);
					//TreeMap排序key是string的话是一位一位比较的，这里传整数
					functionsMap.put(Integer.parseInt(functionId), shortcutKey);
				}
			}
		}
		return functionsMap;
	
	}
	
	
	private static Document createDocument(String filepath) {
		SAXBuilder sb = new SAXBuilder(); // 建立构造器
		Document doc = null;
		FileInputStream fis=null;
		try {
			filepath = URLDecoder.decode(filepath,"GBK");
			fis= new FileInputStream(filepath);
			doc = sb.build(fis);
		} catch (Exception e) {
			logger.error("读取" + filepath + "  文件读取失败!"+e);
		} // 读入指定文件
		finally{

			 try {
				 if (fis != null) {
					 fis.close();
				 }
			    } catch (Exception e) {
			    	logger.error("",e);
			  }
			
		}
		return doc;
	}
}
