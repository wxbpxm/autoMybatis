package fuku.automybatis.freemarkertool;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.osgi.framework.Bundle;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import fuku.automybatis.Activator;
import fuku.automybatis.bean.FieldConfig;
import fuku.automybatis.pageconfig.ui.GeneratePageConfigJDialog;
import fuku.automybatis.ui.SelectTableDialog;

public class GeneratePageTool
{
	
	private GeneratePageConfigJDialog generatePageConfigJDialog;
	
	/** 模块名称，如：DesktopElement */
	private String moduleName;
	private String moduleNameCal;
	
	/**
	 * 几个最终需要用到的代码类对象，所谓代码类，就是为了生成一个类的代码，而自定义的类型CodeClass，尽量做到分解各种语法，做到所向无敌
	 */
	private MyBatisGenerator generator;
	private ProgressCallback callback;
	//AO对象的所有可用字段名称
	private List<String> aoFieldNames;
	//表中的所有字段名称
	private List<String> tableFieldNames;
	
	private List<IntrospectedColumn> introspectedColumns; 
	
	private GeneratedJavaFile gjf;
	
	public GeneratePageTool(MyBatisGenerator generator,
			GeneratedJavaFile gjf, GeneratedXmlFile gxf, ProgressCallback callback) {
		super();
		this.generator = generator;
		this.gjf = gjf;
		String fileName = gjf.getFileName();
		this.moduleName = fileName.substring(0,
				fileName.lastIndexOf("Mapper.java"));
		if(this.moduleName != null) {
			this.moduleNameCal = this.getCalString(this.moduleName);
		}
		this.generator.setModuleName(moduleName);
		this.callback = callback;
		aoFieldNames = new ArrayList<String>();
		tableFieldNames = new ArrayList<String>();
		this.introspectedColumns = gxf.getBaseColumns();
		for(IntrospectedColumn tmpColumn : gxf.getBaseColumns()) {
			if(tmpColumn.getJavaProperty() != null){
				aoFieldNames.add(tmpColumn.getJavaProperty());
				tableFieldNames.add(tmpColumn.getActualColumnName());
			}
		}
	}
 	
