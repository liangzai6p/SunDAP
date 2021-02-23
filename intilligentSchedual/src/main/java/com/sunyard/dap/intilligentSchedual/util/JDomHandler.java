/*
package com.sunyard.dap.intilligentSchedual.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.xml.sax.InputSource;

import com.sunyard.aos.monitor.util.CompareUtil;

public class JDomHandler {
	private Document doc;

	private static String ENCODE = "GBK";

	private static Format format = Format.getPrettyFormat();

	private static XMLOutputter outputter = new XMLOutputter(format);
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JDomHandler.class);

	private static final int PROPERTT_NOT_FOUND = -1;
	private static final Map primitiveDefaults = new HashMap();

	static {
		primitiveDefaults.put(Integer.TYPE, new Integer(0));
		primitiveDefaults.put(Short.TYPE, new Short((short) 0));
		primitiveDefaults.put(Byte.TYPE, new Byte((byte) 0));
		primitiveDefaults.put(Float.TYPE, new Float(0));
		primitiveDefaults.put(Double.TYPE, new Double(0));
		primitiveDefaults.put(Long.TYPE, new Long(0));
		primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
		primitiveDefaults.put(Character.TYPE, new Character('\u0000'));
	}

	public JDomHandler() {
		setEncoding(ENCODE);
	}
	public JDomHandler(String message) {
		setEncoding(ENCODE);
		try {
			this.loadXmlByString(message);
		} catch (Exception e) {
			logger.error("锟斤拷锟斤拷xml锟届常锟斤拷", e);
		}
	}
	public JDomHandler(Document doc) {
		setEncoding(ENCODE);
		this.doc = doc;
	}

	public void setEncoding(String encode) {
		format.setEncoding(encode);
	}

	*/
/***************************************************************************
	 * 锟斤拷锟斤拷锟斤拷锟斤拷诘锟斤拷值锟斤拷锟斤拷锟节碉拷锟斤拷锟斤拷媒诘悴伙拷锟轿拷锟�
	 * 
	 * 
	 * @param parentElementPath
	 *            锟斤拷锟节碉拷路锟斤拷
	 * @param elementName
	 *            锟斤拷拥慕诘锟斤拷路锟斤拷
	 * @param value
	 *            锟斤拷拥慕诘锟斤拷值
	 * @return 锟斤拷锟斤拷Document
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 * 
	 **************************************************************************//*

	public Document addNodeValue(String parentElementPath, String elementName,
			String value) {
		Element parentElement = null;
		if (parentElementPath == null || "".equals(parentElementPath)) {
			logger.error("锟斤拷咏诘锟街碉拷斐ｏ拷锟斤拷锟斤拷诘锟斤拷锟斤拷氩伙拷锟轿拷眨锟斤拷锟斤拷卟锟斤拷锟轿猲ull");
			return doc;
		}
		if (elementName == null || "".equals(elementName)) {
			logger.error("锟斤拷咏诘锟街碉拷斐ｏ拷锟斤拷锟斤拷玫慕诘锟斤拷锟斤拷氩伙拷锟轿拷眨锟斤拷锟斤拷卟锟斤拷锟轿猲ull");
			return doc;
		}
		if (value == null) {
			value = "";
		}
		try {
			parentElement = (Element) XPath.selectSingleNode(doc,
					parentElementPath);
		} catch (JDOMException e) {
			//  
			logger.error(e);
			return doc;
		}
		parentElement.addContent(new Element(elementName).setText(value));
		return doc;
	}

	*/
/***************************************************************************
	 * 锟斤拷锟斤拷锟斤拷锟斤拷诘锟斤拷值锟斤拷锟斤拷锟节碉拷锟斤拷锟斤拷媒诘悴伙拷锟轿拷锟�
	 * 
	 * 
	 * @param parentElementPath
	 *            锟斤拷锟节碉拷路锟斤拷
	 * @param elementName
	 *            锟斤拷拥慕诘锟斤拷路锟斤拷
	 * @param value
	 *            锟斤拷拥慕诘锟斤拷值
	 * @return 锟斤拷锟斤拷Document
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 * 
	 **************************************************************************//*

	public Document addNodeValue(String path, String value) {
		String parentElementPath = "";
		String elementName = "";
		int lastIndex = path.lastIndexOf("/");
		parentElementPath = path.substring(0, lastIndex);
		elementName = path.substring(lastIndex + 1, path.length());
		Element parentElement = null;
		if (parentElementPath == null || "".equals(parentElementPath)) {
			logger.error("锟斤拷咏诘锟街碉拷斐ｏ拷锟斤拷锟斤拷诘锟斤拷锟斤拷氩伙拷锟轿拷眨锟斤拷锟斤拷卟锟斤拷锟轿猲ull");
			return doc;
		}
		if (elementName == null || "".equals(elementName)) {
			logger.error("锟斤拷咏诘锟街碉拷斐ｏ拷锟斤拷锟斤拷玫慕诘锟斤拷锟斤拷氩伙拷锟轿拷眨锟斤拷锟斤拷卟锟斤拷锟轿猲ull");
			return doc;
		}
		if (value == null) {
			value = "";
		}
		try {
			parentElement = (Element) XPath.selectSingleNode(doc,
					parentElementPath);
		} catch (JDOMException e) {
			//  
			logger.error(e);
			return doc;
		}
		parentElement.addContent(new Element(elementName).setText(value));
		return doc;
	}

	*/
