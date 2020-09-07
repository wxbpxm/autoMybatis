/*
* Copyright (c) 2016,2021 云南空港航空食品有限公司. All rights reserved.
* create time Tue Oct 18 13:18:24 CST 2016
*
*/
package fuku.automybatis.bean;

public class BusinessView {
    /**
     *说明：主键
     */
    private String id;

    /**
     *说明：模型id
     */
    private String businessId;

    /**
     *说明：页面类型 00:新增页面 10:修改页面 20:审批页面　30:查看页面
     */
    private String viewType;

    /**
     *说明：页面字段名，必须包含在pojo内
     */
    private String columnName;

    /**
     *说明：组件类型
     */
    private String widget;

    /**
     *说明：排序字段
     */
    private Integer sort;

    /**
     *说明：mongodb及pojo的字段说明
     */
    private String columnRemark;

    /**
     *说明：00:独占一行　10:占半行
     */
    private String style;

    /**
     *说明：组件配置
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

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getColumnRemark() {
        return columnRemark;
    }

    public void setColumnRemark(String columnRemark) {
        this.columnRemark = columnRemark;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}