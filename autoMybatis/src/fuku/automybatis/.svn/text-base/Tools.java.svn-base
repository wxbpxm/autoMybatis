package fuku.automybatis;

import java.io.File;
import java.util.List;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

public class Tools {

	private static MessageConsoleStream consoleStream = null;
	static{
		try {
			if(ConsolePlugin.getDefault() != null && ConsolePlugin.getDefault().getConsoleManager() != null) {
				IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
				IConsole[] consoles = consoleManager.getConsoles();
				if(consoles!=null && consoles.length>0){
					IConsole console = consoles[0];
					if(console instanceof MessageConsole){
						consoleStream = ((MessageConsole)console).newMessageStream();
					}
				}else{
					 // 首先新建一个MessageConsole
					  MessageConsole console = new MessageConsole("aotuMybatis", null);
					  // 通过ConsolePlugin得到ConsoleManager，并把新建立的console 添加进去
					  consoleManager.addConsoles(new IConsole[]{console});
					  
					  // 新建一个MessageConsoleStream，用于接收需要显示的信息
					  consoleStream = console.newMessageStream();
					  // 打开Console视图
					  consoleManager.showConsoleView(console);
				}
			}
		} catch (Throwable e) {
		}
	}
	
    public static void writeLine(String message) {
    	if(consoleStream!=null){
    		consoleStream.println(message);
    	} else {
    		System.out.println(message);
    	}
    }
	
    public static void writeLine() {
    	if(consoleStream!=null){
    		consoleStream.println();
    	}
    }
    
    public static String formatRelativePath(String linkPath, String relativePath) throws Exception {
    	if(linkPath==null || relativePath==null || "".equals(relativePath.trim())){
        	return linkPath;
        }
        if (linkPath.substring(linkPath.length()-1, linkPath.length()).equals(File.separator)) {
        	linkPath = linkPath.substring(0,linkPath.length()-1);
        }
    	relativePath = relativePath.replace("\\", "/");
        if (relativePath.indexOf("/")<0) {
        	relativePath = "/" + relativePath;
        }
        int index = relativePath.indexOf("../");
        while(index>=0){
        	int _index = linkPath.lastIndexOf(File.separator);
        	if(_index<0) 
        		throw new Exception("The relativePath is incorrect base on linkPath. ");
        	
        	linkPath = linkPath.substring(0, _index);
        	relativePath = relativePath.substring(index+2);
        	index = relativePath.indexOf("../");
        }
        String realPath = (linkPath + relativePath).replace("/", File.separator);
        File file = new File(realPath);
        if(!file.exists()){
        	boolean r = file.mkdir();
        	if(!r){
        		r = file.mkdirs();
        	}
        }
        return realPath;
    }
    

    public static void parseTable(Context context, String tableName) {
        if (StringUtility.stringHasValue(tableName)) {
        	List<TableConfiguration> tableConfigs = context.getTableConfigurations();
        	boolean have = false;
        	for(TableConfiguration tmpConfig : tableConfigs) {
        		if(tableName.equals(tmpConfig.getTableName())) {
        			have = true;
        		}
        	}
        	if(!have) {
        		TableConfiguration tc = new TableConfiguration(context);
        		context.addTableConfiguration(tc);
        		tc.setTableName(tableName);
        	}
        }
    }
}