/***************************************************************************
	 * 锟斤拷锟街革拷锟斤拷锟斤拷慕诘锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 * 
	 * @param path
	 *            锟斤拷锟斤拷锟街革拷锟斤拷锟斤拷慕诘锟斤拷路锟斤拷
	 * @param attribute
	 *            指锟斤拷锟侥憋拷锟侥节碉拷锟斤拷锟斤拷锟�
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 * 
	 **************************************************************************//*

	public String getNodeAttribute(String path, String attribute) {
		Element visitElment = null;
		String attribute_Str = "";
		try {
			visitElment = (Element) XPath.selectSingleNode(doc, path);
		} catch (JDOMException e) {
			//  
			logger.error(e);
			return null;
		}
		if (visitElment != null) {
			Attribute attr = visitElment.getAttribute(attribute);
			if (attr == null) {
				logger.error("锟斤拷取xml锟节碉拷锟斤拷锟斤拷锟届常锟斤拷锟斤拷前锟斤拷锟侥节点不锟斤拷锟节碉拷前锟斤拷锟斤拷");
				return null;
			}
			attribute_Str = attr.getValue();
		} else {
			logger.error("锟斤拷取xml锟节碉拷锟斤拷锟斤拷锟届常锟斤拷锟斤拷锟斤拷锟节碉拷前锟斤拷锟侥节碉拷");
			return null;
		}
		return attribute_Str;
	}

	*/
/***************************************************************************
	 * 锟斤拷锟斤拷指锟斤拷锟斤拷锟侥节碉拷锟斤拷锟斤拷锟斤拷锟皆碉拷值锟斤拷
	 * 
	 * @param path
	 *            锟斤拷锟斤拷锟街革拷锟斤拷锟斤拷慕诘锟斤拷路锟斤拷
	 * @param attribute
	 *            指锟斤拷锟侥憋拷锟侥节碉拷锟斤拷锟斤拷锟�
	 * @param value
	 *            锟借定锟斤拷锟斤拷锟斤拷缘锟街�
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 **************************************************************************//*


	public void addNodeAttribute(String path, String attributeName, String value) {
		Element visitElment = null;
		try {
			visitElment = (Element) XPath.selectSingleNode(doc, path);
		} catch (JDOMException e) {
			//  
			logger.error(e);
			return;
		}
		if (visitElment != null) {
			visitElment.setAttribute(attributeName, value);
		} else {
			logger.error("锟斤拷取xml锟节碉拷锟斤拷锟斤拷锟届常锟斤拷锟斤拷锟斤拷锟节碉拷前锟斤拷锟侥节碉拷");
		}
	}

	public void addNodeAttribute(String path, Map<String, String> KV) {
		Element visitElment = null;
		try {
			visitElment = (Element) XPath.selectSingleNode(doc, path);
		} catch (JDOMException e) {
			logger.error(e);
			return;
		}
		String attributeName = "";
		String value = "";
		if (visitElment != null) {
			Set s = KV.keySet();
			Iterator itr = s.iterator();
			while (itr.hasNext()) {
				attributeName = (String) itr.next();
				value = KV.get(attributeName);
				visitElment.setAttribute(attributeName, value);
			}
		} else {
			logger.debug("锟斤拷取xml锟节碉拷锟斤拷锟斤拷锟届常锟斤拷锟斤拷锟斤拷锟节碉拷前锟斤拷锟侥节碉拷");
		}
	}

	*/
/**
	 * 锟矫碉拷锟斤拷锟侥节碉拷锟街�
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*

	public String getNodeValue(String path) {
		Element visitElement = null;
		String value = "";
		try {
			visitElement = (Element) XPath.selectSingleNode(doc, path);
		} catch (JDOMException e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		}
		if (visitElement != null) {
			value = visitElement.getValue();
		} else {
			logger.error("锟斤拷取锟斤拷锟侥节碉拷锟街碉拷锟斤拷?锟斤拷前锟节点不锟斤拷锟斤拷 ,锟节碉拷路锟斤拷为[" + path + "]");
			return "";
		}
		return value;
	}

	*/
