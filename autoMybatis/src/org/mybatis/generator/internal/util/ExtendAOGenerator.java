package org.mybatis.generator.internal.util;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.GeneratedFile;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.exception.ShellException;

public class ExtendAOGenerator {
	
	private MyBatisGenerator generator;
	private List<GeneratedJavaFile> generatedJavaFiles;
	private List<GeneratedXmlFile> generatedXmlFiles;
	private String dbdir;
	private String exampleClassDir;
	private String exampleClassPackage;
	private String aoClassPackage;
	private String domainName;
	private String domainBeanName;
	private Pattern pattern = Pattern.compile("(.*?)Mapper.java");

	private String encoding = "utf-8";
	private byte[] buffer = new byte[500];

	public ExtendAOGenerator(String dbdir, String exampleClassDir,
			String exampleClassPackage, MyBatisGenerator generator, List<GeneratedJavaFile> generatedJavaFiles, List<GeneratedXmlFile> generatedXmlFiles) {
		this.dbdir = dbdir;
		this.exampleClassDir = exampleClassDir;
		this.exampleClassPackage = exampleClassPackage;
		this.aoClassPackage = exampleClassPackage
				.replace("generator", "extend");
		this.generatedJavaFiles = generatedJavaFiles;
		this.generatedXmlFiles = generatedXmlFiles;
		this.generator = generator;
	}

