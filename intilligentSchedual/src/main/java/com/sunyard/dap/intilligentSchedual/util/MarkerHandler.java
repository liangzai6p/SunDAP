/*
package com.sunyard.dap.intilligentSchedual.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sunyard.aos.common.util.HttpUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

*/
/**
 * 根据模板文件生成excle和word工具
 * @author lcq
 *
 *//*



public class MarkerHandler {
	private Logger logger = Logger.getLogger(MarkerHandler.class);// 日志类
	private String defaultEncode = "UTF-8";
//	private String defaultDirectory="config";//测试环节
//	private String defaultDirectory="D:\\Workspaces\\运营管理平台-SunAOS\\src\\main\\resources\\";//开发环节
	private String defaultDirectory=HttpUtil.getAbsolutePath("template");
	private String templateName="";
	
	public  MarkerHandler(){
		cfg = new Configuration();  
	}
	
	// 负责管理FreeMarker模板文件的Configuration实例  
    private Configuration cfg;  
    

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}


	public void setDefaultEncode(String defaultEncode) {
		this.defaultEncode = defaultEncode;
	}



	public void setDefaultDirectory(String defaultDirectory) {
		this.defaultDirectory = defaultDirectory;
	}




	// 负责合并的方法,一般用于浏览器端下载使用 
    public void process(Map data,Writer writer){  
        try {
        	cfg.setDefaultEncoding(defaultEncode);
        	cfg.setDirectoryForTemplateLoading(new File(defaultDirectory));
        	
			Template t = cfg.getTemplate(templateName);  
			t.process(data,writer);
		} catch (Exception e) {
			logger.error("process负责合并的方法  异常",e);
		}
    }
    
    
    private Writer getWriter(String filePath){
    	Writer writer=null;
    	try {
			File file=new File(filePath);
			File parentFile=file.getParentFile();
			
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}else if(file.exists()){
				file.delete();
			}
			
			file.createNewFile();
			
			FileOutputStream fos=new FileOutputStream(file);
			
			writer = new OutputStreamWriter(fos);
		} catch (Exception e) {
			logger.error("getWriter方法 异常",e);
		}		
		return writer;
    }
    
    // 负责合并的方法 ，后台生成文件保存在服务端磁盘
    public void process(Map data,String filePath){  
        try {
        	cfg.setDefaultEncoding(defaultEncode);
        	cfg.setDirectoryForTemplateLoading(new File(defaultDirectory));
			
        	Writer writer=getWriter(filePath);
        	
			Template t = cfg.getTemplate(templateName);  
			
			t.process(data,writer);
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error("process负责合并的方法  异常",e);
		}
    }

}*/
