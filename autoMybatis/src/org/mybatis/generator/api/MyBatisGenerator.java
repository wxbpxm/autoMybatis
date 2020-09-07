package org.mybatis.generator.api;

import static org.mybatis.generator.internal.util.ClassloaderUtility.getCustomClassloader;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.NullProgressCallback;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.XmlFileMergerJaxp;
import org.mybatis.generator.internal.db.ConnectionFactory;
import org.mybatis.generator.internal.util.ExtendAOGenerator;
import org.mybatis.generator.internal.util.FileGenerateUtil;
import org.mybatis.generator.internal.util.GenerateServiceTool;
import org.osgi.framework.Bundle;

import fuku.automybatis.Activator;
import fuku.automybatis.Tools;
import fuku.automybatis.bean.BusinessModelAO;
import fuku.automybatis.bean.BusinessPojoAO;
import fuku.automybatis.bean.BusinessValidationAO;
import fuku.automybatis.bean.BusinessViewAO;
import fuku.automybatis.freemarkertool.GenerateOAPageTool;
import fuku.automybatis.freemarkertool.GeneratePageTool;
import fuku.automybatis.ui.SelectTableDialog;
 
public class MyBatisGenerator {

	private Configuration configuration;

	private ShellCallback shellCallback;

	private List<GeneratedJavaFile> generatedJavaFiles;

	private List<GeneratedXmlFile> generatedXmlFiles;

	private List<String> warnings;
	private Set<String> projects;
	
	private Set<String> fullyQualifiedTableNames;
//	javaModelGenerator targetPackage="com.buding.webuy.appobj.generator" targetProject="" />
//    <sqlMapGenerator targetPackage="com.buding.webuy.db.generator" targetProject="" />
//    <javaClientGenerator targetPackage="com.buding.webuy.db.generator" targetProject="" type="XMLMAPPER" />
/////////////////////////////////////
	private Context context;
//	private JDBCConnectionConfiguration jdbcConnectionConfiguration;
	private JavaModelGeneratorConfiguration javaModelGeneratorConfiguration;
	private SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration;
//	private JavaClientGeneratorConfiguration javaClientGeneratorConfiguration;
	
	private String aoAndExamplePackage; //: com.buding.webuy.appobj.generator 
	private String mapperAndXmlPackage; //: com.buding.webuy.db.generator
	private String customerMaperPackage; //: com.buding.webuy.db.customer
	private String servicePackage; //: com.buding.webuy.service
	private String controllerPackage; //: com.buding.webuy.service
	private String extendAOPackage; //:com.buding.webuy.appobj.extend
	private String pojoPackage; //:com.buding.webuy.mongodb.generator
	private String aoAndExampleDir; //:/Users/wangxuebiao/Desktop/boonadmin/src/main/java/com/buding/webuy/appobj/generator
	private String dbdir;//:/Users/wangxuebiao/Desktop/boonadmin/src/main/java/com/buding/webuy/db/generator
/////////////////////////////////////
	private String moduleName;//Goods
/////////////////////////////////////
	
