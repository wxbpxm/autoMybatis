/*
* Copyright (c) 2016,2021 云南空港航空食品有限公司. All rights reserved.
* create time Mon Oct 17 09:25:12 CST 2016
*
*/
package fuku.automybatis.bean;

import java.io.Serializable;
import java.util.List;


public class BusinessPojoAO extends BusinessPojo implements Serializable {
    private static final long serialVersionUID = -1203223553399154333L;
    
    /** 字段类型  000:String */
    public static final String JAVA_TYPE_STRING = "000";
    /** 字段类型   100:BigDecimal */
    public static final String JAVA_TYPE_BIGDECIMAL = "100";
    /** 字段类型  200:Long */
    public static final String JAVA_TYPE_LONG = "200";
    /** 字段类型  300:Integer */
    public static final String JAVA_TYPE_INTEGER = "300";
    /** 字段类型  400:Date */
    public static final String JAVA_TYPE_DATE = "400";
    /** 字段类型  500:List */
    public static final String JAVA_TYPE_LIST = "500";
    /** 字段类型  600:Pojo */
    public static final String JAVA_TYPE_POJO = "600";
    /** 字段类型  700:BKeyPair */
    public static final String JAVA_TYPE_BKEYPAIR = "700";
    /** 字段类型  750:List<BKeyPair> */
    public static final String JAVA_TYPE_LIST_BKEYPAIR = "750";
    /** 字段类型  800:BFile */
    public static final String JAVA_TYPE_BFILE = "800";
    /** 字段类型  850:List<BFile> */
    public static final String JAVA_TYPE_LIST_BFILE = "850";
    
    private List<BusinessPojoAO> childPojos;
     
    private BusinessPojoAO pojo;
    
    

	public BusinessPojoAO getPojo() {
		return pojo;
	}


	public void setPojo(BusinessPojoAO pojo) {
		this.pojo = pojo;
	}


	public List<BusinessPojoAO> getChildPojos() {
		return childPojos;
	}


	public void setChildPojos(List<BusinessPojoAO> childPojos) {
		this.childPojos = childPojos;
	}


	//用于是否需要导入这个类型，如Date类型则返回java.util.Date,反之返回null
	public String getImportJavaTypeName() {
    	String ret = "未知";
    	if(super.getJavaType() == null) {
    		return ret;
    	}
    	switch (super.getJavaType()) {
		case JAVA_TYPE_STRING:
		case JAVA_TYPE_LONG:
		case JAVA_TYPE_INTEGER:
			return null;
		case JAVA_TYPE_BIGDECIMAL:
			ret = "java.math.BigDecimal";
			break;
		case JAVA_TYPE_DATE:
			ret = "java.util.Date";
			break;
		case JAVA_TYPE_LIST:
		case JAVA_TYPE_LIST_BFILE:
		case JAVA_TYPE_LIST_BKEYPAIR:
			ret = "java.util.List";
			break;
		default:
			return null;
		}
    	return ret;
	}
	public String getJavaTypeName() {
    	String ret = "未知";
    	if(super.getJavaType() == null) {
    		return ret;
    	}
    	switch (super.getJavaType()) {
		case JAVA_TYPE_STRING:
			ret = "String";
			break;
		case JAVA_TYPE_BIGDECIMAL:
			ret = "BigDecimal";
			break;
		case JAVA_TYPE_LONG:
			ret = "Long";
			break;
		case JAVA_TYPE_INTEGER:
			ret = "Integer";
			break;
		case JAVA_TYPE_DATE:
			ret = "Date";
			break;
		case JAVA_TYPE_LIST:
			ret = "List";
			break;
		case JAVA_TYPE_POJO:
			ret = "Pojo";
			break;
		case JAVA_TYPE_BKEYPAIR:
			ret = "BKeyPair";
			break;
		case JAVA_TYPE_LIST_BKEYPAIR:
			ret = "List<BKeyPair>";
			break;
		case JAVA_TYPE_BFILE:
			ret = "BFile";
			break;
		case JAVA_TYPE_LIST_BFILE:
			ret = "List<BFile>";
			break;
			
		default:
			ret = "未知";
			break;
		}
    	return ret;
    
    }
}