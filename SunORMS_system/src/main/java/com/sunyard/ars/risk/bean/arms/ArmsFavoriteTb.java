package com.sunyard.ars.risk.bean.arms;


import java.util.Date;

/**
 * 预警案例库表
 */
public class ArmsFavoriteTb {
    private String id;
    private String type;
    private String entryId;
    private String entryrowId;
    private String formId;
    private String favoriteTitle;
    private Date favoriteTime;
    private String favoriteUser;
    private String favoriteOrgan;
    private String remark;
    private String remarkCommon;
    private String iscommon;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }


    public String getEntryId() {
        return entryId;
    }


    public void setEntryId(String entryId) {
        this.entryId = entryId == null ? null : entryId.trim();
    }

    public String getEntryrowId() {
        return entryrowId;
    }

    public void setEntryrowId(String entryrowId) {
        this.entryrowId = entryrowId == null ? null : entryrowId.trim();
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId == null ? null : formId.trim();
    }

    public String getFavoriteTitle() {
        return favoriteTitle;
    }

    public void setFavoriteTitle(String favoriteTitle) {
        this.favoriteTitle = favoriteTitle == null ? null : favoriteTitle.trim();
    }

    public Date getFavoriteTime() {
        return favoriteTime;
    }

    public void setFavoriteTime(Date favoriteTime) {
        this.favoriteTime = favoriteTime;
    }

    public String getFavoriteUser() {
        return favoriteUser;
    }

    public void setFavoriteUser(String favoriteUser) {
        this.favoriteUser = favoriteUser == null ? null : favoriteUser.trim();
    }

    public String getFavoriteOrgan() {
        return favoriteOrgan;
    }

    public void setFavoriteOrgan(String favoriteOrgan) {
        this.favoriteOrgan = favoriteOrgan == null ? null : favoriteOrgan.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    public String getRemarkCommon() {
        return remarkCommon;
    }

    public void setRemarkCommon(String remarkCommon) {
        this.remarkCommon = remarkCommon == null ? null : remarkCommon.trim();
    }
    public String getIscommon() {
        return iscommon;
    }
    public void setIscommon(String iscommon) {
        this.iscommon = iscommon == null ? null : iscommon.trim();
    }
}
