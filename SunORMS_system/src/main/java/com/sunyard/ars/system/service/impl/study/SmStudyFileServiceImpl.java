package com.sunyard.ars.system.service.impl.study;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.study.SmStudyFileTbBean;
import com.sunyard.ars.system.dao.study.StudyFileDao;
import com.sunyard.ars.system.service.study.ISmStudyFileService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("smStudyFileService")
@Transactional
public class SmStudyFileServiceImpl extends BaseService implements ISmStudyFileService{

	@Resource
	private StudyFileDao studyFileDao;
	
	private  String filePathParamName = "studyFilePath";
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {
			//查询操作， 根据条件
			findSmStudyFileByConfig(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_ADD.equals(oper_type)) {
			//新增学习文件记录
			addNewStudyFileRecord(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_MODIFY.equals(oper_type)) {
			//修改操作
			modifyStudyFileRecord(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_DELETE.equals(oper_type)) {
			//删除操作
			deleteStudyFileRecord(requestBean, responseBean);
		} else if("check".equals(oper_type)) {
			//检查数据的所存的文件是否存在，没有就删除
			checkFileIsExists(requestBean, responseBean);
		} else if("deleteFile".equals(oper_type)) {
			//页面上传成功，但是插入数据库失败（eg:字段长度问题）
			delectFileOfFail(requestBean, responseBean);
		} else if("getFileSavePath".equals(oper_type)) {
			//获得的上传路径，存在系统字典表中，项目运行放入map中。
			getFileSavePath(requestBean, responseBean);
		}
	}

	
	private void getFileSavePath(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get(filePathParamName);
		if(sp == null) {
			throw new Exception(filePathParamName+"在系统参数表没有配置!");
		}
		String saveFilePath = sp.getParamValue();
		
		Map<Object, Object> retMap = new HashMap<Object, Object>();
		retMap.put("path", saveFilePath);
		responseBean.setRetMap(retMap );
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功!");
	}

	private void delectFileOfFail(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String filePath = (String) sysMap.get("filePath");
		HashMap<String, Object> config = new HashMap<String,Object>();
		SmStudyFileTbBean bean = new SmStudyFileTbBean();
		bean.setFilePath(filePath);
		config.put("studyFileBean", bean);
		List<SmStudyFileTbBean> list = studyFileDao.selectStudyFileByConfig(config);
		if(list == null || list.size() == 0 ) {
			SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get(filePathParamName);
			if(sp == null) {
				throw new Exception(filePathParamName+"在系统参数表没有配置!");
			}
			String saveFilePath = sp.getParamValue();
			deleteFileOfStudy(saveFilePath + File.separator + filePath);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功!");
	}

	private void checkFileIsExists(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get(filePathParamName);
		if(sp == null) {
			throw new Exception(filePathParamName+"在系统参数表没有配置!");
		}
		String saveFilePath = sp.getParamValue();
		List<Object> parameterList = requestBean.getParameterList();
		int all = 0;
		for(int i=0; i < parameterList.size();i++) {
			SmStudyFileTbBean studyFileBean = (SmStudyFileTbBean) parameterList.get(i);
			SmStudyFileTbBean bean = studyFileDao.selectStudyFileById(studyFileBean.getId());
			if(bean == null) {
				//插入日志
				/* addOperLogInfo("system", ARSConstants.OPER_TYPE_3, "系统删除无效数据"); */
				all++;
				continue;
			}
			File file = new File(FileUtil.pathManipulation(saveFilePath+File.separator+bean.getFilePath()));
			if(!file.exists()) {
				/*
				 * studyFileDao.deleteStudyFile(bean.getId()); addOperLogInfo("system",
				 * ARSConstants.OPER_TYPE_3, "系统删除无效数据"); 不删除
				 */
				all++;
			}
		}
		Map<Object,Object> retMap = new HashMap<Object,Object>();
		retMap.put("all", all+"");
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询要下载的文件成功!");
	}

	private void deleteStudyFileRecord(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		User user = BaseUtil.getLoginUser();
		List<Object> parameterList = requestBean.getParameterList();
		SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get(filePathParamName);
		if(sp == null) {
			throw new Exception(filePathParamName+"在系统参数表没有配置!");
		}
		String saveFilePath = sp.getParamValue();
		for(int i=0;i<parameterList.size();i++) {
			SmStudyFileTbBean studyFileBean = (SmStudyFileTbBean) parameterList.get(i);
			String log = "";
			SmStudyFileTbBean bean = studyFileDao.selectStudyFileById(studyFileBean.getId());
			if(bean == null) {
				logger.info("数据库不存在该"+studyFileBean.getId()+"的数据!");
				continue;
			}
			studyFileDao.deleteStudyFile(studyFileBean.getId());
			log = user.getUserNo() + "-" + user.getUserName() + "删除id为" + studyFileBean.getId() + "的数据";
			logger.info(log);
			//删除文件
			if(deleteFileOfStudy(saveFilePath + File.separator + bean.getFilePath())) {
				log += ",并删除文件"+bean.getFilePath();
			}
			//插入日志
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功!");
		
	}

	@SuppressWarnings("rawtypes")
	private void modifyStudyFileRecord(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		
		SmStudyFileTbBean studyFileBean = (SmStudyFileTbBean) requestBean.getParameterList().get(0);
		//记录原来文件路径，修改成功后删除
		String oldFilePath = studyFileBean.getFilePath();
		
		//修改的日志
		User user = BaseUtil.getLoginUser();
		StringBuffer log = new StringBuffer();
		log.append(user.getUserNo()).append("-").append(user.getUserName());
		log.append("修改id为").append(studyFileBean.getId()).append("的数据");
		
		Map sysMap = requestBean.getSysMap();
		
		String pathAndName = (String) sysMap.get("filePath");
		//if(pathAndName != null && !pathAndName.equals("")) {

			studyFileBean.setFilePath(pathAndName);
			//修改文件的创建人
			studyFileBean.setFileCreator(user.getUserNo());

			
			//修改数据
			studyFileDao.updateStudyFile(studyFileBean);
        if(pathAndName != null && !pathAndName.equals("")) {
            logger.info("本次有修改本地文件！");
            log.append(",并上传文件").append(pathAndName);
            SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get(filePathParamName);
            if (sp != null) {
                String saveFilePath = sp.getParamValue();
                //删除以前的文件,空表示本次没有重传文件
                deleteFileOfStudy(saveFilePath + File.separator + oldFilePath);
            }
        }
		//}
		
		//插入日志
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log.toString());
		
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功!");
	}

	private void addNewStudyFileRecord(RequestBean requestBean, ResponseBean responseBean) throws Exception {
	
		@SuppressWarnings("rawtypes")
		Map sysMap = requestBean.getSysMap();
		User user = BaseUtil.getLoginUser();
		SmStudyFileTbBean studyFileBean = (SmStudyFileTbBean) requestBean.getParameterList().get(0);
		String pathAndName = (String) sysMap.get("filePath");
		if(pathAndName == null || pathAndName.equals("")) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("上传的文件保存路径为空");
			return;
		}
		studyFileBean.setFilePath(pathAndName);
		
		studyFileBean.setFileCreator(user.getUserNo());
		studyFileDao.insertStudyFile(studyFileBean,ARSConstants.DB_TYPE);
		
		//新增操作的日志
		String log = user.getUserNo() + "-" + user.getUserName()+"新增上传文件"+ pathAndName;
		//插入日志
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log.toString());
		
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("插入成功!");
	}

	//查询学习文件
	@SuppressWarnings({ "rawtypes" })
	private void findSmStudyFileByConfig(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		
		SmStudyFileTbBean studyFileBean = (SmStudyFileTbBean) requestBean.getParameterList().get(0);
		
		Map sysMap = requestBean.getSysMap();
		
		//获取页面参数 ,当前页，页记录数
		int currentPage = (int)sysMap.get("currentPage");
		int pageSize = 0;
		if(currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("pageSize");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}
		//获取 当前页，页记录数 结束
		
		HashMap<String, Object> config = new HashMap<String, Object>();
		config.put("studyFileBean", studyFileBean);
		
		Page<SmStudyFileTbBean> page = PageHelper.startPage(currentPage, pageSize);
		
		List<SmStudyFileTbBean> returnList = studyFileDao.selectStudyFileByConfig(config);
		
		Map<Object,Object> retMap = new HashMap<Object,Object>();
		long totalCount = page.getTotal();
		retMap.put("pageSize", pageSize);
		retMap.put("totalCount", totalCount);
		retMap.put("returnList", returnList);
		retMap.put("currentPage", currentPage);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	
	/**
	 * 删除文件
	 * @param filePath 文件路径加上名字
	 * @return 是否成功
	 */
	private boolean deleteFileOfStudy(String filePath) {
		if(filePath == null || filePath == "") //数据库中的无效数据
			return false;
		File file = new File(FileUtil.pathManipulation(filePath));
		if(file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}

	
}