/**
	 * 锟矫碉拷锟斤拷锟侥节碉拷锟街�
	 * 
	 * @param path
	 * @return
	 * @throws JDomHandlerException 
	 * @throws JDOMException 
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*

	@SuppressWarnings("unchecked")
	public String getNodeValueWithIndex(String path,int index) throws JDOMException, JDomHandlerException  {
		String value = "";
		this.checkPathNodes(path);
		List<Element> valueList = this.getNodes(path);
		if(valueList!=null){
			try{
				value = valueList.get(index).getValue();
			}catch (Exception e) {
				// TODO: handle exception
				logger.debug("index>size");
			}
		}
		return value;
	}
	
	*/
/***************************************************************************
	 * 锟斤拷锟街革拷锟斤拷锟斤拷慕诘锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 * 
	 * @param path
	 *            锟斤拷锟斤拷锟街革拷锟斤拷锟斤拷慕诘锟斤拷路锟斤拷
	 * @param attribute
	 *            指锟斤拷锟侥憋拷锟侥节碉拷锟斤拷锟斤拷锟�
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 * 
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷 锟睫革拷锟叫�
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷 时锟戒： 锟斤拷锟竭ｏ拷 锟斤拷锟斤拷锟斤拷
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷 2009-01-05
	 * 姚锟斤拷 锟斤拷锟斤拷舜锟斤拷锟斤拷锟斤拷锟狡ｏ拷锟斤拷锟捷达拷锟斤拷锟�
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 **************************************************************************//*

	public String getNodeAttributeWithIndex(String path,int index, String attribute)
			throws JDOMException, JDomHandlerException {
		String value = "";
		this.checkPathNodes(path);
		List<Element> valueList = this.getNodes(path);
		if(valueList!=null){
			try{
				Attribute attr = valueList.get(index).getAttribute(attribute);
				if (attr == null) {
					throw new JDomHandlerException("锟斤拷取xml锟节碉拷锟斤拷锟斤拷锟届常锟斤拷锟斤拷前锟斤拷锟侥节点不锟斤拷锟节碉拷前锟斤拷锟斤拷");
				}
				value=attr.getValue();
			}catch (Exception e) {
				logger.debug("index>size");
			}
		}
		return value;
	}
	
	*/
/**
	 * 锟矫碉拷锟斤拷锟侥节碉拷锟街�
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*

	public List getSameNodeValues(String path) {
		List<Element> list = null;
		List retList = new ArrayList();
		try {
			list = XPath.selectNodes(doc, path);
			for (Element e : list) {
				retList.add(e.getText());
			}
		} catch (JDOMException e) {
			logger.error(e);
		}
		return retList;
	}

	*/
/**
	 * 锟矫碉拷锟斤拷锟侥节碉拷锟街�
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*

	public List<String[]> getNodeValues(String path) throws JDOMException {
		List<Element> list = null;
		List retList = new ArrayList();
		list = XPath.selectNodes(doc, path);
		int len = list.size();
		Element e = null;
		List<Element> cacheList = null;
		int cacheLen = 0;
		for (int i = 0; i < len; i++) {
			e = list.get(i);
			cacheList = e.getChildren();
			cacheLen = cacheList.size();
			String[] al = new String[cacheLen];
			for (int l = 0; l < cacheLen; l++) {
				al[l] = cacheList.get(l).getText();
			}
			retList.add(al);
		}
		return retList;
	}
	
	*/
/**
	 * 锟斤拷锟皆伙拷取路锟斤拷锟斤拷锟斤拷锟叫节碉拷
	 * @param path
	 * @return
	 * @throws JDOMException
	 *//*

	public List<Element> getNodeValuesElList(String path) throws JDOMException {
		List list = XPath.selectNodes(doc, path);
		return list;
	}

	
	public Document addElements(String parentPath, List<Element> elements) throws JDOMException {
		Element p = (Element)(XPath.selectSingleNode(doc, parentPath));

		for (Element element : elements) {
			p.addContent((Element)element.clone());
		}

		return doc;
	}
	*/
/**
	 * 锟矫碉拷锟斤拷锟侥节碉拷锟街�
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*


	public Element getNode(String path) throws JDOMException {
		Element visitElement = null;
		visitElement = (Element) XPath.selectSingleNode(doc, path);
		return visitElement;
	}

	*/
/**
	 * 锟矫碉拷锟斤拷锟侥节碉拷
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*


	public Object getNode(String path, Class clz)throws JDOMException {
		Element visitElement = null;
			visitElement = (Element) XPath.selectSingleNode(doc, path);
		return elementToBean(visitElement, clz);
	}

	*/
/**
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 *//*


	public List getNodes(String path) throws JDOMException{
		List list = null;
			list = XPath.selectNodes(doc, path);
		return list;
	}

	*/
