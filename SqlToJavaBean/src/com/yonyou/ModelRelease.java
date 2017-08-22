package com.yonyou;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.filechooser.FileSystemView;



//InputStream��Reader�������������Ļ��ࡣ
//����InputStream�����ֽڣ���Reader�����ַ�
public class ModelRelease {

	Set<DBFiled> DBFiledSet;

	String[] parentMainClassField = { "dr", "bill_code", "bill_type", "bill_state", "creatorid", "creator",
			"createtime", "modifierid", "modifier", "modifytime", "reviewerid", "reviewer", "reviewtime", "tenantid",
			"company_id", "ts", "attachMgr" };
	String[] parentSubClassField = { "dr", "tenantid", "company_id", "rowState", "ts" };
	// �Ӹ����м̳еı�����Ҫ�޳�����ʼ�� ��������ӱ���
	List<String> parentVarMain = new ArrayList<String>(Arrays.asList(parentMainClassField));

	List<String> parentVarSub = new ArrayList<String>(Arrays.asList(parentSubClassField));

	List<String> list = new ArrayList<String>();

	// listBean�����ȡ���������ݱ����������ֶ�
	List<String> listBean;

	// ����table�����ͱ��ע��
	List<TClass> listTable = new ArrayList<>();

	// listBean1�����ȡ������
	List<String> listMainStr = new ArrayList<String>();;
	// listBean2�����ȡ���ӱ�
	List<String> listSubStr = new ArrayList<String>();;
	
	

	public void readFile(String filePath) {
		list.clear();
		String encoding = "utf-8";
		try {
			File sourceFile = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			// InputStreamReader��ת���������ֽ���ת��Ϊ�ַ���
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
			int cc;
			// strWord���������ȡ�ĵ���
			String strWord = null;
			// intռ4���ֽڣ�charռ2���ֽ�
			// inputStreamReader���ַ���������read()ÿ�ζ�ȡ�ĵ�λ���ַ�

			while ((cc = inputStreamReader.read()) > 0) {
				if (!((char) cc == ' ') && !((char) cc == ';')) {
					String strWordTemp = (char) cc + ""; // (char)
															// cc+""����char����ת��Ϊstring����
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
		System.out.println("\n-----------�����е��ַ���ӡ����-----------------");

		selectDBFiled();

	}

	public void readFileJava(File file,String mainOrSub) {
		try {
			StringBuffer sb = new StringBuffer("");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), Charset.forName("utf-8")));

			String str = null;

			while ((str = br.readLine()) != null) {
				if(!str.equals("}")){
					sb.append(str + "\r\n");
					System.out.println(str);
				}
			}
			if(mainOrSub.equals("main")){
				listMainStr.add(sb.toString());
				System.out.println("main: "+listMainStr.size());
			}else{
				listSubStr.add(sb.toString());
				System.out.println("sub: "+listSubStr.size());
			}
			
			br.close();
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ��sql���ֶ�ѡ��������������ͣ��������ƣ�����ע��
	 */

	public void selectDBFiled() {
		DBFiledSet = new LinkedHashSet<DBFiled>();
		DBFiled dbFiled = null;
		TClass t = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).contains(",")) {
				dbFiled = new DBFiled();
			}

			if (list.get(i).contains("CREATE") && list.get(i + 1).contains("TABLE")) {
				// ��������ݱ�ı������ȥ����#��Ϊ��ʶ
				t = new TClass();
				t.setTableName(reMoveVar(list.get(i + 2)));
				dbFiled = new DBFiled();

			} else if (list.get(i).contains("varchar") || list.get(i).contains("int")
					|| list.get(i).contains("timestamp") || list.get(i).equals("date")
					|| list.get(i).contains("decimal")) {
				// ���ݱ�� �ֶ��� �ֶ�����
				dbFiled.setVarType(list.get(i));
				dbFiled.setVarName(reMoveVar(list.get(i - 1)));
			} else if (list.get(i).equals("COMMENT") && !list.get(i).equals("COMMENT=")) {
				// ���ֶε�ע�ͼӽ�ȥ
				dbFiled.setVarDesc(reMoveVar(list.get(i + 1)));
			} else if (list.get(i).contains("COMMENT=")) {
				// �ѱ��ע�ͼӽ�ȥ
				String commentStr = list.get(i);
				String comment = commentStr.substring(commentStr.indexOf("'") + 1, commentStr.length() - 1);
				t.setTableDesc(comment);

				for (DBFiled d : DBFiledSet) {
					System.out.println("------" + d.toString());
				}
				System.out.println(DBFiledSet.size());

				t.setDBFiledSet(DBFiledSet);

				if (t != null) {
					listTable.add(t);
				}
				DBFiledSet = new LinkedHashSet<DBFiled>();

			}

			if (dbFiled != null && dbFiled.getVarType() != null) {
				DBFiledSet.add(dbFiled);
			}

		}

