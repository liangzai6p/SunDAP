/*
package com.sunyard.dap.intilligentSchedual.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class excelTest {
	
	
	public static void main(String[] args) {
		try {
			Map map=new HashMap();
			int countAmount=0;
			List data=new ArrayList();
			
			for (int i = 0; i <40000; i++) {
				Map tasks=new HashMap();
				tasks.put("rn", i+"");
				tasks.put("task_id", "201810021121212012"+i);
				tasks.put("business_name", "2001");
				tasks.put("trans_name", "2019-10-02");
				tasks.put("sys_name", "102001");
				tasks.put("dep_name", 1200+"");
				tasks.put("oper_name", "2019-10-02");
				tasks.put("create_time", "102001");
				tasks.put("state", 1200+"");
				tasks.put("suspend_reason", 1200+"");
				
				countAmount+=1200;
				data.add(tasks);
			}
			
			map.put("year", "2018");
			map.put("list", data);
			map.put("countAmount", countAmount);
			
			MarkerHandler markerHandler = new MarkerHandler();
			markerHandler.setDefaultDirectory("D:\\sunyardAosWorkspace\\运营管理平台-SunAOS\\src\\main\\resources\\");
			markerHandler.setTemplateName("centerCountOperDetail.ftl");
			markerHandler.process(map, "d:\\exc.xls");
			//markerHandler.process(data,response.getWriter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	*/
/*
	public void downloadExcel( HttpServletRequest request,HttpServletResponse response ){
        try {
            // ��װ�������
            Map data=new HashMap();
            data.put("reviews",null); //��һ��worksheet����ݶ���
            data.put("decisions",null); //�ڶ���worksheet����ݶ���
            // ����
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download;");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String("�������ļ�����.xls".getBytes("gb2312"), "ISO8859-1"));
            freemarkerConfiguration.getTemplate("export-format-requisition.xml").process(data,response.getWriter());
        }catch (Exception e){
            e.printStackTrace();
        }
    }*//*


}*/