/**
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 *//*


	public List getNodes(String path, Class claz)throws JDOMException {
		List<Element> list = null;
		List retList = new ArrayList();
			list = XPath.selectNodes(doc, path);
			for (Element e : list) {
				Object o = elementToBean(e, claz);
				retList.add(o);
			}
		return retList;
	}

	*/
/**
	 * 锟叫断伙拷取指锟斤拷Document锟斤拷路锟斤拷锟斤拷锟角凤拷锟斤拷指锟斤拷锟侥节碉拷锟斤拷锟�
	 *  
	 * @param path
	 * @return
	 *//*


	public boolean hasNode(String path){
		Element element = null;
		try{
			element = (Element) XPath.selectSingleNode(doc, path);
		}catch(JDOMException e){
			
		}
		if (element == null) {
			return false;
		} else {
			return true;
		}
	}

	*/
/**
	 * 锟斤拷锟斤拷指锟斤拷锟节碉拷锟街�
	 * 
	 * @param message
	 *            要锟斤拷锟斤拷锟斤拷Document锟斤拷锟斤拷
	 * @param path
	 *            路锟斤拷
	 * @param nodeName
	 *            锟节碉拷锟斤拷锟�
	 * @return 锟斤拷锟斤拷指锟斤拷锟节碉拷值锟斤拷珊锟斤拷Doucument锟斤拷锟斤拷
	 * @throws JDOMException 
	 * @throws JDomHandlerException
	 * @throws JDOMException
	 *//*


	public Document setNodeValue(String path, String value) throws JDOMException {
		if (hasNode(path)) {
			modifyNodeValue(path, value);
		} else {
			addNodeValue(path, value);
		}
		return doc;
	}

	public Document loadXmlByPath(String path) throws JDOMException, IOException{
			SAXBuilder builder = new SAXBuilder(false);
			doc = builder.build(new File(path));
		return doc;
	}
	
	public Document loadXmlByFile(File file) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder(false);
		doc = builder.build(file);
		return doc;
	}
	
	*/
/**
	 * 锟斤拷XML锟斤拷锟斤拷锟斤拷锟侥硷拷
	 * @param file
	 * @param doc
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 *//*

	public boolean saveXmltoFile(File file,Document doc) throws JDOMException, IOException{
		//XMLOutputter outputter = new XMLOutputter();
        FileOutputStream fileoutput = new FileOutputStream(file);
        outputter.output(doc, fileoutput);
        return true;
	}

	public Document loadXmlByStream(InputStream input) throws IOException {
		try {
			SAXBuilder builder = new SAXBuilder(false);
			doc = builder.build(input);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
		return doc;
	}

	public Document modifyNodeValue(String path, String value) throws JDOMException{
			((Element) XPath.selectSingleNode(doc, path)).setText(value);
		return doc;
	}

	public Document removeSubNodes(String path, Document doc) throws JDOMException {
		List l = this.getNodeNames(path);
		String removePath = null;
		int i = 0;
		while (i < l.size()) {
			this.doc = doc;
			Element e = (Element) l.get(i);
			removePath = path + "/" + e.getName();
			doc = this.removeNode(removePath);
		}
		return doc;
	}

	*/
/**
	 * 
	 * @param removeNodeName
	 *            删锟斤拷锟斤拷锟铰凤拷锟�
	 * @return 删锟斤拷一锟斤拷锟斤拷锟斤拷锟絰ml锟斤拷锟斤拷
	 * @throws JDOMException
	 *//*


	public Document removeNode(String removeNodeName)throws JDOMException {
		Element visitElement = null;
			visitElement = (Element) XPath
					.selectSingleNode(doc, removeNodeName);
		if (visitElement != null)
			visitElement.getParent().removeContent(visitElement);
		return doc;
	}

	*/
/**
	 * 锟斤拷媒锟斤拷锟斤拷锟斤拷锟叫的节碉拷
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 *//*


	public List getNodeNames(String path) {
		Element visitElement = null;
		List l = new ArrayList();
		try {
			visitElement = (Element) XPath.selectSingleNode(doc, path);
		} catch (JDOMException e) {
			//  
			logger.error(e);
		}
		if (visitElement != null) {
			l = visitElement.getChildren();
		}
		return l;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public Document loadXmlByString(String xml)throws JDOMException ,IOException {
		StringReader read = new StringReader(xml);
		InputSource source = new InputSource(read);
		SAXBuilder sb = new SAXBuilder();
			doc = sb.build(source);
		return doc;
	}

	public String toString() {
		String xml = "";
		if (doc != null) {
			*/
/*
			 * Format f = Format.getRawFormat(); f.setExpandEmptyElements(true);
			 * outputter.setFormat(f);
			 *//*

			xml = outputter.outputString(doc);
		}
		return xml;
	}
	*/
