package fuku.automybatis.ui;

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
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.mybatis.generator.api.GeneratedFile;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.internal.db.ConnectionFactory;
import org.mybatis.generator.internal.util.FileGenerateUtil;

import fuku.automybatis.Tools;
import fuku.automybatis.bean.BusinessModelAO;
import fuku.automybatis.bean.BusinessPojoAO;

public class SelectTableDialog extends TitleAreaDialog {
	private List<String> tables;
	private Composite contentPane;
	private CheckboxTableViewer ctv;
	private String configFile;
	private String projectPath;
	private String tartDir;
	private IProject project;
	private List<Context> contexts;
	//操作选项，10 用于选择生成dao层
	public static final String OPT_GENERATE_DAO = "10";
	//操作选项， 20用于生成自定义mapper
	public static final String OPT_GENERATE_CUSTOMMAPPER = "20";
	//操作选项， 30用于生成Search
	public static final String OPT_GENERATE_SEARCH = "30";
	//操作选项，40用于生成service
	public static final String OPT_GENERATE_SERVICE = "40";
	//操作选项，50用于生成page
	public static final String OPT_GENERATE_PAGE = "50";
	//操作选项，60用于生成控制器
	public static final String OPT_GENERATE_CONTROLLER = "60";
	//操作选项，70用于生成POJO
	public static final String OPT_GENERATE_POJO= "70";
	//操作选项，80用于生成POJO
	public static final String OPT_GENERATE_OA_PAGE= "80";
	
	private String opt;//操作选项，10 用于选择生成dao层，20用于生成自定义mapper，30用于生成service
	
	public SelectTableDialog(Shell parentShell) {
		super(parentShell);
	}

	public SelectTableDialog(String opt, Shell parentShell, IProject project, String configFile, List<String> tables) {
		this(parentShell);
		this.tables = tables;
		this.configFile = configFile;
		this.project = project;
		this.opt = opt;
	}
	
	public SelectTableDialog(List<Context> contexts, String opt, Shell parentShell, String configFile, String projectPath, String tartDir, List<String> tables) {
		this(parentShell);
		this.tables = tables;
		this.configFile = configFile;
		this.opt = opt;
		this.contexts = contexts;
		this.projectPath = projectPath;
		this.tartDir = tartDir;
	}

	protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        // TitleArea中的Title
        setTitle("选择要操作的表");
        // TitleArea中的Message
        setMessage("请在下面列表中选择要操作的表.");

        createContentPane(area);
        createLeftList(tables);
       