	/**
	 * Constructs a MyBatisGenerator object.
	 * 
	 * @param configuration
	 *            The configuration for this invocation
	 * @param shellCallback
	 *            an instance of a ShellCallback interface. You may specify
	 *            <code>null</code> in which case the DefaultShellCallback will
	 *            be used.
	 * @param warnings
	 *            Any warnings generated during execution will be added to this
	 *            list. Warnings do not affect the running of the tool, but they
	 *            may affect the results. A typical warning is an unsupported
	 *            data type. In that case, the column will be ignored and
	 *            generation will continue. You may specify <code>null</code> if
	 *            you do not want warnings returned.
	 * @throws InvalidConfigurationException
	 *             if the specified configuration is invalid
	 */
	public MyBatisGenerator(Configuration configuration,
			ShellCallback shellCallback, List<String> warnings)
			throws InvalidConfigurationException {
		super();
		if (configuration == null) {
			throw new IllegalArgumentException(getString("RuntimeError.2")); //$NON-NLS-1$
		} else {
			this.configuration = configuration;
		}

		if (shellCallback == null) {
			this.shellCallback = new DefaultShellCallback(false);
		} else {
			this.shellCallback = shellCallback;
		}

		if (warnings == null) {
			this.warnings = new ArrayList<String>();
		} else {
			this.warnings = warnings;
		}
		generatedJavaFiles = new ArrayList<GeneratedJavaFile>();
		generatedXmlFiles = new ArrayList<GeneratedXmlFile>();
		projects = new HashSet<String>();

		this.configuration.validate();
		
		//这里默认都只有一个context，取第一个把常用配置信息读取到实体变量
		if(this.configuration.getContexts().size() > 0) {
			this.context = this.configuration.getContexts().get(0);
//			this.jdbcConnectionConfiguration = this.context.getJdbcConnectionConfiguration();
			this.javaModelGeneratorConfiguration = this.context.getJavaModelGeneratorConfiguration();
			this.sqlMapGeneratorConfiguration = this.context.getSqlMapGeneratorConfiguration();
//		    this.javaClientGeneratorConfiguration = this.context.getJavaClientGeneratorConfiguration();
		    this.aoAndExamplePackage = this.javaModelGeneratorConfiguration.getTargetPackage();
		    this.mapperAndXmlPackage = this.sqlMapGeneratorConfiguration.getTargetPackage();
		    this.extendAOPackage = this.aoAndExamplePackage.replace("generator", "extend");
		    this.pojoPackage = this.extendAOPackage.replace("appobj.extend", "mongodb.generator");
		    this.customerMaperPackage = this.mapperAndXmlPackage.substring(0, this.mapperAndXmlPackage.lastIndexOf(".") + 1) + "customer";
		    this.servicePackage = this.aoAndExamplePackage.substring(0, this.aoAndExamplePackage.lastIndexOf("appobj.generator")) + "service.all"; 
		    this.controllerPackage = this.aoAndExamplePackage.substring(0, this.aoAndExamplePackage.lastIndexOf("appobj.generator")) + "mvc"; 
			File directory; 
			try {
				directory = shellCallback.getDirectory(
						this.javaModelGeneratorConfiguration.getTargetProject(), aoAndExamplePackage);
				this.aoAndExampleDir = directory.getPath();
				
				directory = shellCallback.getDirectory(
						this.sqlMapGeneratorConfiguration.getTargetProject(), this.sqlMapGeneratorConfiguration.getTargetPackage());
				this.dbdir = directory.getPath();
			} catch (ShellException e) {
				e.printStackTrace();
				warnings.add(e.getMessage());
			}
			
		}
		
	}

	/**
	 * This is the main method for generating code. This method is long running,
	 * but progress can be provided and the method can be canceled through the
	 * ProgressCallback interface. This version of the method runs all
	 * configured contexts.
	 * 
	 * @param callback
	 *            an instance of the ProgressCallback interface, or
	 *            <code>null</code> if you do not require progress information
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException
	 *             if the method is canceled through the ProgressCallback
	 */
	public void generate(ProgressCallback callback) throws SQLException,
			IOException, InterruptedException {
		generate(callback, null, null, null);
	}

	/**
	 * This is the main method for generating code. This method is long running,
	 * but progress can be provided and the method can be canceled through the
	 * ProgressCallback interface.
	 * 
	 * @param callback
	 *            an instance of the ProgressCallback interface, or
	 *            <code>null</code> if you do not require progress information
	 * @param contextIds
	 *            a set of Strings containing context ids to run. Only the
	 *            contexts with an id specified in this list will be run. If the
	 *            list is null or empty, than all contexts are run.
	 * @throws InvalidConfigurationException
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException
	 *             if the method is canceled through the ProgressCallback
	 */
	public void generate(ProgressCallback callback, Set<String> contextIds)
			throws SQLException, IOException, InterruptedException {
		generate(callback, contextIds, null, null);
	}

