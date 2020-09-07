package fuku.automybatis.freemarkertool;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.osgi.framework.Bundle;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import fuku.automybatis.Activator;
import fuku.automybatis.Tools;
import fuku.automybatis.bean.BusinessModelAO;

public class GenerateOAPageTool
{
	
	/** 模块名称，如：DesktopElement */	
	private String targetProject;
	private MyBatisGenerator generator;
	private BusinessModelAO model;
	
	/**
	 * 几个最终需要用到的代码类对象，所谓代码类，就是为了生成一个类的代码，而自定义的类型CodeClass，尽量做到分解各种语法，做到所向无敌
	 */
	private ProgressCallback callback;
	
	public GenerateOAPageTool(MyBatisGenerator generator,BusinessModelAO model, String pojoPackage, String targetProject,
			 ProgressCallback callback) {
		super();
		this.generator = generator;
		this.targetProject = targetProject;
		this.model = model;
		this.callback = callback;
	}
 	
	/**
	 * 最核心方法，负责所有的生成工作（通过调用私有方法完成）
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void startGenerateCodes() throws InterruptedException, IOException {
		// 下面是开始处理页面的生成
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL url = bundle.getResource("/oapagetemplate");
		String templatePath = FileLocator.toFileURL(url).getPath();
		templatePath = targetProject.replace("java"+File.separator, "") + "webapp"+ File.separator +"WEB-INF"
				+ File.separator +"views"+ File.separator +"business"+ File.separator +"template"; 
		
		File templateDir = new File(templatePath);
		if(!templateDir.exists() || !templateDir.isDirectory()) {
			Tools.writeLine("找不到模板目录文件夹：" + templatePath);
		}
		File[] templateFileList= templateDir.listFiles();
		if(templateFileList == null || templateFileList.length == 0) {
			Tools.writeLine("找不到模板文件在模板文件夹：" + templatePath);
		}
		
		Properties p = new Properties();
		File configFile = new File(templatePath+File.separator + "config.properties");
		if(configFile.exists()) {
			p.load(new FileInputStream(templatePath+File.separator + "config.properties"));
		} else {
			p.load(this.getClass().getResourceAsStream("/oapagetemplate/config.properties"));
		}
		
		Enumeration<?> keys = p.keys();
		Map<String, String> configMap = new HashMap<String, String>();
		while(keys.hasMoreElements()) {
			String templateName = (String) keys.nextElement();
			if(templateName != null && !templateName.isEmpty()) {
				String value = p.getProperty(templateName);
				configMap.put(templateName, value);
			}
		}
		
		for(File tmpTemplateFile : templateFileList) {
			if(!tmpTemplateFile.isFile() 
					|| !tmpTemplateFile.getName().endsWith(".ftl")) {
				continue;
			}
			String templateName =  tmpTemplateFile.getName();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("model", this.model);
			String pagePath = "business"+ File.separator + this.model.getGenDir() + File.separator + templateName;
			if("add.ftl".equals(templateName)) {
				pagePath = "business" + File.separator + this.model.getGenDir() + File.separator + this.model.getAddView() + ".ftl";
			} else if("approve.ftl".equals(templateName)) {
				pagePath = "business" + File.separator + this.model.getGenDir() + File.separator + this.model.getApproveView() + ".ftl";
			}
			
			try {
				Boolean needOverride = false;
				String config = configMap.get(templateName);
				if(config != null && config.contains("override=true")) {
					needOverride = true;
				}
				genFile(templatePath, templateName, paramMap, pagePath, needOverride);
			} catch (TemplateException e) {
				e.printStackTrace();
			}
		}
		
		
		// 下面调用GenerateMVCViewName进行页面模板的对应路径生成
		try {
			String projectPaht = this.targetProject;// :/Users/wangxuebiao/Documents/runtime-EclipseApplication/boonadmin/src/main/java/
			String separator = File.separator;
			projectPaht = projectPaht.replace(separator + "src" + separator + "main" + separator + "java" + separator,
					"");
			File file = new File(projectPaht + separator + "target" + separator + "classes");
			URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI().toURL() });
			Class<?> cls = loader.loadClass(this.generator.getControllerPackage() + ".GenerateMVCViewName");
			Method method = cls.getDeclaredMethod("main", String[].class);
			method.invoke(null, (Object) new String[] {});
			loader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.freshAllFiles();
	}

	private void freshAllFiles() {
		try {
			ResourcesPlugin.getWorkspace().getRoot()
					.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void genFile(String templatePath, String templateFileName, Map<String,Object> paramMap, String pagePath, Boolean needOverride) throws IOException, TemplateException {
		
		 String projectPaht = targetProject;
		 String separator = File.separator;
		 String viewsPath = projectPaht.replace(separator+"java" + separator, separator+"webapp"+ separator +"WEB-INF"+ separator +"views" + separator + pagePath + separator);
		 File targetFile = new File(viewsPath);
		if (!needOverride && targetFile.exists()) {
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
		callback.startTask(getString("Progress.15", viewsPath)); //$NON-NLS-1$
	}

//	private String getCalString(String content) {
//		if(content == null || content.isEmpty()) {
//			return content;
//		}
//		return content.substring(0,1).toUpperCase() + content.substring(1).toLowerCase();
//	}
	
}