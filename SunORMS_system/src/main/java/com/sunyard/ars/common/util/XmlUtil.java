/*
 * @XmlUtil.java    2012-4-12 下午1:59:58
 *
 * Copyright (c) 2011 Sunyard System Engineering Co., Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Sunyard System Engineering Co., Ltd. ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it
* only in accordance with the terms of the license agreement you entered
* into with Sunyard.
 */

package com.sunyard.ars.common.util;

import com.sunyard.aos.common.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;


/**
 * @ClassName: XmlUtil
 * @Description: TODO xml工具类
 * @Author：陈慧民
 * @Date： 2012-4-12 下午1:59:58 (创建文件的精确时间)
 */
public class XmlUtil {


    /**
     * @Title: stringToDoc
     * @Description: String转换为XML对象
     * @param message
     * @return Document
     * @return Document
     * @throws DocumentException
     */
    public static Document stringConvertDoc(String message) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = null;
        doc = reader.read(new StringReader(message));
        return doc;
    }
    /**
     * @Title: docConvertString
     * @Description: XML转换为String
     * @param doc XML文件路径
     * @return
     * @return String
     */
    public static String docConvertString(Document doc) {
        return doc.asXML();
    }

    /**
     * @Title: docGenerate
     * @Description: 本地XML路径生成DOC
     * @param path
     * @return
     * @return Document
     * @throws DocumentException
     */
    public static Document docGenerate(File path) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = null;
        doc = reader.read(path);
        return doc;
    }

    
	/**
	 * 将组装好的XML信息写入文件
	 * 
	 * @param doc
	 *            组装好的Document对象
	 * @param filePath
	 *            存放路径
	 */
	public static void createXML(Document doc, String filePath) {
		XMLWriter writer=null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			writer = new XMLWriter(out);
			writer.write(doc);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}finally {
			if(out!=null){
				FileUtil.safeClose(out);
			}
			if(writer!=null){
				FileUtil.safeClose(writer);
			}

		}
	}
    
    
}
