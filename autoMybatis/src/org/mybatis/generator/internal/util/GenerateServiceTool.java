package org.mybatis.generator.internal.util;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionDelegate;
import org.mybatis.generator.api.GeneratedFile;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.exception.ShellException;

public class GenerateServiceTool {

	/**
	 * 数据类型的map，key存类型CodeClass的name，value就是CodeClass对象
	 */

	private Map<String, FullyQualifiedJavaType> returnDataTypesMap = new LinkedHashMap<String, FullyQualifiedJavaType>();

	/**
	 * 方法参数的可用数据类型
	 */
	private Map<String, FullyQualifiedJavaType> paramDataTypesMap = new LinkedHashMap<String, FullyQualifiedJavaType>();

	/**
     * 
     */
	private List<String> entityFieldNames = new ArrayList<String>();

	//当前接口方法
	private Method currMethodInter;
	//当前实现方法
	private Method currMethodImpl;
	/** 接口要添加的方法 */
	private List<Method> methodsForInterface = new ArrayList<Method>();
	/** 实现要添加的方法 */
	private List<Method> methodsForImpl = new ArrayList<Method>();

	private boolean needCreateMethodPreview = true;

	private javax.swing.JButton jBtn_addToMethod1;
	private javax.swing.JButton jBtn_generate_preview;
	private javax.swing.JButton jBtn_removeMethod1;
	private javax.swing.JButton jBtn_start;
	private javax.swing.JComboBox<String> jCmb_paraType1;
	private javax.swing.JComboBox<String> jCmb_paraType2;
	private javax.swing.JComboBox<String> jCmb_paraType3;
	private javax.swing.JComboBox<String> jCmb_paraType4;
	private javax.swing.JComboBox<String> jCmb_paraEmptyCheck1;
	private javax.swing.JComboBox<String> jCmb_paraEmptyCheck2;
	private javax.swing.JComboBox<String> jCmb_paraEmptyCheck3;
	private javax.swing.JComboBox<String> jCmb_paraEmptyCheck4;
	private javax.swing.JComboBox<String> jCmb_returnType1;
	private javax.swing.JDialog jDlg_generate_service;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel23;
	private javax.swing.JLabel jLabel25;
	private javax.swing.JLabel jLabel33;
	private javax.swing.JLabel jLabel34;
	private javax.swing.JPanel jPanel17;
	private javax.swing.JPanel jPanel22;
	private javax.swing.JPanel jPanel23;
	private javax.swing.JPanel jPanel24;
	private javax.swing.JPanel jPanel28;
	private javax.swing.JPanel jPanel29;
	private javax.swing.JPanel jPnl_mainInfo1;
	private javax.swing.JPanel jPnl_serviceImpl1;
	private javax.swing.JPanel jPnl_serviceIntefer1;
	private javax.swing.JScrollPane jScrollPane11;
	private javax.swing.JScrollPane jScrollPane12;
	private javax.swing.JScrollPane jScrollPane13;
	private javax.swing.JScrollPane jScrollPane17;
	private javax.swing.JScrollPane jScrollPane18;
	private javax.swing.JTabbedPane jTPnl_tab1;
	private javax.swing.JTextArea jTa_methodPreview1;
	private javax.swing.JTextArea jTa_serviceImplContent1;
	private javax.swing.JTextArea jTa_serviceInterContent1;
	private javax.swing.JTable jTable_methodlst;
	private javax.swing.JTextField jTf_methodName1;
	private javax.swing.JTextField jTf_mpara1;
	private javax.swing.JTextField jTf_mpara2;
	private javax.swing.JTextField jTf_mpara3;
	private javax.swing.JTextField jTf_mpara4;

	/**
	 * 包前缀
	 */
	/** 包前面的部分，如：com.yuanxin.app.portal */
	private String packagePre;
	/** ao类的全包名，如：com.yuanxin.app.portal.appobject */
	private String aoPackage;
	/** 实体查询条件类的全包名，如：com.yuanxin.app.portal.entity.gen */
	private String criteriaPackage;
	private String examplePackage;
	/** 服务类的全包名，如：com.yuanxin.app.portal.service.desktopelement */
	private String servicePackage;

	/** 模块名称，如：DesktopElement */
	private String moduleName;
	
	
	/** dao层mapper的类名，如：DesktopElementGeneratedMapper */
	private String mapperClassName;
	/** dao层example的类名，如：DesktopElementExample */
	private String exampleClassName;
	
	/** 实体类的类名，如：DesktopElementAO */
	private String aoClassName;
	/** 实体查询条件类的类名，如：DesktopElementCriteria */
	private String criteriaClassName;
	/** 服务接口的类名，如：IDesktopElementService */
	private String serviceInterClassName;
	/** 服务实行的类名，如：DesktopElementService */
	private String serviceImplClassName;

	/**
	 * 几个最终需要用到的代码类对象，所谓代码类，就是为了生成一个类的代码，而自定义的类型CodeClass，尽量做到分解各种语法，做到所向无敌
	 */
	private GeneratedJavaFile serviceInterCls;
	/** 服务接口的代码类对象 */
	private GeneratedJavaFile serviceImplCls;
	/** 服务接口的代码类对象 */
	private FullyQualifiedJavaType entityCls;
	/** 实体的代码类对象 */

	private MyBatisGenerator generator;
	private GeneratedJavaFile gjf;
	private ProgressCallback callback;
	//AO对象的所有可用字段名称
	private List<String> aoFieldNames;
	//表中的所有字段名称
	private List<String> tableFieldNames;
	
	/**
	 * Constructor for Action1.
	 */
	public GenerateServiceTool(MyBatisGenerator generator,
			GeneratedJavaFile gjf, GeneratedXmlFile gxf, ProgressCallback callback) {
		super();
		this.generator = generator;
		this.gjf = gjf;
		String fileName = gjf.getFileName();
		this.moduleName = fileName.substring(0,
				fileName.lastIndexOf("Mapper.java"));
		this.generator.setModuleName(moduleName);
		this.callback = callback;
		aoFieldNames = new ArrayList<String>();
		tableFieldNames = new ArrayList<String>();
		for(IntrospectedColumn tmpColumn : gxf.getBaseColumns()) {
			if(tmpColumn.getJavaProperty() != null){
				aoFieldNames.add(tmpColumn.getJavaProperty());
				tableFieldNames.add(tmpColumn.getActualColumnName());
			}
		}
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void show() {
		/** 首先进行有效性检验的判断,无效直接返回 */
		if (!validationCheck()) {
			return;
		}

		/** 这里需要把上次的数据清空 */
		this.clearData();

		this.iniDataTypes();
		this.initComponents();

		freshCmps();
		this.initMethods();
		freshMethodsLst();
		jDlg_generate_service.setBounds(300, 100, 740, 460);
		jDlg_generate_service.setAlwaysOnTop(true);
		this.jDlg_generate_service.setVisible(true);
	}

	/**
	 * 最核心方法，负责所有的生成工作（通过调用私有方法完成）
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void startGenerateCodes(Boolean needInit) throws InterruptedException, IOException {
		if(needInit) {
			validationCheck();
			/** 这里需要把上次的数据清空 */
			this.clearData();
			this.initMethods();
		}
		
		/** 实例化服务接口类 */
		serviceInterCls = FileGenerateUtil.getServiceInterGJFJava(this.gjf,
				this.methodsForInterface, this.servicePackage, this.moduleName);
		/** 实例化服务实现类 */
		serviceImplCls = FileGenerateUtil.getServiceImplGJFJava(this.gjf,
				this.methodsForImpl, this.aoFieldNames, this.servicePackage, this.moduleName);
		
		File targetFile = null;
		try {
			File directory = generator.getShellCallback().getDirectory(
					serviceInterCls.getTargetProject(),
					serviceInterCls.getTargetPackage());
			targetFile = new File(directory, serviceInterCls.getFileName());
			if (targetFile.exists()) {
//				JOptionPane.showMessageDialog(this.jDlg_generate_service,
//						"不好！发现目标文件已存在，请检查后删除源文件再尝试生成！");
				generator.getWarnings().add(
						getString("Warning.26", targetFile.getAbsolutePath())); //$NON-NLS-1$
				return;
			}
		} catch (ShellException e) {
			e.printStackTrace();
		}
		
