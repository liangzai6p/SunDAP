/*
package com.sunyard.dap.intilligentSchedual.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;

import java.util.Base64;
*/
/*import sun.misc.BASE64Encoder;*//*

import java.util.Base64.Encoder;
*/
/*import org.apache.commons.codec.binary.Base64;*//*

import freemarker.template.Configuration;
import freemarker.template.Template;
public class WordUtil {
	private static final Logger logger = Logger.getLogger(WordUtil.class);
	 */
/**
     * @param dataMap 
     *            word����Ҫչʾ�Ķ�̬���ݣ���map���������� 
     * @param templateName 
     *            wordģ�����ƣ����磺teample.ftl 
     * @param filePath 
     *            �ļ����ɵ�Ŀ��·�������磺D:/ 
     * @param fileName 
     *            ���ɵ��ļ����� 
     *//*

    @SuppressWarnings("unchecked")  
    public static void createWord(Map dataMap, String tempPath,String templateName,String filePath, String fileName) { 
    	logger.info("createWord");
        try {  
            // ��������ʵ��  
            Configuration configuration = new Configuration();  
  
            // ���ñ���  
            configuration.setDefaultEncoding("UTF-8");  
  
            // ftlģ���ļ�  
            configuration.setClassForTemplateLoading(WordUtil.class,tempPath);  
  
            // ��ȡģ��  
            Template template = configuration.getTemplate(templateName);  
  
            // ����ļ�  
            File outFile = new File(filePath + File.separator + fileName);  
  
            // ������Ŀ���ļ��в����ڣ��򴴽�  
            if (!outFile.getParentFile().exists()) {  
                outFile.getParentFile().mkdirs();  
            }  
  
            // ��ģ�������ģ�ͺϲ������ļ�  
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));  
  
            // �����ļ�  
            template.process(dataMap, out);  
  
            // �ر���  
            out.flush();  
            out.close();  
        } catch (Exception e) {  
            logger.error("createWord Exception",e);
        }  
        logger.info("createWord end");
    }  
    
    */
/**
     * ��ͼƬת��ΪBASE64Ϊ�ַ��� 
     * @param filename 
     * @return 
     * @throws IOException 
     *//*

    public static String getImageString(String filename) throws IOException {    
        InputStream in = null;    
        byte[] data = null;    
        try {    
            in = new FileInputStream(filename);    
            data = new byte[in.available()];    
            in.read(data);    
            in.close();    
        } catch (IOException e) {    
            throw e;    
        } finally {    
            if(in != null) in.close();    
        }    
        Encoder encoder = Base64.getEncoder();
        return data != null ? encoder.encodeToString(data): ""; 
   }

}
*/
