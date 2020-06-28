package com.sunyard.ars.system.pojo.mc;

import java.io.Serializable;
public class McAutoyjFieldTb implements Serializable {
    private String modelId;

    private String netNo;

    private String tellerNo;

    private String operationDate;

    private String accountNo;

    private String accountName;

    private String accountMoney;

    private String voucherNo;

    private String voucherDate;

    private String flowNo;

    private String modelName;

    private String modelrowId;

    private String currencyType;

    private static final long serialVersionUID = 1L;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getNetNo() {
        return netNo;
    }

    public void setNetNo(String netNo) {
        this.netNo = netNo;
    }

    public String getTellerNo() {
        return tellerNo;
    }

    public void setTellerNo(String tellerNo) {
        this.tellerNo = tellerNo;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(String accountMoney) {
        this.accountMoney = accountMoney;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelrowId() {
        return modelrowId;
    }

    public void setModelrowId(String modelrowId) {
        this.modelrowId = modelrowId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", modelId=").append(modelId);
        sb.append(", netNo=").append(netNo);
        sb.append(", tellerNo=").append(tellerNo);
        sb.append(", operationDate=").append(operationDate);
        sb.append(", accountNo=").append(accountNo);
        sb.append(", accountName=").append(accountName);
        sb.append(", accountMoney=").append(accountMoney);
        sb.append(", voucherNo=").append(voucherNo);
        sb.append(", voucherDate=").append(voucherDate);
        sb.append(", flowNo=").append(flowNo);
        sb.append(", modelName=").append(modelName);
        sb.append(", modelrowId=").append(modelrowId);
        sb.append(", currencyType=").append(currencyType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}