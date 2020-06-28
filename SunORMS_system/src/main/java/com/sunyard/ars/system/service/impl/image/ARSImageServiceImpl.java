package com.sunyard.ars.system.service.impl.image;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.*;
import com.sunyard.ars.common.pojo.FileUrlBean;
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.common.pojo.TmpData;
import com.sunyard.ars.common.util.ECMUtil;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.system.bean.sc.OrganData;
import com.sunyard.ars.system.bean.sc.SCDatasource;
import com.sunyard.ars.system.bean.sc.ServiceReg;
import com.sunyard.ars.system.dao.et.ModelDataQueryMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.dao.mc.QueryImgMapper;
import com.sunyard.ars.system.dao.sc.OrganDataMapper;
import com.sunyard.ars.system.dao.sc.SCDatasourceMapper;
import com.sunyard.ars.system.dao.sc.ServiceRegMapper;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.pojo.mc.QueryImg;
import com.sunyard.ars.system.service.image.IARSImageService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("arsImageService")
@Transactional
public class ARSImageServiceImpl extends BaseService implements IARSImageService{
    @Resource
    private SCDatasourceMapper scDatasourceMapper;

    @Resource
    private ServiceRegMapper serviceRegMapper ;

    @Resource
    private TmpBatchMapper tmpBatchMapper ;

    @Resource
    private OrganDataMapper organDataMapper;

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private SmFieldDefTbMapper smFieldDefTbMapper;

    @Resource
    private TmpDataMapper tmpDataMapper;

    @Resource
    private FlowMapper flowMapper;

    @Resource
    private QueryImgMapper queryImgMapper;

    @Resource
    private ModelDataQueryMapper modelDataQueryMapper;

    @Resource
    private ModelMapper modelMapper;


    @Override
    public ResponseBean execute(RequestBean requestBean) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        return executeAction(requestBean, responseBean);
    }

    @Override
    @SuppressWarnings({ "rawtypes" })
    protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // TODO Auto-generated method stub
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();

        // 获取操作标识
        String oper_type = (String) sysMap.get("oper_type");
        if ("showImg".equals(oper_type)) {
            // 查询影像url(单张)
            showImg(requestBean,responseBean);
        }else if ("showImgByBatch".equals(oper_type)) {
            //查询影像url(批次)
            showImgByBatch(requestBean,responseBean);
        }else if("queryArmsImageInfo".equals(oper_type)){
            //查询预警，监测任务看图信息
            queryArmsImageInfo(requestBean,responseBean);
        }else if ("showImgList".equals(oper_type)) {
            showImgList(requestBean,responseBean);
        }
    }



    /**
     * 查询影像url(单笔)
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    private void showImg(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        Map<String,Object> sysMap = requestBean.getSysMap();
        String batchId =(String)sysMap.get("batchId");
        String inputDate = (String)sysMap.get("inputDate");
        String fileName = (String)sysMap.get("fileName");
        String contentId = (String)sysMap.get("contentId");
        String siteNo = (String)sysMap.get("siteNo");
        String moduleId = (String)sysMap.get("module");//看图模块
        logger.info("[showImg]：batchId:"+batchId+",contentId:"+contentId+",fileName:"+fileName);
        ECMUtil ecmUtil=null;
        String url = "";

        HashMap hashMap = new HashMap();
        hashMap.put("dataTb","BP_TMPDATA_1_TB");
        hashMap.put("batchId",BaseUtil.filterSqlParam(batchId));
        hashMap.put("fileName",BaseUtil.filterSqlParam(fileName));
        //获取影像记录
        List<TmpData> list = tmpDataMapper.selectDataNps(hashMap);
        if(list==null || list.size()==0){
            hashMap.put("dataTb","BP_TMPDATA_1_TB_HIS");
            list = tmpDataMapper.selectDataNps(hashMap);
        }
        String dataSId = list.get(0).getDataSourceId();
        SCDatasource source = scDatasourceMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(dataSId));
        //判断看图方式
        if(!ARSConstants.DATA_SOURCE_TYPE_ARS.equals(source.getDataSourceType())){
            //无纸化看图
            contentId = BaseUtil.filterSqlParam(list.get(0).getContentId());//从bp_tmpdata_1_tb中获取
            logger.info("无纸化看图：batchId:"+batchId+",contentId:"+contentId+",fileName:"+fileName);
            SysParameter npsQueryType= ARSConstants.SYSTEM_PARAMETER.get("IMG_QUERY_TYPE_NPS");
            if(npsQueryType!=null && "1".equals(npsQueryType.getParamValue())){
                showImgForNpBatch( responseBean, contentId, source, inputDate, fileName);
            }else{
                showImgForNp( responseBean, contentId, source, inputDate, fileName);
            }
            return;
        }

		try {
		    //页面上可能未传入inputDate,siteNo
            if(StringUtil.checkNull(inputDate)||StringUtil.checkNull(siteNo)){
                //获取批次信息
                TmpBatch batch = tmpBatchMapper.selectByPrimaryKey(batchId);
                inputDate = batch.getInputDate();
                siteNo = batch.getSiteNo();
            }
			//获取后督系统对应的ECM信息
            ecmUtil = getEcmUtil(siteNo,null);
            if(ecmUtil == null){
                logger.error("ecmUtil为空，机构数据表配置异常，机构号："+ BaseUtil.filterLog(siteNo));
                responseBean.setRetMsg("机构数据表配置异常，机构号："+siteNo);
                responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                return;
            }

			//可能存在contentId未能成功反写的批次，查询后存入session中
			if(StringUtil.checkNull(contentId)){
				HttpSession session = RequestUtil.getRequest().getSession();
				String arsImgInfo =session.getAttribute(moduleId+"_"+"arsImgInfo")+"";
				if(arsImgInfo!=null && !arsImgInfo.equals("") && !arsImgInfo.equals("null") && arsImgInfo.split(";")[0].equals(batchId)){
					contentId = arsImgInfo.split(";")[1];
				}else{
					String batchXml = ecmUtil.heightQuery(batchId, inputDate);
					contentId = ecmUtil.parseHeightQueryXML(batchXml);
					//将批次contentId信息存入session中
					arsImgInfo = batchId+";"+contentId;
					session.setAttribute(moduleId+"_"+"arsImgInfo", arsImgInfo);
				}
			}
			//获取图片信息
			String fileXml = ecmUtil.queryFile(contentId, inputDate, fileName);
			url = ecmUtil.parseXMLforUrl(fileXml);
			logger.info("成功获取到url");
		}  catch (Exception e) {
            logger.error("从ECM获取图片URL出现异常", e);
            responseBean.setRetMsg("从ECM获取图片URL出现异常");
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            return;
        }
        Map retMap = new HashMap();
        retMap.put("url",url);
        responseBean.setRetMap(retMap);
        responseBean.setRetMsg("查询影像信息成功");
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);


    }

    /**
     * 查询影像url(批次)
     * @param requestBean
     * @param responseBean
     */
    private void showImgByBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
        Map<String,Object> sysMap = requestBean.getSysMap();
        String batchId = (String)sysMap.get("batchId");
        String inputDate = (String)sysMap.get("inputDate");
        String fileName = (String)sysMap.get("fileName");
        String contentId = (String)sysMap.get("contentId");
        String siteNo = (String)sysMap.get("siteNo");
        String moduleId = (String)sysMap.get("module");//看图模块
        boolean flag = false;
        logger.info("[showImgByBatch]批次：batchId:"+batchId+",contentId:"+contentId+",fileName:"+fileName);


        //测试看图代码
