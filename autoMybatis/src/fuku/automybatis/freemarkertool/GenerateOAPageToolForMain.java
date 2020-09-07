package fuku.automybatis.freemarkertool;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.mybatis.generator.api.ProgressCallback;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import fuku.automybatis.bean.BusinessModelAO;

public class GenerateOAPageToolForMain
{
	
	private BusinessModelAO model;
	private String templatePath;
	
	/**
	 * 几个最终需要用到的代码类对象，所谓代码类，就是为了生成一个类的代码，而自定义的类型CodeClass，尽量做到分解各种语法，做到所向无敌
	 */
	private ProgressCallback callback;
	
	public GenerateOAPageToolForMain(BusinessModelAO model, String templatePath) {
		super();
		this.templatePath = templatePath;
		this.model = model;
	}
 	
	/**
	 * 最核心方法，负责所有的生成工作（通过调用私有方法完成）
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void startGenerateCodes() throws InterruptedException, IOException {
		// 下面是开始处理页面的生成
		File templateDir = new File(templatePath);
		if(!templateDir.exists() || !templateDir.isDirectory()) {
			System.out.println("找不到模板目录文件夹：" + templatePath);
		}
		File[] templateFileList= templateDir.listFiles();
		if(templateFileList == null || templateFileList.length == 0) {
			System.out.println("找不到模板文件在模板文件夹：" + templatePath);
		}
		
		Properties p = new Properties();
		File configFile = new File(templatePath+File.separator + "config.properties");
		if(configFile.exists()) {
			p.load(new FileInputStream(templatePath+File.separator + "config.properties"));
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
	}

	public void genFile(String templatePath, String templateFileName, Map<String,Object> paramMap, String pagePath, Boolean needOverride) throws IOException, TemplateException {
		
		 String separator = File.separator;
		 String viewsPath =   templatePath.replace("business/template", "") + pagePath + separator;
		 File targetFile = new File(viewsPath);
		if (!needOverride && targetFile.exists()) {
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
		if(callback != null) {
			callback.startTask(getString("Progress.15", viewsPath)); //$NON-NLS-1$
		}
	}
}