	/**
	 * This is the main method for generating code. This method is long running,
	 * but progress can be provided and the method can be cancelled through the
	 * ProgressCallback interface.
	 * 
	 * @param callback
	 *            an instance of the ProgressCallback interface, or
	 *            <code>null</code> if you do not require progress information
	 * @param contextIds
	 *            a set of Strings containing context ids to run. Only the
	 *            contexts with an id specified in this list will be run. If the
	 *            list is null or empty, than all contexts are run.
	 * @param fullyQualifiedTableNames
	 *            a set of table names to generate. The elements of the set must
	 *            be Strings that exactly match what's specified in the
	 *            configuration. For example, if table name = "foo" and schema =
	 *            "bar", then the fully qualified table name is "foo.bar". If
	 *            the Set is null or empty, then all tables in the configuration
	 *            will be used for code generation.
	 * @throws InvalidConfigurationException
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException
	 *             if the method is canceled through the ProgressCallback
	 */
	public void generate(ProgressCallback callback, Set<String> contextIds,
			Set<String> fullyQualifiedTableNames, String opt)
			throws SQLException, IOException, InterruptedException {

		if (callback == null) {
			callback = new NullProgressCallback();
		}
		this.fullyQualifiedTableNames = fullyQualifiedTableNames;
		int size = fullyQualifiedTableNames.size();
		if(size == 0) {
			warnings.add("至少选择一个文件！");
			return;
		}
		
		generatedJavaFiles.clear();
		generatedXmlFiles.clear();

		// calculate the contexts to run
		List<Context> contextsToRun;
		if (contextIds == null || contextIds.size() == 0) {
			contextsToRun = configuration.getContexts();
		} else {
			contextsToRun = new ArrayList<Context>();
			for (Context context : configuration.getContexts()) {
				if (contextIds.contains(context.getId())) {
					contextsToRun.add(context);
				}
			}
		}

		// setup custom classloader if required
		if (configuration.getClassPathEntries().size() > 0) {
			ClassLoader classLoader = getCustomClassloader(configuration
					.getClassPathEntries());
			ObjectFactory.setExternalClassLoader(classLoader);
		}

		// now run the introspections...
		int totalSteps = 0;
		for (Context context : contextsToRun) {
			totalSteps += context.getIntrospectionSteps();
		}
		callback.introspectionStarted(totalSteps);

		for (Context context : contextsToRun) {
			context.introspectTables(callback, warnings,
					fullyQualifiedTableNames);
		}

		// now run the generates
		totalSteps = 0;
		for (Context context : contextsToRun) {
			totalSteps += context.getGenerationSteps();
		}
		callback.generationStarted(totalSteps);
		for (Context context : contextsToRun) {
			context.generateFiles(callback, generatedJavaFiles,
					generatedXmlFiles, warnings);
		}
		
		// now save the files
		callback.saveStarted(generatedXmlFiles.size()
				+ generatedJavaFiles.size());
		
		
		// 下面根据opt的值进行区别看待
		if (SelectTableDialog.OPT_GENERATE_DAO.equals(opt) || opt == null) {// dao层
			generateDAO(callback);
		} else if (SelectTableDialog.OPT_GENERATE_CUSTOMMAPPER.equals(opt)) {// 自定义mapper
			generateCustomerMapper(callback);
		} else if (SelectTableDialog.OPT_GENERATE_SEARCH.equals(opt)) {// Search对象的生成
			generateSearch(callback);
		} else if (SelectTableDialog.OPT_GENERATE_SERVICE.equals(opt)) { //生产service
			//必须只能选择一个表，也就是只能同时生成一个模块的service
			if(size > 1) {
				warnings.add("只能选择一个表，来进行生成一个模块的service！");
				return;
			}
			generateService(callback);
		} else if (SelectTableDialog.OPT_GENERATE_PAGE.equals(opt)) {//生产页面
			//必须只能选择一个表，也就是只能同时生成一个模块的页面
			if(size > 1) {
				warnings.add("只能选择一个表，来进行生成一个模块的页面！");
				return;
			}
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID); 
			 URL url = bundle.getResource("/template"); 
			 warnings.add("url:" + url);
			 String templatePath = FileLocator.toFileURL(url).getPath();
			 warnings.add("templatePath:" + templatePath);
			 System.out.println("templatePath:" + templatePath);
			new GeneratePageTool(this, getMapperGJF(), getMapperXML(), callback).show();
		} else if (SelectTableDialog.OPT_GENERATE_CONTROLLER.equals(opt)) {// 控制器对象的生成
			generateController(callback, moduleName.toLowerCase());
		}  else if (SelectTableDialog.OPT_GENERATE_POJO.equals(opt)) { //生产POJO
			//必须只能选择一个表，也就是只能同时生成一个模块的pojo
			if(size > 1) {
				warnings.add("只能选择一个表，来进行生成一个模块的POJO！");
				return;
			}
			generatePojo(callback);
		} else if (SelectTableDialog.OPT_GENERATE_OA_PAGE.equals(opt)) { //生产POJO
			//必须只能选择一个表，也就是只能同时生成一个模块的OA页面
			if(size > 1) {
				warnings.add("只能选择一个表，来进行生成一个模块的OA页面！");
				return;
			}
			generateOAPage(callback);
		}

