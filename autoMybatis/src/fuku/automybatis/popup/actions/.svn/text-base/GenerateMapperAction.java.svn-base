package fuku.automybatis.popup.actions;

import static org.mybatis.generator.internal.util.ClassloaderUtility.getCustomClassloader;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.db.ConnectionFactory;

import fuku.automybatis.Tools;
import fuku.automybatis.ui.SelectTableDialog;

public class GenerateMapperAction implements IObjectActionDelegate {
	private Shell shell;
	private IWorkbenchPart targetPart;
	/**
	 * Constructor for Action1.
	 */
	public GenerateMapperAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		this.targetPart = targetPart;
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		 //String title = targetPart.getTitle();  
         ISelection selection = targetPart.getSite().getSelectionProvider()  
                 .getSelection();
         
         try {
			if (selection instanceof TreeSelection) {  
			     Object obj = ((TreeSelection) selection).getFirstElement();
			     IResource resource = null;  
			     //String path = null;  
			     // common resource file  
			     if (obj instanceof IFile) {  
			     	resource = (IResource) obj;
			     	IPath rawPath = resource.getRawLocation();
			     	File configurationFile = rawPath.toFile();
			     	ConfigurationParser cp = new ConfigurationParser(null);
			     	Configuration config = cp.parseConfiguration(configurationFile);
			     	List<Context> contexts = config.getContexts();
			     	if (config.getClassPathEntries().size() > 0) {
			            ClassLoader classLoader = getCustomClassloader(config.getClassPathEntries());
			            ObjectFactory.setExternalClassLoader(classLoader);
			        }else {
			        	Tools.writeLine("The xml file is invalid.");
				    	return;
			        }
			     	if(contexts!=null && contexts.size()>0 && contexts.get(0)!=null){
			     		JDBCConnectionConfiguration jdbcConfig = contexts.get(0).getJdbcConnectionConfiguration();
			     		Connection con = null;
			     		try {
			     			con = ConnectionFactory.getInstance().getConnection(
			     					jdbcConfig); 

			     		   DatabaseMetaData meta = con.getMetaData();
			     		   ResultSet rs = meta.getTables(null, null, null, new String[]{"TABLE"});
			     		   List<String> tables = new ArrayList<String>();
			     		   while (rs.next()) {
			     			  tables.add(rs.getString(3));
			     		   }
			     		   con.close();
			     		   showTableSelectDialog(resource.getProject(), configurationFile.getAbsolutePath(),tables);
		     		   } catch (Exception e) {
			     		   try {
			     			   if(con!=null)
			     				   con.close();
			     		   } catch (SQLException e1) {
			     				Tools.writeLine(e1.getLocalizedMessage());
			     		   }
			     		   Tools.writeLine(e.getLocalizedMessage());
		     		   }
			     	}else {
			        	Tools.writeLine("The xml file is incorrect.");
				    	return;
			        }
			     }else {
		        	Tools.writeLine("The selected file is invalid.");
			    	return;
		        }
			 }else {
	        	Tools.writeLine("The selected file is invalid.");
		    	return;
	        }
		} catch (Exception e) {
			Tools.writeLine("The selected file is invalid.");
		}
	}

	private void showTableSelectDialog(IProject project, String configFile, List<String> tables) {
		SelectTableDialog dialog = new SelectTableDialog(SelectTableDialog.OPT_GENERATE_DAO, shell, project, configFile, tables);
		dialog.open(); 
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
