package fuku.automybatis.popup.actions;

import static org.mybatis.generator.internal.util.ClassloaderUtility.getCustomClassloader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.db.ConnectionFactory;

import fuku.automybatis.bean.BusinessModelAO;
import fuku.automybatis.bean.BusinessValidationAO;
import fuku.automybatis.bean.BusinessViewAO;
import fuku.automybatis.freemarkertool.GenerateOAPageToolForMain;

public class GenerateOAPageActionForMain{

	/**
	 * Constructor for Action1.
	 */
	public GenerateOAPageActionForMain() {
		super();
	}
 
	
	/**
	 */
	public void start(String configPath, String templatePath, String tableName) {
		try {
			if (configPath != null && !configPath.isEmpty()) {
				File configurationFile = new File(configPath);  
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
						generateOAPage(contexts, tableName, templatePath);
					}else {
						System.out.println("The xml file is incorrect.");
						return;
					}
				}else {
					System.out.println("The selected file is invalid.");
					return;
				}
			}else {
				System.out.println("The selected file is invalid.");
				return;
			}
		} catch (Exception e) {
			System.out.println("The selected file is invalid.");
		}
	}

	 
	private void generateOAPage(List<Context> contexts, String tableName, String templatePath) throws InterruptedException, IOException {
		
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
						System.err.println("Cant find the business model for name:" + tableName);
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
					new GenerateOAPageToolForMain(model, templatePath).startGenerateCodes();
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
					System.err.println(e1.getLocalizedMessage());
				}
				System.err.println(e.getLocalizedMessage());
			}
		}else {
			System.err.println("The xml file is incorrect.");
			return;
		}
	}
}
