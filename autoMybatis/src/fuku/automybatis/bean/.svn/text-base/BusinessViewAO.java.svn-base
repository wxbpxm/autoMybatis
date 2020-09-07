/*
* Copyright (c) 2016,2021 云南空港航空食品有限公司. All rights reserved.
* create time Mon Oct 17 09:25:12 CST 2016
*
*/
package fuku.automybatis.bean;

import java.io.Serializable;

public class BusinessViewAO extends BusinessView implements Serializable {
    private static final long serialVersionUID = 2083963729717106998L;
    
    /** 组件  文本：text */
    public static final String WIDGET_TEXT = "text";
    /** 组件  文本域：textarea */
    public static final String WIDGET_TEXTAREA = "textarea";
    /** 组件  日期：date */
    public static final String WIDGET_DATE = "date";
    /** 组件  时间：time */
    public static final String WIDGET_TIME = "time";
    /** 组件  下拉框：select */
    public static final String WIDGET_SELECT = "select";
    /** 组件  复选框：check */
    public static final String WIDGET_CHECK = "check";
    /** 组件  单选框：radio */
    public static final String WIDGET_RADIO = "radio";
    /** 组件  表格：table */
    public static final String WIDGET_TABLE = "table";
    /** 组件  标签：label */
    public static final String WIDGET_LABEL = "label";
    
    /** 展示风格 00:独占一行 */
    public static final String STYLE_ONE_LINE = "00";
    /** 展示风格 10:占半行  */
    public static final String STYLE_HALF_LINE = "10";
    
    /** 页面类型  00:新增页面 */
    public static final String VIEW_TYPE_ADD = "00";
    /** 页面类型   10:修改页面  */
    public static final String VIEW_TYPE_EDIT = "10";
    /** 页面类型  20:审批页面 */
    public static final String VIEW_TYPE_APPROVE = "20";
    /** 页面类型  30:查看页面*/
    public static final String VIEW_TYPE_VIEW = "30";
    
    private String pojoId;
    
    
    public String getPojoId() {
		return pojoId;
	}

	public void setPojoId(String pojoId) {
		this.pojoId = pojoId;
	}

	public String getStyleName() {
		String ret = "未知";
		switch (super.getStyle()) {
		case STYLE_ONE_LINE:
			ret = "独占一行";
			break;
		case STYLE_HALF_LINE:
			ret = "占半行";
			break;
		default:
			ret = "未知";
			break;
		}
		return ret;
	}
	
	public String getColCls() {
		String ret = "未知";
		switch (super.getStyle()) {
		case STYLE_ONE_LINE:
			ret = "col2";
			break;
		case STYLE_HALF_LINE:
			ret = "col";
			break;
		default:
			ret = "未知";
			break;
		}
		return ret;
	}
	
	public String getWidgetName() {
    	String ret = "未知";
    	switch (super.getWidget()) {
		case WIDGET_TEXT:
			ret = "文本";
			break;
		case WIDGET_TEXTAREA:
			ret = "文本域";
			break;
		case WIDGET_DATE:
			ret = "日期";
			break;
		case WIDGET_TIME:
			ret = "时间";
			break;
		case WIDGET_SELECT:
			ret = "下拉框";
			break;
		case WIDGET_CHECK:
			ret = "复选框";
			break;
		case WIDGET_RADIO:
			ret = "单选框";
			break;
		case WIDGET_TABLE:
			ret = "表格";
			break;
		case WIDGET_LABEL:
			ret = "标签";
			break;
			
		default:
			ret = "未知";
			break;
		}
    	return ret;
    }
}