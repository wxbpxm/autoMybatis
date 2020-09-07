/*
* Copyright (c) 2016,2021 云南空港航空食品有限公司. All rights reserved.
* create time Tue Oct 18 16:29:38 CST 2016
*
*/
package fuku.automybatis.bean;

public class BusinessValidation {
    /**
     *说明：主键
     */
    private String id;

    /**
     *说明：模型id
     */
    private String businessId;

    /**
     *说明：页面类型 00:新增页面 10:修改页面 20:审批页面　30:查看页面 40:所有页面
     */
    private String viewType;

    /**
     *说明：页面字段名，必须包含在pojo内
     */
    private String columnName;

    /**
     *说明：字段说明
     */
    private String columnRemark;

    /**
     *说明：校验类名称
     */
    private String clazz;

    /**
     *说明：排序字段
     */
    private Integer sort;

    /**
     *说明：校验类配置
     */
    private String param;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnRemark() {
        return columnRemark;
    }

    public void setColumnRemark(String columnRemark) {
        this.columnRemark = columnRemark;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}