//        Random random = new Random();
//        int a = random.nextInt(5)+1;
//        String url = "http://localhost:8080/SunARS/static/img/tibet-"+a+".jpg";
        //测试看图end----------------

        HashMap hashMap = new HashMap();
        hashMap.put("dataTb","BP_TMPDATA_1_TB");
        hashMap.put("batchId",batchId);
        hashMap.put("fileName",fileName);
        //获取影像记录
        List<TmpData> list = tmpDataMapper.selectDataNps(hashMap);
        if(list==null || list.size()==0){
            hashMap.put("dataTb","BP_TMPDATA_1_TB_HIS");
            list = tmpDataMapper.selectDataNps(hashMap);
        }
        String dataSId = list.get(0).getDataSourceId();
        SCDatasource source = scDatasourceMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(dataSId));
        //判断看图方式
        if(!ARSConstants.DATA_SOURCE_TYPE_ARS.equals(source.getDataSourceType())){
            //无纸化看图
            contentId = BaseUtil.filterSqlParam(list.get(0).getContentId());//从bp_tmpdata_1_tb中获取
            logger.info("无纸化看图：batchId:"+batchId+",contentId:"+contentId+",fileName:"+fileName);
            SysParameter npsQueryType= ARSConstants.SYSTEM_PARAMETER.get("IMG_QUERY_TYPE_NPS");
            if(npsQueryType!=null && "1".equals(npsQueryType.getParamValue())){
                showImgForNpBatch( responseBean, contentId, source, inputDate, fileName);
            }else{
                showImgForNp( responseBean, contentId, source, inputDate, fileName);
            }
            return;
        }

        HttpSession session = RequestUtil.getRequest().getSession();
        String url = "";
        ECMUtil ecmUtil =null;
        try {
            Object arsObj = session.getAttribute(moduleId+"_"+"arsBatchImg");
            if(arsObj!=null){
                Map<String,Object> arsImgMap = (Map)arsObj;
                if(arsImgMap.get("batchId")!=null && String.valueOf(arsImgMap.get("batchId")).equals(batchId)){
                    if(arsImgMap.get("urlGetTime")!=null && ((System.currentTimeMillis()-(long)arsImgMap.get("urlGetTime"))/60000> ARSConstants.URL_OVER_TIME)){
                        logger.info("批次："+batchId+"获取到的url已经超时，开始重新获取");
                        //获取图片信息
                        ecmUtil = getEcmUtil(siteNo,null);
                        if(ecmUtil == null){
                            logger.error("ecmUtil为空，机构数据表配置异常，机构号："+siteNo);
                            responseBean.setRetMsg("机构数据表配置异常，机构号："+siteNo);
                            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                            return;
                        }
                        contentId = arsImgMap.get("contentId")+"";
                        String filesXml = ecmUtil.queryBatch(contentId, inputDate);
                        arsImgMap = ecmUtil.parseXMLforUrls(filesXml,fileName);
                        arsImgMap.put("batchId", batchId);
                        arsImgMap.put("contentId", contentId);
                        arsImgMap.put("urlGetTime", System.currentTimeMillis());

                        FileUrlBean fileUrlBean = arsImgMap.get(fileName)==null?null:(FileUrlBean)arsImgMap.get(fileName);
                        if(fileUrlBean!=null){
                            url = fileUrlBean.getUrl();
                            flag = true;
                        }else{
                            logger.info("获取url出现异常");
                        }
                        session.setAttribute(moduleId+"_"+"arsBatchImg", arsImgMap);

                    }else{
                        FileUrlBean fileUrlBean = arsImgMap.get(fileName)==null?null:(FileUrlBean)arsImgMap.get(fileName);
                        if(fileUrlBean !=null && fileUrlBean.getUseTime()<ARSConstants.URL_USE_TIMES){//url未超过使用次数
                            url = fileUrlBean.getUrl();
                            fileUrlBean.setUseTime(fileUrlBean.getUseTime()+1);//url使用后次数+1
                            arsImgMap.put(fileName, fileUrlBean);
                            session.setAttribute(moduleId+"_"+"arsBatchImg", arsImgMap);
                            logger.info("本次url从session中获取");

                        }else{
                            fileUrlBean = new FileUrlBean();
                            //重新获取url
                            ecmUtil = getEcmUtil(siteNo,null);
                            if(ecmUtil == null){
                                logger.error("ecmUtil为空，机构数据表配置异常，机构号："+siteNo);
                                responseBean.setRetMsg("机构数据表配置异常，机构号："+siteNo);
                                responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                                return;
                            }
                            contentId = arsImgMap.get("contentId")+"";
                            String fileXml = ecmUtil.queryFile(contentId, inputDate, fileName);
                            url = ecmUtil.parseXMLforUrl(fileXml);
                            fileUrlBean.setUseTime(1);
                            fileUrlBean.setUrl(url);
                            fileUrlBean.setFileName(fileName);
                            arsImgMap.put(fileName, fileUrlBean);
                            session.setAttribute(moduleId+"_"+"arsBatchImg", arsImgMap);
                            logger.info("重新查询影像URL,fileName:"+fileName);
                        }
                        flag = true;
                    }
                }

            }

            if(!flag){
                logger.info("重新查询批次URL,batchId:"+batchId);
                ecmUtil = getEcmUtil(siteNo,null);
                if(ecmUtil == null){
                    logger.error("ecmUtil为空，机构数据表配置异常，机构号："+siteNo);
                    responseBean.setRetMsg("机构数据表配置异常，机构号："+siteNo);
                    responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                    return;
                }
                if(contentId==null || "".equals(contentId)){
                    //获取批次对象信息
                    String batchXml = ecmUtil.heightQuery(batchId, inputDate);
                    contentId = ecmUtil.parseHeightQueryXML(batchXml);
                }
                //获取图片信息
                String filesXml = ecmUtil.queryBatch(contentId, inputDate);
                Map<String,Object> arsImgMap = ecmUtil.parseXMLforUrls(filesXml,fileName);
                arsImgMap.put("batchId", batchId);
                arsImgMap.put("contentId", contentId);
                arsImgMap.put("urlGetTime", System.currentTimeMillis());

                FileUrlBean fileUrlBean = arsImgMap.get(fileName)==null?null:(FileUrlBean)arsImgMap.get(fileName);
                if(fileUrlBean !=null){
                    url = fileUrlBean.getUrl();
                    session.setAttribute(moduleId+"_"+"arsBatchImg", arsImgMap);
                }else{
                    responseBean.setRetMsg("从ECM获取图片URL出现异常");
                    responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                    return;
                }


            }

        } catch (Exception e) {
            logger.error("从ECM获取图片URL出现异常", e);
            responseBean.setRetMsg("从ECM获取图片URL出现异常");
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            return;
        }
        Map retMap = new HashMap();
        retMap.put("url",url);
        responseBean.setRetMap(retMap);
        responseBean.setRetMsg("查询影像信息成功");
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

    }


    /**
     * 无纸化看图(单张)
     * @param responseBean
     * @param contentId
     * @param source
     * @param inputDate
     * @param fileName
     * @throws Exception
     */
    private void showImgForNp(ResponseBean responseBean,String contentId,SCDatasource source,String inputDate,String fileName) throws Exception {
        ECMUtil ecmUtil=null;
        String url = "";
        try {
            //获取后督系统对应的ECM信息
            ecmUtil = getEcmUtil(null,source);
            if(ecmUtil == null){
                logger.error("ecmUtil为空，系统来源定义表，无纸化来源配置异常");
                responseBean.setRetMsg("系统来源定义表"+source.getDataSourceId()+"配置异常");
                responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                return;
            }
            //获取图片信息
            String fileXml = ecmUtil.queryFile(contentId, inputDate, fileName);
            url = ecmUtil.parseXMLforUrl(fileXml);
            logger.info("成功获取到url");
        }  catch (Exception e) {
            logger.error("从ECM获取图片URL出现异常", e);
            responseBean.setRetMsg("从ECM获取图片URL出现异常");
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            return;
        }
        Map retMap = new HashMap();
        retMap.put("url",url);
        responseBean.setRetMap(retMap);
        responseBean.setRetMsg("查询影像信息成功");
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);


    }


    /**
     * 无纸化看图(一笔业务)
     * @param responseBean
     * @param contentId
     * @param source
     * @param inputDate
     * @param fileName
     * @throws Exception
     */
    private void showImgForNpBatch(ResponseBean responseBean,String contentId,SCDatasource source,String inputDate,String fileName) throws Exception {
        HttpSession session = RequestUtil.getRequest().getSession();
        ECMUtil ecmUtil=null;
        String url = "";
        boolean flag = false;
        try {

            Object arsObj = session.getAttribute("nps_arsBatchImg");
            if(arsObj!=null){
                Map<String,Object> arsImgMap = (Map)arsObj;
                if(arsImgMap.get("contentId")!=null && String.valueOf(arsImgMap.get("contentId")).equals(contentId)){
                    if(arsImgMap.get("urlGetTime")!=null && ((System.currentTimeMillis()-(long)arsImgMap.get("urlGetTime"))/60000> ARSConstants.URL_OVER_TIME)){
                        logger.info("无纸化业务："+contentId+"获取到的url已经超时，开始重新获取");
                        //获取图片信息
                        ecmUtil = getEcmUtil(null,source);
                        if(ecmUtil == null){
                            logger.error("ecmUtil为空，系统来源定义表，无纸化来源配置异常");
                            responseBean.setRetMsg("系统来源定义表"+source.getDataSourceId()+"配置异常");
                            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                            return;
                        }
                        contentId = arsImgMap.get("contentId")+"";
                        String filesXml = ecmUtil.queryBatch(contentId, inputDate);
                        arsImgMap = ecmUtil.parseXMLforUrls(filesXml,fileName);
                        arsImgMap.put("contentId", contentId);
                        arsImgMap.put("urlGetTime", System.currentTimeMillis());

                        FileUrlBean fileUrlBean = arsImgMap.get(fileName)==null?null:(FileUrlBean)arsImgMap.get(fileName);
                        if(fileUrlBean!=null){
                            url = fileUrlBean.getUrl();
                            flag = true;
                        }else{
                            logger.info("获取url出现异常");
                        }
                        session.setAttribute("nps_arsBatchImg", arsImgMap);

                    }else{
                        FileUrlBean fileUrlBean = arsImgMap.get(fileName)==null?null:(FileUrlBean)arsImgMap.get(fileName);
                        if(fileUrlBean !=null && fileUrlBean.getUseTime()<ARSConstants.URL_USE_TIMES){//url未超过使用次数
                            url = fileUrlBean.getUrl();
                            fileUrlBean.setUseTime(fileUrlBean.getUseTime()+1);//url使用后次数+1
                            arsImgMap.put(fileName, fileUrlBean);
                            session.setAttribute("nps_arsBatchImg", arsImgMap);
                            logger.info("本次url从session中获取");

                        }else{
                            fileUrlBean = new FileUrlBean();
                            //重新获取url
                            ecmUtil = getEcmUtil(null,source);
                            if(ecmUtil == null){
                                logger.error("ecmUtil为空，系统来源定义表，无纸化来源配置异常");
                                responseBean.setRetMsg("系统来源定义表"+source.getDataSourceId()+"配置异常");
                                responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                                return;
                            }
                            contentId = arsImgMap.get("contentId")+"";
                            String fileXml = ecmUtil.queryFile(contentId, inputDate, fileName);
                            url = ecmUtil.parseXMLforUrl(fileXml);
                            fileUrlBean.setUseTime(1);
                            fileUrlBean.setUrl(url);
                            fileUrlBean.setFileName(fileName);
                            arsImgMap.put(fileName, fileUrlBean);
                            session.setAttribute("nps_arsBatchImg", arsImgMap);
                            logger.info("重新查询影像URL,fileName:"+fileName);
                        }
                        flag = true;
                    }
                }

            }

            if(!flag){
                logger.info("重新查询无纸化业务URL,contentId:"+contentId);
                ecmUtil = getEcmUtil(null,source);
                if(ecmUtil == null){
                    logger.error("ecmUtil为空，系统来源定义表，无纸化来源配置异常");
                    responseBean.setRetMsg("系统来源定义表"+source.getDataSourceId()+"配置异常");
                    responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                    return;
                }
                //获取图片信息
                String filesXml = ecmUtil.queryBatch(contentId, inputDate);
                Map<String,Object> arsImgMap = ecmUtil.parseXMLforUrls(filesXml,fileName);
                arsImgMap.put("contentId", contentId);
                arsImgMap.put("urlGetTime", System.currentTimeMillis());

                FileUrlBean fileUrlBean = arsImgMap.get(fileName)==null?null:(FileUrlBean)arsImgMap.get(fileName);
                if(fileUrlBean !=null){
                    url = fileUrlBean.getUrl();
                    session.setAttribute("nps_arsBatchImg", arsImgMap);
                }else{
                    responseBean.setRetMsg("从ECM获取图片URL出现异常");
                    responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                    return;
                }


            }


        }  catch (Exception e) {
            logger.error("从ECM获取图片URL出现异常", e);
            responseBean.setRetMsg("从ECM获取图片URL出现异常");
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            return;
        }
        Map retMap = new HashMap();
        retMap.put("url",url);
        responseBean.setRetMap(retMap);
        responseBean.setRetMsg("查询影像信息成功");
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);


    }




    /**
     * 获取预警看图信息
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    private void queryArmsImageInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
        Map sysMap = requestBean.getSysMap();
        String entryId = BaseUtil.filterSqlParam((String)sysMap.get("entryId"));
        Integer entryrowId = (Integer)sysMap.get("entryrowId");
        entryrowId = Integer.valueOf(BaseUtil.filterSqlParam(entryrowId+""));


        //查询模型看图分组信息
        QueryImg queryImg = new QueryImg();
        queryImg.setMid(Integer.parseInt(entryId));
        List<QueryImg> queryImgList = queryImgMapper.selectBySelective(queryImg);

        //判断模型看图分组配置
        if(queryImgList.size() == 0) {
            logger.error("未配置看图分组信息，无法看图");
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("未配置看图分组信息，无法看图");
            return ;
        }

        //获取模型信息
        Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(entryId));
        //获取该笔模型的信息
        Map<String, Object> modelData = modelDataQueryMapper.getThisModelData(Integer.valueOf(entryId), entryrowId, BaseUtil.filterSqlParam(modelInfo.getTableName()), null);
        if(null == modelData){
            modelData = modelDataQueryMapper.getThisModelData(Integer.valueOf(entryId), entryrowId, BaseUtil.filterSqlParam(modelInfo.getTableName()+"_HIS"), null);
            modelInfo.setTableName(modelInfo.getTableName()+"_HIS");
        }


        Map<Integer, StringBuffer> flowSQLMap = new HashMap<Integer, StringBuffer>();

        //分组组装查询流水sql
        for(QueryImg qImg : queryImgList){
            if(modelData.get(qImg.getName()) != null && !"null".equals(modelData.get(qImg.getName()).toString()) && !"".equals(modelData.get(qImg.getName()).toString())) {
                if(!flowSQLMap.containsKey(qImg.getGroupId())) {
                    if(isNumeric(modelData.get(qImg.getName()))) {
                        flowSQLMap.put(qImg.getGroupId(), new StringBuffer("select distinct t3.BATCH_ID, t3.OCCUR_DATE, t3.SITE_NO, t3.OPERATOR_NO, t3.CONTENT_ID, t3.INPUT_DATE, t2.FLOW_ID, t4.INCCODEIN_BATCH, t4.SERIAL_NO, o.ORGAN_NAME, t.TELLER_NAME from " + BaseUtil.filterSqlParam(modelInfo.getTableName()) + " t1, FL_FLOW_TB t2, BP_TMPBATCH_TB t3, BP_TMPDATA_1_TB t4, SM_ORGAN_TB o, SM_TELLER_TB t  where t2.OCCUR_DATE=t3.OCCUR_DATE and t2.SITE_NO=t3.SITE_NO and t2.OPERATOR_NO=t3.OPERATOR_NO and t3.BATCH_ID=t4.BATCH_ID and t2.LSERIAL_NO=t4.SERIAL_NO and t3.SITE_NO=o.ORGAN_NO and t3.OPERATOR_NO=t.TELLER_NO and t1." + BaseUtil.filterSqlParam(qImg.getName()) + "=t2." + BaseUtil.filterSqlParam(qImg.getMapName()) + " and t1." + BaseUtil.filterSqlParam(qImg.getName()) + "=" + BaseUtil.filterSqlParam(modelData.get(qImg.getName())+"")));
                    }else
                    {
                        flowSQLMap.put(qImg.getGroupId(), new StringBuffer("select distinct t3.BATCH_ID, t3.OCCUR_DATE, t3.SITE_NO, t3.OPERATOR_NO, t3.CONTENT_ID, t3.INPUT_DATE, t2.FLOW_ID, t4.INCCODEIN_BATCH, t4.SERIAL_NO, o.ORGAN_NAME, t.TELLER_NAME from " + BaseUtil.filterSqlParam(modelInfo.getTableName()) + " t1, FL_FLOW_TB t2, BP_TMPBATCH_TB t3, BP_TMPDATA_1_TB t4, SM_ORGAN_TB o, SM_TELLER_TB t where t2.OCCUR_DATE=t3.OCCUR_DATE and t2.SITE_NO=t3.SITE_NO and t2.OPERATOR_NO=t3.OPERATOR_NO and t3.BATCH_ID=t4.BATCH_ID and t2.LSERIAL_NO=t4.SERIAL_NO and t3.SITE_NO=o.ORGAN_NO and t3.OPERATOR_NO=t.TELLER_NO and t1." + BaseUtil.filterSqlParam(qImg.getName()) + "=t2." + BaseUtil.filterSqlParam(qImg.getMapName()) + " and t1." + BaseUtil.filterSqlParam(qImg.getName()) + "='" + BaseUtil.filterSqlParam(modelData.get(qImg.getName())+"") + "'"));
                    }
                }else
                {
                    if(isNumeric(modelData.get(qImg.getName()))) {
                        flowSQLMap.get(qImg.getGroupId()).append(" and t1." + BaseUtil.filterSqlParam(qImg.getName()) + "=t2." + BaseUtil.filterSqlParam(qImg.getMapName()) + " and t1." + BaseUtil.filterSqlParam(qImg.getName()) + "=" + BaseUtil.filterSqlParam(modelData.get(qImg.getName())+""));
                    }else
                    {
                        flowSQLMap.get(qImg.getGroupId()).append(" and t1." + BaseUtil.filterSqlParam(qImg.getName()) + "=t2." + BaseUtil.filterSqlParam(qImg.getMapName()) + " and t1." + BaseUtil.filterSqlParam(qImg.getName()) + "='" + BaseUtil.filterSqlParam(modelData.get(qImg.getName())+"") + "'");
                    }
                }
            }
        }

        //组装最终查询sql
        if(flowSQLMap.size()==0){
            //配置存在异常，可能导致无法正常组装sql
            logger.error("组装查询sql失败，请检查看图分组配置");
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("组装查询sql失败，请检查看图分组配置");
            return ;
        }


        StringBuffer querySQL = new StringBuffer();
        for(Integer key : flowSQLMap.keySet()){
            querySQL.append(flowSQLMap.get(key));
            querySQL.append(" union ");
        }
        querySQL.delete(querySQL.lastIndexOf(" union "), querySQL.length());
        querySQL.insert(0, "select * from (").append(") order by BATCH_ID,INCCODEIN_BATCH");

        //执行sql查询
        boolean isHis = false;
        List<Map<String, Object>> infoMapList = commonMapper.executeSelect(querySQL.toString());
        if(infoMapList==null ||infoMapList.size()==0){
            logger.info("临时流水表中未能查询到流水数据，开始查询历史流水表");
            infoMapList = commonMapper.executeSelect(querySQL.toString().replace("FL_FLOW_TB","FL_FLOW_TB_HIS").replace("BP_TMPDATA_1_TB","BP_TMPDATA_1_TB_HIS"));
            if(infoMapList==null ||infoMapList.size()==0){
                logger.info("临时流水表，历史流水表中均未能查询到流水数据，无法看图");
                responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                responseBean.setRetMsg("未查询到相应的流水信息，无法看图");
                return ;
            }
            isHis = true;
        }


        //循环获取批次图像信息
        //获取流水展现字段
        List<HashMap<String, Object>> flowFields = smFieldDefTbMapper.getFieldsByTableId(3);
        ArrayList<TmpData> tmpDataList = new ArrayList<TmpData>();
        List<TmpData> tmpData = null;
        List<HashMap<String, Object>> flowList = new ArrayList<>();
        List<HashMap<String, Object>> flow = null;
        List<TmpBatch> batchList = new ArrayList<TmpBatch>();
        TmpBatch batch = null;
        String batchId = null;
        for (Map<String, Object> infoMap : infoMapList) {
            if(batchId == null || !((String) infoMap.get("BATCH_ID")).equals(batchId)) {
                batchId = (String) infoMap.get("BATCH_ID");
                batch = new TmpBatch();
                batch.setBatchId(batchId);
                batch.setOccurDate((String) infoMap.get("OCCUR_DATE"));
                batch.setSiteNo((String) infoMap.get("SITE_NO") + " - " + (String) infoMap.get("ORGAN_NAME"));
                batch.setOperatorNo((String) infoMap.get("OPERATOR_NO") + " - " + (String) infoMap.get("TELLER_NAME"));
                batch.setInputDate((String)infoMap.get("INPUT_DATE"));
                batch.setContentId((String)infoMap.get("CONTENT_ID"));
                batchList.add(batch);
            }
            //获取图像主附件
            tmpData = getTmpDatas(isHis,batchId,infoMap.get("INCCODEIN_BATCH"));
            //获取对应流水信息
            flow = getFlows(flowFields,isHis, String.valueOf(infoMap.get("SERIAL_NO")),(String)infoMap.get("OCCUR_DATE"),(String) infoMap.get("SITE_NO"),(String) infoMap.get("OPERATOR_NO"));
            flowList.addAll(flow);
            tmpDataList.addAll(tmpData);
            tmpData=null;
            flow=null;
        }

        Map retMap = new HashMap();
        retMap.put("batchList",batchList);
        retMap.put("tmpDataList",tmpDataList);
        retMap.put("flowList",flowList);
        retMap.put("flowFields",flowFields);

        responseBean.setRetMap(retMap);
        responseBean.setRetMsg("查询影像信息成功");
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

    }


    /**
     * 获取影像列表
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    private void showImgList(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        {
            Map<String,Object> sysMap = requestBean.getSysMap();
            List tmpDataList = (List)sysMap.get("imageList");
            String batchId = "";
            String inputDate = "";
            String fileName = "";
            String contentId = "";
            String siteNo = "";
            String backFileName="";
            String ecmUtilOrgan="";
            ECMUtil ecmUtil =null;
            List<Map> urlList = new ArrayList<>();
            Map<String,Object> urlMap = null;

            Map<Object,List> batchMap = new HashMap<Object, List>();
            List<Map> imageList =null;

            //整理影像数据列表
            if(tmpDataList!=null && tmpDataList.size()>0){
                for(int j=0;j<tmpDataList.size();j++){
                    Map map = (Map)tmpDataList.get(j);
                    if(batchMap.get(map.get("BATCH_ID"))!=null){
                        batchMap.get(map.get("BATCH_ID")).add(map);
                    }else{
                        imageList = new ArrayList<>();
                        imageList.add(map);
                        batchMap.put(map.get("BATCH_ID"),imageList);
                    }
                }

                //迭代batchMap,获取影像url
                for(Object key : batchMap.keySet()){
                    imageList = batchMap.get(key);
                    for(int i=0;i<imageList.size();i++){
                        if(i==0){
                            siteNo = (String) imageList.get(i).get("SITE_NO");
                            if(!ecmUtilOrgan.equals(siteNo)){
                                ecmUtilOrgan = siteNo;
                                ecmUtil = getEcmUtil(siteNo,null);
                            }
                            inputDate = (String) imageList.get(i).get("INPUT_DATE");
                            contentId = (String) imageList.get(i).get("CONTENT_ID");
                            if(StringUtil.checkNull(contentId)){
                                //可能存在contentId未能成功反写的批次，重新进行查询
                                if(ecmUtil != null){
                                    String batchXml = ecmUtil.heightQuery(batchId, inputDate);
                                    contentId = ecmUtil.parseHeightQueryXML(batchXml);
                                }else{
                                    logger.error("ecmUtil为空，机构数据表配置异常，机构号："+siteNo);
                                    responseBean.setRetMsg("机构数据表配置异常，机构号："+siteNo);
                                    responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                                    return;
                                }

                            }
                        }
                        fileName = (String) imageList.get(i).get("FILE_NAME");
                        backFileName = (String) imageList.get(i).get("BACK_FILE_NAME");

                        if(ecmUtil != null){
                            //查询url
                            String fileXml = ecmUtil.queryFile(contentId, inputDate, fileName);
                            String backFileXml = ecmUtil.queryFile(contentId, inputDate, backFileName);

                            urlMap = new HashMap<>();
                            urlMap.put("fileUrl",ecmUtil.parseXMLforUrl(fileXml));
                            urlMap.put("backFileUrl",ecmUtil.parseXMLforUrl(backFileXml));
                            urlList.add(urlMap);
                            logger.info("成功获取到url");
                        }else{
                            logger.error("ecmUtil为空，机构数据表配置异常，机构号："+siteNo);
                            responseBean.setRetMsg("机构数据表配置异常，机构号："+siteNo);
                            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                            return;
                        }
                    }

                }
            }

            Map<Object,Object> retMap = new HashMap<>();
            retMap.put("urlList",urlList);
            responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
            responseBean.setRetMsg("获取url成功");
            responseBean.setRetMap(retMap);
        }
    }


    /**
     * 获取ecmUtil对象
     * @param siteNo
     * @return
     */
    private ECMUtil getEcmUtil(String siteNo,SCDatasource source)throws Exception{
        if(source==null){
            siteNo = BaseUtil.filterHeader(siteNo);
            //获取后督系统对应的ECM信息
            OrganData organData = new OrganData();
            organData.setOrganNo(siteNo);
            List<OrganData> selectBySelective = organDataMapper.selectBySelective(organData);
            if(selectBySelective==null||selectBySelective.size()==0){
                return null;
            }
            String  dataSId = selectBySelective.get(0).getEcmserviceId();
            if(dataSId==null||dataSId.equals("")){
                return null;
            }
            dataSId = BaseUtil.filterHeader(dataSId);
            source = scDatasourceMapper.selectByPrimaryKey(dataSId);
        }


        String serviceId = BaseUtil.filterSqlParam(source.getServiceId());
        ServiceReg serviceReg = serviceRegMapper.selectByPrimaryKey(new BigDecimal(serviceId));

        ECMUtil ecmUtil  = new ECMUtil(serviceReg.getServiceIp(),Integer.parseInt(serviceReg.getServicePort()),serviceReg.getLoginName(),serviceReg.getLoginPass());
        ecmUtil.set(source.getGroupName(), source.getModeCode(),source.getFilePartName(), source.getIndexName());

        return ecmUtil;
    }

    /**
     * 查询图像信息
     * @param isHis
     * @param batchId
     * @param inccodeinBatch
     * @return
     */
    private List getTmpDatas(boolean isHis,String batchId,Object inccodeinBatch)throws Exception{
        HashMap<String,Object> codeMap = new HashMap();
        if(isHis){
            codeMap.put("tableName","BP_TMPDATA_1_TB_HIS");
        }else{
            codeMap.put("tableName","BP_TMPDATA_1_TB");
        }
        codeMap.put("batchId",BaseUtil.filterSqlParam(batchId));
        codeMap.put("inccodeinBatch",BaseUtil.filterSqlParam(inccodeinBatch+""));
        List tmpDatas = tmpDataMapper.selByInccodein(codeMap);
        return tmpDatas;
    }




    /**
     * 查询流水信息
     * @param flowFields
     * @param isHis
     * @param serialNo
     * @return
     */
    private List getFlows(List<HashMap<String,Object>> flowFields,boolean isHis,String serialNo,String occurDate,String siteNo,String operatorNo)throws Exception{
        StringBuffer sb = new StringBuffer();
        for (HashMap fieldMap: flowFields) {
            sb.append(fieldMap.get("FIELD_NAME") + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        String flowTb ="FL_FLOW_TB";
        if(isHis){
            flowTb = flowTb+"_HIS";
        }
        HashMap<String,Object> codeMap = new HashMap<>();
        codeMap.put("flowQueryFields",sb);
        codeMap.put("flowTb",flowTb);
        codeMap.put("serialNo",serialNo);
        codeMap.put("occurDate",occurDate);
        codeMap.put("siteNo",siteNo);
        codeMap.put("operatorNo",operatorNo);
        List<HashMap<String, Object>> returnList =  flowMapper.getFlowInfoList(codeMap);
        return returnList;
    }


    private boolean isNumeric(Object o) {
        if(o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float) {
            return true;
        }
        return false;
    }
}
