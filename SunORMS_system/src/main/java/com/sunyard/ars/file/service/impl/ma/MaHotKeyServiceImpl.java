package com.sunyard.ars.file.service.impl.ma;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.file.dao.ma.HotKeyMapper;
import com.sunyard.ars.file.pojo.ma.HotKey;
import com.sunyard.ars.file.service.ma.IMaHotKeyService;
import com.sunyard.ars.system.bean.sc.ShortcutKey;
import com.sunyard.ars.system.init.ReadXML;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("maHotKeyService")
@Transactional
public class MaHotKeyServiceImpl extends BaseService  implements IMaHotKeyService {
    
	@Resource
	private HotKeyMapper hotKeyMapper;
	

    @Override
    public ResponseBean execute(RequestBean requestBean) throws Exception{
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
    }

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map reqMap = new HashMap();
		reqMap = requestBean.getSysMap();
		String oper_type = (String) reqMap.get("oper_type");
		if("checkShortKeyConfig".equals(oper_type)){
			checkShortKeyConfig(requestBean,responseBean);
		}else if("keyList".equals(oper_type)){
			keyList(requestBean,responseBean);
		}else if("saveShortKey".equals(oper_type)){
			saveShortKey(requestBean,responseBean);
		}else if ("getUserHotKeys".equals(oper_type)) {
			getUserHotKeys(requestBean,responseBean);
		}
	}	

	
	
	/**
	 * 检测配置文件中的快捷键与数据库是否一致
	 */
	public void checkShortKeyConfig(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		boolean flag = true;
		User user = BaseUtil.getLoginUser();
		String moduleName = (String)requestBean.getSysMap().get("moduleName");
		HashMap<String,Object> codeMap = new HashMap<String,Object>();
		codeMap.put("userNo", user.getUserNo());
		codeMap.put("moduleList", moduleName);
		
		//获得设置过的存入数据库的全部快捷键
		List<HotKey> keyList = hotKeyMapper.selectBySelective(codeMap);
		Map functionsMap = ReadXML.readFunctions(ARSConstants.CONFIGPATH, moduleName);
		if(keyList != null && keyList.size() > 0){
			for(HotKey hotkey : keyList){
				String functionId = hotkey.getFunctionId();
				//获取该快捷键功能
				ShortcutKey shortcutKey = (ShortcutKey) functionsMap.get(Integer.parseInt(functionId));
				if(shortcutKey == null){
					codeMap = new HashMap<String,Object>();
					//快捷键重置
					hotKeyMapper.deleteBySelective(codeMap);
					flag = false;
					break;
				}
			}
		}
		
		Map<Object, Object> retMap = new HashMap<Object, Object>();
		//配置有问题
		if(!flag){
			retMap.put("isSuccess", "false");
			retMap.put("errorInfo", "快捷键的配置文件和数据库数据有误,已经重置！");
		}
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	
	/**
	 * 获取快捷键列表(全部展现用)
	 */
	public void keyList(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获得设置过的存入数据库的全部快捷键
		List<HotKey> keyList = null;
		User user = BaseUtil.getLoginUser();
		String moduleName = (String)requestBean.getSysMap().get("moduleName");
		Map<Object, Object> retMap = new HashMap<>();
		HashMap<String,Object> codeMap = new HashMap<>();
		codeMap.put("userNo", user.getUserNo());
		codeMap.put("moduleList", moduleName);
		//获得设置过的存入数据库的全部快捷键
		List resultList = new ArrayList();

		keyList = hotKeyMapper.selectBySelective(codeMap);
		
		//若快捷键为空，可设置默认快捷键
		if(keyList == null || keyList.size() <= 0){
			HashMap<String,Object> defaultMap = new HashMap<>();
			defaultMap.put("userNo", "######");
			defaultMap.put("moduleList", moduleName);
			keyList = hotKeyMapper.selectBySelective(defaultMap);
			retMap.put("isdefault", "true");
		}
		
		
		//获得对应模块xml中配置的全部快捷键功能列表,不存放到session,无法及时更新
		Map functionsMap =  ReadXML.readFunctions(ARSConstants.CONFIGPATH,moduleName);
		//覆盖已设置的值
		if(keyList != null && keyList.size() > 0){
			for(HotKey hotkey : keyList){
				String functionId = hotkey.getFunctionId();
				//获取该快捷键功能
				ShortcutKey shortcutKey = (ShortcutKey) functionsMap.get(Integer.parseInt(functionId));
				if(shortcutKey == null){
					//快捷键重置
					hotKeyMapper.deleteBySelective(codeMap);
					break;
				}
				shortcutKey.setId(hotkey.getId());
				String keyCode1 = BaseUtil.filterHeader(hotkey.getKeyCode1());
				String keyCode2 =BaseUtil.filterHeader( hotkey.getKeyCode2());
				if(keyCode1 != null){
					shortcutKey.setKeyCode1(keyCode1);
				}else{
					shortcutKey.setKeyCode1("");
				}
				if(keyCode2 != null){
					shortcutKey.setKeyCode2(keyCode2);
				}else{
					shortcutKey.setKeyCode2("");
				}
				//更新全部功能列表map中设置过的快捷键
				functionsMap.put(Integer.parseInt(BaseUtil.filterHeader(functionId)), shortcutKey);
			}
		}
		
		Iterator iterator = functionsMap.keySet().iterator();
		while(iterator.hasNext()){
			int key = (Integer)iterator.next();
			ShortcutKey result = (ShortcutKey) functionsMap.get(key);
				resultList.add(result);
			}		
		
		retMap.put("resultList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 保存快捷键
	 */
	public void saveShortKey(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		Map reqMap = new HashMap();
		reqMap = requestBean.getSysMap();
		User user = BaseUtil.getLoginUser();
		String moduleName = (String)requestBean.getSysMap().get("moduleName");
		String attr = (String) reqMap.get("attr");
		

		String key1 = "''";
		String key2 = "''";
		String functionId = "''";
		int id = 0;
		List<HotKey> list = new ArrayList<HotKey>();
		HotKey key = null;
		
		String[] tmpArray = attr.split(",");
		
		for(int i=0;i<tmpArray.length;i++){
			String str = tmpArray[i];
			if(!"".equals(str)){
				String[] data = str.split("\\|");
				functionId = data[0];
				try {
					key1 = data[1];
				} catch (ArrayIndexOutOfBoundsException e) {
					key1 = null;
				}
				
				try {
					key2 = data[2];
				} catch (ArrayIndexOutOfBoundsException e) {
					key2 = null;
				}
				key = new HotKey();
				key.setFunctionId(functionId);
				key.setKeyCode1(key1);
				key.setKeyCode2(key2);
				key.setModuleList(moduleName);
				key.setUserNo(user.getUserNo());
				if(!StringUtil.checkNull(data[3])){
					id = Integer.parseInt(data[3]);
					key.setId(id);
				}
				list.add(key);
			}
			
		}
		
		for(HotKey hotKey:list){
			if(hotKey.getId()!=null){
				if(StringUtil.checkNull(hotKey.getKeyCode2())){
					//删除空快捷键
					hotKeyMapper.deleteByPrimaryKey(hotKey.getId());
				}else{
					//修改
					hotKeyMapper.updateByPrimaryKey(hotKey);
				}
			}else{
				if(!StringUtil.checkNull(hotKey.getKeyCode2())){
					//新增
					hotKeyMapper.insert(hotKey);
				}
			}
		}
		
		
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("更新成功");
		
	}
	
	/**
	 * 查询用户快捷键
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void getUserHotKeys(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		User user = BaseUtil.getLoginUser();
		Map<Object, Object> retMap = new HashMap<Object, Object>();
		Map<String,List<HotKey>> keyMap = new HashMap<>();
		List<HotKey> hotKeyList = null;
		HashMap<String,Object> codeMap = new HashMap<String,Object>();
		codeMap.put("userNo", user.getUserNo());
		//获得设置过的存入数据库的全部快捷键
		List<HotKey> keyList = hotKeyMapper.selectBySelective(codeMap);
		if(keyList==null || keyList.size()==0){
			//获取默认的快捷键
			HashMap<String,Object> defaultMap = new HashMap<String,Object>();
			defaultMap.put("userNo", "######");
			keyList = hotKeyMapper.selectBySelective(defaultMap);
		}
		for (HotKey hotKey : keyList) {
			if(keyMap.get(hotKey.getModuleList()) == null){
				hotKeyList = new ArrayList<HotKey>();
				hotKeyList.add(hotKey);
				keyMap.put(hotKey.getModuleList(), hotKeyList);
			}else{
				keyMap.get(hotKey.getModuleList()).add(hotKey);
			}
		}
		retMap.put("hotKeyMap", keyMap);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	

}
