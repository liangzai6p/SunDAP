package com.sunyard.ars.system.pojo.mc;

/**
 * 机构模型表
 * MC_ORGAN_MODEL_TB
 *
 */
public class OrganModel {

	/**
	 * 编号
	 */
    private Integer id;

	/**
	 * 交易机构
	 */
    private String organNo;

    /**
     * 模型编号
     */
    private Integer modelId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganNo() {
        return organNo;
    }

    public void setOrganNo(String organNo) {
        this.organNo = organNo == null ? null : organNo.trim();
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
}