		this.writeGenerateFile(serviceInterCls, this.callback);
		this.writeGenerateFile(serviceImplCls, this.callback);
	}

	/**
	 * 私有方法，在每次运行的时候，清空上次成员变量值
	 */
	private void clearData() {
		this.methodsForInterface.clear();
		this.methodsForInterface.clear();
		this.returnDataTypesMap.clear();
		this.paramDataTypesMap.clear();
	}

	/************************* 事件处理开始 ****************************************/
	private void jCmb_paraType1ItemStateChanged(java.awt.event.ItemEvent evt) {
		createMethodPriview();
	}

	private void jCmb_paraType4ItemStateChanged(java.awt.event.ItemEvent evt) {
		createMethodPriview();
	}

	private void jCmb_paraType3ItemStateChanged(java.awt.event.ItemEvent evt) {
		createMethodPriview();
	}

	private void jCmb_paraType2ItemStateChanged(java.awt.event.ItemEvent evt) {
		createMethodPriview();
	}

	private void jTf_mpara1FocusLost(java.awt.event.FocusEvent evt) {
		createMethodPriview();
	}

	private void jTf_mpara4FocusLost(java.awt.event.FocusEvent evt) {
		createMethodPriview();
	}

	private void jTf_mpara3FocusLost(java.awt.event.FocusEvent evt) {
		createMethodPriview();
	}

	private void jTf_mpara2FocusLost(java.awt.event.FocusEvent evt) {
		createMethodPriview();
	}

	private void jTable_methodlstMouseClicked(java.awt.event.MouseEvent evt) {
		int seletRow = jTable_methodlst.getSelectedRow();
		this.currMethodInter = this.methodsForInterface.get(seletRow);
		this.currMethodImpl = this.methodsForImpl.get(seletRow);
		jTa_methodPreview1.setText(this.currMethodInter.getDeclare().trim());
	}

	private void jTf_methodName1FocusLost(java.awt.event.FocusEvent evt) {
		createMethodPriview();
	}

	private void resetParams() {
		needCreateMethodPreview = false;
		this.jTf_mpara1.setText("");
		this.jTf_mpara2.setText("");
		this.jTf_mpara3.setText("");
		this.jTf_mpara4.setText("");
		this.jCmb_paraType1.setSelectedIndex(0);
		this.jCmb_paraType2.setSelectedIndex(0);
		this.jCmb_paraType3.setSelectedIndex(0);
		this.jCmb_paraType4.setSelectedIndex(0);
		this.jCmb_paraEmptyCheck1.setSelectedIndex(0);
		this.jCmb_paraEmptyCheck2.setSelectedIndex(0);
		this.jCmb_paraEmptyCheck3.setSelectedIndex(0);
		this.jCmb_paraEmptyCheck4.setSelectedIndex(0);
		needCreateMethodPreview = true;
	}

	private void jCmb_returnType1ItemStateChanged(java.awt.event.ItemEvent evt) {
		resetParams();
		// 先判断方法名称是否为空，如果为空，就自动根据选择的返回类型初始化一个名称，并设置相关参数
		String methodName = this.jTf_methodName1.getText().trim();
		String retType1 = "DataList<" + aoClassName + ">";
		String retType2 = aoClassName;
		String retType3 = "Result<Boolean>";
		String returnType = jCmb_returnType1.getSelectedItem().toString();
		if (returnType.equals(retType1)) { // 是实体的list包装类型
			methodName = "get" + this.moduleName + "List";
			this.jTf_methodName1.setText(methodName);
			this.jCmb_paraType1.setSelectedIndex(0);
			List<String> matchedLst = this.getMatchedFieldNames("user");
			if (matchedLst.isEmpty()) {
				matchedLst = this.getMatchedFieldNames("company");
			}
			if (!matchedLst.isEmpty()) {
				this.jTf_mpara1.setText(matchedLst.get(0));
			}
		} else if (returnType.equals(retType2)) {// 单个实体的包装类型,默认为读取操作
			methodName = "get" + this.moduleName + "ById";
			this.jTf_methodName1.setText(methodName);
			this.jCmb_paraType1.setSelectedIndex(0);
			this.jTf_mpara1.setText("id");
		} else if (returnType.equals(retType3)) {
			methodName = "dele" + this.moduleName + "ById";
			this.jTf_methodName1.setText(methodName);
			this.jCmb_paraType1.setSelectedIndex(0);
			this.jTf_mpara1.setText("id");
		}
		needCreateMethodPreview = true;
		createMethodPriview();
	}

	private void jBtn_generate_previewMouseReleased(
			java.awt.event.MouseEvent evt) {
		serviceInterCls = FileGenerateUtil.getServiceInterGJFJava(this.gjf,
				this.methodsForInterface, this.servicePackage, this.moduleName);
		serviceImplCls = FileGenerateUtil.getServiceImplGJFJava(this.gjf,
				this.methodsForImpl, this.aoFieldNames, this.servicePackage, this.moduleName);

		jTa_serviceInterContent1.setText(this.serviceInterCls
				.getFormattedContent());
		jTa_serviceImplContent1.setText(this.serviceImplCls
				.getFormattedContent());

		jTPnl_tab1.setSelectedIndex(1);
	}

	private void jBtn_startMouseReleased(java.awt.event.MouseEvent evt) {
		try {
			this.startGenerateCodes(false);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		this.jDlg_generate_service.setVisible(false);
	}

	private void jBtn_addToMethod1MouseReleased(java.awt.event.MouseEvent evt) {
		if (exist(currMethodInter)) {
			JOptionPane.showMessageDialog(this.jDlg_generate_service,
					"已经添加过同名方法，请先删除后再添加！");
		} else if (currMethodInter == null || currMethodInter.getName() == null) {
			JOptionPane.showMessageDialog(this.jDlg_generate_service,
					"请先输入方法名称并配置必要参数后再添加！");
		} else {
			this.methodsForInterface.add(currMethodInter);
			this.methodsForImpl.add(currMethodImpl);
			freshMethodsLst();
		}
	}

	private void jBtn_removeMethod1MouseReleased(java.awt.event.MouseEvent evt) {
		int[] seletRows = jTable_methodlst.getSelectedRows();
		List<String> selectNames = new ArrayList<String>();
		if (seletRows.length == 0) {
			JOptionPane.showMessageDialog(this.jDlg_generate_service,
					"请在下面的列表中选择要删除的方法！");
			return;
		}
		for (int row : seletRows) {
			String tableName = jTable_methodlst.getValueAt(row, 0).toString();
			String params = jTable_methodlst.getValueAt(row, 1).toString();
			selectNames.add(tableName + "-" + params);
		}
		List<Method> tmpMethods = new ArrayList<Method>();
		for (Method tmp : this.methodsForInterface) {

			if (!selectNames.contains(tmp.getName() + "-"
					+ tmp.getParamLstAsString())) {
				tmpMethods.add(tmp);
			}
		}
		this.methodsForInterface = tmpMethods;
		for (Method tmp : this.methodsForImpl) {
			
			if (!selectNames.contains(tmp.getName() + "-"
					+ tmp.getParamLstAsString())) {
				tmpMethods.add(tmp);
			}
		}
		this.methodsForImpl = tmpMethods;
		freshMethodsLst();
	}

	private void jTf_mpara1FocusGained(java.awt.event.FocusEvent evt) {
		String content = jTf_mpara1.getText().trim();
		if (content.isEmpty()) {
			String type = jCmb_paraType1.getSelectedItem().toString();
			String argName = getArgsNameByType(type);
			jTf_mpara1.setText(argName);
		}
		jTf_mpara1.selectAll();
	}

	private void jTf_mpara2FocusGained(java.awt.event.FocusEvent evt) {
		String content = jTf_mpara2.getText().trim();
		if (content.isEmpty()) {
			String type = jCmb_paraType2.getSelectedItem().toString();
			String argName = getArgsNameByType(type);
			jTf_mpara2.setText(argName);
		}
		jTf_mpara2.selectAll();
	}

	private void jTf_mpara3FocusGained(java.awt.event.FocusEvent evt) {
		String content = jTf_mpara3.getText().trim();
		if (content.isEmpty()) {
			String type = jCmb_paraType3.getSelectedItem().toString();
			String argName = getArgsNameByType(type);
			jTf_mpara3.setText(argName);
		}
		jTf_mpara3.selectAll();
	}

	private void jTf_mpara4FocusGained(java.awt.event.FocusEvent evt) {
		String content = jTf_mpara4.getText().trim();
		if (content.isEmpty()) {
			String type = jCmb_paraType4.getSelectedItem().toString();
			String argName = getArgsNameByType(type);
			jTf_mpara4.setText(argName);
		}
		jTf_mpara4.selectAll();
	}

	private void jTf_mparaKeyReleased(java.awt.event.KeyEvent evt,
			JTextField textField) {
		char pressedChar = evt.getKeyChar();
		if (pressedChar == '\b') {
			return;
		}
		String tmpContent = textField.getText();
		if (tmpContent.length() > 1) {
			/** 从3个字符开始出提示 */
			List<String> matched = getMatchedFieldNames(tmpContent);
			/** 这里先去第一个去匹配 */
			if (!matched.isEmpty()) {
				String autoContent = matched.get(0);
				textField.setText(autoContent);
				textField.select(tmpContent.length(), autoContent.length());
			}
		}
	}

	private List<String> getMatchedFieldNames(String keyword) {
		List<String> retLst = new ArrayList<String>();
		for (String tmp : this.entityFieldNames) {
			if (tmp.toLowerCase().startsWith(keyword.toLowerCase())) {
				retLst.add(tmp);
			}
		}
		return retLst;
	}

	/************************* 事件处理结束 ****************************************/

	private void freshMethodsLst() {
		int rows = this.methodsForInterface.size();
		int cols = 3;
		Object[][] vecValues = new Object[rows][cols];
		for (int i = 0; i < rows; i++) {
			Method method = methodsForInterface.get(i);
			vecValues[i][0] = method.getName();
			vecValues[i][1] = method.getParamLstAsString();
			vecValues[i][2] = method.getReturnType().getFullyQualifiedName();
		}
		String[] colNames = { "方法名", "参数列表", "返回类型" };
		DefaultTableModel model = (DefaultTableModel) this.jTable_methodlst
				.getModel();
		model.setDataVector(vecValues, colNames);
		setTableColumnWidth();
	}

	private void setTableColumnWidth() {
		TableColumn col1 = this.jTable_methodlst.getColumnModel().getColumn(0);
		col1.setPreferredWidth(100);
		// title
		TableColumn col2 = this.jTable_methodlst.getColumnModel().getColumn(1);
		col2.setPreferredWidth(400);
		// price
		TableColumn col3 = this.jTable_methodlst.getColumnModel().getColumn(2);
		col3.setPreferredWidth(250);
	}

	private boolean exist(Method method) {
		for (Method tmpMethod : this.methodsForInterface) {
			if (tmpMethod.getName().equals(method.getName())) {
				List<Parameter> paramLst = method.getParameters();
				List<Parameter> tmpParamLst = tmpMethod.getParameters();
				if (paramLst.size() != tmpParamLst.size()) {
					continue;
				}

				/** 下面先对2个参数列表都按照参数的类型名称排序 */
				Collections.sort(paramLst, new Comparator<Parameter>() {
					@Override
					public int compare(final Parameter param1,
							final Parameter param2) {
						return param1.getType().getShortName()
								.compareTo(param2.getType().getShortName());
					}

				});

				Collections.sort(tmpParamLst, new Comparator<Parameter>() {
					@Override
					public int compare(final Parameter param1,
							final Parameter param2) {
						return param1.getType().getShortName()
								.compareTo(param2.getType().getShortName());
					}

				});

				/** 下面对排序好的2个参数列表进行比较是否完全一样 */
				boolean isSame = true;
				for (int i = 0; i < paramLst.size(); i++) {
					/** 注意，这里2个lst的size是一样的，用哪个都可以 */
					Parameter param1 = paramLst.get(i);
					Parameter param2 = tmpParamLst.get(i);
					if (!param1.getType().getShortName()
							.equals(param2.getType().getShortName())) {
						/** 发现有个参数不一样，就说明当前遍历的方法肯定是不一样的，可以不用往下走了 */
						isSame = false;
						break;
					}
				}
				/** 走到这里，如果isSame 仍然保持true，说明当前遍历的方法，是完全一样的，可以直接返回true */
				if (isSame) {
					return true;
				}
			}
		}
		return false;
	}

	private void freshCmps() {
		needCreateMethodPreview = false;
		jCmb_paraType1.removeAllItems();
		jCmb_paraType2.removeAllItems();
		jCmb_paraType3.removeAllItems();
		jCmb_paraType4.removeAllItems();
		for (String key : this.paramDataTypesMap.keySet()) {
			jCmb_paraType1.addItem(key);
			jCmb_paraType2.addItem(key);
			jCmb_paraType3.addItem(key);
			jCmb_paraType4.addItem(key);
		}

		jCmb_returnType1.removeAllItems();

		for (String key : this.returnDataTypesMap.keySet()) {
			jCmb_returnType1.addItem(key);
		}
		needCreateMethodPreview = true;

		/** 默认分别按照添加顺序（最常用的在前面），进行初始化选择 */
		jCmb_paraType1.setSelectedIndex(0);
		jCmb_paraType2.setSelectedIndex(0);
		jCmb_paraType3.setSelectedIndex(0);
		jCmb_paraType4.setSelectedIndex(0);
	}
	
	private void initMethods() {
		
		Method getDataByIdInter =  handlegetDataByIdInterMethod();
		getDataByIdImpMethod(getDataByIdInter);
		
		Method getListMthdInter =  handleGetListInterMethod();
		handleGetListImpMethod(getListMthdInter);

		Method deleteInter =  handleDeleteInterMethod();
		handleDeleteImpMethod(deleteInter);
		
		Method saveInter = handleSaveInterMethod();
		handleSaveImpMethod(saveInter);
		
		if(this.aoFieldNames.contains("status")) {
			Method updateStatusInter = handleUpdateStatusInterMethod();
			handleUpdateStatusImpMethod(updateStatusInter);
			
		}
	}

	private Method handleUpdateStatusInterMethod() {
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("boolean");
		Method method = new Method("update" + this.moduleName + "Status");
		method.setReturnType(returnType);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), this.getCaluName("id")));
		method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), this.getCaluName("status")));
		method.addJavaDocLine("\n\t/**根据ID,状态对"+moduleName+"对象进行状态更新*/");
		//处理类型依赖
		List<FullyQualifiedJavaType> dependTypes = new ArrayList<FullyQualifiedJavaType>();
		method.setDependTypes(dependTypes);
		this.methodsForInterface.add(method);
		return method;
	}
	
	private Method handleUpdateStatusImpMethod(Method method) {
		if(method == null) {
			return null;
		}
		Method implMethod = new Method(method.getName());
		implMethod.setReturnType(method.getReturnType());
		for(Parameter parameter : method.getParameters()) {
			implMethod.addParameter(parameter);
		}
		
		List<FullyQualifiedJavaType> dependTypes = new ArrayList<FullyQualifiedJavaType>();
		for(FullyQualifiedJavaType type : method.getDependTypes()) {
			dependTypes.add(type);
		}
		
		implMethod.setDependTypes(dependTypes);
		implMethod.setConstructor(method.isConstructor());
		implMethod.setFinal(method.isFinal());
		implMethod.setStatic(method.isStatic());
		implMethod.setVisibility(method.getVisibility());
		
		
		String mapperFieldName = this.getCaluName(moduleName) + "Mapper";
		
		implMethod.addBodyLine("if(id == null || id.isEmpty() || status == null");
		implMethod.addBodyLine("|| status.isEmpty()) {");
		implMethod.addBodyLine("return false;");
		implMethod.addBodyLine("}");
		implMethod.addBodyLine("");
		implMethod.addBodyLine(this.aoClassName + " " + this.getCaluName(moduleName) + " = new " + this.aoClassName + "();");
		implMethod.addBodyLine(this.getCaluName(moduleName) + ".setId(id);");
		implMethod.addBodyLine(this.getCaluName(moduleName) + ".setStatus(status);");
		implMethod.addBodyLine("");
		implMethod.addBodyLine("int count = " + mapperFieldName + ".updateByPrimaryKeySelective(" + this.getCaluName(moduleName) + ");");
		implMethod.addBodyLine("return count > 0;");
		implMethod.addAnnotation("@Override");
		implMethod.addAnnotation("@Transactional(rollbackFor=Throwable.class)");
		this.methodsForImpl.add(implMethod);
		return implMethod;
	}
	
	private Method handleDeleteInterMethod() {
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("boolean");
		Method method = new Method("delete" + this.moduleName + "ById");
		method.setReturnType(returnType);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), this.getCaluName("id")));
		method.addJavaDocLine("\n\t/**根据ID删除"+moduleName+"对象*/");
		//处理类型依赖
		List<FullyQualifiedJavaType> dependTypes = new ArrayList<FullyQualifiedJavaType>();
		method.setDependTypes(dependTypes);
		this.methodsForInterface.add(method);
		return method;
	}
	
	private Method handleDeleteImpMethod(Method method) {
		if(method == null) {
			return null;
		}
		Method implMethod = new Method(method.getName());
		implMethod.setReturnType(method.getReturnType());
		for(Parameter parameter : method.getParameters()) {
			implMethod.addParameter(parameter);
		}
		
		List<FullyQualifiedJavaType> dependTypes = new ArrayList<FullyQualifiedJavaType>();
		for(FullyQualifiedJavaType type : method.getDependTypes()) {
			dependTypes.add(type);
		}
		
		implMethod.setDependTypes(dependTypes);
		implMethod.setConstructor(method.isConstructor());
		implMethod.setFinal(method.isFinal());
		implMethod.setStatic(method.isStatic());
		implMethod.setVisibility(method.getVisibility());
		
		String mapperFieldName = this.getCaluName(moduleName) + "Mapper";
		implMethod.addBodyLine("if(id == null || id.isEmpty()) {");
		implMethod.addBodyLine("return false;");
		implMethod.addBodyLine("}");
		implMethod.addBodyLine("int count = " + mapperFieldName + ".deleteByPrimaryKey(id);");
		implMethod.addBodyLine("return count > 0;");
		implMethod.addAnnotation("@Override");
		implMethod.addAnnotation("@Transactional(rollbackFor=Throwable.class)");
		this.methodsForImpl.add(implMethod);
		return implMethod;
	}
	
	
	private Method handleSaveInterMethod() {
		FullyQualifiedJavaType aoType = new FullyQualifiedJavaType(this.aoPackage + "." + this.aoClassName);
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("com.ynkghs.framework.obj.Result<Boolean>");
		Method method = new Method("saveOrUpdate" + this.moduleName);
		method.setReturnType(returnType);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addParameter(new Parameter(aoType, this.getCaluName(this.moduleName)));
		method.addJavaDocLine("\n\t/**保存或者修改"+moduleName+"对象*/");
		//处理类型依赖
		List<FullyQualifiedJavaType> dependTypes = new ArrayList<FullyQualifiedJavaType>();
		dependTypes.add(aoType);
		dependTypes.add(returnType);
		method.setDependTypes(dependTypes);
		this.methodsForInterface.add(method);
		return method;
	}
	
	private Method handleSaveImpMethod(Method method) {
		if(method == null) {
			return null;
		}
		Method implMethod = new Method(method.getName());
		implMethod.setReturnType(method.getReturnType());
		for(Parameter parameter : method.getParameters()) {
			implMethod.addParameter(parameter);
		}
		
		List<FullyQualifiedJavaType> dependTypes = new ArrayList<FullyQualifiedJavaType>();
		for(FullyQualifiedJavaType type : method.getDependTypes()) {
			dependTypes.add(type);
		}
		
		implMethod.setDependTypes(dependTypes);
		implMethod.setConstructor(method.isConstructor());
		implMethod.setFinal(method.isFinal());
		implMethod.setStatic(method.isStatic());
		implMethod.setVisibility(method.getVisibility());
		
		String mapperFieldName = this.getCaluName(moduleName) + "Mapper";
		String aoParamName = this.getCaluName(this.moduleName);
		implMethod.addBodyLine("if(" + aoParamName + " == null) {");
		implMethod.addBodyLine("return new Result<Boolean>(false, \"参数" + aoParamName + "为空！\");");
		implMethod.addBodyLine("}");
		if(this.aoFieldNames.contains("name") || this.aoFieldNames.contains("title")) {
			implMethod.addBodyLine("//首先判断一下是否存在重复记录");
			implMethod.addBodyLine(this.exampleClassName + " example = new " + this.exampleClassName + "();");
			implMethod.addBodyLine(this.exampleClassName + ".Criteria criteria = example.createCriteria();");
			
			implMethod.addBodyLine("if(" + aoParamName + ".getId() != null && !" + aoParamName + ".getId().isEmpty()) {");
			implMethod.addBodyLine("criteria.andIdNotEqualTo(" + aoParamName + ".getId());");
			implMethod.addBodyLine("}");
			
			if(this.aoFieldNames.contains("name")) {
				implMethod.addBodyLine("criteria.andNameEqualTo(" + aoParamName + ".getName());");
			} else if(this.aoFieldNames.contains("title")) {
				implMethod.addBodyLine("criteria.andTitleEqualTo(" + aoParamName + ".getTitle());");
			}
			implMethod.addBodyLine("int count = " + mapperFieldName + ".countByExample(example);");
			implMethod.addBodyLine("if(count > 0) {");
			if(this.aoFieldNames.contains("name")) {
				implMethod.addBodyLine("return new Result<Boolean>(false, \"已经存在同名对象，请检查修改后再试！\");");
			} else if(this.aoFieldNames.contains("title")) {
				implMethod.addBodyLine("return new Result<Boolean>(false, \"已经存在同标题对象，请检查修改后再试！\");");
			}
			implMethod.addBodyLine("}");
		}
		
		implMethod.addBodyLine("//根据是新增还是修改进行下一步操作");
		implMethod.addBodyLine("if(" + aoParamName + ".getId() == null || " + aoParamName + ".getId().isEmpty()) {");
		if(this.aoFieldNames.contains("status")) {
			implMethod.addBodyLine(aoParamName + ".setStatus(\"10\");//TODO 状态值需要改成自定义常量");
		}
		if(this.aoFieldNames.contains("createDate")) {
			implMethod.addBodyLine(aoParamName + ".setCreateDate(new Date());");
			FullyQualifiedJavaType dateType = new FullyQualifiedJavaType("java.util.Date"); 
			dependTypes.add(dateType);
		}
		if(this.aoFieldNames.contains("createTime")) {
			implMethod.addBodyLine(aoParamName + ".setCreateTime(new Date());");
			FullyQualifiedJavaType dateType = new FullyQualifiedJavaType("java.util.Date"); 
			dependTypes.add(dateType);
		}
		
		implMethod.addBodyLine(mapperFieldName + ".insert(" + aoParamName + ");");
		implMethod.addBodyLine("} else {");
		implMethod.addBodyLine(mapperFieldName + ".updateByPrimaryKeySelective(" + aoParamName + ");");
		implMethod.addBodyLine("}");
		
		if(this.aoFieldNames.contains("logo")) {
			String saveResourceMthdName = "saveResource4" + this.moduleName;
			implMethod.addBodyLine("if(" + aoParamName + ".getLogoId() != null && !" + aoParamName + ".getLogoId().isEmpty()) {");
			implMethod.addBodyLine(saveResourceMthdName + "(" + aoParamName + ");");
			implMethod.addBodyLine("}");
			
			//这里就需要把saveResource4Data方法实现掉
			Method saveResoureMethod = new Method(saveResourceMthdName);
			List<FullyQualifiedJavaType> tmpDependTypes = new ArrayList<FullyQualifiedJavaType>();
			tmpDependTypes.add(this.entityCls);
			tmpDependTypes.add(new FullyQualifiedJavaType(this.aoPackage + ".ResourceAO"));
			tmpDependTypes.add(new FullyQualifiedJavaType(this.examplePackage + ".ResourceExample"));
			saveResoureMethod.setDependTypes(tmpDependTypes);
			saveResoureMethod.setVisibility(JavaVisibility.PRIVATE);
			saveResoureMethod.addParameter(new Parameter(new FullyQualifiedJavaType(this.aoPackage + "." + this.aoClassName), aoParamName));
			
			 //处理文件数据
			saveResoureMethod
					.addBodyLine("ResourceExample re = new ResourceExample();");
			saveResoureMethod
					.addBodyLine("re.createCriteria().andDataIdEqualTo(" + aoParamName + ".getId());");
			saveResoureMethod
					.addBodyLine("resourceMapper.deleteByExample(re);");
			saveResoureMethod
					.addBodyLine("String tmpFileId  = " + aoParamName + ".getLogoId();");
			saveResoureMethod
					.addBodyLine("String tmpFilePath  = " + aoParamName + ".getLogo();");
			saveResoureMethod
					.addBodyLine("ResourceAO resourceAO = new ResourceAO();");
			saveResoureMethod
					.addBodyLine("resourceAO.setCreateTime(new Date());");
			saveResoureMethod
					.addBodyLine("resourceAO.setDataId(" + aoParamName + ".getId());");
			saveResoureMethod
					.addBodyLine("resourceAO.setDataType(ResourceAO.DATA_TYPE_GOODS_MAIN);");
			saveResoureMethod.addBodyLine("resourceAO.setFileId(tmpFileId);");
			saveResoureMethod
					.addBodyLine("resourceAO.setFilePath(tmpFilePath);");
			saveResoureMethod.addBodyLine("resourceAO.setSort(1);");
			saveResoureMethod
					.addBodyLine("resourceAO.setResourceType(ResourceAO.RESOURCE_TYPE_IMG);");
			saveResoureMethod
					.addBodyLine("resourceAO.setStatus(ResourceAO.STATUS_NOMAL);");
			saveResoureMethod.addBodyLine("resourceMapper.insert(resourceAO);");
			this.methodsForImpl.add(saveResoureMethod);
		}

		implMethod.addBodyLine("return new Result<Boolean>(true);");
		implMethod.addAnnotation("@Override");
		implMethod.addAnnotation("@Transactional(rollbackFor=Throwable.class)");
		this.methodsForImpl.add(implMethod);
		return implMethod;
	}
	
	
	private Method handlegetDataByIdInterMethod() {
		FullyQualifiedJavaType aoType = new FullyQualifiedJavaType(this.aoPackage + "." + this.aoClassName);
		Method method = new Method("get" + this.moduleName + "ById");
		method.setReturnType(aoType);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
		method.addJavaDocLine("\n\t/**根据Id条件查询"+moduleName+"对象*/");
		//处理类型依赖
		List<FullyQualifiedJavaType> dependTypes = new ArrayList<FullyQualifiedJavaType>();
		dependTypes.add(aoType);
		method.setDependTypes(dependTypes);
		this.methodsForInterface.add(method);
		return method;
	}
	
	private Method getDataByIdImpMethod(Method method) {
		if(method == null) {
			return null;
		}
		Method implMethod = new Method(method.getName());
		implMethod.setReturnType(method.getReturnType());
		for(Parameter parameter : method.getParameters()) {
			implMethod.addParameter(parameter);
		}
		implMethod.setDependTypes(method.getDependTypes());
		implMethod.setConstructor(method.isConstructor());
		implMethod.setFinal(method.isFinal());
		implMethod.setStatic(method.isStatic());
		implMethod.setVisibility(method.getVisibility());
		String mapperFieldName = this.getCaluName(moduleName) + "Mapper";
		implMethod.addBodyLine("return " + mapperFieldName + ".selectByPrimaryKey(id);");
		implMethod.addAnnotation("@Override");
		this.methodsForImpl.add(implMethod);
		return implMethod;
	}
	
	private Method handleGetListInterMethod() {
		FullyQualifiedJavaType pageType = new FullyQualifiedJavaType("com.ynkghs.framework.obj.Page");
		FullyQualifiedJavaType searchType = new FullyQualifiedJavaType(this.aoPackage + "." + moduleName + "SearchAO");
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("com.ynkghs.framework.obj.DataList<" + this.aoClassName + ">");
		FullyQualifiedJavaType aoType = new FullyQualifiedJavaType(this.aoPackage + "." + this.aoClassName);
		Method method = new Method("get" + this.moduleName + "List");
		method.setReturnType(returnType);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addParameter(new Parameter(searchType, "search"));
		method.addParameter(new Parameter(pageType, "page"));
		method.addJavaDocLine("\n\t/**根据search条件查询"+moduleName+"列表*/");
		//处理类型依赖
		List<FullyQualifiedJavaType> dependTypes = new ArrayList<FullyQualifiedJavaType>();
		dependTypes.add(pageType);
		dependTypes.add(searchType);
		dependTypes.add(returnType);
		dependTypes.add(aoType);
		method.setDependTypes(dependTypes);
		this.methodsForInterface.add(method);
		return method;
	}
	
	private Method handleGetListImpMethod(Method method) {
		if(method == null) {
			return null;
		}
		Method implMethod = new Method(method.getName());
		implMethod.setReturnType(method.getReturnType());
		for(Parameter parameter : method.getParameters()) {
			implMethod.addParameter(parameter);
		}
		implMethod.setDependTypes(method.getDependTypes());
		implMethod.setConstructor(method.isConstructor());
		implMethod.setFinal(method.isFinal());
		implMethod.setStatic(method.isStatic());
		implMethod.setVisibility(method.getVisibility());
		String mapperFieldName = this.getCaluName(moduleName) + "Mapper";
		String listFieldName = this.getCaluName(moduleName) + "List";
		implMethod.addBodyLine("List<" + this.aoClassName + "> " + listFieldName + " = null;");
		implMethod.addBodyLine("int cnt = 0;");
		implMethod.addBodyLine(this.exampleClassName + " example = new " + this.exampleClassName + "();");
		implMethod.addBodyLine("if(page != null) {");
		implMethod.addBodyLine("example.setPage(page);");
		implMethod.addBodyLine("}");
		implMethod.addBodyLine(this.exampleClassName + ".Criteria criteria = example.createCriteria();");
		implMethod.addBodyLine("if(search != null) {");
		implMethod.addBodyLine("if(search.getKeyword() != null && !search.getKeyword().isEmpty()) {");
		//判断一下是否有name，title 这样的字段, 如果有，就拿keyword来匹配
		if(this.aoFieldNames.contains("name")) {
			implMethod.addBodyLine("criteria.andNameLike(\"%\" + search.getKeyword() + \"%\");");
		} else if(this.aoFieldNames.contains("title")) {
			implMethod.addBodyLine("criteria.andTitleLike(\"%\" + search.getKeyword() + \"%\");");
		} else {
			implMethod.addBodyLine("//TODO");
		}
		implMethod.addBodyLine("}");
		implMethod.addBodyLine("}");
		
		if(this.tableFieldNames.contains("sort")) {
			implMethod.addBodyLine("example.setOrderByClause(\"sort\");");
		} else if(this.tableFieldNames.contains("create_time")) {
			implMethod.addBodyLine("example.setOrderByClause(\"create_time desc\");");
		} else {
			implMethod.addBodyLine("//TODO 排序规则设置");
		}
		implMethod.addBodyLine("");
		implMethod.addBodyLine("cnt = " + mapperFieldName + ".countByExample(example);");
		implMethod.addBodyLine(listFieldName + " = " + mapperFieldName + ".selectByExample(example);");
		
		implMethod.addBodyLine("return new DataList<" + this.aoClassName + ">(" + listFieldName + ", cnt);");
		implMethod.addAnnotation("@Override");
		this.methodsForImpl.add(implMethod);
		return implMethod;
	}
 

	private Method getImpMethod(Method method) {
		if(method == null) {
			return null;
		}
		Method implMethod = new Method(method.getName());
		implMethod.setReturnType(method.getReturnType());
		for(Parameter parameter : method.getParameters()) {
			implMethod.addParameter(parameter);
		}
		implMethod.setDependTypes(method.getDependTypes());
		implMethod.setConstructor(method.isConstructor());
		implMethod.setFinal(method.isFinal());
		implMethod.setStatic(method.isStatic());
		implMethod.setVisibility(method.getVisibility());
		implMethod.addBodyLine("//TODO 排序规则设置");
		return implMethod;
	}
	
	
	/**
	 * 根据方法配置信息，构建方法对象，并产生方法签名的预览
	 */
	private void createMethodPriview() {
		currMethodInter = new Method();
		String methName = jTf_methodName1.getText().trim();
		if (methName.isEmpty() || !needCreateMethodPreview) {
			return;
		}

		currMethodInter.setName(methName);
		currMethodInter.setReturnType(new FullyQualifiedJavaType(jCmb_returnType1
				.getSelectedItem().toString()));

		if (!jTf_mpara1.getText().trim().isEmpty()) {
			Parameter param = new Parameter(new FullyQualifiedJavaType(
					jCmb_paraType1.getSelectedItem().toString()), jTf_mpara1
					.getText().trim());
			String emptyCheck = jCmb_paraEmptyCheck1.getSelectedItem()
					.toString();
			if (emptyCheck.equals("非空")) {
				param.setCanEmpty(false);
			} else {
				param.setCanEmpty(true);
			}
			currMethodInter.addParameter(param);
		}
		if (!jTf_mpara2.getText().trim().isEmpty()) {
			Parameter param = new Parameter(new FullyQualifiedJavaType(
					jCmb_paraType2.getSelectedItem().toString()), jTf_mpara2
					.getText().trim());
			String emptyCheck = jCmb_paraEmptyCheck2.getSelectedItem()
					.toString();
			if (emptyCheck.equals("非空")) {
				param.setCanEmpty(false);
			} else {
				param.setCanEmpty(true);
			}

			currMethodInter.addParameter(param);
		}
		if (!jTf_mpara3.getText().trim().isEmpty()) {
			Parameter param = new Parameter(new FullyQualifiedJavaType(
					jCmb_paraType3.getSelectedItem().toString()), jTf_mpara3
					.getText().trim());
			String emptyCheck = jCmb_paraEmptyCheck3.getSelectedItem()
					.toString();
			if (emptyCheck.equals("非空")) {
				param.setCanEmpty(false);
			} else {
				param.setCanEmpty(true);
			}

			currMethodInter.addParameter(param);
		}
		if (!jTf_mpara4.getText().trim().isEmpty()) {
			Parameter param = new Parameter(new FullyQualifiedJavaType(
					jCmb_paraType4.getSelectedItem().toString()), jTf_mpara4
					.getText().trim());
			String emptyCheck = jCmb_paraEmptyCheck4.getSelectedItem()
					.toString();
			if (emptyCheck.equals("非空")) {
				param.setCanEmpty(false);
			} else {
				param.setCanEmpty(true);
			}

			currMethodInter.addParameter(param);
		}

		jTa_methodPreview1.setText(currMethodInter.getDeclare().trim());
		
		//处理实现方法
		currMethodImpl = getImpMethod(currMethodInter); 
	}

	private void iniDataTypes() {
		/***************** 先处理返回值类型 ****************************/
		/** 首先把实体类集合的包装结果集类加入 */
		FullyQualifiedJavaType rsLstAO = new FullyQualifiedJavaType("DataList<"
				+ aoClassName + ">");
		this.returnDataTypesMap.put(rsLstAO.getShortName(), rsLstAO);

		/** 首先把实体类的包装结果集类加入 */
		FullyQualifiedJavaType rsAO = new FullyQualifiedJavaType(
				aoClassName);
		this.returnDataTypesMap.put(aoClassName, rsAO);

		/** 首先把Boolean的包装结果集类加入 */
		FullyQualifiedJavaType rsBoolean = new FullyQualifiedJavaType(
				"Result<Boolean>");
		this.returnDataTypesMap.put(rsBoolean.getShortName(), rsBoolean);

		/***************** 开始处理参数类型 ****************************/
		/** 首先把实体类加入 */
		this.paramDataTypesMap.put(aoClassName, entityCls);
	}

	private void freshAllFiles() {
		try {
			ResourcesPlugin.getWorkspace().getRoot()
					.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检验选择文件的合法性，并赋值
	 * 
	 * @return
	 */
	private boolean validationCheck() {
		moduleName = generator.getModuleName();
		mapperClassName = moduleName + "Mapper";
		exampleClassName = moduleName + "Example";
		serviceImplClassName = moduleName + "Service";
		serviceInterClassName = "I" + moduleName + "Service";
		aoClassName = moduleName + "AO";
		criteriaClassName = moduleName + "Criteria";
		aoPackage = generator.getExtendAOPackage();
		criteriaPackage = generator.getMapperAndXmlPackage();
		examplePackage = criteriaPackage.replace(".db.generator", ".appobj.generator");
		servicePackage = generator.getServicePackage();

		/** 实例类 */
		entityCls = new FullyQualifiedJavaType(aoClassName);

		return true;
	}

	private String getArgsNameByType(String type) {
		if (type == null || type.isEmpty()) {
			return "";
		} else if (type.endsWith("AO")) {
			return getCaluName(type);
		}
		String aoName = "";
		String ret = "";
		switch (type) {
		case "String":
			ret = "usercode";
			break;
		case "Long":
			aoName = getCaluName(aoClassName);
			aoName = aoName.substring(0, aoName.length() - 2);
			ret = aoName + "Id";
			break;
		case "Date":
			ret = "date";
			break;
		case "List<Long>":
			aoName = getCaluName(aoClassName);
			aoName = aoName.substring(0, aoName.length() - 2);
			ret = aoName + "IdLst";
			break;
		}
		return ret;
	}

	public void printInfo() {
		System.out.println("packagePre:" + packagePre);
		System.out.println("aoPackage:" + aoPackage);
		System.out.println("criteriaPackage:" + criteriaPackage);
		System.out.println("moduleName:" + moduleName);
		System.out.println("mappName:" + mapperClassName);
		System.out.println("entityAOName:" + aoClassName);
		System.out.println("serviceInterName:" + serviceInterClassName);
		System.out.println("serviceImplName:" + serviceImplClassName);
		System.out.println("entityCriteriaName:" + criteriaClassName);
	}

	private void writeGenerateFile(GeneratedFile usedGif,
			ProgressCallback callback) throws InterruptedException, IOException {
		File targetFile = null;
		String source = null;
		try {
			File directory = generator.getShellCallback().getDirectory(
					usedGif.getTargetProject(), usedGif.getTargetPackage());
			targetFile = new File(directory, usedGif.getFileName());
			if (targetFile.exists()) {
//				 JOptionPane.showMessageDialog(this.jDlg_generate_service,
//						"发现Service文件已存在！忽略service的生成！");
				generator.getWarnings().add(
						getString("Warning.26", targetFile.getAbsolutePath())); //$NON-NLS-1$
				return;
			} else {
				source = usedGif.getFormattedContent();
			}

			callback.checkCancel();
			callback.startTask(getString("Progress.15", targetFile.getName())); //$NON-NLS-1$
			generator.writeFile(targetFile, source);
		} catch (ShellException e) {
			generator.getWarnings().add(e.getMessage());
		}
		this.freshAllFiles();
	}

	private void initComponents() {
		jDlg_generate_service = new javax.swing.JDialog();
		jTPnl_tab1 = new javax.swing.JTabbedPane();
		jPnl_mainInfo1 = new javax.swing.JPanel();
		jPanel22 = new javax.swing.JPanel();
		jPanel23 = new javax.swing.JPanel();
		jCmb_paraType1 = new javax.swing.JComboBox<String>();
		jCmb_paraType4 = new javax.swing.JComboBox<String>();
		jCmb_paraType3 = new javax.swing.JComboBox<String>();
		jCmb_paraType2 = new javax.swing.JComboBox<String>();
		jTf_mpara1 = new javax.swing.JTextField();
		jTf_mpara4 = new javax.swing.JTextField();
		jTf_mpara3 = new javax.swing.JTextField();
		jTf_mpara2 = new javax.swing.JTextField();
		jLabel21 = new javax.swing.JLabel();
		jLabel22 = new javax.swing.JLabel();
		jLabel23 = new javax.swing.JLabel();
		jLabel25 = new javax.swing.JLabel();
		jScrollPane11 = new javax.swing.JScrollPane();
		jTa_methodPreview1 = new javax.swing.JTextArea();
		jPanel24 = new javax.swing.JPanel();
		jScrollPane12 = new javax.swing.JScrollPane();
		jScrollPane13 = new javax.swing.JScrollPane();
		jTable_methodlst = new javax.swing.JTable();
		jBtn_removeMethod1 = new javax.swing.JButton();
		jBtn_addToMethod1 = new javax.swing.JButton();
		jPanel17 = new javax.swing.JPanel();
		jTf_methodName1 = new javax.swing.JTextField();
		jLabel34 = new javax.swing.JLabel();
		jLabel33 = new javax.swing.JLabel();
		jCmb_returnType1 = new javax.swing.JComboBox<String>();
		jBtn_start = new javax.swing.JButton();
		jBtn_generate_preview = new javax.swing.JButton();
		jPnl_serviceIntefer1 = new javax.swing.JPanel();
		jPanel28 = new javax.swing.JPanel();
		jScrollPane17 = new javax.swing.JScrollPane();
		jTa_serviceInterContent1 = new javax.swing.JTextArea();
		jPnl_serviceImpl1 = new javax.swing.JPanel();
		jPanel29 = new javax.swing.JPanel();
		jScrollPane18 = new javax.swing.JScrollPane();
		jTa_serviceImplContent1 = new javax.swing.JTextArea();
		jCmb_paraEmptyCheck1 = new javax.swing.JComboBox<String>();
		jCmb_paraEmptyCheck2 = new javax.swing.JComboBox<String>();
		jCmb_paraEmptyCheck3 = new javax.swing.JComboBox<String>();
		jCmb_paraEmptyCheck4 = new javax.swing.JComboBox<String>();

		jDlg_generate_service.setName("jDlg_generate_service"); // NOI18N

		jTPnl_tab1.setName("jTPnl_tab1"); // NOI18N

		jPnl_mainInfo1.setName("jPnl_mainInfo1"); // NOI18N
		jPnl_mainInfo1
				.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jCmb_paraEmptyCheck4
				.setModel(new javax.swing.DefaultComboBoxModel<String>(
						new String[] { "非空", "可空" }));
		jCmb_paraEmptyCheck4.setName("jCmb_paraEmptyCheck4"); // NOI18N
		jPanel23.add(jCmb_paraEmptyCheck4,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 150, -1,
						-1));

		jCmb_paraEmptyCheck1
				.setModel(new javax.swing.DefaultComboBoxModel<String>(
						new String[] { "非空", "可空" }));
		jCmb_paraEmptyCheck1.setName("jCmb_paraEmptyCheck1"); // NOI18N
		jPanel23.add(jCmb_paraEmptyCheck1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 30, -1,
						-1));

		jCmb_paraEmptyCheck2
				.setModel(new javax.swing.DefaultComboBoxModel<String>(
						new String[] { "非空", "可空" }));
		jCmb_paraEmptyCheck2.setName("jCmb_paraEmptyCheck2"); // NOI18N
		jPanel23.add(jCmb_paraEmptyCheck2,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, -1,
						-1));

		jCmb_paraEmptyCheck3
				.setModel(new javax.swing.DefaultComboBoxModel<String>(
						new String[] { "非空", "可空" }));
		jCmb_paraEmptyCheck3.setName("jCmb_paraEmptyCheck3"); // NOI18N
		jPanel23.add(jCmb_paraEmptyCheck3,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, -1,
						-1));

		jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"添加业务方法",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.TOP)); // NOI18N
		jPanel22.setName("jPanel22"); // NOI18N
		jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanel23.setBorder(javax.swing.BorderFactory
				.createTitledBorder("方法参数列表")); // NOI18N
		jPanel23.setName("jPanel23"); // NOI18N
		jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jCmb_paraType1.setToolTipText("方法参数类型");
		jCmb_paraType1.setName("jCmb_paraType1"); // NOI18N
		jCmb_paraType1.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				jCmb_paraType1ItemStateChanged(evt);
			}
		});
		jPanel23.add(jCmb_paraType1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 180,
						-1));

		jCmb_paraType4.setToolTipText("方法参数类型");
		jCmb_paraType4.setName("jCmb_paraType4"); // NOI18N
		jCmb_paraType4.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				jCmb_paraType4ItemStateChanged(evt);
			}
		});
		jPanel23.add(jCmb_paraType4,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 180,
						-1));

		jCmb_paraType3.setToolTipText("方法参数类型");
		jCmb_paraType3.setName("jCmb_paraType3"); // NOI18N
		jCmb_paraType3.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				jCmb_paraType3ItemStateChanged(evt);
			}
		});
		jPanel23.add(jCmb_paraType3,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 180,
						-1));

		jCmb_paraType2.setToolTipText("方法参数类型");
		jCmb_paraType2.setName("jCmb_paraType2"); // NOI18N
		jCmb_paraType2.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				jCmb_paraType2ItemStateChanged(evt);
			}
		});
		jPanel23.add(jCmb_paraType2,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 180,
						-1));

		jTf_mpara1
				.setToolTipText("参数名称，可根据类型自动初始化一个值，也可手工修改，手工输入时为根据当前模块的实体属性自动完成填充！");
		jTf_mpara1.setName("jTf_mpara1"); // NOI18N
		jTf_mpara1.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				jTf_mpara1FocusGained(evt);
			}

			public void focusLost(java.awt.event.FocusEvent evt) {
				jTf_mpara1FocusLost(evt);
			}
		});
		jTf_mpara1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jTf_mparaKeyReleased(evt, jTf_mpara1);
			}
		});

		jPanel23.add(jTf_mpara1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 30, 125,
						-1));

		jTf_mpara4
				.setToolTipText("参数名称，可根据类型自动初始化一个值，也可手工修改，手工输入时为根据当前模块的实体属性自动完成填充！");
		jTf_mpara4.setName("jTf_mpara4"); // NOI18N
		jTf_mpara4.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				jTf_mpara4FocusGained(evt);
			}

			public void focusLost(java.awt.event.FocusEvent evt) {
				jTf_mpara4FocusLost(evt);
			}
		});
		jTf_mpara4.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jTf_mparaKeyReleased(evt, jTf_mpara4);
			}
		});

		jPanel23.add(jTf_mpara4,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 150,
						125, -1));

		jTf_mpara3
				.setToolTipText("参数名称，可根据类型自动初始化一个值，也可手工修改，手工输入时为根据当前模块的实体属性自动完成填充！");
		jTf_mpara3.setName("jTf_mpara3"); // NOI18N
		jTf_mpara3.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				jTf_mpara3FocusGained(evt);
			}

			public void focusLost(java.awt.event.FocusEvent evt) {
				jTf_mpara3FocusLost(evt);
			}
		});
		jTf_mpara3.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jTf_mparaKeyReleased(evt, jTf_mpara3);
			}
		});

		jPanel23.add(jTf_mpara3,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 110,
						125, -1));

		jTf_mpara2
				.setToolTipText("参数名称，可根据类型自动初始化一个值，也可手工修改，手工输入时为根据当前模块的实体属性自动完成填充！");
		jTf_mpara2.setName("jTf_mpara2"); // NOI18N
		jTf_mpara2.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				jTf_mpara2FocusGained(evt);
			}

			public void focusLost(java.awt.event.FocusEvent evt) {
				jTf_mpara2FocusLost(evt);
			}
		});

		jTf_mpara2.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jTf_mparaKeyReleased(evt, jTf_mpara2);
			}
		});

		jPanel23.add(jTf_mpara2,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 70, 125,
						-1));

		jLabel21.setText("参数4："); // NOI18N
		jLabel21.setName("jLabel21"); // NOI18N
		jPanel23.add(jLabel21,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1,
						-1));

		jLabel22.setText("参数3："); // NOI18N
		jLabel22.setName("jLabel22"); // NOI18N
		jPanel23.add(jLabel22,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1,
						-1));

		jLabel23.setText("参数2："); // NOI18N
		jLabel23.setName("jLabel23"); // NOI18N
		jPanel23.add(jLabel23,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1,
						-1));

		jLabel25.setText("参数1："); // NOI18N
		jLabel25.setName("jLabel25"); // NOI18N
		jPanel23.add(jLabel25,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1,
						-1));

		jCmb_paraEmptyCheck4.setToolTipText("参数是否为非空，如果设置非空，将自动生成参数非空的检验代码^_^");
		jCmb_paraEmptyCheck4
				.setModel(new javax.swing.DefaultComboBoxModel<String>(
						new String[] { "非空", "可空" }));
		jCmb_paraEmptyCheck4.setName("jCmb_paraEmptyCheck4"); // NOI18N
		jPanel23.add(jCmb_paraEmptyCheck4,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(373, 150, 55,
						-1));

		jCmb_paraEmptyCheck1.setToolTipText("参数是否为非空，如果设置非空，将自动生成参数非空的检验代码^_^");
		jCmb_paraEmptyCheck1
				.setModel(new javax.swing.DefaultComboBoxModel<String>(
						new String[] { "非空", "可空" }));
		jCmb_paraEmptyCheck1.setName("jCmb_paraEmptyCheck1"); // NOI18N
		jPanel23.add(jCmb_paraEmptyCheck1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(373, 30, 55,
						-1));

		jCmb_paraEmptyCheck2.setToolTipText("参数是否为非空，如果设置非空，将自动生成参数非空的检验代码^_^");
		jCmb_paraEmptyCheck2
				.setModel(new javax.swing.DefaultComboBoxModel<String>(
						new String[] { "非空", "可空" }));
		jCmb_paraEmptyCheck2.setName("jCmb_paraEmptyCheck2"); // NOI18N
		jPanel23.add(jCmb_paraEmptyCheck2,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(373, 70, 55,
						-1));

		jCmb_paraEmptyCheck3.setToolTipText("参数是否为非空，如果设置非空，将自动生成参数非空的检验代码^_^");
		jCmb_paraEmptyCheck3
				.setModel(new javax.swing.DefaultComboBoxModel<String>(
						new String[] { "非空", "可空" }));
		jCmb_paraEmptyCheck3.setName("jCmb_paraEmptyCheck3"); // NOI18N
		jPanel23.add(jCmb_paraEmptyCheck3,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(373, 110, 55,
						-1));

		jPanel22.add(jPanel23,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 430,
						190));

		jScrollPane11.setName("jScrollPane11"); // NOI18N

		jTa_methodPreview1.setColumns(20);
		jTa_methodPreview1.setEditable(false);
		jTa_methodPreview1.setLineWrap(true);
		jTa_methodPreview1.setRows(2);
		jTa_methodPreview1
				.setToolTipText("方法的签名预览，在返回类型，方法名，参数信息等发生改变时，自动刷新内容");
		jTa_methodPreview1.setName("jTa_methodPreview1"); // NOI18N
		jScrollPane11.setViewportView(jTa_methodPreview1);

		jPanel22.add(jScrollPane11,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 710,
						50));

		jPanel24.setBorder(javax.swing.BorderFactory
				.createTitledBorder("要添加的方法")); // NOI18N
		jPanel24.setName("jPanel24"); // NOI18N
		jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jScrollPane12.setName("jScrollPane12"); // NOI18N
		jPanel24.add(jScrollPane12,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 120,
						160, 100));

		jScrollPane13.setName("jScrollPane13"); // NOI18N

		jTable_methodlst.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { {}, {}, {}, {} }, new String[] {

				}));
		jTable_methodlst.setToolTipText("要添加到service的方法列表");
		jTable_methodlst.setName("jTable_methodlst"); // NOI18N
		jTable_methodlst.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jTable_methodlstMouseClicked(evt);
			}
		});
		jScrollPane13.setViewportView(jTable_methodlst);

		jPanel24.add(jScrollPane13,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 690,
						80));

		jPanel22.add(jPanel24,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 710,
						100));

		jBtn_removeMethod1
				.setToolTipText("从下面列表中把选择的方法删除，这样生成的service类中就无此方法了");
		jBtn_removeMethod1.setText("删除"); // NOI18N
		jBtn_removeMethod1.setAlignmentY(0.0F);
		jBtn_removeMethod1.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jBtn_removeMethod1.setName("jBtn_removeMethod1"); // NOI18N
		jBtn_removeMethod1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				jBtn_removeMethod1MouseReleased(evt);
			}
		});
		jPanel22.add(jBtn_removeMethod1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 60,
						-1));

		jBtn_addToMethod1
				.setToolTipText("把当前配置预览的方法添加到下面的列表，这样在生成的service类中就有此方法了");
		jBtn_addToMethod1.setText("添加"); // NOI18N
		jBtn_addToMethod1.setAlignmentY(0.0F);
		jBtn_addToMethod1.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jBtn_addToMethod1.setName("jBtn_addToMethod1"); // NOI18N
		jBtn_addToMethod1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				jBtn_addToMethod1MouseReleased(evt);
			}
		});

		jPanel22.add(jBtn_addToMethod1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 270, 60,
						-1));

		jPanel17.setBorder(javax.swing.BorderFactory
				.createTitledBorder("方法基本信息")); // NOI18N
		jPanel17.setName("jPanel17"); // NOI18N
		jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jTf_methodName1.setToolTipText("可根据返回类型自动生成，也可以手工修改！"); // NOI18N
		jTf_methodName1.setName("jTf_methodName1"); // NOI18N
		jTf_methodName1.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				jTf_methodName1FocusLost(evt);
			}
		});
		jPanel17.add(jTf_methodName1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 170,
						-1));

		jLabel34.setText("方法名："); // NOI18N
		jLabel34.setName("jLabel34"); // NOI18N
		jPanel17.add(jLabel34,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1,
						-1));

		jLabel33.setText("返 回："); // NOI18N
		jLabel33.setName("jLabel33"); // NOI18N
		jPanel17.add(jLabel33,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1,
						-1));

		jCmb_returnType1.setToolTipText("方法返回的类型，改变选项生成方法名称和必要参数！");
		jCmb_returnType1.setName("jCmb_returnType1"); // NOI18N
		jCmb_returnType1.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				jCmb_returnType1ItemStateChanged(evt);
			}
		});
		jPanel17.add(jCmb_returnType1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 270,
						-1));

		jPanel22.add(jPanel17,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 280,
						110));

		jBtn_start.setText("开始生成"); // NOI18N
		jBtn_start.setToolTipText("生成service类到项目中"); // NOI18N
		jBtn_start.setName("jBtn_start"); // NOI18N
		jBtn_start.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				jBtn_startMouseReleased(evt);
			}
		});
		jPanel22.add(jBtn_start,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 110,
						50));

		jBtn_generate_preview.setText("生成预览"); // NOI18N
		jBtn_generate_preview.setToolTipText("生成代码预览到右边的tab页面"); // NOI18N
		jBtn_generate_preview.setName("jBtn_generate_preview"); // NOI18N
		jBtn_generate_preview
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseReleased(java.awt.event.MouseEvent evt) {
						jBtn_generate_previewMouseReleased(evt);
					}
				});

		jPanel22.add(jBtn_generate_preview,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110,
						50));

		jPnl_mainInfo1.add(jPanel22,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730,
						400));

		jTPnl_tab1.addTab("模块基本配置信息", jPnl_mainInfo1); // NOI18N

		jPnl_serviceIntefer1.setToolTipText("service接口类的代码展示");
		jPnl_serviceIntefer1.setName("jPnl_serviceIntefer1"); // NOI18N

		jPanel28.setName("jPanel28"); // NOI18N

		jScrollPane17.setName("jScrollPane17"); // NOI18N

		jTa_serviceInterContent1.setColumns(20);
		jTa_serviceInterContent1.setRows(5);
		jTa_serviceInterContent1.setName("jTa_serviceInterContent1"); // NOI18N
		jScrollPane17.setViewportView(jTa_serviceInterContent1);

		javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(
				jPanel28);
		jPanel28.setLayout(jPanel28Layout);
		jPanel28Layout.setHorizontalGroup(jPanel28Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 730,
				Short.MAX_VALUE));
		jPanel28Layout.setVerticalGroup(jPanel28Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 380,
				Short.MAX_VALUE));

		javax.swing.GroupLayout jPnl_serviceIntefer1Layout = new javax.swing.GroupLayout(
				jPnl_serviceIntefer1);
		jPnl_serviceIntefer1.setLayout(jPnl_serviceIntefer1Layout);
		jPnl_serviceIntefer1Layout
				.setHorizontalGroup(jPnl_serviceIntefer1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jPanel28,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE));
		jPnl_serviceIntefer1Layout.setVerticalGroup(jPnl_serviceIntefer1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		jTPnl_tab1.addTab("service接口信息", jPnl_serviceIntefer1); // NOI18N

		jPnl_serviceImpl1.setToolTipText("service实现类的代码展示");
		jPnl_serviceImpl1.setName("jPnl_serviceImpl1"); // NOI18N

		jPanel29.setName("jPanel29"); // NOI18N

		jScrollPane18.setName("jScrollPane18"); // NOI18N

		jTa_serviceImplContent1.setColumns(20);
		jTa_serviceImplContent1.setRows(5);
		jTa_serviceImplContent1.setName("jTa_serviceImplContent1"); // NOI18N
		jScrollPane18.setViewportView(jTa_serviceImplContent1);

		javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(
				jPanel29);
		jPanel29.setLayout(jPanel29Layout);
		jPanel29Layout.setHorizontalGroup(jPanel29Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 730,
				Short.MAX_VALUE));
		jPanel29Layout.setVerticalGroup(jPanel29Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 380,
				Short.MAX_VALUE));

		javax.swing.GroupLayout jPnl_serviceImpl1Layout = new javax.swing.GroupLayout(
				jPnl_serviceImpl1);
		jPnl_serviceImpl1.setLayout(jPnl_serviceImpl1Layout);
		jPnl_serviceImpl1Layout.setHorizontalGroup(jPnl_serviceImpl1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		jPnl_serviceImpl1Layout.setVerticalGroup(jPnl_serviceImpl1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		jTPnl_tab1.addTab("service实现信息", jPnl_serviceImpl1); // NOI18N

		javax.swing.GroupLayout jDlg_generate_serviceLayout = new javax.swing.GroupLayout(
				jDlg_generate_service.getContentPane());
		jDlg_generate_service.getContentPane().setLayout(
				jDlg_generate_serviceLayout);
		jDlg_generate_serviceLayout
				.setHorizontalGroup(jDlg_generate_serviceLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jDlg_generate_serviceLayout
										.createSequentialGroup()
										.addComponent(jTPnl_tab1)
										.addContainerGap()));
		jDlg_generate_serviceLayout
				.setVerticalGroup(jDlg_generate_serviceLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jTPnl_tab1,
								javax.swing.GroupLayout.Alignment.TRAILING,
								javax.swing.GroupLayout.DEFAULT_SIZE, 406,
								Short.MAX_VALUE));

	}

	public String getCaluName(String name) {
		if (name == null || name.isEmpty()) {
			return "";
		}
		String first = name.substring(0, 1);
		return name.replaceFirst(first, first.toLowerCase());
	}
}