/***
	 * airstar 锟斤拷锟斤拷 
	 * 2012-03-14 airstar update
	 * 锟斤拷锟铰凤拷锟斤拷锟揭伙拷锟紼lement转锟斤拷为一锟斤拷锟街凤拷
	 * @param element
	 * @return
	 *//*

	public String toString(String path)throws JDOMException{
//		StringBuilder ss = new StringBuilder();
		Element element=getNode(path);
		if (element==null) {
        	throw new JDOMException("锟节点："+path+"锟斤拷锟斤拷锟斤拷.");
		}
		String xml = outputter.outputString(element);
        return xml.replaceAll("(\r)|(\n)", "");
//		getElements(element, ss);
//		return ss.toString();
	}

	*/
/**
	 * 锟斤拷锟斤拷锟斤拷转锟斤拷为锟斤拷应锟侥憋拷锟斤拷锟绞�
	 * @param ENCODE
	 * @return
	 *//*

	public String toStringEncode(String ENCODE) {
		Format forMat = Format.getPrettyFormat();
		forMat.setEncoding(ENCODE);
		forMat.setTextMode(Format.TextMode.TRIM_FULL_WHITE);
		XMLOutputter out = new XMLOutputter(forMat);
		String xml = out.outputString(doc);
		return xml;
	}
	
	*/
/***
	 * 锟矫凤拷锟斤拷锟结导锟铰憋拷锟斤拷锟叫碉拷转锟斤拷锟街凤拷转锟斤拷,锟窖诧拷使锟斤拷,锟斤拷锟铰版本锟叫匡拷去锟斤拷  airstar 2012-3-14
	 * @param element
	 * @param ss
	 *//*

	private void getElements(Element element, StringBuilder ss) {
		String name = element.getName();
		String text = element.getText();
		List<Attribute> attributes =element.getAttributes();
		ss.append("<" + name );
		for(Attribute attr:attributes){
			String attrName=attr.getName();
			String attrValue=attr.getValue();
			ss.append(" "+attrName+"="+"\""+attrValue+"\"");
		}
		ss.append(">");
		List<Element> elements = element.getChildren();
		for (Element e : elements) {
			getElements(e, ss);
		}
		ss.append(element.getText().replaceAll("(\t)|(\n)", ""));
		ss.append("</" + name + ">");
	}
	public Document getDoc() {
		return doc;
	}
	
	*/
/**
	 * 锟斤拷锟斤拷陆诘锟�每锟斤拷锟斤拷锟斤拷一锟斤拷锟节碉拷
	 * 
	 * @param parentElementPath
	 * @param elementName
	 * @param value
	 * @return
	 * @throws JDOMException 
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*

	public Document addRepeatNode(String parentElementPath, String elementName) throws JDOMException {
		Element parentElement = null;
		if (parentElementPath == null || "".equals(parentElementPath)) {
			logger.error("锟斤拷咏诘锟街碉拷斐ｏ拷锟斤拷锟斤拷诘锟斤拷锟斤拷氩伙拷锟轿拷眨锟斤拷锟斤拷卟锟斤拷锟轿猲ull");
			return doc;
		}
		if (elementName == null || "".equals(elementName)) {
			logger.error("锟斤拷咏诘锟街碉拷斐ｏ拷锟斤拷锟斤拷玫慕诘锟斤拷锟斤拷氩伙拷锟轿拷眨锟斤拷锟斤拷卟锟斤拷锟轿猲ull");
			return doc;
		}
		
		try {
			parentElement = (Element) XPath.selectSingleNode(doc,
					parentElementPath);
		} catch (JDOMException e) {
			logger.error(e);
		}
		if (parentElement != null) {
			parentElement.addContent(new Element(elementName));
		} else {
			logger.error("缺锟劫革拷锟节点！");
		}
		return doc;
	}
	
	*/
/**
	 * 锟斤拷锟斤拷陆诘锟�锟斤拷锟斤拷路锟斤拷锟斤拷锟斤拷锟斤拷,锟斤拷锟皆讹拷锟斤拷一锟斤拷
	 * @param parentElementPath
	 * @param element
	 * @return
	 * @throws JDOMException
	 *//*

	public Document addNode(String parentElementPath, Element element) throws JDOMException {
		Element parentElement = null;
		if (parentElementPath == null || "".equals(parentElementPath)) {
			logger.error("锟斤拷咏诘锟街碉拷斐ｏ拷锟斤拷锟斤拷诘锟斤拷锟斤拷氩伙拷锟轿拷眨锟斤拷锟斤拷卟锟斤拷锟轿猲ull");
			return doc;
		}
		// 锟叫断革拷锟节碉拷锟角凤拷锟斤拷锟�锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		if (!hasNode(parentElementPath)) {
			checkPathNodes(parentElementPath);
		}
		parentElement = (Element) XPath.selectSingleNode(doc,parentElementPath);
		parentElement.addContent(element);
		return doc;
	}

	*/
