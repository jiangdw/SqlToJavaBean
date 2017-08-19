package com.yonyou;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


//InputStream和Reader是所有输入流的基类。
//区别：InputStream面向字节，而Reader面向字符
public class ModelRelease{

	Set<DBFiled> DBFiledSet;
	
	
	String[] parentMainClassField = { "dr", "bill_code", "bill_type", "bill_state", "creatorid", "creator", "createtime",
			"modifierid", "modifier", "modifytime", "reviewerid", "reviewer", "reviewtime", "tenantid", "company_id",
			"ts", "attachMgr" };
	String[] parentSubClassField ={"dr", "tenantid", "company_id", "rowState", "ts"};
	// 从父类中继承的变量，要剔除，初始化 主表父类和子表父类
	List<String> parentVarMain = new ArrayList<String>(Arrays.asList(parentMainClassField));

	List<String> parentVarSub = new ArrayList<String>(Arrays.asList(parentSubClassField));
	
	List<String> list = new ArrayList<String>();

	// listBean保存读取的所有数据表名和数据字段
	List<String> listBean;
	
	//保存table表名和表的注释
	List<TClass> listTable = new ArrayList<>();
	
	// listBean1保存读取的数据表1信息
	List<String> listBean1;
	// listBean2保存读取的数据表2信息
	List<String> listBean2 = new ArrayList<>();
	
	//String fileName = "";// 文件名，类名
	String filePath2 = "";


	public void readFile(String filePath) {
		list.clear();
		String encoding = "utf-8";
		try {
			File sourceFile = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			// InputStreamReader是转换流，将字节流转化为字符流
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
			int cc;
			// strWord用来保存读取的单词
			String strWord = null;
			// int占4个字节，char占2个字节
			// inputStreamReader是字符流，调用read()每次读取的单位是字符

			while ((cc = inputStreamReader.read()) > 0) {
				if (!((char) cc == ' ') && !((char) cc == ';')) {
					String strWordTemp = (char) cc + ""; // (char)
															// cc+""代表char类型转化为string类型
					strWord = strWord + strWordTemp;
				} else {
					list.add(strWord);
					strWord = "";
				}
			}
			inputStreamReader.close();
			fileInputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String s : list) {
			System.out.print(s + " ");
		}
		System.out.println("\n-----------将所有的字符打印出来-----------------");

	}

	
	/**
	 * 将sql中字段选择出来，属性类型，属性名称，属性注释
	 */