	public void generator() {
		File parent = new File(this.dbdir);
		File[] files = parent.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if (!pathname.isFile()) {
					return false;
				}
				return pathname.getName().matches(".*?Mapper.java");
			}
		});
		for (File f : files)
			generator(f);
	}

	private void generatorExtendClass(GeneratedJavaFile gjf, GeneratedXmlFile gxf) {
		GeneratedJavaFile aoExtendCls = FileGenerateUtil.getExtendAOGJFJava(gjf, gxf, this.aoClassPackage, this.exampleClassPackage, this.domainName, this.domainBeanName);
		try {
			writeGenerateFile(aoExtendCls);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeGenerateFile(GeneratedFile usedGif) throws InterruptedException, IOException {
		File targetFile = null;
		String source = null;
		try {
			File directory = generator.getShellCallback().getDirectory(
					usedGif.getTargetProject(), usedGif.getTargetPackage());
			targetFile = new File(directory, usedGif.getFileName());
			if (targetFile.exists()) {
				generator.getWarnings().add(
						getString("Warning.26", targetFile.getAbsolutePath())); //$NON-NLS-1$
				return;
			} else {
				source = usedGif.getFormattedContent();
			}
			generator.writeFile(targetFile, source);
		} catch (ShellException e) {
			generator.getWarnings().add(e.getMessage());
		}
	}
	private void generator(File f) {
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}

		String fileName = f.getName();
		System.err.println("开始处理" + fileName);
		Matcher m = this.pattern.matcher(fileName);
		if (m.find()) {
			this.domainName = m.group(1);
			this.domainBeanName = (this.domainName + "AO");
		} else {
			System.err.println("生成[" + f.getPath() + "]发生错误，文件名不匹配!");
			return;
		}
		String tmpFileName = fileName.replace(".java", "");
		GeneratedJavaFile gjf = getGjf(tmpFileName);
		GeneratedXmlFile gxf = getGxf(tmpFileName);;
		if(gjf == null) {
			return;
		}
		generatorExtendClass(gjf, gxf);
		String xmlPath = f.getParentFile().getPath() + File.separator
				+ this.domainName + "Mapper.xml";
//		generatorJava(f);
//		generatorXml(new File(xmlPath));
//		generatorExample();
		System.err.println("完成处理" + fileName);
	}
	
	private GeneratedJavaFile getGjf(String fileName) {
		for(GeneratedJavaFile gjf : generatedJavaFiles) {
			String tmp = gjf.getCompilationUnit().getType().getFullyQualifiedName();
			if(tmp.contains(fileName)) {
				return gjf;
			}
		}
		return null;
	}
	
	private GeneratedXmlFile getGxf(String fileName) {
		fileName = fileName.replaceAll(".java", ".xml");
		for(GeneratedXmlFile gxm : generatedXmlFiles) {
			if(gxm.getFileName().contains(fileName)) {
				return gxm;
			}
		}
		return null;
	}

	private void generatorExample() {
		String pathname = this.exampleClassDir + File.separator
				+ this.domainName + "Example.java";
		File f = new File(pathname);
		InputStream is = null;
		OutputStream os = null;
		Pattern p0 = Pattern.compile("public class " + this.domainName
				+ "Example");
		Pattern p1 = Pattern.compile("生成分页所需元素开始");
		Pattern p2 = Pattern.compile("生成分页所需元素结束");
		try {
			is = new FileInputStream(f);
			os = new ByteArrayOutputStream();
			String line = null;
			boolean continueFlag = false;
			while ((line = readLine(is)) != null)
				if (p0.matcher(line).find()) {
					os.write((line + "\n").getBytes(this.encoding));
					os.write("\t/*生成分页所需元素开始*/\r\n".getBytes(this.encoding));
					os.write("\tprivate int startRecord = -1;\r\n"
							.getBytes(this.encoding));
					os.write("\r\n".getBytes(this.encoding));
					os.write("\tprivate int pageCount = -1;\r\n"
							.getBytes(this.encoding));
					os.write("\r\n".getBytes(this.encoding));
					os.write("\tpublic void setStartRecord(int startRecord){\r\n"
							.getBytes(this.encoding));
					os.write("\t   this.startRecord = startRecord;\r\n"
							.getBytes(this.encoding));
					os.write("\t}\r\n".getBytes(this.encoding));
					os.write("\r\n".getBytes(this.encoding));
					os.write("\tpublic int getStartRecord(){\r\n"
							.getBytes(this.encoding));
					os.write("\t   return startRecord;\r\n"
							.getBytes(this.encoding));
					os.write("\t}\r\n".getBytes(this.encoding));
					os.write("\r\n".getBytes(this.encoding));
					os.write("\tpublic void setPageCount(int pageCount){\r\n"
							.getBytes(this.encoding));
					os.write("\t   this.pageCount = pageCount;\r\n"
							.getBytes(this.encoding));
					os.write("\t}\r\n".getBytes(this.encoding));
					os.write("\r\n".getBytes(this.encoding));
					os.write("\tpublic int getPageCount(){\r\n"
							.getBytes(this.encoding));
					os.write("\t   return pageCount;\r\n"
							.getBytes(this.encoding));
					os.write("\t}\r\n".getBytes(this.encoding));
					os.write("\r\n".getBytes(this.encoding));
					os.write("\t/*生成分页所需元素结束*/\r\n".getBytes(this.encoding));
				} else {
					if (p1.matcher(line).find()) {
						continueFlag = true;
					} else if (p2.matcher(line).find()) {
						continueFlag = false;
						continue;
					}
					if (continueFlag) {
						System.err.write((line + "\n").getBytes(this.encoding));
					} else
						os.write((line + "\n").getBytes(this.encoding));
				}
			close(os);
			byte[] data = ((ByteArrayOutputStream) os).toByteArray();
			os = new FileOutputStream(f);
			os.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(is);
			close(os);
		}
	}

	private void generatorJava(File f) {
		InputStream is = null;
		OutputStream os = null;
		String tmp1 = this.aoClassPackage.replaceAll("\\.", "\\\\\\.");
		String tmp2 = this.exampleClassPackage.replaceAll("\\.", "\\\\\\.");
		Pattern p0 = Pattern.compile("import " + tmp1 + "\\."
				+ this.domainBeanName + ";");
		Pattern p1 = Pattern.compile("import " + tmp2 + "\\." + this.domainName
				+ ";");
		Pattern p2 = Pattern.compile("(.*?)List<" + this.domainName + ">(.*)");
		Pattern p3 = Pattern
				.compile(this.domainName + "\\s*selectByPrimaryKey");
		try {
			is = new FileInputStream(f);
			os = new ByteArrayOutputStream();
			String line = null;
			while ((line = readLine(is)) != null) {
				if (p0.matcher(line).find()) {
					continue;
				}
				if (p1.matcher(line).find()) {
					os.write((line + "\n").getBytes(this.encoding));
					os.write(("import " + this.aoClassPackage + "."
							+ this.domainBeanName + ";\r\n")
							.getBytes(this.encoding));
				} else {
					Matcher m = p2.matcher(line);
					if (m.find()) {
						os.write((m.group(1) + "List<" + this.domainBeanName
								+ ">" + m.group(2) + "\n")
								.getBytes(this.encoding));
					} else {
						m = p3.matcher(line);
						if (m.find())
							os.write(("\t" + this.domainBeanName + " selectByPrimaryKey(String id);")
									.getBytes(this.encoding));
						else {
							os.write((line + "\n").getBytes(this.encoding));
						}
					}
				}
			}
			close(os);
			byte[] data = ((ByteArrayOutputStream) os).toByteArray();
			os = new FileOutputStream(f);
			os.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(is);
			close(os);
		}
	}

	private void close(InputStream is) {
		try {
			if (is != null)
				is.close();
		} catch (Exception localException) {
		}
	}

	private void close(OutputStream os) {
		try {
			if (os != null)
				os.flush();
		} catch (Exception localException) {
		}
		try {
			if (os != null)
				os.close();
		} catch (Exception localException1) {
		}
	}

	private void generatorXml(File f) {
		InputStream is = null;
		OutputStream os = null;
		Pattern p = Pattern.compile("(.*?)id=\"BaseResultMap\" type=\""
				+ this.exampleClassPackage + "." + this.domainName + "\"(.*)");
		Pattern p3 = Pattern.compile("(.*?)id=\"ResultMapWithBLOBs\" type=\""
				+ this.exampleClassPackage + "." + this.domainName + "\"(.*)");
		Pattern p0 = Pattern.compile("order.*?by.*?orderByClause");
		Pattern p1 = Pattern.compile("生成分页所需元素开始");
		Pattern p2 = Pattern.compile("生成分页所需元素结束");
		int flag = -1;
		try {
			is = new FileInputStream(f);
			os = new ByteArrayOutputStream();
			String line = null;
			boolean continueFlag = false;
			while ((line = readLine(is)) != null) {
				Matcher m = p.matcher(line);
				Matcher m3 = p3.matcher(line);
				if (m.find()) {
					os.write((m.group(1) + "id=\"BaseResultMap\" type=\""
							+ this.aoClassPackage + "." + this.domainBeanName
							+ "\"" + m.group(2) + "\n").getBytes(this.encoding));
				} else if (m3.find()) {
					os.write((m3.group(1) + "id=\"ResultMapWithBLOBs\" type=\""
							+ this.aoClassPackage + "." + this.domainBeanName
							+ "\"" + m3.group(2) + "\n")
							.getBytes(this.encoding));
				} else if (p0.matcher(line).find()) {
					os.write((line + "\n").getBytes(this.encoding));
					flag = 0;
				} else {
					if (p1.matcher(line).find()) {
						continueFlag = true;
					} else if (p2.matcher(line).find()) {
						continueFlag = false;
						continue;
					}

					if (flag == 0) {
						os.write((line + "\n").getBytes(this.encoding));
						os.write("\t   <!-- 生成分页所需元素开始 -->\r\n"
								.getBytes(this.encoding));
						os.write("    <if test=\"startRecord != -1\">\r\n"
								.getBytes(this.encoding));
						os.write("      limit #{startRecord},#{pageCount}\r\n"
								.getBytes(this.encoding));
						os.write("    </if>\r\n".getBytes(this.encoding));
						os.write("\t   <!-- 生成分页所需元素结束 -->\r\n"
								.getBytes(this.encoding));
						flag = -1;
					} else {
						if (continueFlag) {
							continue;
						}
						os.write((line + "\n").getBytes(this.encoding));
					}
				}
			}
			close(os);
			byte[] data = ((ByteArrayOutputStream) os).toByteArray();
			os = new FileOutputStream(f);
			os.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(is);
			close(os);
		}
	}

	private String readLine(InputStream reader) throws Exception {
		int len = 0;
		int offset = 0;
		byte[] middle = (byte[]) null;
		byte[] result = (byte[]) null;
		int bt = -1;

		while ((bt = reader.read()) != -1) {
			if (bt == 10) {
				break;
			}
			if (len == 500) {
				if (offset != 0) {
					middle = new byte[offset];
					System.arraycopy(result, 0, middle, 0, offset);
				}
				offset += len;
				result = new byte[offset];
				if (offset != len) {
					System.arraycopy(middle, 0, result, 0, offset - len);
				}
				System.arraycopy(this.buffer, 0, result, offset - len, len);
				len = 0;
			}
			this.buffer[(len++)] = (byte) bt;
		}
		offset += len;
		if ((bt == -1) && (offset == 0))
			return null;
		if (offset == 0) {
			return "";
		}
		if (offset > 500) {
			middle = new byte[offset - len];
			System.arraycopy(result, 0, middle, 0, offset - len);
			result = new byte[offset];
			System.arraycopy(middle, 0, result, 0, offset - len);
		} else {
			result = new byte[offset];
		}
		System.arraycopy(this.buffer, 0, result, offset - len, len);

		return new String(result, this.encoding);
	}
}