		System.out.println("\n--------------listTable������-------------------");

		for (TClass s : listTable) {
			System.out.println(s.toString());
		}
		// return listTable;
	}

	// ��ȡ����
	public String getTableName(List<TClass> list, int i) {
		return list.get(i).getTableName();
	}

	// �޸ı���
	public void fixTableName(List<TClass> list, int i, String str) {
		list.get(i).setTableName(str);
	}

	/**
	 * д���ļ�
	 */
	public void writeFile(String outFilePath, String parentClass, String className) {
		// ����ļ������ ����Ŀ���ļ���Ϊresutl.txt ���ǵķ�ʽ
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(outFilePath + className + ".java", false);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			// ��ͨ��Writerд�����ݲ�̫���㣬��װ��BufferedWriter
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

			String temp_var = "";// �����

			String tableName = listTable.get(0).getTableName();
			String tableDesc = listTable.get(0).getTableDesc();
			bufferedWriter.write(writeClassName(tableName, tableDesc, parentClass, className));

			System.out.println(listTable.get(0).getDBFiledSet().size());

			for (DBFiled bean : listTable.get(0).getDBFiledSet()) {
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
	 * дsetter����
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
	 * д����
	 * 
	 * @param str
	 * @return
	 */
	public static String writeVar(String strType, String strName, String strDesc) {
		String s = "";
		// ���Ƚ�������ת��Ϊ���ʵĸ�ʽ �շ�ʽ
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
		// ���Ƚ�������ת��Ϊ���ʵĸ�ʽ �շ�ʽ

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
	 * д���캯��
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
	 * д��������ز���
	 * 
	 * @param str
	 * @return
	 */
	public static String writeClassName(String tableName, String classDesc, String parentClass, String className) {
		// String tem = writeFileName(tableName);
		String classHeader = "@Entity\r\n" + "@Table(name = \"" + tableName + "\")\r\n" + "@Display(\"" + classDesc
				+ "\")\r\n" + "public class " + className + " extends " + parentClass + "{\r\n" + "\r\n";
		return classHeader;
	}

	/***
	 * ������д ��sql����ת��Ϊ�����ĵ�������ĸ��д
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
	 * ȥ����һ�������һ�������ַ�
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
	 * ��һ����д�����һ��ȥ��
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
	 * ת��Ϊ������ ��һ�����ʵ�����ĸСд��������������ĸ��д
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
	
	
	public void writeMainOrSub(String[] mainStr, String[] subStr) {
		/**
		 * ��������
		 */

		for (int i = 0; i < mainStr.length; i++) {
			try {
				File filepath = FileSystemView.getFileSystemView().getHomeDirectory();
				System.out.println(filepath +mainStr[i]);
				FileWriter writer = new FileWriter(filepath+"\\" +mainStr[i]);
				BufferedWriter bw = new BufferedWriter(writer);
				String str ="";
				for(int j = 0;j < subStr.length;j++){
					String className = subStr[j].substring(0, subStr[j].indexOf("."));
					String oneToMany = "@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = \"parent\")\r\n"
							+ "private List<"+className+"> "+className+" = new ArrayList<>();\r\n\r\n";
					str+=oneToMany;
					
				}
				System.out.println(str);
				String temp = listMainStr.get(i)+str+"}";
				bw.write(temp);
				bw.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/**
		 * �����ӱ�
		 */
		for (int i = 0; i < subStr.length; i++) {
			try {
				File filepath = FileSystemView.getFileSystemView().getHomeDirectory();
				System.out.println(filepath +subStr[i]);
				FileWriter writer = new FileWriter(filepath+"\\" +subStr[i]);
				BufferedWriter bw = new BufferedWriter(writer);
				String str ="";
				System.out.println(mainStr.length);
				for(int j = 0;j < mainStr.length;j++){
					String className = mainStr[0].substring(0, mainStr[0].indexOf("."));
					
					String manyToOne = "@ManyToOne\r\n@JoinColumn(name = \"pid\",insertable = false,updatable = false)\r\n"
							+ "private "+className+" parent;\r\n\r\n";
					str+=manyToOne;
					
				}
				System.out.println(str);
				String temp = listSubStr.get(i)+str+"}";
				bw.write(temp);
				bw.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}

	// ---------------------------------------------------------------

/*	public static void main(String[] args) {
		ModelRelease test = new ModelRelease();

		String filePath = "C:\\Users\\zhaowenr\\Desktop\\sqlToJavaBean\\";
		String file = filePath + "ss_quy_subsection_acpt.sql";
		test.readFile(file);
		test.writeFile(filePath, "SuperMainEntity", "Test");
	}*/

	

}
