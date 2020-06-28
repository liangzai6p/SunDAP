package com.sunyard.ars.system.pojo.mc;

/**
 * 视图设置表
 *	MC_VIEW_TB
 */
public class View {

	/**
	 * 视图编号
	 */
    private Integer id;

	/**
	 * 名称
	 */
    private String name;

    /**
     * 相应的预警项的ID
     */
    private Integer modelId;

    /**
     * 数据源ID
     */
    private Integer dsId;

    /**
     * 组合类型0：纵向插入1：横向插入2：记录更新3：删除4：自定义5:临时视图
     */
    private String compositeType;

    /**
     * 是否分组0：不分组1：分组
     */
    private String isgroupby;

    /**
     * SQL语句
     */
    private String queryStr;

    /**
     * 序号
     */
    private Integer seqno;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getDsId() {
        return dsId;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }

    public String getCompositeType() {
        return compositeType;
    }

    public void setCompositeType(String compositeType) {
        this.compositeType = compositeType == null ? null : compositeType.trim();
    }

    public String getIsgroupby() {
        return isgroupby;
    }

    public void setIsgroupby(String isgroupby) {
        this.isgroupby = isgroupby == null ? null : isgroupby.trim();
    }

    public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr == null ? null : queryStr.trim();
    }

    public Integer getSeqno() {
        return seqno;
    }

    public void setSeqno(Integer seqno) {
        this.seqno = seqno;
    }
}