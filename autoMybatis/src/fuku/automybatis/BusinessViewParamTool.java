package fuku.automybatis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class BusinessViewParamTool {
	private static String script = null;

	public static List<String> parseDics(String param) {
		List<String> ret = new ArrayList<>();
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		SimpleBindings n = new SimpleBindings();
		
		if(script == null) {
			script = getScript();
		}
		if(script == null) {
			return ret;
		}
		n.put("obj", param);
		n.put("console", System.err);
		ScriptObjectMirror result = null;
		try {
			result = (ScriptObjectMirror)engine.eval(script, n);
			for(Entry<String, Object> entry: result.entrySet()) {
				if(entry != null && entry.getValue() != null) {
					ret.add(entry.getValue().toString());
				}
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static String getScript() {
		BufferedReader br = null;
		try {
			InputStream is = BusinessViewParamTool.class.getResourceAsStream("/oapagetemplate/finddic.js");
			if(is == null) {
				String finddicPath = BusinessViewParamTool.class.getResource("/").getFile().toString(); 
				finddicPath = finddicPath.replace("target/classes/", "") + "src/main/webapp/WEB-INF/views/business/template/finddic.js";
				is = new FileInputStream(finddicPath);
			}
			br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String tmp = null;
			while((tmp = br.readLine()) != null) {
				sb.append(tmp).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
}
