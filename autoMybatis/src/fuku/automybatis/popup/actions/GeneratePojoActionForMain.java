package fuku.automybatis.popup.actions;

import static org.mybatis.generator.internal.util.ClassloaderUtility.getCustomClassloader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.mybatis.generator.api.GeneratedFile;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.db.ConnectionFactory;
import org.mybatis.generator.internal.util.FileGenerateUtil;

import fuku.automybatis.bean.BusinessModelAO;
import fuku.automybatis.bean.BusinessPojoAO;

public class GeneratePojoActionForMain{
	private Shell shell;
	/**
	 * Constructor for Action1.
	 */
	public GeneratePojoActionForMain() {
		super();
	}
 

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	public void start(String configPath, String tartDir, String tableName) {
        try {
			if (configPath != null && !configPath.isEmpty()) {  
			     File configurationFile = new File(configPath);  
			     //String path = null;  
			     // common resource file  
			     if (configurationFile.exists()) {  
			     	ConfigurationParser cp = new ConfigurationParser(null);
			     	Configuration config = cp.parseConfiguration(configurationFile);
			     	List<Context> contexts = config.getContexts();
			     	if (config.getClassPathEntries().size() > 0) {
			            ClassLoader classLoader = getCustomClassloader(config.getClassPathEntries());
			            ObjectFactory.setExternalClassLoader(classLoader);
			        }else {
			        	System.out.println("The xml file is invalid.");
				    	return;
			        }
			     	if(contexts!=null && contexts.size()>0 && contexts.get(0)!=null){
			     		generatePojo(contexts, tartDir, tableName);
			     	}else {
			        	System.out.println("The xml file is incorrect.");
				    	return;
			        }
			     }else {
		        	System.err.println("The path is invalid.");
			    	return;
		        }
			 }else {
	        	System.err.println("The path is invalid.");
		    	return;
	        }
		} catch (Exception e) {
			System.err.println("The path is invalid.");
		}
	}
	
	
	private void generatePojo(List<Context> contexts, String tartDir, String tableName) throws InterruptedException, IOException {
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
       			   System.out.println("Cant find the business model for name:" + tableName);
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
 				String projectPath = this.getClass().getResource("/").getFile().toString().replace("/target/classes", "");
 				GeneratedJavaFile usedGif = FileGenerateUtil.getPojoJava(model, projectPath);
 				//写文件
 				writeGenerateFile(usedGif, tartDir, true);
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
     				System.out.println(e1.getLocalizedMessage());
     		   }
     		   System.out.println(e.getLocalizedMessage());
 		   }
     	}else {
        	System.out.println("The xml file is incorrect.");
	    	return;
        }
	}
	
	private void writeGenerateFile(GeneratedFile usedGif, String tartDir, Boolean overwrite) throws InterruptedException, IOException {
		File targetFile = null;
		String source = null;
		try {
			targetFile = new File(tartDir, usedGif.getFileName());
			if(overwrite) {
				source = usedGif.getFormattedContent();
			} else {
				if (targetFile.exists()) {
					return;
				} else {
					source = usedGif.getFormattedContent();
				}
			}
			if(source != null) {
				writeFile(targetFile, source);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeFile(File file, String content) throws IOException {
//		BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		bw.write(content);
		bw.close();
	}
}
