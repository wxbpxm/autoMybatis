package org.mybatis.generator.internal.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import fuku.automybatis.bean.BusinessModelAO;
import fuku.automybatis.bean.BusinessPojoAO;


public class FileGenerateUtil {

	public static GeneratedJavaFile getSearchJava(GeneratedJavaFile mapperGif, String extendAOPackage, String aoAndExamplePackage, String moduleName) {
		GeneratedJavaFile retGif = null;
		CompilationUnit oldCompilationUnit = mapperGif.getCompilationUnit();
		TopLevelClass usedCompilationUnit = new TopLevelClass(extendAOPackage+ "."  + moduleName + "SearchAO");
		FullyQualifiedJavaType serializableType = new FullyQualifiedJavaType("java.io.Serializable");
        usedCompilationUnit.addSuperInterface(serializableType);
        usedCompilationUnit.addImportedType(serializableType);
		for (String commentLine : oldCompilationUnit
				.getFileCommentLines()) {
			usedCompilationUnit.addFileCommentLine(commentLine);
		}

		Field serialVersionUIDField = new Field("serialVersionUID", new FullyQualifiedJavaType("java.lang.long"));
		serialVersionUIDField.setFinal(true);
		serialVersionUIDField.setInitializationString(new Random().nextLong() + "L;");
		serialVersionUIDField.setStatic(true);
		serialVersionUIDField.setVisibility(JavaVisibility.PRIVATE);
		usedCompilationUnit.addField(serialVersionUIDField);
		
		usedCompilationUnit.setSuperClass(new FullyQualifiedJavaType(aoAndExamplePackage + ".SearchBaseAO"));
		usedCompilationUnit.setVisibility(JavaVisibility.PUBLIC);
		retGif = new GeneratedJavaFile(usedCompilationUnit,
				mapperGif.getTargetProject());
		return retGif;
	}
	
