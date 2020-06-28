package com.sunyard.ars.common.comm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.license.B;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyard.aos.common.dao.BaseDao;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.OcrTaskMapper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.dao.TmpDataMapper;
import com.sunyard.ars.common.pojo.FormFieldModel;
import com.sunyard.ars.common.pojo.OcrTaskBean;
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.common.pojo.TmpData;
import com.sunyard.ars.common.util.CombinationUtil;
import com.sunyard.ars.flows.service.api.SunFlowService;
import com.sunyard.ars.flows.service.api.WorkItemInfo;
import com.sunyard.ars.flows.service.impl.api.SunFlowServiceImpl;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.dao.SysParameterMapper;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import com.sunyard.sunflow.client.FlowClient;
import com.sunyard.sunflow.client.ISunflowClient;
import com.sunyard.sunflow.engine.dataclass.WMTAttribute;
import com.sunyard.sunflow.engine.workflowexception.SunflowException;




public class ARSOcrWork {
	@Resource
	OcrTaskMapper ocrTaskMapper;
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private SunFlowService sunFlowService = new SunFlowServiceImpl();
	
	private Timer timer = null;
	// 数据库接口
	@Resource
	TmpBatchMapper tmpBatchMapper;
	
	@Resource
	TmpDataMapper tmpDataMapper;
	@Resource
	SysParameterMapper sysParameterMapper;
	@Resource
	FlowMapper flowMapper;
	@Resource
	private BaseDao baseDao;
	