	public void selectDBFiled() {
		DBFiledSet = new LinkedHashSet<DBFiled>();
		DBFiled dbFiled = null;
		TClass t = null;
		listBean = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).contains(",")) {
				dbFiled = new DBFiled();
			}

			if (list.get(i).contains("CREATE") && list.get(i + 1).contains("TABLE")) {
				// 这里把数据表的表名存进去，以#作为标识
				t = new TClass();
				t.setTableName(reMoveVar(list.get(i + 2)));
				//listBean.add(reMoveVar(list.get(i + 2)) + "#");
				dbFiled = new DBFiled();

			} else if (list.get(i).contains("varchar") || list.get(i).contains("int")
					|| list.get(i).contains("timestamp") || list.get(i).equals("date")
					|| list.get(i).contains("decimal")) {
				// 数据表的 字段名 字段类型
				dbFiled.setVarType(list.get(i));
				dbFiled.setVarName(reMoveVar(list.get(i - 1)));
			} else if (list.get(i).equals("COMMENT") && !list.get(i).equals("COMMENT=")) {
				// 把字段的注释加进去
				dbFiled.setVarDesc(reMoveVar(list.get(i + 1)));
			}else if(list.get(i).contains("COMMENT=")){
				// 把表的注释加进去
				String str = list.get(i);
				String string = str.substring(str.indexOf("'") + 1, str.length() - 1);
				t.setTableDesc(string);
				if(t!=null){
					listTable.add(t);
				}
				//listBean.add(string);
			}

			if (dbFiled != null&&dbFiled.getVarType()!=null) {
				DBFiledSet.add(dbFiled);
			}
		}

		System.out.println("---------------打印出所有的属性---------------");

		for (DBFiled b : DBFiledSet) {
			System.out.println(b.toString());
		}
		

	/*	if (list.get(list.size() - 1).contains("COMMENT=")) {
			// 把表的注释加进去
			String str = list.get(list.size() - 1);
			String string = str.substring(str.indexOf("'") + 1, str.length() - 1);
			listBean.add(string);
		}*/

		System.out.println("\n--------------listTable中内容-------------------");
		
		  for (TClass s : listTable) { System.out.println(s.toString()); }
		  
		 
	}

	//获取表名
	public String getTableName(List<TClass> list,int i){
		return list.get(i).getTableName();
	}
	//修改表名
	public void fixTableName(List<TClass> list,int i,String str){
		 list.get(i).setTableName(str);
	}
	

	/**
	 * 写入文件
	 */
	public void writeFile(String outFilePath, String parentClass,String className) {

		// 获得文件输出流 假设目标文件名为resutl.txt 覆盖的方式
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(outFilePath + className + ".java", false);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			// 普通的Writer写入内容不太方便，包装成BufferedWriter
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

			String temp_var = "";// 存变量
			// String temp_fun = "";// 存函数
			// String temp_funG = "";// 存构造函数

			for (int i = 0; i < listBean.size() - 1; i++) {
				if (listBean.get(i).contains("#")) {
					String tableName = listBean.get(i);
					tableName = tableName.substring(0, tableName.length() - 1);
					String classDesc = listBean.get(listBean.size() - 1);
					// 写入类名
					bufferedWriter.write(writeClassName(tableName, classDesc, parentClass,className));
				}

			}

			for (DBFiled bean : DBFiledSet) {
				if (parentClass == "SuperMainEntity") {
					if (bean.getVarName() != null && !parentVarMain.contains(bean.getVarName())) {
						temp_var = temp_var + writeVar2(bean);
					}
				} else {
					if (bean.getVarName() != null && !parentVarSub.contains(bean.getVarName())) {
						temp_var = temp_var + writeVar2(bean);
					}
				}
			}

			bufferedWriter.write(temp_var);
			bufferedWriter.write("}");
			bufferedWriter.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * 写setter函数
	 * 
	 * @param str
	 * @return
	 */
	public static String writeSetFunction(String str) {
		String tem = str.charAt(0) + "";
		tem = str.replace(str.charAt(0), tem.toUpperCase().charAt(0));
		System.out.println(str.replace(str.charAt(0), tem.toUpperCase().charAt(0)));

		String s1 = "public void set" + tem + "(String " + str + "){" + "\r\n" + "\t  this." + str + "=" + str + ";\r\n"
				+ "}" + "\r\n";

		String s2 = "public String get" + tem + "(" + "){" + "\r\n" + "\treturn this." + str + ";\r\n" + "}" + "\r\n";

		return s1 + s2;
	}

	/***
	 * 
	 * 写变量
	 * 
	 * @param str
	 * @return
	 */
	public static String writeVar(String strType, String strName, String strDesc) {
		String s = "";
		// 首先将变量名转化为合适的格式 驼峰式
		String tempStrName = ReplaceFirstToLow(strName);

		if (strType.contains("varchar")) {
			if (tempStrName.equals("id")) {
				s += "@Id\r\n" + "@GenericGenerator(name = \"" + "system-uuid\"" + ", strategy = \"" + "uuid\")\r\n"
						+ "@Column(name = \"" + "id\"" + ", unique = true, nullable = false, length= 32)\r\n";
				s += "private String " + tempStrName + ";\r\n\r\n";

			} else {
				s += "@Display(\"" + strDesc + "\")\r\n" + "@Column(name = \"" + strName + "\")\r\n";
				s += "private String " + tempStrName + ";\r\n\r\n";
			}

		} else if (strType.contains("int")) {
			s += "@Display(\"" + strDesc + "\")\r\n" + "@Column(name = \"" + strName + "\")\r\n";
			s += "private int " + tempStrName + ";\r\n\r\n";
		} else if (strType.contains("timestamp")) {
			s += "@Display(\"" + strDesc + "\")\r\n" + "@Column(name = \"" + strName + "\")\r\n";
			s += "private Date " + tempStrName + ";\r\n\r\n";
		} else if (strType.contains("datetime")) {
			s += "@Display(\"" + strDesc + "\")\r\n" + "@Column(name = \"" + strName + "\")\r\n";
			s += "private Date " + tempStrName + ";\r\n\r\n";
		} else if (strType.contains("date")) {
			s += "@Display(\"" + strDesc + "\")\r\n" + "@Column(name = \"" + strName + "\")\r\n";
			s += "private String " + tempStrName + ";\r\n\r\n";
		}
		return s;
	}

	private String writeVar2(DBFiled bean) {
		String strType = bean.getVarType();
		String strName = bean.getVarName();
		String strDesc = bean.getVarDesc();
		String s = "";
		// 首先将变量名转化为合适的格式 驼峰式

		String tempStrName = ReplaceFirstToLow(strName);

		if (strType.contains("varchar")) {
			if (tempStrName.equals("id")) {
				s += "@Id\r\n" + "@GenericGenerator(name = \"" + "system-uuid\"" + ", strategy = \"" + "uuid\")\r\n"
						+ "@Column(name = \"" + "id\"" + ", unique = true, nullable = false, length= 32)\r\n";
				s += "private String " + tempStrName + ";\r\n\r\n";
			} else {
				if (strDesc != null) {
					s += "@Display(\"" + strDesc + "\")\r\n";
				}
				s += "@Column(name = \"" + strName + "\")\r\n";
				s += "private String " + tempStrName + ";\r\n\r\n";
			}

		} else if (strType.contains("int")) {
			if (strDesc != null) {
				s += "@Display(\"" + strDesc + "\")\r\n";
			}
			s += "@Column(name = \"" + strName + "\")\r\n";
			s += "private int " + tempStrName + ";\r\n\r\n";
		} else if (strType.contains("timestamp")) {
			s += "@Display(\"" + strDesc + "\")\r\n" + "@Column(name = \"" + strName + "\")\r\n";
			s += "private Date " + tempStrName + ";\r\n\r\n";
		} else if (strType.contains("datetime")) {
			s += "@Display(\"" + strDesc + "\")\r\n" + "@Column(name = \"" + strName + "\")\r\n";
			s += "@JsonFormat(pattern = \"yyyy-MM-dd\", timezone = \"GMT+8\")\r\n";
			s += "private Date " + tempStrName + ";\r\n\r\n";
		} else if (strType.contains("date")) {
			s += "@Display(\"" + strDesc + "\")\r\n" + "@Column(name = \"" + strName + "\")\r\n";
			s += "@JsonFormat(pattern = \"yyyy-MM-dd\", timezone = \"GMT+8\")\r\n";
			s += "private Date " + tempStrName + ";\r\n\r\n";
		} else if (strType.contains("decimal")) {
			s += "@Display(\"" + strDesc + "\")\r\n" + "@Column(name = \"" + strName + "\")\r\n";
			s += "private BigDecimal " + tempStrName + ";\r\n\r\n";
		}
		return s;
	}

	/***
	 * 
	 * 写构造函数
	 * 
	 * @param str
	 * @return
	 */
	public static String writeFun(String str) {
		String tem = str.charAt(0) + "";
		tem = str.replace(str.charAt(0), tem.toUpperCase().charAt(0));
		String s1 = "public " + tem + "" + "Model(){\r\n" + "}\r\n";
		return s1;
	}

	/***
	 * 
	 * 写类名和相关部分
	 * 
	 * @param str
	 * @return
	 */
	public static String writeClassName(String tableName, String classDesc, String parentClass,String className) {
		//String tem = writeFileName(tableName);
		String classHeader = "@Entity\r\n" + "@Table(name = \"" + tableName + "\")\r\n" + "@Display(\"" + classDesc + "\")\r\n"
				+ "public class " + className + " extends " + parentClass + "{\r\n" + "\r\n";
		return classHeader;
	}

	/***
	 * 类名大写 将sql表名转化为类名的单词首字母大写
	 * 
	 * @param str
	 * @return StrModel
	 */
	public static String writeFileName(String str) {
		String[] string = str.split("_");
		String temp = "";
		for (String s : string) {
			if (Character.isUpperCase(s.charAt(0))) {
				temp += s;
			} else {
				String str1 = s.substring(0, 1).toUpperCase() + s.substring(1);
				temp += str1;
			}
		}
		String s1 = temp + "" + "Entity";
		return s1;
	}

	/***
	 * 
	 * 去掉第一个和最后一个多余字符
	 * 
	 * @param str
	 * @return
	 */
	public static String reMoveVar(String str) {
		if (str.contains(",")) {
			return str.substring(1, str.length() - 4);
		} else {
			return str.substring(1, str.length() - 1);
		}
	}

	/***
	 * 
	 * 第一个大写和最后一个去掉
	 * 
	 * @param str
	 * @return
	 */
	public static String reMoveLastAndReplaceFirst(String str) {
		String tem = str.charAt(0) + "";
		tem = str.replace(str.charAt(0), tem.toUpperCase().charAt(0));
		return tem.substring(0, str.length() - 1);
	}

	/***
	 * 转化为变量名 第一个单词的首字母小写和其他单词首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String ReplaceFirstToLow(String str) {
		String[] var = str.split("_");
		int len = var.length;
		if (len > 1) {
			String temp = var[0];
			for (int i = 1; i < len; i++) {
				if (Character.isUpperCase(var[i].charAt(0))) {
					temp += var[i];
				} else {
					String str1 = var[i].substring(0, 1).toUpperCase() + var[i].substring(1);
					temp += str1;
				}
			}
			return temp;

		} else {
			return var[0];
		}
	}

	// ---------------------------------------------------------------

	public static void main(String[] args) {
		ModelRelease test = new ModelRelease();

		String filePath = "C:\\Users\\zhaowenr\\Desktop\\sqlToJavaBean\\";
		String file = filePath + "ss_quy_subsection_acpt.sql";
		test.readFile(file);
		test.selectDBFiled();
		test.writeFile(filePath, "SuperMainEntity", file);
	}
	 

}