/**
	 * 锟斤拷锟斤拷陆诘锟�锟斤拷锟节碉拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 * 
	 * @param parentElementPath
	 * @param elementName
	 * @param value
	 * @return
	 * @throws JDOMException 
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*

	public Document addNode(String parentElementPath, String elementName) throws JDOMException {
		Element parentElement = null;
		if (parentElementPath == null || "".equals(parentElementPath)) {
			logger.error("锟斤拷咏诘锟街碉拷斐ｏ拷锟斤拷锟斤拷诘锟斤拷锟斤拷氩伙拷锟轿拷眨锟斤拷锟斤拷卟锟斤拷锟轿猲ull");
			return doc;
		}
		if (elementName == null || "".equals(elementName)) {
			logger.error("锟斤拷咏诘锟街碉拷斐ｏ拷锟斤拷锟斤拷玫慕诘锟斤拷锟斤拷氩伙拷锟轿拷眨锟斤拷锟斤拷卟锟斤拷锟轿猲ull");
			return doc;
		}
		// 锟叫断节碉拷锟角凤拷锟斤拷锟�锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷樱锟斤拷锟斤拷锟斤拷锟斤拷锟铰节碉拷
		if (!hasNode(parentElementPath + "/" + elementName)) {
			try {
				parentElement = (Element) XPath.selectSingleNode(doc,
						parentElementPath);
			} catch (JDOMException e) {
				logger.error(e);
			}
			if (parentElement != null) {
				parentElement.addContent(new Element(elementName));
			} else {
				logger.error("缺锟劫革拷锟节点！");
			}
		}
		return doc;
	}

	*/
/**
	 * 锟斤拷锟斤拷指锟斤拷锟节碉拷锟街�锟饺硷拷锟斤拷锟斤拷锟铰凤拷锟斤拷慕诘锟斤拷欠锟斤拷锟节ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷咏诘锟�
	 * 
	 * @param message
	 *            要锟斤拷锟斤拷锟斤拷Document锟斤拷锟斤拷
	 * @param path
	 *            路锟斤拷
	 * @param nodeName
	 *            锟节碉拷锟斤拷锟�
	 * @return 锟斤拷锟斤拷指锟斤拷锟节碉拷值锟斤拷珊锟斤拷Doucument锟斤拷锟斤拷
	 * @throws JDOMException 
	 * @throws JDomHandlerException
	 * @throws JDOMException
	 *//*

	public Document setNodeValueWithCheckAllPathNodes(String path, String value) throws JDOMException {
		checkPathNodes(path);
		setNodeValue(path, value);
		return doc;
	}

	*/
/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷诘锟�
	 * 
	 * @param path
	 * @param nodeMap
	 * @return
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*


	public Document addBatchNodes(String path, Map<String, String> nodeMap)throws JDOMException {
		int point = path.lastIndexOf("/");
		String praElementPath = path.substring(0, point);
		String parNodeName = path.substring(point + 1, path.length());

		this.checkPathNodes(praElementPath);

		Element praElement = null;
			praElement = (Element) XPath.selectSingleNode(doc, praElementPath);
		Element element = new Element(parNodeName);
		Element childElenemt;

		List<String> keyList = new ArrayList<String>(nodeMap.keySet());
		String name;
		String value;
		for (String key : keyList) {
			name = key;
			value = nodeMap.get(key);

			childElenemt = new Element(name).setText(value);
			element.addContent(childElenemt);
		}

		praElement.addContent(element);

		return doc;
	}

	public Document addNodeAndAttr(String path, String nodeName, Map attrMap)throws JDOMException {

		this.checkPathNodes(path);

		Element praElement = null;
			praElement = (Element) XPath.selectSingleNode(doc, path);
		Element element = new Element(nodeName);

		String attributeName = "";
		String value = "";

		Set s = attrMap.keySet();
		Iterator itr = s.iterator();
		while (itr.hasNext()) {
			attributeName = (String) itr.next();
			value = (String) attrMap.get(attributeName);
			element.setAttribute(attributeName, value);
		}

		praElement.addContent(element);

		return doc;
	}

	*/
