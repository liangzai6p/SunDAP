package com.sunyard.ars.risk.bean.arms;

/**
 * 转差错需要的属性字段
 */
public class SendSlipBean {


    private String bankcode;
    private String bankname;
    private String teller; //警报信息处理人
    private String tellerName;
    private String transdate;
    private String ac_no;
    private String ac_name;
    private String amt;
    private String currency_type;
    private String flow_id;
    private String entry_id;
    private String entry_name;
    private String entryrow_id;
    private String table_name;
    private String tx_code;
    private String tx_name;
    private String remark;
    private String userNo;
    private String userName;
    private String exhibitInfo;
    private String backDate;
    private String workDate;
    private String workTime;
    private String entryLevel;
    private String interiorFormId;//INTERIOR_FORM_ID内部差错ID
    private String dealResultInfo;//警报信息处理结果
    private String alarmCreateTime; //预警系统：警报信息生成时间
    private String riskOrgan;//警报信息风险机构
    private String monitorId; //警报信息的警报单号；
    private String monitorOrgan; //再监督监控机构;
    private String deal_state;//转发处理结果
    private String proc_date;//预警提取日期

    //dadong20180319 模型中的预警数据创建日期、时间：create_date_new格式：20180315 12:12:12  proc_date_new格式：20180315
    private String create_date_new;
    private String proc_date_new;

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate;
    }

    public String getEntryLevel() {
        return entryLevel;
    }

    public void setEntryLevel(String entryLevel) {
        this.entryLevel = entryLevel;
    }

    public void setCurrency_type(String currency_type){
        this.currency_type=currency_type;
    }

    public String getCurrency_type(){
        return this.currency_type;
    }

    public void setTable_name(String table_name){
        this.table_name=table_name;
    }

    public String getTable_name(){
        return this.table_name;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserNo(String userNo){
        this.userNo=userNo;
    }

    public String getUserNo(){
        return this.userNo;
    }

    public void setRemark(String remark){
        this.remark=remark;
    }

    public String getRemark(){
        return this.remark;
    }

    public void setTx_name(String tx_name){
        this.tx_name=tx_name;
    }

    public String getTx_name(){
        return this.tx_name;
    }

    public void setTx_code(String tx_code){
        this.tx_code=tx_code;
    }

    public String getTx_code(){
        return this.tx_code;
    }

    public void setEntryrow_id(String entryrow_id){
        this.entryrow_id=entryrow_id;
    }

    public String getEntryrow_id(){
        return this.entryrow_id;
    }

    public void setEntry_name(String entry_name){
        this.entry_name=entry_name;
    }

    public String getEntry_name(){
        return this.entry_name;
    }

    public void setEntry_id(String entry_id){
        this.entry_id=entry_id;
    }

    public String getEntry_id(){
        return this.entry_id;
    }

    public void setFlow_id(String flow_id){
        this.flow_id=flow_id;
    }

    public String getFlow_id(){
        return this.flow_id;
    }

    public void setAmt(String amt){
        this.amt=amt;
    }

    public String getAmt(){
        return this.amt;
    }

    public void setAc_name(String ac_name){
        this.ac_name=ac_name;
    }

    public String getAc_name(){
        return this.ac_name;
    }

    public void setAc_no(String ac_no){
        this.ac_no=ac_no;
    }

    public String getAc_no(){
        return this.ac_no;
    }

    public void setTransdate(String transdate){
        this.transdate=transdate;
    }

    public String getTransdate(){
        return this.transdate;
    }

    public void setTeller(String teller){
        this.teller=teller;
    }

    public String getTeller(){
        return this.teller;
    }

    public void setTellerName(String tellerName){
        this.tellerName=tellerName;
    }

    public String getTellerName(){
        return this.tellerName;
    }

    public void setBankname(String bankname){
        this.bankname=bankname;
    }

    public String getBankname(){
        return this.bankname;
    }

    public void setBankcode(String bankcode){
        this.bankcode=bankcode;
    }

    public String getBankcode(){
        return this.bankcode;
    }

    public static void main(String [] args)
    {
    }

    public String getExhibitInfo() {
        return exhibitInfo;
    }

    public void setExhibitInfo(String exhibitInfo) {
        this.exhibitInfo = exhibitInfo;
    }

    public String getInteriorFormId() {
        return interiorFormId;
    }

    public void setInteriorFormId(String interiorFormId) {
        this.interiorFormId = interiorFormId;
    }

    public String getAlarmCreateTime() {
        return alarmCreateTime;
    }

    public void setAlarmCreateTime(String alarmCreateTime) {
        this.alarmCreateTime = alarmCreateTime;
    }

    public String getDealResultInfo() {
        return dealResultInfo;
    }

    public void setDealResultInfo(String dealResultInfo) {
        this.dealResultInfo = dealResultInfo;
    }

    public String getRiskOrgan() {
        return riskOrgan;
    }

    public void setRiskOrgan(String riskOrgan) {
        this.riskOrgan = riskOrgan;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getMonitorOrgan() {
        return monitorOrgan;
    }

    public void setMonitorOrgan(String monitorOrgan) {
        this.monitorOrgan = monitorOrgan;
    }

    public String getDeal_state() {
        return deal_state;
    }

    public void setDeal_state(String deal_state) {
        this.deal_state = deal_state;
    }

    public String getProc_date() {
        return proc_date;
    }

    public void setProc_date(String proc_date) {
        this.proc_date = proc_date;
    }

    public String getCreate_date_new() {
        return create_date_new;
    }

    public void setCreate_date_new(String create_date_new) {
        this.create_date_new = create_date_new;
    }

    public String getProc_date_new() {
        return proc_date_new;
    }

    public void setProc_date_new(String proc_date_new) {
        this.proc_date_new = proc_date_new;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }
}
