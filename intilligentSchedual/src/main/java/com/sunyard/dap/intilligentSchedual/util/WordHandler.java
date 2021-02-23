/*
package com.sunyard.dap.intilligentSchedual.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class WordHandler {
	private static final Logger logger = Logger.getLogger(WordHandler.class);
	public static final String ENCODE_GB2312 = "UTF-8";
	
	private Configuration configuration = null;
	
	private Template template = null;
	
	private Map data=null;
	
	@SuppressWarnings("deprecation")
	public
	WordHandler(){
		configuration = new Configuration();
		configuration.setDefaultEncoding(ENCODE_GB2312);  
	}
	
	public void loadTemp(String path,String name){
		logger.info("loadTemp:"+path);
		try {
//			configuration.setClassForTemplateLoading(WordHandler.class,path);  
			configuration.setDirectoryForTemplateLoading(new File(path));
			template = configuration.getTemplate(name);
		}catch (Exception e) {
			logger.error("loadTemp�쳣",e);
		}
	}
	
	public void setData(Map data) {
		this.data = data;
	}

	public boolean exportWord(String file){
		logger.info("exportWord:"+file);
		boolean success=false;
		try {
            File outFile = new File(file);  
  
            if (!outFile.getParentFile().exists()) {  
                outFile.getParentFile().mkdirs();  
            }  
  
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), ENCODE_GB2312));  
  
            template.process(data, out);  
            
            out.flush();  
            success=true;
		}catch (Exception e) {
			success=false;
			logger.error("exportWord�쳣",e);
		}
		return success;
	}
	
	 public void writeProcess(Writer writer){  
	        try {
				template.process(data,writer);
			} catch (Exception e) {
				logger.error("process����ϲ��ķ���  �쳣",e);
			}
	    }
	
}
*/