/**
	 * 路锟斤拷锟斤拷锟�
	 * 
	 * @param path
	 * @throws JDOMException 
	 * @throws JDOMException
	 * @throws JDomHandlerException
	 *//*


	public void checkPathNodes(String path) throws JDOMException {
		String[] pathNames = path.split("/");
		String headPath = "";
		String tailName = "";

		for (int i = 0; i < pathNames.length - 1; i++) {
			if (i == 0) {
				headPath = pathNames[i];
			} else {
				headPath = headPath + "/" + pathNames[i];
			}
			tailName = pathNames[i + 1];
			if (!hasNode(headPath + "/" + tailName)) {
				addNode(headPath, tailName);
			}
		}
	}

	public Object elementToBean(Element e, Class claz) {
		PropertyDescriptor[] props = this.propertyDescriptors(claz);
		int[] elementToProperties = mappingElementToProperties(e, props);
		return this.createBean(e, claz, props, elementToProperties);
	}

	private Object processLabel(List<Element> list, int index, Class propType) {
		if (!propType.isPrimitive() && list.get(index).getText() == null) {
			return null;
		}
		if (propType.equals(String.class)) {
			return list.get(index).getText();
		} else if (propType.equals(Integer.TYPE)
				|| propType.equals(Integer.class)) {
			return new Integer(list.get(index).getText());
		} else if (propType.equals(Boolean.TYPE)
				|| propType.equals(Boolean.class)) {
			return new Boolean(list.get(index).getText());

		} else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
			return new Long(list.get(index).getText());

		} else if (propType.equals(Double.TYPE)
				|| propType.equals(Double.class)) {
			return new Double(list.get(index).getText());

		} else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
			return new Float(list.get(index).getText());

		} else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
			return new Short(list.get(index).getText());

		} else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
			return new Byte(list.get(index).getText());

		} else if (propType.equals(Date.class)) {
			return new Date(list.get(index).getText());

		} else if (propType.equals(ArrayList.class)) {
			List<Element> lstcache = list.get(index).getChildren();
			List lst = elementListToObjectList(lstcache);
			return lst;

		} else if (propType.equals(Vector.class)) {
			List lst = new Vector();
			List<Element> lstcache = list.get(index).getChildren();
			for (Element e : lstcache) {
				lst.add(e.getText());

			}
			return lst;

		} else if (CompareUtil.isInterface(propType, "java.util.List")) {
			List<Element> lstcache = list.get(index).getChildren();
			List lst = elementListToObjectList(lstcache);
			return lst;
		} else if (CompareUtil.isInterface(propType, "java.util.Collection")) {
			List<Element> lstcache = list.get(index).getChildren();
			List lst = elementListToObjectList(lstcache);
			return lst;
		} else {
			return list.get(index).getText();
		}
	}

	private List elementListToObjectList(List<Element> elementList) {
		List lst = new ArrayList();
		for (Element e : elementList) {
			lst.add(e.getText());
		}
		return lst;
	}

	*/
/**
	 * 锟斤拷锟斤拷锟斤拷应bean
	 * 
	 * @param e
	 * @param claz
	 * @param props
	 * @param elementToProperties
	 * @return
	 *//*

	private Object createBean(Element e, Class claz,
			PropertyDescriptor[] props, int[] elementToProperties) {
		Object bean = null;
		try {
			bean = claz.newInstance();
			List<Element> list = e.getChildren();
			for (int i = 0; i < elementToProperties.length; i++) {
				if (CompareUtil.equ(elementToProperties[i], PROPERTT_NOT_FOUND)) {
					continue;
				}
				PropertyDescriptor prop = props[elementToProperties[i]];
				Class propClaz = prop.getPropertyType();
				Object value = processLabel(list, i, propClaz);
				if (propClaz != null && value == null && propClaz.isPrimitive()) {
					value = primitiveDefaults.get(propClaz);
				}
				this.callSetter(bean, prop, value);
			}
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		return bean;
	}

	*/
/**
	 * 映锟斤拷element锟斤拷锟斤拷锟斤拷之锟斤拷墓锟较�
	 * 
	 * @param e
	 * @param props
	 * @return
	 *//*

	private int[] mappingElementToProperties(Element e,
			PropertyDescriptor[] props) {
		List<Element> cacheList = e.getChildren();
		int count = cacheList.size();
		int[] elementToProperties = new int[count + 1];
		Arrays.fill(elementToProperties, PROPERTT_NOT_FOUND);
		for (int col = 0; col < count; col++) {
			String labelName = cacheList.get(col).getName();
			for (int i = 0; i < props.length; i++) {
				if (CompareUtil.equIgnoreCase(labelName, props[i].getName())) {
					elementToProperties[col] = i;
					break;
				}
			}
		}
		return elementToProperties;
	}

	*/
/**
	 * 锟斤拷锟矫革拷锟斤拷锟斤拷锟皆碉拷setter锟斤拷锟斤拷
	 * 
	 * @param target
	 * @param prop
	 * @param value
	 *//*

	private void callSetter(Object target, PropertyDescriptor prop, Object value) {
		Method setter = prop.getWriteMethod();
		if (setter == null) {
			return;
		}
		Class[] params = setter.getParameterTypes();
		try {
			if (isCompatibleType(value, params[0])) {
				setter.invoke(target, new Object[] { value });
			} else {
				logger.error("没锟斤拷锟揭碉拷锟斤拷锟绞碉拷锟斤拷锟斤拷匹锟斤拷");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();

		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	*/