		for (String project : projects) {
			shellCallback.refreshProject(project);
		}

		callback.done();
	}
	
	public void generateDAO(ProgressCallback callback) throws InterruptedException, IOException {
		// 处理java文件
		handleJava(callback);
		
		// 处理xml文件
		handleXml(callback);
		
		// 处理继承AO类
		ExtendAOGenerator generator = new ExtendAOGenerator(this.dbdir,
			this.aoAndExampleDir, this.aoAndExamplePackage, this, this.generatedJavaFiles, this.generatedXmlFiles);
		generator.generator();
	}
	public void generateCustomerMapper(ProgressCallback callback) throws InterruptedException, IOException {
		// 处理java文件
		handleCustomerMapperJava(callback);
					
		// 处理xml文件
		handleCustomerMapperXml(callback);
	}
	public void generateSearch(ProgressCallback callback) throws InterruptedException, IOException {
		// 处理java文件
		handleSearchJava(callback);
	}
	
	public void generateController(ProgressCallback callback, String pagePath) throws InterruptedException, IOException {
		// 处理java文件
		handleControllerJava(callback, pagePath);
	}
	
	private void generateService(ProgressCallback callback) throws InterruptedException, IOException {
		new GenerateServiceTool(this, getMapperGJF(), getMapperXML(), callback).show();
	}
	
	private void generatePojo(ProgressCallback callback) throws InterruptedException, IOException {
		List<Context> contexts = configuration.getContexts();
		if(contexts!=null && contexts.size()>0 && contexts.get(0)!=null){
     		JDBCConnectionConfiguration jdbcConfig = contexts.get(0).getJdbcConnectionConfiguration();
     		Connection con = null;
     		Statement statement = null;
     		PreparedStatement statement2 = null;
     		ResultSet rs = null;
     		ResultSet rs2 = null;
     		try {
     			con = ConnectionFactory.getInstance().getConnection(
     					jdbcConfig); 
     			statement = con.createStatement();
     		    for (String modelName : fullyQualifiedTableNames) {
     		    	String tableName = modelName.substring(modelName.indexOf("|")+1);
     		    	String sql = "select id, name, table_name, add_view, edit_view, approve_view, gen_dir, pojo_name, show_view from t_business_model where table_name = '" + tableName +"'";
     		    	System.out.println("sql:"+sql);
	       		   rs = statement.executeQuery(sql);
	       		   BusinessModelAO model = new BusinessModelAO();
	       		   if(rs.next()) {
	       			  model.setId(rs.getString(1));
	       			  model.setName(rs.getString(2));
	       			  model.setTableName(rs.getString(3));
	       			  model.setAddView(rs.getString(4));
	       			  model.setEditView(rs.getString(5));
	       			  model.setApproveView(rs.getString(6));
	       			  model.setGenDir(rs.getString(7));
	       			  model.setPojoName(rs.getString(8));
	       			  model.setShowView(rs.getString(9));
	       		   } else {
	       			   Tools.writeLine("Cant find the business model for name:" + modelName);
	       			   return;
	       		   }
	       		    
		       		String sql2 = "select id, business_id, pojo_id, column_name, java_type, sort, column_remark from t_business_pojo where business_id = ? order by sort";
	     			statement2 = con.prepareStatement(sql2);
	     			statement2.setString(1, model.getId());
	     			rs2 = statement2.executeQuery();
	     			List<BusinessPojoAO> pojoList = new ArrayList<>();
     				while(rs2.next()) {
	     				BusinessPojoAO pojo = new BusinessPojoAO();
	     				pojo.setId(rs2.getString(1));
	     				pojo.setBusinessId(rs2.getString(2));
	     				pojo.setPojoId(rs2.getString(3));
	     				pojo.setColumnName(rs2.getString(4));
	     				pojo.setJavaType(rs2.getString(5));
	     				pojo.setSort(rs2.getInt(6));
	     				pojo.setColumnRemark(rs2.getString(7));
	     				pojoList.add(pojo);
  	       		   }
     				model.setPojoList(pojoList);
     				GeneratedJavaFile usedGif = FileGenerateUtil.getPojoJava(model, javaModelGeneratorConfiguration.getTargetProject());
     				//写文件
     				writeGenerateFile(usedGif, callback, true);
     			}
     		   con.close();
 		   } catch (Exception e) {
 			   e.printStackTrace();
     		   try {
     			   if(con!=null) {
     				   con.close();
     			   }
     			   if(statement!=null) {
     				  statement.close();
     			   }
     			   if(statement2!=null) {
     				   statement2.close();
     			   }
     			  
     		   } catch (SQLException e1) {
     				Tools.writeLine(e1.getLocalizedMessage());
     		   }
     		   Tools.writeLine(e.getLocalizedMessage());
 		   }
     	}else {
        	Tools.writeLine("The xml file is incorrect.");
	    	return;
        }
	}
	
	private void generateOAPage(ProgressCallback callback) throws InterruptedException, IOException {
		List<Context> contexts = configuration.getContexts();
		if(contexts!=null && contexts.size()>0 && contexts.get(0)!=null){
			JDBCConnectionConfiguration jdbcConfig = contexts.get(0).getJdbcConnectionConfiguration();
			Connection con = null;
			Statement statement = null;
			PreparedStatement statement2 = null;
			PreparedStatement statement3 = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			try {
				con = ConnectionFactory.getInstance().getConnection(
						jdbcConfig); 
				statement = con.createStatement();
				for (String modelName : fullyQualifiedTableNames) {
					String tableName = modelName.substring(modelName.indexOf("|")+1);
					String sql = "select id, name, table_name, add_view, edit_view, approve_view, gen_dir, pojo_name, show_view from t_business_model where table_name = '" + tableName +"'";
					System.out.println("sql:"+sql);
					rs = statement.executeQuery(sql);
					BusinessModelAO model = new BusinessModelAO();
					if(rs.next()) {
						model.setId(rs.getString(1));
						model.setName(rs.getString(2));
						model.setTableName(rs.getString(3));
						model.setAddView(rs.getString(4));
						model.setEditView(rs.getString(5));
						model.setApproveView(rs.getString(6));
						model.setGenDir(rs.getString(7));
						model.setPojoName(rs.getString(8));
						model.setShowView(rs.getString(9));
					} else {
						Tools.writeLine("Cant find the business model for name:" + modelName);
						return;
					}
					
					String sql2 = "select id, business_id, view_type, column_name, widget, sort, column_remark, style, param from t_business_view where business_id = ? order by sort";
					statement2 = con.prepareStatement(sql2);
					statement2.setString(1, model.getId());
					rs2 = statement2.executeQuery();
					List<BusinessViewAO> viewList = new ArrayList<>();
					while(rs2.next()) {
						BusinessViewAO view = new BusinessViewAO();
						view.setId(rs2.getString(1));
						view.setBusinessId(rs2.getString(2));
						view.setViewType(rs2.getString(3));
						view.setColumnName(rs2.getString(4));
						view.setWidget(rs2.getString(5));
						view.setSort(rs2.getInt(6));
						view.setColumnRemark(rs2.getString(7));
						view.setStyle(rs2.getString(8));
						view.setParam(rs2.getString(9));
						viewList.add(view);
					}
					model.setViewList(viewList);
					String sql3 = "select id, business_id, view_type, column_name, column_remark, clazz, sort, param from t_business_validation where business_id = ? order by sort";
					statement3 = con.prepareStatement(sql3);
					statement3.setString(1, model.getId());
					ResultSet rs3 = statement3.executeQuery();
					List<BusinessValidationAO> validationList = new ArrayList<>();
					while(rs3.next()) {
						BusinessValidationAO validation = new BusinessValidationAO();
						validation.setId(rs3.getString(1));
						validation.setBusinessId(rs3.getString(2));
						validation.setViewType(rs3.getString(3));
						validation.setColumnName(rs3.getString(4));
						validation.setColumnRemark(rs3.getString(5));
						validation.setClazz(rs3.getString(6));
						validation.setSort(rs3.getInt(7));
						validation.setParam(rs3.getString(8));
						validationList.add(validation);
					}
					model.setValidationList(validationList);
					new GenerateOAPageTool(this, model, this.pojoPackage, javaModelGeneratorConfiguration.getTargetProject(), callback).startGenerateCodes();;
//					FileGenerateUtil.getPojoJava(model, this.pojoPackage, javaModelGeneratorConfiguration.getTargetProject());
				}
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if(con!=null) {
						con.close();
					}
					if(statement!=null) {
						statement.close();
					}
					if(statement2!=null) {
						statement2.close();
					}
					
				} catch (SQLException e1) {
					Tools.writeLine(e1.getLocalizedMessage());
				}
				Tools.writeLine(e.getLocalizedMessage());
			}
		}else {
			Tools.writeLine("The xml file is incorrect.");
			return;
		}
	}
	
	public void generateService2(ProgressCallback callback) throws InterruptedException, IOException {
		new GenerateServiceTool(this, getMapperGJF(), getMapperXML(), callback).startGenerateCodes(true);
	}

	private GeneratedJavaFile getMapperGJF() {
		for(GeneratedJavaFile tmpGJF : this.generatedJavaFiles) {
			if(tmpGJF.getFileName().endsWith("Mapper.java")) {
				return tmpGJF;
			}
		}
		return null;
	}
	
	private GeneratedXmlFile getMapperXML() {
		for(GeneratedXmlFile tmpGXF : this.generatedXmlFiles) {
			if(tmpGXF.getFileName().endsWith("Mapper.xml")) {
				return tmpGXF;
			}
		}
		return null;
	}

	private void handleSearchJava(ProgressCallback callback)
			throws InterruptedException, IOException {
		for (GeneratedJavaFile gjf : generatedJavaFiles) {
			projects.add(gjf.getTargetProject());
			GeneratedJavaFile usedGif = null;
			if (gjf.getFileName().contains("Mapper")) {
				this.moduleName = gjf.getFileName().substring(0,gjf.getFileName().lastIndexOf("Mapper.java"));
				usedGif = FileGenerateUtil.getSearchJava(gjf, this.extendAOPackage, this.aoAndExamplePackage, this.moduleName);
			} else {
				continue;
			}
			//写文件
			writeGenerateFile(usedGif, callback, false);
		}
	}
	
	private void handleControllerJava(ProgressCallback callback, String pagePath)
			throws InterruptedException, IOException {
		int i = 0;
		for (GeneratedJavaFile gjf : generatedJavaFiles) {
			projects.add(gjf.getTargetProject());
			GeneratedJavaFile usedGif = null;
			if (gjf.getFileName().contains("Mapper")) {
				this.moduleName = gjf.getFileName().substring(0,gjf.getFileName().lastIndexOf("Mapper.java"));
				usedGif = FileGenerateUtil.getControllerJava(gjf, this.extendAOPackage, this.controllerPackage, this.servicePackage, pagePath, this.moduleName, this.generatedXmlFiles.get(i).getBaseColumns());
			} else {
				continue;
			}
			//写文件
			writeGenerateFile(usedGif, callback, false);
			i++;
		}
	}
	
	private void handleCustomerMapperJava(ProgressCallback callback)
			throws InterruptedException, IOException {
		for (GeneratedJavaFile gjf : generatedJavaFiles) {
			projects.add(gjf.getTargetProject());
			String fileName = gjf.getFileName();
			GeneratedJavaFile usedGif = null;
			if (gjf.getFileName().contains("Mapper")) {
				fileName = fileName.substring(0,
						fileName.lastIndexOf("Mapper.java"))
						+ "CustomMapper.java";
				this.moduleName = gjf.getFileName().substring(0,gjf.getFileName().lastIndexOf("Mapper.java"));
				usedGif = FileGenerateUtil.getCustomMapperJava(gjf, this.customerMaperPackage, this.extendAOPackage, this.moduleName);
			} else {
				continue;
			}
			//写文件
			writeGenerateFile(usedGif, callback, false);
		}
	}
	
	
	private void handleCustomerMapperXml(ProgressCallback callback)
			throws InterruptedException, IOException {
		for (GeneratedXmlFile gxf : generatedXmlFiles) {
			projects.add(gxf.getTargetProject());

			String targetPackage = this.customerMaperPackage;
			String fileName = gxf.getFileName();
			this.moduleName = fileName.substring(0, fileName.lastIndexOf("Mapper.xml"));
			GeneratedXmlFile usedgxf = null;
			usedgxf = FileGenerateUtil.getCustomMapperXml(gxf, this.extendAOPackage, targetPackage, this.moduleName);
			this.writeGenerateFile(usedgxf, callback, false);
		}

	}

	private void handleXml(ProgressCallback callback)
			throws InterruptedException, IOException {
		for (GeneratedXmlFile gxf : generatedXmlFiles) {
			projects.add(gxf.getTargetProject());

			File targetFile;
			String source;
			try {
				File directory = shellCallback.getDirectory(
						gxf.getTargetProject(), gxf.getTargetPackage());
				targetFile = new File(directory, gxf.getFileName());
				if (targetFile.exists()) {
					if (gxf.isMergeable()) {
						source = XmlFileMergerJaxp.getMergedSource(gxf,
								targetFile);
					} else if (shellCallback.isOverwriteEnabled()) {
						source = gxf.getFormattedContent();
						warnings.add(getString("Warning.11", //$NON-NLS-1$
								targetFile.getAbsolutePath()));
					} else {
						source = gxf.getFormattedContent();
						targetFile = getUniqueFileName(directory,
								gxf.getFileName());
						warnings.add(getString(
								"Warning.2", targetFile.getAbsolutePath())); //$NON-NLS-1$
					}
				} else {
					source = gxf.getFormattedContent();
				}
			} catch (ShellException e) {
				warnings.add(e.getMessage());
				continue;
			}

			callback.checkCancel();
			callback.startTask(getString("Progress.15", targetFile.getName())); //$NON-NLS-1$
			writeFile(targetFile, source);
		}

	}
	
	private void handleJava(ProgressCallback callback)
			throws InterruptedException, IOException {
		for (GeneratedJavaFile gjf : generatedJavaFiles) {
			projects.add(gjf.getTargetProject());

			File targetFile;
			String source;
			try {
				File directory = shellCallback.getDirectory(
						gjf.getTargetProject(), gjf.getTargetPackage());
				targetFile = new File(directory, gjf.getFileName());
				if (targetFile.exists()) {
					if (shellCallback.isMergeSupported()) {
						source = shellCallback.mergeJavaFile(
								gjf.getFormattedContent(),
								targetFile.getAbsolutePath(),
								MergeConstants.OLD_ELEMENT_TAGS);
					} else if (shellCallback.isOverwriteEnabled()) {
						source = gjf.getFormattedContent();
						warnings.add(getString("Warning.11", //$NON-NLS-1$
								targetFile.getAbsolutePath()));
					} else {
						source = gjf.getFormattedContent();
						targetFile = getUniqueFileName(directory,
								gjf.getFileName());
						warnings.add(getString(
								"Warning.2", targetFile.getAbsolutePath())); //$NON-NLS-1$
					}
				} else {
					source = gjf.getFormattedContent();
				}

				callback.checkCancel();
				callback.startTask(getString(
						"Progress.15", targetFile.getName())); //$NON-NLS-1$
				writeFile(targetFile, source);
			} catch (ShellException e) {
				warnings.add(e.getMessage());
			}
		}
	}

	/**
	 * Writes, or overwrites, the contents of the specified file
	 * 
	 * @param file
	 * @param content
	 */
	public void writeFile(File file, String content) throws IOException {
//		BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		bw.write(content);
		bw.close();
	}

	private File getUniqueFileName(File directory, String fileName) {
		File answer = null;

		// try up to 1000 times to generate a unique file name
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < 1000; i++) {
			sb.setLength(0);
			sb.append(fileName);
			sb.append('.');
			sb.append(i);

			File testFile = new File(directory, sb.toString());
			if (!testFile.exists()) {
				answer = testFile;
				break;
			}
		}

		if (answer == null) {
			throw new RuntimeException(getString(
					"RuntimeError.3", directory.getAbsolutePath())); //$NON-NLS-1$
		}

		return answer;
	}
	
 
	
	private void writeGenerateFile(GeneratedFile usedGif, ProgressCallback callback, Boolean overwrite) throws InterruptedException, IOException {
		File targetFile = null;
		String source = null;
		try {
			File directory = shellCallback.getDirectory(
					usedGif.getTargetProject(), usedGif.getTargetPackage());
			this.dbdir = directory.getPath();
			targetFile = new File(directory, usedGif.getFileName());
			if(overwrite) {
				source = usedGif.getFormattedContent();
			} else {
				if (targetFile.exists()) {
					warnings.add(getString(
							"Warning.26", targetFile.getAbsolutePath())); //$NON-NLS-1$
					return;
				} else {
					source = usedGif.getFormattedContent();
				}
			}

			callback.checkCancel();
			callback.startTask(getString(
					"Progress.15", targetFile.getName())); //$NON-NLS-1$
			if(source != null) {
				writeFile(targetFile, source);
			}
		} catch (ShellException e) {
			warnings.add(e.getMessage());
		}
	}

	public String getAoAndExamplePackage() {
		return aoAndExamplePackage;
	}

	public void setAoAndExamplePackage(String aoAndExamplePackage) {
		this.aoAndExamplePackage = aoAndExamplePackage;
	}

	public String getExtendAOPackage() {
		return extendAOPackage;
	}

	public void setExtendAOPackage(String extendAOPackage) {
		this.extendAOPackage = extendAOPackage;
	}

	public String getAoAndExampleDir() {
		return aoAndExampleDir;
	}

	public void setAoAndExampleDir(String aoAndExampleDir) {
		this.aoAndExampleDir = aoAndExampleDir;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getMapperAndXmlPackage() {
		return mapperAndXmlPackage;
	}

	public void setMapperAndXmlPackage(String mapperAndXmlPackage) {
		this.mapperAndXmlPackage = mapperAndXmlPackage;
	}

	public ShellCallback getShellCallback() {
		return shellCallback;
	}

	public void setShellCallback(ShellCallback shellCallback) {
		this.shellCallback = shellCallback;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public String getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}

	public String getControllerPackage() {
		return controllerPackage;
	}

	public void setControllerPackage(String controllerPackage) {
		this.controllerPackage = controllerPackage;
	}
	
	
}
