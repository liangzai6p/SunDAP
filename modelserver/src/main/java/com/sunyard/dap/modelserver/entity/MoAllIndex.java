package com.sunyard.dap.modelserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@TableName("MO_ALL_INDEX")
@Data
public class MoAllIndex extends Model<MoAllIndex> {

    private static final long serialVersionUID=1L;

    /**
     * 指标名
     */
    @TableField("INDEX_NAME")
    private String indexName;

    /**
     * 指标表名
     */
    @TableField("INDEX_TABLE_NAME")
    private String indexTableName;

    /**
     * 一级分类
     */
    @TableField("FIRST_CLASSIFICATION")
    private String firstClassification;

    /**
     * 二级分类
     */
    @TableField("SECOND_CLASSIFICATION")
    private String secondClassification;

    /**
     * 指标描述
     */
    @TableField("INDEX_DESCRIBE")
    private String indexDescribe;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
