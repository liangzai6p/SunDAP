package com.sunyard.ars.system.pojo.mc;

/**
 * 表定义表
 * MC_TABLE_TB
 */
public class McTable {

	/**
	 * 编号
	 */
    private Integer id;

	/**
	 * 数据源ID
	 */
    private Integer dsId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 描述
     */
    private String tableDesc;

    /**
     * 表类型0:参数表 1:配置中间表 2:预警展现表 3:重点监督表 
     */
    private String tableType;

    /**
     * 表空间
     */
    private String tableSpace;

    /**
     * 索引空间
     */
    private String indexSpace;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDsId() {
        return dsId;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc == null ? null : tableDesc.trim();
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType == null ? null : tableType.trim();
    }

    public String getTableSpace() {
        return tableSpace;
    }

    public void setTableSpace(String tableSpace) {
        this.tableSpace = tableSpace == null ? null : tableSpace.trim();
    }

    public String getIndexSpace() {
        return indexSpace;
    }

    public void setIndexSpace(String indexSpace) {
        this.indexSpace = indexSpace == null ? null : indexSpace.trim();
    }
}