/**
	 * 锟斤拷锟紺LASS 锟斤拷取bean锟侥革拷锟斤拷锟斤拷息
	 * 
	 * @param c
	 * @return
	 *//*

	private PropertyDescriptor[] propertyDescriptors(Class c) {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);
		} catch (IntrospectionException e) {
			System.err.println("error");
		}
		return beanInfo.getPropertyDescriptors();
	}

	public static boolean isCompatibleType(Object value, Class type) {
		// Do object check first, then primitives
		if (value == null || type.isInstance(value)) {
			return true;

		} else if (type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
			return true;

		} else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
			return true;

		} else if (type.equals(Double.TYPE) && Double.class.isInstance(value)) {
			return true;

		} else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
			return true;

		} else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
			return true;

		} else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
			return true;

		} else if (type.equals(Character.TYPE)
				&& Character.class.isInstance(value)) {
			return true;

		} else if (type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
			return true;

		} else if (CompareUtil.isInterface(type.getClass(), "java.util.List")) {
			return true;
		} else if (CompareUtil.isInterface(type.getClass(),
				"java.util.Collection")) {
			return true;
		} else {
			return false;
		}

	}

	public void writeFile(String path) throws IOException,
			FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		if (doc != null) {
			FileOutputStream output = new FileOutputStream(path);
			outputter.output(doc, output);
			output.close();
		}
	}

	public List getNodesByAttributeValue(String path, String attribute,
			String value) {
		// TODO Auto-generated method stub
		String exp = new StringBuilder(path).append("[@").append(attribute)
				.append("='").append(value).append("']").toString();
		// System.out.println(exp);

		List lst = null;
		*/
/*
		 * exp="//default:beans"; XPath xPath = XPath.newInstance(exp);
		 * xPath.addNamespace
		 * ("default","http://www.springframework.org/schema/beans");
		 *//*

		// xPath.addNamespace(doc.getRootElement().getNamespace());
		*/
/* lst=xPath.selectNodes(doc); *//*

		try {
			lst = XPath.selectNodes(doc, exp);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lst;
	}

	public List getNodesByAttributeValueHasNS(String path, String attribute,
			String value, String prefix, String nameSpace) {
		// TODO Auto-generated method stub
		String[] paths = path.split("/");
		StringBuilder sb = new StringBuilder();
		for (String s : paths) {
			sb.append("/").append(prefix).append(":").append(s);
		}
		String exp = new StringBuilder(sb.toString()).append("[@").append(
				attribute).append("='").append(value).append("']").toString();
		System.out.println(exp);
		List lst = null;
		try {
			XPath xPath = XPath.newInstance(exp);
			xPath.addNamespace(prefix, nameSpace);
			lst = xPath.selectNodes(doc);

		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return lst;
	}
	
	public Document setNodeAttribute(String path, String attributeName, String attributeValue) {
		try {
			Element visitElment = (Element) XPath.selectSingleNode(doc, path);
			visitElment.setAttribute(attributeName, attributeValue);
		} catch (Exception e) {
			logger.error("setNodeAttribute锟斤拷锟斤拷锟届常锟斤拷", e);
		}
		return doc;
	}

	public Element getSingleNodeByAttributeValueHasNS(String path,
			String attribute, String value, String prefix, String nameSpace) {
		// TODO Auto-generated method stub
		String[] paths = path.split("/");
		StringBuilder sb = new StringBuilder();
		for (String s : paths) {
			sb.append("/").append(prefix).append(":").append(s);
		}
		String exp = new StringBuilder(sb.toString()).append("[@").append(
				attribute).append("='").append(value).append("']").toString();
		System.out.println(exp);
		Element ele = null;
		try {
			XPath xPath = XPath.newInstance(exp);
			xPath.addNamespace(prefix, nameSpace);
			ele = (Element) xPath.selectSingleNode(doc);

		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return ele;
	}

	public Document createNewDoc(String name) {
		doc = new Document();
		Element element = new Element(name);
		doc.addContent(element);
		return doc;
	}
	
	*/
/**
	 * 锟斤拷媒锟斤拷锟斤拷锟斤拷锟叫的节碉拷
	 * 
	 * @param path
	 * @return
	 * @throws JDOMException
	 *//*

	public List getSingleNodeChildren(String path) throws JDOMException {
		Element visitElement = null;
		List l =null;
		visitElement = (Element) XPath.selectSingleNode(doc, path);
		if (visitElement != null) {
			l = visitElement.getChildren();
		}
		return l;
	}
}
 */