	OcrWoker ocrWoker=null;
	int period=1000*120;
	public void ocrBackWork(){
		//安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		try{
			String param_item = "OCR_BACK_PERIOD";
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("param_item", param_item);
			map.put("bank_no", "SUNYARD");
			map.put("system_no", "AOS");
			map.put("project_no","AOS");
			String	paramValue = baseDao.getSysParamValue(map);
			if(paramValue!=null&&!paramValue.equals("")){
				int i=Integer.valueOf(paramValue);
				period=i*1000;
			}

			
			timer=new Timer();
			ocrWoker=new OcrWoker();
			timer.schedule(ocrWoker,period);
			logger.info("timer.schedule开始进行OCR识别勾兑,"+period+"毫秒后第一次执行");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	class OcrWoker extends TimerTask{
		@Override
		public void run() {
				try {
					dowork();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		}
	}
	public void  dowork() throws Exception {
		OcrTaskBean ocrTask =null;
		while(true){
			Thread.currentThread().sleep(1000);
			ocrTask = ocrTaskMapper.select();
			if(ocrTask==null){
				timer.cancel();
				timer.purge();
				timer = null;
				ocrWoker=null;
				timer=new Timer();
				ocrWoker=new OcrWoker();
				timer.schedule(ocrWoker,period);
				logger.info("timer.schedule开始进行OCR识别勾兑,"+period+"毫秒后执行");
				return;
			}

			String taskId = BaseUtil.filterSqlParam(ocrTask.getTaskId());
			if(ocrTaskMapper.lock(taskId)==0){
				continue;
			}
			boolean submitFlag=true;
			String message = ocrTask.getMessage();
				logger.info("开始识别：" + BaseUtil.filterLog(ocrTask.getTaskId()));
			//从识别平台接收的回调报文是ResponseBean格式的。
				ResponseBean ocrRes;
				//解析返回报文
				String retCode;
				Map<Object, Object> retMap;
				String batchId;
				try {
					logger.info("----------开始解析报文-------------");
					ObjectMapper objectMapper = new ObjectMapper();
							ocrRes = objectMapper.readValue(message, ResponseBean.class);
							retCode = ocrRes.getRetCode();
							retMap = ocrRes.getRetMap();
							//获取批次信息
							Map batchInfo = (Map)retMap.get("batchInfo");
							batchId = BaseUtil.filterLog((String)batchInfo.get("batchId"));
					logger.info("解析报文结束，批次号："+batchId+"------------");
				} catch (Exception e1) {
					logger.info("报文解析失败------------");
					ocrTaskMapper.updateWrong(taskId,"报文解析失败");
					continue;
				}
						//根据批次号获取整个批次信息
						TmpBatch tmpBatch = tmpBatchMapper.selectByPrimaryKey(batchId);

						if(tmpBatch==null){
							logger.info("批次"+batchId+"未找到,本次识别分析终止");
							ocrTaskMapper.updateWrong(taskId,"批次"+batchId+"未找到");
							continue;
						}
						if(ARSConstants.HANDLE_SUCCESS.equals(retCode)){//报文返回识别成功，开始解析
							//判断批次是否在识别流程内
							logger.info("批次识别成功-----retCode："+retCode);
							if(tmpBatch.getProgressFlag().equals(ARSConstants.PROCESS_FLAG_10)){
								//获取识别结果信息
								logger.info("----------开始解析批次-------------");
								List ocrResultList = (List)retMap.get("BatchOcrResult");
								if(ocrResultList !=null && ocrResultList.size()>0){
									try {
										
                                        Map<String,Map<String,Object>> ocrMap = new HashMap<>();
                                        String ecmFileName = null;
                                        Map<String,Object> ocrResult =null;
										//开始处理报文
                                        for(int i=0;i<ocrResultList.size();i++){
                                            ocrResult = (Map<String,Object>)ocrResultList.get(i);
                                            ecmFileName = (String) ocrResult.get("ecmFileName");
                                            if(ecmFileName!=null && !"".equals(ecmFileName)){
                                                ocrMap.put(ecmFileName,ocrResult);
                                            }
                                            ecmFileName = null;
                                        }
                                        logger.info("----------识别结果装载map执行结束-------------");
                                        
                                        
                                        //查询待勾对图像信息
                                        HashMap tmpDataMap = new HashMap();
										tmpDataMap.put("batchId", batchId);
										tmpDataMap.put("copyInccodein", 0);
										tmpDataMap.put("checkFlag", ARSConstants.CHECK_FLAG_FAIL);
                                        List<TmpData> tmpdataList  = tmpDataMapper.selectDataBySelective(tmpDataMap);
                                        //存放已识别flowId
										List<String> flowIdList = new ArrayList<>();
										//存放勾对sql
										List<String> updateSqls = new ArrayList<String>();
										//开始进行勾对处理
										logger.info("----------开始解析图像信息-------------");
										
										
										
										for(TmpData tmpData : tmpdataList){
											//获取图像process_state进行判断处理
											String processState = tmpData.getProcessState();
											//判断前三位，若图像未做过识别，且未做过补录，且未做过审核，则进行识别分析
											if("1".equals(processState.substring(0, 1))){
												logger.info("图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName())+"图像已经做过识别，不再进行处理----------");
												continue;
											}else if("1".equals(processState.substring(1, 2))){
												logger.info("图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName())+"图像已经做过补录，不再进行处理-----------");
												continue;
											}else if("1".equals(processState.substring(2, 3))){
												logger.info("图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName())+"图像已经做过审核，不再进行处理-----------");
												continue;
											}
											
											//获取图像识别结果
										    ocrResult = ocrMap.get(tmpData.getFileName());
                                            if(ocrResult==null){
                                                logger.info(BaseUtil.filterLog(tmpData.getFileName())+"未找到待勾对图像,识别报文可能存在异常-----");
                                                continue;
                                            }
											tmpData.setFormId((String)ocrResult.get("pageId"));
											
											//凭证版面，识别失败则设置默认版面
											if(ocrResult.get("formName")==null){
												tmpData.setFormName(ARSConstants.OCRFAIL_FORMNAME);
											}else{
												tmpData.setFormName((String)ocrResult.get("formName"));
											}
											
											String ocrRetCode = (String)ocrResult.get("retCode");
											if(ARSConstants.OCR_SUCCESS.equals(ocrRetCode)){
												//识别成功
												//判断图像主附件
												String imageType = (String)ocrResult.get("imageType");
												if(ARSConstants.PS_LEVEL.equals(imageType)){
													//主件，需勾对流水
													//图像识别成功，执行勾对操作
													logger.info("获取图像信息成功，且为主键---serialno:"+BaseUtil.filterLog(tmpData.getSerialNo())+",fileName:"+"图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName()));
													List<Map<String,Object>> fieldResult = (List<Map<String,Object>>)ocrResult.get("fieldResult");
													
                                                    //判断是否存在识别域
													if(fieldResult==null || fieldResult.size()==0){
														logger.info("图像未配置识别域,勾对失败---serialno:"+BaseUtil.filterLog(tmpData.getSerialNo())+",fileName:"+"图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName()));
														tmpData.setProcessState("200000"); // 200000表示自动识别失败
														tmpData.setCheckFlag(ARSConstants.CHECK_FLAG_FAIL); //勾对失败
														tmpData.setPsLevel(ARSConstants.PS_LEVEL);
														//只拼装图像sql
														updateSqls.add(this.getImageUpdateSql(tmpData));
													    continue;
													}
													
													
													List<List> list = new ArrayList<>();
													//处理识别结果
													Map<String,List> fieldResultMap = new HashMap<String,List>();
													FormFieldModel fieldModel =null;
													for(Map<String,Object> fieldMap : fieldResult){{
														try{
															//判断是否为签名识别
															if("IS_SIGN".equalsIgnoreCase(((String)fieldMap.get("ocrFieldName")))){
																String isSign = (String)fieldMap.get("fieldValue");
																if("null".equals(isSign) || "".equals(isSign)){
																	//签名识别失败
																	tmpData.setIsSign("2");
																}else {
																	//签名识别成功
																	tmpData.setIsSign("1");
																}
	                                                            continue;
															}



															if(fieldResultMap.get(fieldMap.get("ocrFieldName")) == null){
																if("AMOUNT".equalsIgnoreCase(((String)fieldMap.get("ocrFieldName"))) && fieldMap.get("fieldValue")!=null){
																	//识别字段为金额时做特殊处理
																	String fv = (String)fieldMap.get("fieldValue");
																	if("null".equals(fv) || "".equals(fv)){
																		fieldModel =	new FormFieldModel((String)fieldMap.get("ocrFieldName"),fv);
																	}else{
																		fieldModel =	new FormFieldModel((String)fieldMap.get("ocrFieldName"),String.valueOf(Double.valueOf(fv)/100.00));
																	}

																}else{
																	fieldModel =	new FormFieldModel((String)fieldMap.get("ocrFieldName"),(String)fieldMap.get("fieldValue"));
																}
																List<FormFieldModel> fieldModelList =  new ArrayList<FormFieldModel>();
																fieldModelList.add(fieldModel);
																fieldResultMap.put((String)fieldMap.get("ocrFieldName"), fieldModelList);

															}else{

																if("AMOUNT".equalsIgnoreCase(((String)fieldMap.get("ocrFieldName"))) && fieldMap.get("fieldValue")!=null){
																	//识别字段为金额时做特殊处理
																	String fv = (String)fieldMap.get("fieldValue");
																	if("".equals(fv) || "null".equals(fv)){
																		fieldModel =	new FormFieldModel((String)fieldMap.get("ocrFieldName"),fv);
																	}else{
																		fieldModel =	new FormFieldModel((String)fieldMap.get("ocrFieldName"),String.valueOf(Double.valueOf(fv)/100.00));
																	}
																}else{
																	fieldModel =	new FormFieldModel((String)fieldMap.get("ocrFieldName"),(String)fieldMap.get("fieldValue"));
																}
																fieldResultMap.get(fieldMap.get("ocrFieldName")).add(fieldModel);
															}
														}catch(Exception e){
															logger.error("识别结果fieldMap解析出现异常，跳过本次识别字段解析，开始解析下一个fieldMap",e);
														}

													}}
													
													//将识别结果转到list中
												    for (Map.Entry<String, List> entry : fieldResultMap.entrySet()) {
												    	logger.info("识别结果----"+entry.getKey());
													   list.add(entry.getValue());
													 }
													
													 //对所有识别结果进行排列组合
												    List<List<FormFieldModel>> fList =  CombinationUtil.combinationOcr(list);
												
													logger.info("图像fileName："+"图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName())+"识别结果组装结束,开始进行流水匹配");
													boolean flowVerifyFlag = false;
													HashMap flowCondMap =null;
													 //根据识别结果查询流水是否存在
													for(int j=0;j<fList.size();j++){
														//执行流水查找
														flowCondMap = new HashMap();
														flowCondMap.put("formFieldList",fList.get(j));
														flowCondMap.put("occurDate",tmpBatch.getOccurDate());
														flowCondMap.put("siteNo",tmpBatch.getSiteNo());
														flowCondMap.put("operatorNo",tmpBatch.getOperatorNo());
														flowCondMap.put("checkFlag", ARSConstants.CHECK_FLAG_FAIL);
														List<String> flowIds =null;
														try{
															flowIds = flowMapper.selectVerifyFlow(flowCondMap);
														}catch(Exception e){
															logger.info("流水匹配查询出现异常",e);
															continue;
														}
															//查询到流水，则执行流水勾对，并跳出循环
															if(flowIds!=null && flowIds.size()>0){
																 //查询到流水，则执行流水勾对，并跳出循环
		                                                            //判断流水号是否已勾对过，流水已被勾对，当做附件处理
		                                                            if(flowIdList.contains(flowIds.get(0))){
		                                                                logger.info("流水号flowId："+BaseUtil.filterLog(flowIds.get(0))+"已被勾对过，图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName())+"直接做附件处理");
		                                                                tmpData.setProcessState("100000"); // 100000表示自动识别成功
		                                                                tmpData.setCheckFlag(ARSConstants.CHECK_FLAG_SUCCESS_PL);
		                                                                tmpData.setPsLevel(ARSConstants.PL_LEVEL);
		                                                                //如果版面未正常识别，则设置为附件默认版面
		                                                                if(ARSConstants.OCRFAIL_FORMNAME.equals(tmpData.getFormName())){
		                                                                    tmpData.setFormName(ARSConstants.PL_FORMNAME);
		                                                                }
		                                                                updateSqls.add(this.getImageUpdateSql(tmpData));


		                                                            }else{//流水未勾对，执行勾对逻辑
		                                                                logger.info("图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName())+"成功匹配到流水，流水号flowId："+BaseUtil.filterLog(flowIds.get(0)));

		                                                                //组装图像勾对条件
		                                                                tmpData.setFlowId(flowIds.get(0));
		                                                                tmpData.setProcessState("100000"); // 100000表示自动识别成功
		                                                                tmpData.setCheckFlag(ARSConstants.CHECK_FLAG_SUCCESS_PS);//主件勾对成功
		                                                                tmpData.setPsLevel(ARSConstants.PS_LEVEL);

		                                                                //不直接勾对,拼装sql后，按批次统一处理
		                                                                //组装流水勾对sql
		                                                                updateSqls.add(this.getFlowUpdateSql(flowIds.get(0), tmpBatch, tmpData));
		                                                                //组装图像勾对sql
		                                                                updateSqls.add(this.getImageUpdateSql(tmpData));
		                                                                flowIdList.add(flowIds.get(0));
		                                                            }
		                                                            flowVerifyFlag = true;
		                                                            break;
															
															}
														
														
														
													}
													
													//循环结束,未成功匹配到流水时，说明此次勾对失败
													if(!flowVerifyFlag){
														logger.info("图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName())+"匹配流水失败");
														tmpData.setProcessState("200000"); // 200000表示自动识别失败
														tmpData.setCheckFlag(ARSConstants.CHECK_FLAG_FAIL); //勾对失败
														tmpData.setPsLevel(ARSConstants.PS_LEVEL);
														//只拼装图像sql
														updateSqls.add(this.getImageUpdateSql(tmpData));
													}
													
												}else{
													logger.info("获取图像信息成功，且为附件---serialno:"+BaseUtil.filterLog(tmpData.getSerialNo())+",fileName:"+"图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName()));
													//附件，仅识别版面
													tmpData.setProcessState("100000"); // 100000表示自动识别成功
													tmpData.setCheckFlag(ARSConstants.CHECK_FLAG_SUCCESS_PL);
													tmpData.setPsLevel(ARSConstants.PL_LEVEL);
													//如果版面未正常识别，则设置为附件默认版面
													if(ARSConstants.OCRFAIL_FORMNAME.equals(tmpData.getFormName())){
														tmpData.setFormName(ARSConstants.PL_FORMNAME);
													}
													updateSqls.add(this.getImageUpdateSql(tmpData));
													logger.info("图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName())+"识别结果为附件");
												}
											
											}else{
												//图像识别失败，当做未知版面处理
												tmpData.setProcessState("200000"); // 200000表示自动识别失败
												tmpData.setCheckFlag(ARSConstants.CHECK_FLAG_FAIL);
												tmpData.setPsLevel(ARSConstants.PS_LEVEL);  //识别失败主件
												updateSqls.add(this.getImageUpdateSql(tmpData));
												logger.info("图像inccodein_batch："+BaseUtil.filterLog(tmpData.getInccodeinBatch()+"")+",fileName："+BaseUtil.filterLog(tmpData.getFileName())+"识别失败");
											}
										}	
										//识别分析结束，开始执行勾对sql	
										if(updateSqls!=null && updateSqls.size()>0){
											logger.info("开始执行勾对sql---共"+updateSqls.size()+"条");
											flowMapper.executeSqls(updateSqls);
											logger.info("勾对sql执行完毕---------");
											tmpdataList=null;
											flowIdList=null;
											updateSqls=null;
										}
										logger.info("识别分析结束----------");
									} catch (Exception e) {
										logger.info("勾对出现异常 ",e);										
									}
									
								}else{
									//识别结果为空
									logger.info("识别结果为空，直接进入补录----batch："+batchId+"----");
								}
							}else{
								//批次已不在识别流程里,不进行工作流提交
								submitFlag = false;
								logger.error("识别分析异常，当前批次不在识别流程内,异常batch"+batchId+"----");
								ocrTaskMapper.updateWrong(taskId,"识别分析异常，当前批次不在识别流程内");
							}
						}else{
							//识别失败，直接进入补录
							logger.info("识别失败，直接进入补录----batch："+batchId+"----"+ocrRes.getRetMsg());
							
						}
				//调工作流直接进入下一个流程
				if(submitFlag){
					try {
						sunflowPush(tmpBatch,taskId);
					} catch (Exception e2) {
						ocrTaskMapper.updateWrong(taskId,e2.getMessage());
					}
				}
				logger.info("批次"+batchId+"回调结束,批次进入下一个流程");
			}
		}
			
			/**
			 * 拼装流水勾对sql
			 * @param fields
			 * @param tmpbatch
			 * @param tmpData
			 * @return
			 */
			private String getFlowUpdateSql(String flowId,TmpBatch tmpbatch,TmpData tmpData) throws Exception {
				StringBuffer sql = new StringBuffer("UPDATE FL_FLOW_TB A SET CHECK_FLAG='1', LSERIAL_NO = '"+tmpData.getSerialNo()+"'");
			    sql.append(" WHERE A.OCCUR_DATE = '"+tmpbatch.getOccurDate()+"' AND A.SITE_NO = '"+tmpbatch.getSiteNo()+"' AND A.OPERATOR_NO = '"+tmpbatch.getOperatorNo()+"'");
//			    for(FormFieldModel fieldModel : fieldModelList){
//			    	if(fieldModel.getFieldValue() instanceof String){
//			    		sql.append(" AND "+fieldModel.getFieldName()).append(" = '").append(fieldModel.getFieldValue()).append("'");
//			    	}else{
//			    		sql.append(" AND "+fieldModel.getFieldName()).append(" = ").append(fieldModel.getFieldValue());
//			    	}
//			    }
			    sql.append("  AND A.flow_id = '"+flowId+"'");
			    logger.info("流水勾对sql:"+BaseUtil.filterLog(sql.toString()));
			    return sql.toString();
			}
			
			/**
			 * 拼装图像勾对sql
			 * @param tmpData
			 * @return
			 */
			private String getImageUpdateSql(TmpData tmpData) throws Exception {
				StringBuffer sql = new StringBuffer("UPDATE BP_TMPDATA_1_TB A SET ");
				//勾对标志
				sql.append("CHECK_FLAG='"+tmpData.getCheckFlag()+"'");
				
				//版面名称
				sql.append(",FORM_NAME ='"+tmpData.getFormName()+"'");
				
				//图像处理标志
				sql.append(",PROCESS_STATE='"+tmpData.getProcessState()+"'" );
				
				//流水号
				if(tmpData.getFlowId()!=null && !"".equals(tmpData.getFlowId())){
					sql.append(",FLOW_ID = '"+tmpData.getFlowId()+"'");
				}
				
				//版面代码
				if(tmpData.getFormId()!=null && !"".equals(tmpData.getFormId())){
					sql.append(",FORM_ID ='"+tmpData.getFormId()+"'");
				}
				
				//主附件
				if(tmpData.getPsLevel()!=null && !"".equals(tmpData.getPsLevel())){
					sql.append(",PS_LEVEL = '"+tmpData.getPsLevel()+"'");
				}

				//签名
				if(tmpData.getIsSign()!=null && !"".equals(tmpData.getIsSign())){
					sql.append(",IS_SIGN = '"+tmpData.getIsSign()+"'");
				}
				
				sql.append(" where SERIAL_NO='"+tmpData.getSerialNo()+"'");
				logger.info("图像勾对sql:"+BaseUtil.filterLog(sql.toString()));
				return sql.toString();
			}
			
			
			private void sunflowPush(TmpBatch tmpBatch,String taskId){
				Long procinstId = tmpBatch.getProcinstId();
				List<WorkItemInfo> workList = sunFlowService.getWorksByPriIdandWname("sa","ocr", procinstId, tmpBatch.getAreaCode(), 5);
				if (workList != null && workList.size() > 0) {
					WorkItemInfo workItemInfo = workList.get(0);
					long workItemId = workItemInfo.getWorkItemId();
					ISunflowClient client = null;
					try {
						client = new FlowClient();
						client.connect("sa", "000000");
						client.applyWorkItem(workItemId);
						Map<String, String> workItemAttris = workItemInfo.getWorkItemAttris();
						workItemAttris.put("flowFlag", ARSConstants.PROCESS_FLAG_30);
						workItemInfo.setWorkItemAttris(workItemAttris);
						Map<String, String> workItemAttris2 = workItemInfo.getWorkItemAttris();
						Set<String> attriNames = workItemAttris2.keySet();
						WMTAttribute[] attributes = new WMTAttribute[attriNames.size()];
						int index = 0;
						for (String attriName : attriNames) {
							WMTAttribute wmtAttribute = new WMTAttribute();
							wmtAttribute.setAttributeName(attriName);
							wmtAttribute.setStringValue(workItemAttris2.get(attriName));
							attributes[index] = wmtAttribute;
							index++;
						}
						client.modifyWorkItemRelevantData(workItemId, attributes, tmpBatch.getAreaCode());
						client.checkInWorkItem(workItemId);
						
						//更新bp_tmpbatch_tb orc识别标志为已识别
						tmpBatch.setOcrFactorFlag("2");
						tmpBatch.setProgressFlag("30");
						tmpBatchMapper.updateByPrimaryKeySelective(tmpBatch);
						ocrTaskMapper.update(taskId);
						
					} catch (Exception e) {
						logger.error("ARSFlowServiceImpl-流程工作项推送认领异常", e);
						logger.error("识别结束工作流推进异常，异常流程ID......."+procinstId);
						throw new RuntimeException(e);
					}finally {
						if (client != null)
							try {
								client.disconnect();
							} catch (SunflowException e) {
								logger.error("ARSFlowServiceImpl-流程客户端关闭异常", e);
							}
					}
					
				}else {
					throw new RuntimeException("获取工作项信息异常");
				}
				
		}
			
			
			public  void destroyTimer(){
				if(timer!=null){
					logger.info("终止OCR线程！");
					timer.cancel();
					timer.purge();
					timer = null;
				}
			}

	}

