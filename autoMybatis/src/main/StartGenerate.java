package main;

import fuku.automybatis.popup.actions.GeneratePojoActionForMain;

public class StartGenerate {

	public static void main(String[] args) {
		String path = "E:/workspace-sts/oa/src/main/java/generatorConfig.xml";
		String targetDir = "E:/workspace-sts/oa/src/main/java/com/ynkghs/oa/mongodb/generator";
		String tableName = "zzsq";
		new GeneratePojoActionForMain().start(path, targetDir, tableName);
	}

}
