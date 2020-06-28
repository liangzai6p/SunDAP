package com.sunyard.ars.system.pojo.mc;

/**
 * 
 *预警模型看图分组配置表
 *MC_QUERY_IMG_TB
 */
public class QueryImg {
	
	/**
	 * 序号
	 */
    private Integer id;

	/**
	 * 模型号
	 */
    private Integer mid;

    /**
     * 组号
     */
    private Integer groupId;

    /**
     * 组内序号
     */
    private Integer seqno;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 关联字段名称
     */
    private String mapName;

    /**
     * 表类型
     */
    private String type;

    /**
     * 存储表ID
     */
    private Integer tableId;

    /**
     * 模型名称
     */
    private String mcName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getSeqno() {
        return seqno;
    }

    public void setSeqno(Integer seqno) {
        this.seqno = seqno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName == null ? null : mapName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getMcName() {
        return mcName;
    }

    public void setMcName(String mcName) {
        this.mcName = mcName == null ? null : mcName.trim();
    }
}