        return area;
    }

    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        if(OPT_GENERATE_DAO.equals(opt) || opt == null) {
        	newShell.setText("选择表生成dao层代码");
        } else if(OPT_GENERATE_CUSTOMMAPPER.equals(opt)) {
        	newShell.setText("选择表生成自定义mapper代码");
        }  else if(OPT_GENERATE_SEARCH.equals(opt)) {
        	newShell.setText("选择表生成Searchd对象");
        } else if(OPT_GENERATE_SERVICE.equals(opt)) {
        	newShell.setText("选择表生成service代码");
        } else if(OPT_GENERATE_PAGE.equals(opt)) {
        	newShell.setText("选择表生成页面");
        }
        	
    }
    
    private void createLeftList(List<String> tables){
    	TableViewer viewer = new TableViewer(contentPane, SWT.CHECK | SWT.MULTI | SWT.BORDER);
    	Table table=viewer.getTable();
		 GridData griddata = new GridData(GridData.FILL_BOTH);
		 griddata.heightHint=400;
		 table.setLayoutData(griddata);
		 TableLayout layout = new TableLayout();
		 layout.addColumnData(new ColumnWeightData(1, true));
		 
		 TableColumn column2 = new TableColumn(table,SWT.CENTER);
		 column2.setText("tables");
		 viewer.setColumnProperties(new String[]{"tables"});
		 table.setLayout(layout);
		 table.setLinesVisible(true);
		 table.setHeaderVisible(true);
			ctv = new CheckboxTableViewer(table);
		 for(String tbl : tables){
			 ctv.add(new String[] {tbl});
		 }
    }
    
    private void createContentPane(Composite parent){
    	contentPane= new Composite(parent,SWT.DEFAULT);
    	//采用三列的GridLayout
    	GridLayout layout=new GridLayout();
    	//以下数值通过实验调整得出，这样看起来能够使输入框在中间位置，比较美观
    	layout.marginTop = 10;
    	layout.horizontalSpacing=10;
    	contentPane.setLayout(layout);
    	//注意充满FILL_BOTH
    	contentPane.setLayoutData(new GridData(GridData.FILL_BOTH));
    	contentPane.setFont(parent.getFont());
    }

	@Override
	protected void okPressed() {
		Object[] elements = ctv.getCheckedElements();
		String es = "";
		if(elements!=null){
			for(int i=0; i<elements.length; i++){
				Object obj = elements[i];
				if(obj!=null){
					if(i>0){ es = es + ",";}
					es = es + obj.toString();
				}
			}
		}
		if(project != null) {
			Tools.writeLine("tables:"+es);
		} else {
			System.out.println("tables:"+es);
		}
		String[] args = new String[]{
				"-configfile", configFile,
				"-overwrite",  "-relative",
				"-tables", es
		};
		if(project != null) {
			ShellRunner.main(project, args, opt);
		} else {//针对main方法直接调用
			if (SelectTableDialog.OPT_GENERATE_DAO.equals(opt) || opt == null) {// dao层
//				generateDAO(null);
			} else if (SelectTableDialog.OPT_GENERATE_CUSTOMMAPPER.equals(opt)) {// 自定义mapper
//				generateCustomerMapper(null);
			} else if (SelectTableDialog.OPT_GENERATE_SEARCH.equals(opt)) {// Search对象的生成
//				generateSearch(callback);
			} else if (SelectTableDialog.OPT_GENERATE_SERVICE.equals(opt)) { //生产service
//				//必须只能选择一个表，也就是只能同时生成一个模块的service
//				if(size > 1) {
//					warnings.add("只能选择一个表，来进行生成一个模块的service！");
//					return;
//				}
//				generateService(callback);
			} else if (SelectTableDialog.OPT_GENERATE_PAGE.equals(opt)) {//生产页面
//				//必须只能选择一个表，也就是只能同时生成一个模块的页面
//				if(size > 1) {
//					warnings.add("只能选择一个表，来进行生成一个模块的页面！");
//					return;
//				}
//				Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID); 
//				 URL url = bundle.getResource("/template"); 
//				 warnings.add("url:" + url);
//				 String templatePath = FileLocator.toFileURL(url).getPath();
//				 warnings.add("templatePath:" + templatePath);
//				 System.out.println("templatePath:" + templatePath);
//				new GeneratePageTool(this, getMapperGJF(), getMapperXML(), callback).show();
			} else if (SelectTableDialog.OPT_GENERATE_CONTROLLER.equals(opt)) {// 控制器对象的生成
//				generateController(callback, moduleName.toLowerCase());
			}  else if (SelectTableDialog.OPT_GENERATE_POJO.equals(opt)) { //生产POJO
				//必须只能选择一个表，也就是只能同时生成一个模块的pojo
				try {
					generatePojo(es);
				}  catch (Exception e) {
					e.printStackTrace();
				}
			} else if (SelectTableDialog.OPT_GENERATE_OA_PAGE.equals(opt)) { //生产POJO
//				//必须只能选择一个表，也就是只能同时生成一个模块的OA页面
//				if(size > 1) {
//					warnings.add("只能选择一个表，来进行生成一个模块的OA页面！");
//					return;
//				}
//				generateOAPage(es);
			}
		}
		super.okPressed();
	}
    
	private void generatePojo(String es) throws InterruptedException, IOException {
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
     			String[] fullyQualifiedTableNameArt = es.split(",");
     			List<String> fullyQualifiedTableNames = Arrays.asList(fullyQualifiedTableNameArt);
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
     				GeneratedJavaFile usedGif = FileGenerateUtil.getPojoJava(model, projectPath);
     				//写文件
     				writeGenerateFile(usedGif, tartDir, true);
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
