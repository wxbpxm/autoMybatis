/*
* Copyright (c) 2016,2021 云南空港航空食品有限公司. All rights reserved.
* create time Tue Oct 18 13:57:28 CST 2016
*
*/
package fuku.automybatis.bean;

import java.io.Serializable;

public class BusinessValidationAO extends BusinessValidation implements Serializable {
    private static final long serialVersionUID = -3605052951176748314L;
    
    /** 校验类  非空： require */
    public static final String CLAZZ_REQUIRE = "require";
    /** 校验类  电话： mobile */
    public static final String CLAZZ_MOBILE = "mobile";
    /** 校验类  邮箱： email */
    public static final String CLAZZ_EMAIL = "email";
    /** 校验类  自定义： custom */
    public static final String CLAZZ_CUSTOM = "custom";
    /** 校验类 整型： int */
    public static final String CLAZZ_INT = "int";
    /** 校验类 数值： number */
    public static final String CLAZZ_NUMBER = "number";
    
    
    
    /** 针对页面 所有页面：40 */
    public static final String VIEW_TYPE_ALL = "40";
    /** 针对页面 新增页面：00 */
    public static final String VIEW_TYPE_ADD = "00";
    /** 针对页面 修改页面：10 */
    public static final String VIEW_TYPE_EDIT = "10";
    /** 针对页面 审核页面：20 */
    public static final String VIEW_TYPE_AUDIT = "20";
    /** 针对页面 查看页面：30 */
    public static final String VIEW_TYPE_VIEW = "30";

    public String getClazzName() {
    	String ret = "未知";
    	switch (super.getClazz()) {
    	case CLAZZ_REQUIRE:
    		ret = "非空";
    		break;
    	case CLAZZ_MOBILE:
    		ret = "手机";
    		break;
    	case CLAZZ_EMAIL:
    		ret = "邮箱";
    		break;
    	case CLAZZ_CUSTOM:
    		ret = "自定义";
    		break;
    	case CLAZZ_INT:
    		ret = "整型";
    		break;
    	case CLAZZ_NUMBER:
    		ret = "数值";
    		break;
    		
    	default:
    		ret = "未知";
    		break;
    	}
    	return ret;
    }
    
    public String getViewTypeName() {
    	String ret = "未知";
    	switch (super.getViewType()) {
    	case VIEW_TYPE_ALL:
			ret = "所有页面";
			break;
		case VIEW_TYPE_ADD:
			ret = "新增页面";
			break;
		case VIEW_TYPE_EDIT:
			ret = "修改页面";
			break;
		case VIEW_TYPE_AUDIT:
			ret = "审核页面";
			break;
		case VIEW_TYPE_VIEW:
			ret = "查看页面";
			break;
			
		default:
			ret = "未知";
			break;
		}
    	return ret;
    }
    
}