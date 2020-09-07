/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fuku.automybatis.bean;

import java.util.Arrays;
import java.util.List;


/**
 * 
 * @author linfeng
 */
public class FieldConfig {
	
	private String label;
	
	private String name;

	private String jdbcType;
	 
	private boolean canBeNull;
	
	//main页面是否需要链接到详情
	private boolean needLink;

	private String htmlType;
	
	//仅仅用于status这样的下拉框，存放数据表的字段备注，以便在页面中解析
	private List<Option> options;
	
	public String getLabel() {
		int indx1 = label.indexOf(" ");
		int	indx2 = label.indexOf("，");
		int	indx3 = label.indexOf(",");
		int min = getMin(indx1, indx2, indx3);
		if(min > 0) {
			return label.substring(0,min);
		} else {
			return label;
		}
	}

	private int getMin(int indx1, int indx2, int indx3) {
		int [] tmp = {indx1, indx2, indx3}; 
		Arrays.sort(tmp);
		for(int i = 0; i < 3; i++) {
			if(tmp[i] > 0) {
				return tmp[i];
			}
		}
		return -1;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public boolean isCanBeNull() {
		return canBeNull;
	}

	public void setCanBeNull(boolean canBeNull) {
		this.canBeNull = canBeNull;
	}

	public String getHtmlType() {
		return htmlType;
	}

	public void setHtmlType(String htmlType) {
		this.htmlType = htmlType;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public boolean isNeedLink() {
		return needLink;
	}

	public void setNeedLink(boolean needLink) {
		this.needLink = needLink;
	}
	
}