	public static GeneratedJavaFile getControllerJava(GeneratedJavaFile mapperGif, String extendAOPackage, String controllerPackage,
			String servicePackage, String pagePath, String moduleName, List<IntrospectedColumn> introspectedColumns) {
		GeneratedJavaFile retGif = null;
		CompilationUnit oldCompilationUnit = mapperGif.getCompilationUnit();
		TopLevelClass usedCompilationUnit = new TopLevelClass(controllerPackage+ "."  + moduleName + "Controller");
		for (String commentLine : oldCompilationUnit
				.getFileCommentLines()) {
			usedCompilationUnit.addFileCommentLine(commentLine);
		}
		
		String utilPackage = extendAOPackage.replace("appobj.extend", "util");

		String serviceFieldName = getCaluName(moduleName) + "Service";
		String aoName = moduleName + "AO";
		
		FullyQualifiedJavaType serviceType = new FullyQualifiedJavaType(servicePackage + ".I" + moduleName + "Service");
		FullyQualifiedJavaType aoType = new FullyQualifiedJavaType(extendAOPackage + "." + aoName);
		FullyQualifiedJavaType modelType = new FullyQualifiedJavaType("org.springframework.ui.Model");
		FullyQualifiedJavaType requestType = new FullyQualifiedJavaType("javax.servlet.http.HttpServletRequest");
		FullyQualifiedJavaType managerUserType = new FullyQualifiedJavaType(extendAOPackage + ".ManageUserAO");
		FullyQualifiedJavaType userUtilType = new FullyQualifiedJavaType(utilPackage + ".UserUtil");
		FullyQualifiedJavaType searchType = new FullyQualifiedJavaType(extendAOPackage + "." + moduleName + "SearchAO");
		
		Field serviceField = new Field(serviceFieldName, serviceType);
		serviceField.setVisibility(JavaVisibility.PRIVATE);
		serviceField.addAnnotation("@Resource");
		usedCompilationUnit.addField(serviceField);

		usedCompilationUnit.setVisibility(JavaVisibility.PUBLIC);
		
		usedCompilationUnit.addImportedType(serviceType);
		usedCompilationUnit.addImportedType(new FullyQualifiedJavaType("javax.annotation.Resource"));
		usedCompilationUnit.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Controller"));
		usedCompilationUnit.addImportedType(modelType);
		usedCompilationUnit.addImportedType(requestType);
		usedCompilationUnit.addImportedType(managerUserType);
		usedCompilationUnit.addImportedType(userUtilType);
		usedCompilationUnit.addImportedType(searchType);
		usedCompilationUnit.addImportedType(aoType);
		usedCompilationUnit.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.ModelAttribute"));
		usedCompilationUnit.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping"));
		usedCompilationUnit.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.ResponseBody"));
		usedCompilationUnit.addImportedType(new FullyQualifiedJavaType("com.ynkghs.framework.obj.DataList"));
		usedCompilationUnit.addImportedType(new FullyQualifiedJavaType("com.ynkghs.framework.obj.Page"));
		
		usedCompilationUnit.addAnnotation("@Controller");
		usedCompilationUnit.addAnnotation("@RequestMapping(\"/" + pagePath + "\")");
		
		Method setCurrentUserMethod = new Method();
		setCurrentUserMethod.setName("setCurrentUser");
		setCurrentUserMethod.setVisibility(JavaVisibility.PUBLIC);
		setCurrentUserMethod.addParameter(new Parameter(modelType, "model"));
		setCurrentUserMethod.addParameter(new Parameter(requestType, "request"));
		setCurrentUserMethod.addBodyLine("ManageUserAO user = (ManageUserAO) UserUtil.getCurrentUser(request);");
		setCurrentUserMethod.addBodyLine("model.addAttribute(\"currentUser\", user);");
		setCurrentUserMethod.addAnnotation("@ModelAttribute");
		usedCompilationUnit.addMethod(setCurrentUserMethod);
		
		Method listMethod = new Method();
		listMethod.setName(getCaluName(moduleName));
		listMethod.setVisibility(JavaVisibility.PUBLIC);
		listMethod.setReturnType(new FullyQualifiedJavaType("java.lang.Object"));
		listMethod.addParameter(new Parameter(searchType, "search"));
		listMethod.addParameter(new Parameter(modelType, "model"));
		listMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "pageNo"));
		listMethod.addBodyLine("if(pageNo == null) {");
		listMethod.addBodyLine("\tpageNo = 1;");
		listMethod.addBodyLine("}");
		listMethod.addBodyLine("");
		listMethod.addBodyLine("Page page = new Page(pageNo);");
		listMethod.addBodyLine("DataList<"+ aoName +"> datas = " + serviceFieldName + ".get" + moduleName + "List(search, page);");
		listMethod.addBodyLine("model.addAttribute(\"datalist\", datas.getData());");
		listMethod.addBodyLine("page = Page.makePage(page, datas.getTotal());");
		listMethod.addBodyLine("model.addAttribute(\"page\", page);");
		listMethod.addBodyLine("model.addAttribute(\"search\", search);");
		listMethod.addBodyLine("return MVCViewName."+ pagePath.toUpperCase() + "_" + moduleName.toUpperCase() +"_MAIN.view();");
		listMethod.addAnnotation("@RequestMapping(\"\")");
		usedCompilationUnit.addMethod(listMethod);
		
		Method detailMethod = new Method();
		detailMethod.setName(getCaluName(moduleName)+"Detail");
		detailMethod.setVisibility(JavaVisibility.PUBLIC);
		detailMethod.setReturnType(new FullyQualifiedJavaType("java.lang.Object"));
		detailMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.String"), "id"));
		detailMethod.addParameter(new Parameter(modelType, "model"));
		detailMethod.addBodyLine("if(id != null && !id.isEmpty()) {");
		detailMethod.addBodyLine("\t"+ aoName +" detail = "+ serviceFieldName +".get"+ moduleName +"ById(id);");
		detailMethod.addBodyLine("\tmodel.addAttribute(\"detail\", detail);");
		detailMethod.addBodyLine("}");
		detailMethod.addBodyLine("");
		detailMethod.addBodyLine("return MVCViewName."+ pagePath.toUpperCase() + "_" + moduleName.toUpperCase() +"_DETAIL.view();");
		detailMethod.addAnnotation("@RequestMapping(\"/detail\")");
		usedCompilationUnit.addMethod(detailMethod);
		
		Method saveMethod = new Method();
		saveMethod.setName("save" + moduleName);
		saveMethod.setVisibility(JavaVisibility.PUBLIC);
		saveMethod.setReturnType(new FullyQualifiedJavaType("java.lang.Object"));
		saveMethod.addParameter(new Parameter(aoType, getCaluName(moduleName)));
		saveMethod.addBodyLine("return  "+ serviceFieldName +".saveOrUpdate"+ moduleName +"("+ getCaluName(moduleName) +");");
		saveMethod.addAnnotation("@ResponseBody");
		saveMethod.addAnnotation("@RequestMapping(\"/save\")");
		usedCompilationUnit.addMethod(saveMethod);
		
		List<String> aoFieldNames = new ArrayList<String>();
		for(IntrospectedColumn tmpColumn : introspectedColumns) {
			if(tmpColumn.getJavaProperty() != null){
				aoFieldNames.add(tmpColumn.getJavaProperty());
			}
		}
		
		Method deleteMethod = new Method();
		deleteMethod.setName("save" + moduleName);
		deleteMethod.setVisibility(JavaVisibility.PUBLIC);
		deleteMethod.setReturnType(new FullyQualifiedJavaType("java.lang.Object"));
		deleteMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.String"), "id"));
		if(aoFieldNames.contains("status")) {
			deleteMethod.addBodyLine("return  "+ serviceFieldName +".update"+ moduleName +"Status(id, "+ aoName +".STATUS_DELETE);");
		} else {
			deleteMethod.addBodyLine("return  "+ serviceFieldName +".delete"+ moduleName +"ById(id);");
		}
		deleteMethod.addAnnotation("@ResponseBody");
		deleteMethod.addAnnotation("@RequestMapping(\"/delete\")");
		usedCompilationUnit.addMethod(deleteMethod);
		
		retGif = new GeneratedJavaFile(usedCompilationUnit,
				mapperGif.getTargetProject());
		return retGif;
	}
	
	public static GeneratedJavaFile getServiceInterGJFJava(GeneratedJavaFile mapperGjf,List<Method> methods, String serviceInterPackage, String moduleName) {
		GeneratedJavaFile retGif = null;
		CompilationUnit oldCompilationUnit = mapperGjf.getCompilationUnit();
		
		String type = serviceInterPackage + ".I"
				+  moduleName + "Service" ;
		Interface usedCompilationUnit = new Interface(type);
		for (String commentLine : oldCompilationUnit
				.getFileCommentLines()) {
			usedCompilationUnit.addFileCommentLine(commentLine);
		}
		
		usedCompilationUnit.setVisibility(JavaVisibility.PUBLIC);
		for(Method method : methods) {
			usedCompilationUnit.addMethod(method);
			for(FullyQualifiedJavaType tmpType : method.getDependTypes()) {
				usedCompilationUnit.addImportedType(tmpType);
			}
		}
		
		retGif = new GeneratedJavaFile(usedCompilationUnit,
				mapperGjf.getTargetProject());
		return retGif;
	}
	
	public static GeneratedJavaFile getServiceImplGJFJava(GeneratedJavaFile mapperGjf, List<Method> methods, List<String> aoFieldNames, String serviceInterPackage, String moduleName) {
		GeneratedJavaFile retGif = null;
		CompilationUnit oldCompilationUnit = mapperGjf.getCompilationUnit();
		
		String serviceTypeFullName = serviceInterPackage + "."
				+  moduleName + "Service" ;
		String mapperTypeFullName = mapperGjf.getTargetPackage() + "."
				+  moduleName + "Mapper" ;
		String interTypeFullName = serviceInterPackage + ".I"
				+  moduleName + "Service" ;
		String exampleFullName = mapperGjf.getTargetPackage().replace(".db.generator", ".appobj.generator") + "."
				+  moduleName + "Example" ;
		String listTypeFullName = "java.util.List";
		
		TopLevelClass usedCompilationUnit = new TopLevelClass(serviceTypeFullName);
		usedCompilationUnit.addAnnotation("@Service");
		for (String commentLine : oldCompilationUnit
				.getFileCommentLines()) {
			usedCompilationUnit.addFileCommentLine(commentLine);
		}
		
		usedCompilationUnit.setAbstract(false);
		usedCompilationUnit.addSuperInterface(new FullyQualifiedJavaType(interTypeFullName));
		usedCompilationUnit.setVisibility(JavaVisibility.PUBLIC);
		
		FullyQualifiedJavaType resourceType = new FullyQualifiedJavaType("javax.annotation.Resource");
		usedCompilationUnit.addImportedType(resourceType);
		FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(mapperTypeFullName);
		usedCompilationUnit.addImportedType(mapperType);
		FullyQualifiedJavaType listType = new FullyQualifiedJavaType(listTypeFullName);
		usedCompilationUnit.addImportedType(listType);
		FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(exampleFullName);
		usedCompilationUnit.addImportedType(exampleType);
		usedCompilationUnit.addImportedType("org.springframework.stereotype.Service");
		usedCompilationUnit.addImportedType("org.springframework.transaction.annotation.Transactional");
		
		Field field = new Field(getCaluName(moduleName) + "Mapper", mapperType);
		field.addAnnotation("@Resource");
		field.setVisibility(JavaVisibility.PRIVATE);
		usedCompilationUnit.addField(field);
		for(Method method : methods) {
			usedCompilationUnit.addMethod(method);
			if(method.getDependTypes() != null) {
				for(FullyQualifiedJavaType tmpType : method.getDependTypes()) {
					usedCompilationUnit.addImportedType(tmpType);
				}
			}
		}
		
		if(aoFieldNames.contains("logo")) { //如果包括图片，需要把ResourceMapper注解进来
			Field resourceMapperField = new Field("resourceMapper", new FullyQualifiedJavaType(mapperGjf.getTargetPackage() + ".ResourceMapper"));
			resourceMapperField.addAnnotation("@Resource");
			resourceMapperField.setVisibility(JavaVisibility.PRIVATE);
			usedCompilationUnit.addField(resourceMapperField);
			usedCompilationUnit.addImportedType(mapperGjf.getTargetPackage() + ".ResourceMapper");
		}
		
		retGif = new GeneratedJavaFile(usedCompilationUnit,
				mapperGjf.getTargetProject());
		return retGif;
	}

	private static String getCaluName(String name) {
		if (name == null || name.isEmpty()) {
			return "";
		}
		String first = name.substring(0, 1);
		return name.replaceFirst(first, first.toLowerCase());
	}
	
	private static String getReverseCaluName(String name) {
		if (name == null || name.isEmpty()) {
			return "";
		}
		String first = name.substring(0, 1);
		return name.replaceFirst(first, first.toUpperCase());
	}
	
	public static GeneratedJavaFile getCustomMapperJava(GeneratedJavaFile mapperGjf, String customerMaperPackage, String extendAOPackage, String moduleName) {
		GeneratedJavaFile retGif = null;
		CompilationUnit oldCompilationUnit = mapperGjf.getCompilationUnit();
		String type = customerMaperPackage + "."
				+  moduleName + "CustomMapper" ;
		Interface usedCompilationUnit = new Interface(type);
		for (String commentLine : oldCompilationUnit
				.getFileCommentLines()) {
			usedCompilationUnit.addFileCommentLine(commentLine);
		}
		
		String extendAOName = moduleName+"AO";
		String mapperGetDataListMethodName = "get" + moduleName + "List"; 
		String mapperGetDataCountMethodName = "get" + moduleName + "Count"; 
		FullyQualifiedJavaType pageType = new FullyQualifiedJavaType("com.ynkghs.framework.obj.Page");
		FullyQualifiedJavaType searchType = new FullyQualifiedJavaType(extendAOPackage + "." + moduleName + "SearchAO");
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.util.List");
		FullyQualifiedJavaType annotationType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param");
		usedCompilationUnit.setVisibility(JavaVisibility.PUBLIC);
		String entityAOType = extendAOPackage + "." + extendAOName;
		usedCompilationUnit.addImportedType(returnType);
		usedCompilationUnit.addImportedType(annotationType);
		usedCompilationUnit.addImportedType(new FullyQualifiedJavaType(entityAOType));
		usedCompilationUnit.addImportedType(pageType);
		usedCompilationUnit.addImportedType(searchType);
		Method getListMethod = new Method();
		getListMethod.setName(mapperGetDataListMethodName);
		getListMethod.setVisibility(JavaVisibility.PUBLIC);
		getListMethod.setReturnType(new FullyQualifiedJavaType("List<"+ extendAOName +">"));
		getListMethod.addParameter(new Parameter(searchType, "search", "@Param(\"search\")"));
		getListMethod.addParameter(new Parameter(pageType, "page", "@Param(\"page\")"));
		getListMethod.addJavaDocLine("\n\t/**根据search条件查询"+moduleName+"列表*/");
		
		Method getDataCountMethod = new Method();
		getDataCountMethod.setName(mapperGetDataCountMethodName);
		getDataCountMethod.setVisibility(JavaVisibility.PUBLIC);
		getDataCountMethod.setReturnType(new FullyQualifiedJavaType("Integer"));
		getDataCountMethod.addParameter(new Parameter(searchType, "search", "@Param(\"search\")"));
		getDataCountMethod.addJavaDocLine("/**根据search条件查询"+moduleName+"数量*/");
		
		usedCompilationUnit.addMethod(getListMethod);
		usedCompilationUnit.addMethod(getDataCountMethod);
		retGif = new GeneratedJavaFile(usedCompilationUnit,
				mapperGjf.getTargetProject());
		return retGif;
	}
	
	public static GeneratedXmlFile getCustomMapperXml(GeneratedXmlFile mapperGxf, String extendAOPackage, String customerMaperPackage, String moduleName) {
		GeneratedXmlFile retGxf = null;
		Document document = mapperGxf.getDocument();
		Document newDocument = new Document(document.getPublicId(),
				document.getSystemId());
		XmlElement rootElement = new XmlElement("mapper");
		newDocument.setRootElement(rootElement);
		Attribute tmpAttr1 = new Attribute("namespace", customerMaperPackage + "." + moduleName + "CustomMapper");
		rootElement.addAttribute(tmpAttr1);
		handleResult(mapperGxf.getBaseColumns(),extendAOPackage, moduleName, rootElement,mapperGxf.getTableName());
		handleGetDataListSelect(mapperGxf.getBaseColumns(),moduleName, rootElement,mapperGxf.getTableName());
		handleGetDataCountSelect(moduleName, rootElement, mapperGxf.getTableName());
		String fileName = moduleName + "CustomMapper.xml";
		retGxf = new GeneratedXmlFile(newDocument, fileName,
				customerMaperPackage, mapperGxf.getTargetProject(), false);
		return retGxf;
	}
	

    public static GeneratedJavaFile getExtendAOGJFJava(GeneratedJavaFile mapperGjf, GeneratedXmlFile gxf, String aoClassPackage, String exampleClassPackage, String domainName, String domainBeanName) {
        GeneratedJavaFile retGif = null;
        CompilationUnit oldCompilationUnit = mapperGjf.getCompilationUnit();
        
        TopLevelClass usedCompilationUnit = new TopLevelClass(aoClassPackage + "." + domainBeanName);

        
        FullyQualifiedJavaType serializableType = new FullyQualifiedJavaType("java.io.Serializable");
        usedCompilationUnit.addSuperInterface(serializableType);
        usedCompilationUnit.addImportedType(serializableType);
        
        for (String commentLine : oldCompilationUnit
                .getFileCommentLines()) {
            usedCompilationUnit.addFileCommentLine(commentLine);
        }
        
        List<IntrospectedColumn> columns = gxf.getBaseColumns();
        List<String> fieldNames = new ArrayList<String>();
        Map<String, IntrospectedColumn> colMap = new HashMap<>();
        for(IntrospectedColumn column : columns) {
        	fieldNames.add(column.getJavaProperty());
        	colMap.put(column.getJavaProperty(), column);
        }
        
        usedCompilationUnit.setAbstract(false);
        usedCompilationUnit.setSuperClass(mapperGjf.getTargetPackage() + "." + domainName);
        usedCompilationUnit.setVisibility(JavaVisibility.PUBLIC);
        
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(exampleClassPackage + "." + domainName);
        usedCompilationUnit.addImportedType(entityType);
        
		Field field = new Field("serialVersionUID", new FullyQualifiedJavaType("long"));
		field.setFinal(true);
		field.setStatic(true);
		field.setInitializationString(new Random().nextLong() + "L");
		field.setVisibility(JavaVisibility.PRIVATE);
		usedCompilationUnit.addField(field);
		
		//定义常量区域，主要是针对如果有status，type这样的字段
		if(fieldNames.contains("status")) {
			//首先要获得数据库定义的列注释
			IntrospectedColumn statusColumn = colMap.get("status");
			String remark = statusColumn.getRemarks();
			if(remark != null && !remark.isEmpty() && statusColumn.getJdbcTypeName().equals("CHAR")) {
				handleConstantField(usedCompilationUnit, "status", remark);
				
				//添加get方法，以便获得汉字常量解释
				handleConstantGetMethod(usedCompilationUnit, "status", remark);
			}
		}  
		if(fieldNames.contains("type")) {
			//首先要获得数据库定义的列注释
			IntrospectedColumn statusColumn = colMap.get("type");
			String remark = statusColumn.getRemarks();
			if(remark != null && !remark.isEmpty() && statusColumn.getJdbcTypeName().equals("CHAR")) {
				handleConstantField(usedCompilationUnit, "type", remark);
				//添加get方法，以便获得汉字常量解释
				handleConstantGetMethod(usedCompilationUnit, "type", remark);
			}
		}  
		
		//判断是否有主图字段logo，如果有的话，为了配合页面操作，需要生成一个logoId的字段
		if(fieldNames.contains("logo")) {
			//首先要获得数据库定义的列注释
			IntrospectedColumn logoColumn = colMap.get("logo");
			if("VARCHAR".equals(logoColumn.getJdbcTypeName())) { //暂认为只有varchar类型（存放图片url）的logo字段才真的是该模块的主图字段
				handleLogoField(usedCompilationUnit, "logo");
				//添加get方法，以便获得汉字常量解释
				handleLogoMethod(usedCompilationUnit, "logo");
			}
		}
		
		retGif = new GeneratedJavaFile(usedCompilationUnit,
					mapperGjf.getTargetProject());
        return retGif;
    }
    
    private static void handleLogoField(TopLevelClass usedCompilationUnit, String fieldName) {
		String logoIdName = fieldName + "Id";
		Field constantField = new Field(logoIdName, new FullyQualifiedJavaType("String"));
		constantField.setVisibility(JavaVisibility.PRIVATE);
		constantField.addJavaDocLine("/** 用于后台保存模块时，存放上传的图片文件id */");
		usedCompilationUnit.addField(constantField);
    }
    
    private static void handleLogoMethod(TopLevelClass usedCompilationUnit, String fieldName) {
    	String logoIdName = fieldName + "Id";
    	String feildGetMthdName = "get" + getReverseCaluName(logoIdName);
    	String feildSetMthdName = "set" + getReverseCaluName(logoIdName);
    	
    	Method getMethod = new Method(feildGetMthdName);
    	getMethod.setReturnType(new FullyQualifiedJavaType("String"));
    	getMethod.setVisibility(JavaVisibility.PUBLIC);
    	getMethod.addBodyLine("return "+ logoIdName +";");
    	usedCompilationUnit.addMethod(getMethod);
    	
    	Method setMethod = new Method(feildSetMthdName);
    	setMethod.setVisibility(JavaVisibility.PUBLIC);
    	setMethod.addParameter(new Parameter(new FullyQualifiedJavaType("String"), logoIdName));
    	setMethod.addBodyLine("this." + logoIdName + " = " + logoIdName + ";");
    	usedCompilationUnit.addMethod(setMethod);
    }
    
    private static void handleConstantField(TopLevelClass usedCompilationUnit, String fieldName, String remark) {
//    	类型 10：美食攻略 20：广告攻略 30:图库资源
//		使用状态 00:删除  10:正常
    	if(remark != null && !remark.isEmpty()) {
    		String[] remarkAry = remark.split(" ");
    		//默认认为第一个是字段解释， 第二个开始是值和含义
    		String cmt = remarkAry[0];
    		int j = 1;
    		for(int i = 1; i < remarkAry.length; i++) {
    			String tmp = remarkAry[i];
    			String splitChar = null;
    			if(tmp.contains("：")) {
    				splitChar = "：";
    			} else if(tmp.contains(":")) {
    				splitChar = ":";
    			}
    			if(splitChar == null) {
    				continue;
    			}
    			String value = tmp.substring(0, tmp.indexOf(splitChar));
    			String name = tmp.substring(tmp.indexOf(splitChar)+1);
    			Field constantField = new Field(fieldName.toUpperCase() + "_" + j++, new FullyQualifiedJavaType("String"));
    			constantField.setFinal(true);
    			constantField.setStatic(true);
    			constantField.setInitializationString("\"" + value + "\"");
    			constantField.setVisibility(JavaVisibility.PUBLIC);
    			constantField.addJavaDocLine("/**" + cmt + " " + value + "——"  +  name + "*/");
    			usedCompilationUnit.addField(constantField);
    		}
    	}
    }
    
    private static void handleConstantGetMethod(TopLevelClass usedCompilationUnit, String fieldName, String remark) {
//    	类型 10：美食攻略 20：广告攻略 30:图库资源
//		使用状态 00:删除  10:正常
    	String feildGetMthdName = "get" + getReverseCaluName(fieldName);
    	Method method = new Method(feildGetMthdName + "Name");
    	method.setReturnType(new FullyQualifiedJavaType("String"));
    	method.setVisibility(JavaVisibility.PUBLIC);
    	method.addBodyLine("if(this."+ feildGetMthdName +"() == null || this."+ feildGetMthdName +"().isEmpty()) {");
    	method.addBodyLine("return null;");
    	method.addBodyLine("}");
    	method.addBodyLine("String ret = null;");
    	method.addBodyLine("switch(this."+ feildGetMthdName +"()) {");
		if(remark != null && !remark.isEmpty()) {
			String[] remarkAry = remark.split(" ");
			//默认认为第一个是字段解释， 第二个开始是值和含义
			int j = 1;
			for(int i = 1; i < remarkAry.length; i++) {
				String tmp = remarkAry[i];
				String splitChar = null;
				if(tmp.contains("：")) {
					splitChar = "：";
				} else if(tmp.contains(":")) {
					splitChar = ":";
				}
				if(splitChar == null) {
					continue;
				}
				String name = tmp.substring(tmp.indexOf(splitChar)+1);
				method.addBodyLine("case "+ fieldName.toUpperCase() + "_" + j++ +":");
		    	method.addBodyLine("ret = \""+ name + "\";");
		    	method.addBodyLine("break;");
			}
		}
		method.addBodyLine("default:");
    	method.addBodyLine("ret = \"未知\";");
    	method.addBodyLine("break;");
    	method.addBodyLine("}");
    	method.addBodyLine("return ret;");
    	usedCompilationUnit.addMethod(method);
    }
    
    
	private static void handleResult(List<IntrospectedColumn> baseColumns, String extendAOPackage, String moduleName, XmlElement rootElement, String tableName) {
		XmlElement resultMapEle = new XmlElement("resultMap");
		resultMapEle.addAttribute(new Attribute("id", "BaseResultMap"));
		resultMapEle.addAttribute(new Attribute("type", extendAOPackage + "." + moduleName + "AO"));
		XmlElement idEle = new XmlElement("id");
		idEle.addAttribute(new Attribute("column", "id"));
		idEle.addAttribute(new Attribute("jdbcType", "BIGINT"));
		idEle.addAttribute(new Attribute("property", "id"));
		resultMapEle.addElement(idEle);
		for(IntrospectedColumn tmpColumn : baseColumns) {
			XmlElement tmpEle = new XmlElement("result");
			tmpEle.addAttribute(new Attribute("column", tmpColumn.getActualColumnName()));
			tmpEle.addAttribute(new Attribute("jdbcType", tmpColumn.getJdbcTypeName()));
			tmpEle.addAttribute(new Attribute("property", tmpColumn.getJavaProperty()));
			resultMapEle.addElement(tmpEle);
		}
		rootElement.addElement(resultMapEle);
	}
	
	private static void handleGetDataListSelect(List<IntrospectedColumn> baseColumns, String moduleName, XmlElement rootElement, String tableName) {
		XmlElement selectEle = new XmlElement("select");
		String mapperGetDataListMethodName = "get" + moduleName + "List"; 
		Attribute tmpAttr1 = new Attribute("id", mapperGetDataListMethodName);
		Attribute tmpAttr2 = new Attribute("resultMap", "BaseResultMap");
		Attribute tmpAttr3 = new Attribute("parameterType", "map");
		selectEle.addAttribute(tmpAttr1);
		selectEle.addAttribute(tmpAttr2);
		selectEle.addAttribute(tmpAttr3);
		String tableAliase = String.valueOf(moduleName.charAt(0)).toLowerCase();
		
		StringBuilder tmpSb = new StringBuilder();
		tmpSb.append("select ");
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("id");
		tmpSb.append(tableAliase).append(".id");
		for(IntrospectedColumn tmpColumn : baseColumns) {
			tmpSb.append(",");
			columnNames.add(tmpColumn.getActualColumnName().trim());
			tmpSb.append(tableAliase).append(".").append(tmpColumn.getActualColumnName().trim());
		}
		tmpSb.append("\n\tfrom ").append(tableName).append(" ").append(tableAliase).append("\n\t")
		.append("where 1=1");
		TextElement selectTxtEle = new TextElement(tmpSb.toString());
		selectEle.addElement(selectTxtEle);
		XmlElement ifSearchEle = new XmlElement("if");
		Attribute attribute = new Attribute("test", "search != null");
		ifSearchEle.addAttribute(attribute);
		TextElement searchBody = new TextElement("");
		ifSearchEle.addElement(searchBody);
		selectEle.addElement(ifSearchEle);
		if(columnNames.contains("create_time")) {
			TextElement orderbyBody = new TextElement("order by "+ tableAliase +".create_time desc");
			selectEle.addElement(orderbyBody);
		} else if(columnNames.contains("create_date")) {
			TextElement orderbyBody = new TextElement("order by "+ tableAliase +".create_date desc");
			selectEle.addElement(orderbyBody);
		}
		
		XmlElement ifPageEle = new XmlElement("if");
	    attribute = new Attribute("test", "page != null");
	    ifPageEle.addAttribute(attribute);
		TextElement pageBody = new TextElement("limit #{page.start} , #{page.pageSize}");
		ifPageEle.addElement(pageBody);
		selectEle.addElement(ifPageEle);
		rootElement.addElement(selectEle);
	}
	
	private static void handleGetDataCountSelect(String moduleName, XmlElement rootElement, String tableName) {
		XmlElement selectEle = new XmlElement("select");
		String mapperGetDataListMethodName = "get" + moduleName + "Count"; 
		
		Attribute tmpAttr1 = new Attribute("id", mapperGetDataListMethodName);
		Attribute tmpAttr2 = new Attribute("resultType", "java.lang.Integer");
		Attribute tmpAttr3 = new Attribute("parameterType", "String");
		selectEle.addAttribute(tmpAttr1);
		selectEle.addAttribute(tmpAttr2);
		selectEle.addAttribute(tmpAttr3);
		String tableAliase = String.valueOf(moduleName.charAt(0)).toLowerCase();
		
		StringBuilder tmpSb = new StringBuilder();
		tmpSb.append("select count(").append(tableAliase).append(".id)")
			.append("\n\t").append("from ").append(tableName).append(" ").append(tableAliase).append("\n\t")
			.append("where 1=1\n");
		TextElement selectTxtEle = new TextElement(tmpSb.toString());
		selectEle.addElement(selectTxtEle);
		XmlElement ifSearchEle = new XmlElement("if");
		Attribute attribute = new Attribute("test", "search != null");
		ifSearchEle.addAttribute(attribute);
		TextElement searchBody = new TextElement("");
		ifSearchEle.addElement(searchBody);
		selectEle.addElement(ifSearchEle);
		rootElement.addElement(selectEle);
	}
	
	public static GeneratedJavaFile getPojoJava(BusinessModelAO model, String targetProject) {
		GeneratedJavaFile retGif = null;
		String pojoPackage = "";
		if(model.getPojoName().lastIndexOf(".") > 0) {
			pojoPackage = model.getPojoName().substring(0, model.getPojoName().lastIndexOf("."));
		}
		String pojoParentPackage = pojoPackage.replace(".generator", "");
		FullyQualifiedJavaType topPojoAdapto = new FullyQualifiedJavaType(pojoParentPackage + ".BPojoAdaptor");
		String type = model.getPojoName();
		TopLevelClass pojoClass = new TopLevelClass(type);
		pojoClass.setVisibility(JavaVisibility.PUBLIC);
		pojoClass.setSuperClass(topPojoAdapto);
		pojoClass.addImportedType(topPojoAdapto);
		pojoClass.addFileCommentLine(CommonDataUtil.getCopyRight());
		 
		//开始处理属性（所有属性）
		//特殊处理id
		List<Field> fieldList = new ArrayList<>();
		
		//复杂的pojo（就是list和pojo类型的）要单独记录下来
		List<BusinessPojoAO> complexPojos = null;
		List<BusinessPojoAO> topPojos = null;
		Map<String, List<BusinessPojoAO>> complexPojoConfigs = null;
		Boolean containBFile = false;
		if(model.getPojoList() != null) {
			topPojos = new ArrayList<>();
			complexPojos = new ArrayList<>();
			complexPojoConfigs = new HashMap<>();
			for(BusinessPojoAO pojo : model.getPojoList()) {
				if(BusinessPojoAO.JAVA_TYPE_LIST.equals(pojo.getJavaType()) 
						|| BusinessPojoAO.JAVA_TYPE_POJO.equals(pojo.getJavaType()) ) {
					complexPojos.add(pojo);
				} else if(BusinessPojoAO.JAVA_TYPE_BFILE.equals(pojo.getJavaType())
						|| BusinessPojoAO.JAVA_TYPE_LIST_BFILE.equals(pojo.getJavaType())){
					containBFile = true;
				}
				if(pojo.getPojoId() != null) {
					List<BusinessPojoAO> groupPojos = complexPojoConfigs.get(pojo.getPojoId());
					if(groupPojos == null) {
						groupPojos = new ArrayList<>();
						complexPojoConfigs.put(pojo.getPojoId(), groupPojos);
					}
					groupPojos.add(pojo);
				} else {
					topPojos.add(pojo);
				}
			}
		}
		if(topPojos != null && topPojos.size() > 0) {
			for(BusinessPojoAO simplePojo : topPojos) {
				Field tmpField = getField(simplePojo.getColumnName(),simplePojo.getJavaTypeName());
				pojoClass.addField(tmpField);
				fieldList.add(tmpField);
				if(simplePojo.getImportJavaTypeName() != null) {
					pojoClass.addImportedType(new FullyQualifiedJavaType(simplePojo.getImportJavaTypeName()));
				}
				
				if(simplePojo.getJavaType().equals(BusinessPojoAO.JAVA_TYPE_POJO)) {
					pojoClass.addImportedType(new FullyQualifiedJavaType(pojoParentPackage + ".BPojo"));
				}
				if(simplePojo.getJavaType().equals(BusinessPojoAO.JAVA_TYPE_BKEYPAIR)
						|| simplePojo.getJavaType().equals(BusinessPojoAO.JAVA_TYPE_LIST_BKEYPAIR)) {
					pojoClass.addImportedType(new FullyQualifiedJavaType(pojoParentPackage + ".BKeyPair"));
				}
				if(simplePojo.getJavaType().equals(BusinessPojoAO.JAVA_TYPE_BFILE)
						|| simplePojo.getJavaType().equals(BusinessPojoAO.JAVA_TYPE_LIST_BFILE)) {
					pojoClass.addImportedType(new FullyQualifiedJavaType(pojoParentPackage + ".BFile"));
				}
			}
		}
		
		if(complexPojos != null && complexPojos.size() > 0) {//复杂的需要生产内部类
			for(BusinessPojoAO complexPojo : complexPojos) {
				List<BusinessPojoAO> childPojos = complexPojoConfigs.get(complexPojo.getId());
				if(childPojos != null) {
					handleInnerClass(pojoPackage, pojoClass, complexPojo, childPojos);
				}
			}
		}
		
		
		if(containBFile) {
			FullyQualifiedJavaType bcontainFile = new FullyQualifiedJavaType(pojoParentPackage + ".BContainFile");
			pojoClass.addSuperInterface(bcontainFile);
			pojoClass.addImportedType(bcontainFile);
		}
		
		//开始处理get、set方法
		for(Field field : fieldList) {
			pojoClass.addMethod(getGetMethod(field));
			pojoClass.addMethod(getSetMethod(field));
		}
		retGif = new GeneratedJavaFile(pojoClass,
				targetProject);
		return retGif;
	}

	private static void handleInnerClass(String pojoPackage, TopLevelClass pojoClass, BusinessPojoAO complexPojo, List<BusinessPojoAO> childPojos) {
		String pojoParentPackage = pojoPackage.replace(".generator", "");
		FullyQualifiedJavaType businessPojoType = new FullyQualifiedJavaType(pojoParentPackage + ".BPojo");
		FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(getFistUpperName(complexPojo.getColumnName()));
		InnerClass innerClass = new InnerClass(fullyQualifiedJavaType);
		innerClass.setStatic(true);
		innerClass.addSuperInterface(businessPojoType);
		pojoClass.addImportedType(businessPojoType);
		innerClass.setVisibility(JavaVisibility.PUBLIC);
		
		List<Field> fieldList = new ArrayList<>();
		for(BusinessPojoAO pojo : childPojos) {
			Field tmpField = getField(pojo.getColumnName(),pojo.getJavaTypeName());
			innerClass.addField(tmpField);
			fieldList.add(tmpField);
			if(pojo.getImportJavaTypeName() != null) {
				pojoClass.addImportedType(new FullyQualifiedJavaType(pojo.getImportJavaTypeName()));
			}
			if(pojo.getJavaType().equals(BusinessPojoAO.JAVA_TYPE_BKEYPAIR)) {
				pojoClass.addImportedType(new FullyQualifiedJavaType(pojoParentPackage + ".BKeyPair"));
			}
			if(pojo.getJavaType().equals(BusinessPojoAO.JAVA_TYPE_BFILE)) {
				pojoClass.addImportedType(new FullyQualifiedJavaType(pojoParentPackage + ".BFile"));
			}
		}
		
		//开始处理get、set方法
		for(Field field : fieldList) {
			innerClass.addMethod(getGetMethod(field));
			innerClass.addMethod(getSetMethod(field));
		}
		pojoClass.addInnerClass(innerClass);
	}
	
	private static Method getGetMethod(Field field) {
		Method method = new Method("get" + getFistUpperName(field.getName()));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(field.getType());
		method.addBodyLine("return " + field.getName() + ";");
		return method;
	}
	private static Method getSetMethod(Field field) {
		Method method = new Method("set" + getFistUpperName(field.getName()));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addParameter(new Parameter(field.getType(), field.getName()));
		method.addBodyLine("this." + field.getName() + " = " + field.getName() + ";");
		return method;
	}
	private static Field getField(String name, String type) {
		if(name == null || type == null) {
			return null;
		}
		Field idField = new Field();
		idField.setName(name);
		FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(type);
		if("List".equals(type)) {
			fullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType(getFistUpperName(name)));;
		}
		idField.setType(fullyQualifiedJavaType);
		idField.setVisibility(JavaVisibility.PRIVATE);
		return idField;
	}
	
	private static String getFistUpperName(String name) {
		if(name == null || name.isEmpty()) {
			return name;
		} else if(name.length() == 1) {
			return name.toUpperCase();
		}
		return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
	}

	

//	private static boolean haveAttribute(Element tmpEle, String name, String value) {
//		if(name == null || name.isEmpty() || value == null || value.isEmpty()) {
//			return false;
//		}
//		if(!(tmpEle instanceof XmlElement)) {
//			return false;
//		}
//		XmlElement xmlEle = (XmlElement) tmpEle;
//		List<Attribute> attributes = xmlEle.getAttributes();
//		for(Attribute attribute : attributes) {
//			if(name.equals(attribute.getName()) && value.equals(attribute.getValue())) {
//				return true;
//			}
//		}
//		return false;
//	}

}