	/**
	 * 最核心方法，负责所有的生成工作（通过调用私有方法完成）
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void startGenerateCodes(List<FieldConfig> listConfigs, List<FieldConfig> detailConfigs, String pageName, String moduleName, String pagePath, List<String> options) throws InterruptedException, IOException {
		
		//首先判断底层代码选项勾选情况，以便优先生成对应代码
		if(options != null && !options.isEmpty()) {
			if(options.contains(SelectTableDialog.OPT_GENERATE_DAO)) {
				this.generator.generateDAO(callback);
			}
			if(options.contains(SelectTableDialog.OPT_GENERATE_SEARCH)) {
				this.generator.generateSearch(callback);
			}
			if(options.contains(SelectTableDialog.OPT_GENERATE_SERVICE)) {
				this.generator.generateService2(callback);
			}
			if(options.contains(SelectTableDialog.OPT_GENERATE_CONTROLLER)) {
				this.generator.generateController(callback, pagePath);
			}
			
		}
 		//下面是开始处理2个页面的生成
		 Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID); 
		 URL url = bundle.getResource("/template"); 
		 String templatePath = FileLocator.toFileURL(url).getPath();
		 System.out.println("templatePath:" + templatePath);
		 String mainTargetFileName = this.moduleName.toLowerCase() + "_main.ftl";
		 String mainTemplateFileName = "main_page.ftl";
		 String detailTargetFileName = this.moduleName.toLowerCase() + "_detail.ftl";
		 String detailTemplateFileName = "detail_page.ftl";

		 Map<String,Object> detailParamMap = new HashMap<String, Object>();
		 detailParamMap.put("fields", detailConfigs);
		 detailParamMap.put("pageName", pageName);
		 detailParamMap.put("moduleName", moduleName);
		 detailParamMap.put("moduleNameCal", moduleNameCal);
		 if(needImgUpload(detailConfigs)) {
			 detailParamMap.put("needImgUpload", true);
		 } else {
			 detailParamMap.put("needImgUpload", false);
		 }
		 
		Map<String,Object> listParamMap = new HashMap<String, Object>();
		listParamMap.put("fields", listConfigs);
		listParamMap.put("moduleName", moduleName);
		listParamMap.put("moduleNameCal", moduleNameCal);
		listParamMap.put("pageName", pageName);
		List<FieldConfig> searchFields = new ArrayList<FieldConfig>();
		List<String> searchFieldNames = new ArrayList<String>();
		searchFieldNames.add("name");
		searchFieldNames.add("title");
		searchFieldNames.add("status");
		for(FieldConfig tmp : listConfigs) {
			if(searchFieldNames.contains(tmp.getName())) {
				searchFields.add(tmp);
			}
		}
		if(searchFields.size() > 0) {
			listParamMap.put("searchFields", searchFields);
		}
		try {
			genFile(templatePath, detailTemplateFileName, detailTargetFileName, detailParamMap, pagePath);
			genFile(templatePath, mainTemplateFileName, mainTargetFileName, listParamMap, pagePath);
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		//下面调用GenerateMVCViewName进行页面模板的对应路径生成
		try {
			 String projectPaht = this.gjf.getTargetProject();//:/Users/wangxuebiao/Documents/runtime-EclipseApplication/boonadmin/src/main/java/
			 String separator = File.separator;
			 projectPaht = projectPaht.replace(separator+"src"+ separator +"main"+ separator +"java" + separator, "");
			 File   file   =   new   File(projectPaht + separator +"target"+ separator +"classes");   
			 URLClassLoader   loader   =   new   URLClassLoader(new   URL[]   {  file.toURI().toURL() });   
			 Class   cls   =   loader.loadClass(this.generator.getControllerPackage() + ".GenerateMVCViewName");  
			Method method = cls.getDeclaredMethod("main", String[].class);
			method.invoke(null, (Object) new String[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}  
		this.freshAllFiles();
	}
	
	private boolean needImgUpload(List<FieldConfig> configs) {
		for(FieldConfig tmpConfig : configs) {
			if("图片".equals(tmpConfig.getHtmlType())) {
				return true;
			}
		}
		return false;
	}

	private void freshAllFiles() {
		try {
			ResourcesPlugin.getWorkspace().getRoot()
					.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void show() {
		if(generatePageConfigJDialog == null) {
			generatePageConfigJDialog = new GeneratePageConfigJDialog(this);
			generatePageConfigJDialog.addWindowListener(new java.awt.event.WindowAdapter() {
	              @Override
	              public void windowClosing(java.awt.event.WindowEvent e) {
	            	  generatePageConfigJDialog.setVisible(false);
	              }
           });
		}
		generatePageConfigJDialog.setBounds(300, 100, 940, 660);
		generatePageConfigJDialog.setVisible(true);
	}
	
	
	public void genFile(String templatePath, String templateFileName, String targetFileName, Map<String,Object> paramMap, String pagePath) throws IOException, TemplateException {
		
		 String projectPaht = this.gjf.getTargetProject();
		 String separator = File.separator;
		 String viewsPath = projectPaht.replace(separator+"java" + separator, separator+"webapp"+ separator +"WEB-INF"+ separator +"views" + separator + pagePath + separator);
		 File targetFile = new File(viewsPath, targetFileName);
		if (targetFile.exists()) {
			generator.getWarnings().add(
					getString("Warning.26", targetFile.getAbsolutePath())); //$NON-NLS-1$
			return;
		}
		String targetPath = targetFile.getAbsolutePath();
		
		File localFile = new File(targetPath);
		if (!localFile.exists()) {
			if (!localFile.getParentFile().exists())
				localFile.getParentFile().mkdirs();
			localFile.createNewFile();
		}

		OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(
				new FileOutputStream(localFile), "UTF-8");

		Configuration freemarkerConfigration = new Configuration();
		freemarkerConfigration.setDirectoryForTemplateLoading(new File(
				templatePath));

		Template localTemplate = freemarkerConfigration.getTemplate(
				templateFileName,  "UTF-8");
		localTemplate.process(paramMap, localOutputStreamWriter);

		localOutputStreamWriter.close();
		callback.startTask(getString("Progress.15", targetFileName)); //$NON-NLS-1$
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<String> getAoFieldNames() {
		return aoFieldNames;
	}

	public void setAoFieldNames(List<String> aoFieldNames) {
		this.aoFieldNames = aoFieldNames;
	}

	public List<String> getTableFieldNames() {
		return tableFieldNames;
	}

	public void setTableFieldNames(List<String> tableFieldNames) {
		this.tableFieldNames = tableFieldNames;
	}

	public List<IntrospectedColumn> getIntrospectedColumns() {
		return introspectedColumns;
	}

	public void setIntrospectedColumns(List<IntrospectedColumn> introspectedColumns) {
		this.introspectedColumns = introspectedColumns;
	}
	
	private String getCalString(String content) {
		if(content == null || content.isEmpty()) {
			return content;
		}
		return content.substring(0,1).toUpperCase() + content.substring(1).toLowerCase();
	}
	
}