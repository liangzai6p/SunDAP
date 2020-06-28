package com.sunyard.ars.system.pojo.mc;

public class LabMcVersion {
    
    private Integer id;
        
    private Integer modelId;
	private Integer tableId;
	
    private Integer versionId;
   
    private String versionName;
    
    private String modelName;
    
    private String modelDescription;
    
    private Integer produceModelId;
    
    private String labState;
    
    private String versionSql;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getModelId() {
        return modelId;
    }

    
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    
    public Integer getVersionId() {
        return versionId;
    }

    
    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    
    public String getVersionName() {
        return versionName;
    }

    
    public void setVersionName(String versionName) {
        this.versionName = versionName == null ? null : versionName.trim();
    }

    
    public String getModelName() {
        return modelName;
    }

    
    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    
    public String getModelDescription() {
        return modelDescription;
    }

    
    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription == null ? null : modelDescription.trim();
    }

    
    public Integer getProduceModelId() {
        return produceModelId;
    }

    
    public void setProduceModelId(Integer produceModelId) {
        this.produceModelId = produceModelId;
    }

    
    public String getLabState() {
        return labState;
    }

    
    public void setLabState(String labState) {
        this.labState = labState == null ? null : labState.trim();
    }
    
    public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}


	public String getVersionSql() {
		return versionSql;
	}


	public void setVersionSql(String versionSql) {
		this.versionSql = versionSql;
	}

	
	
}