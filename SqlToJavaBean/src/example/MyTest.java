package example;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
//InputStream��Reader�������������Ļ��ࡣ
//����InputStream�����ֽڣ���Reader�����ַ�
public class MyTest {

	 public static void main(String[] args) {	
		 //list�����ȡ�����е���
		 List<String> list = new ArrayList<String>();
		 //listBean�����ȡ���������ݱ����������ֶ�
		 List<String> listBean = new ArrayList<String>();
		//listBean1�����ȡ�����ݱ�1��Ϣ
		 List<String> listBean1 = new ArrayList<String>();
		//listBean2�����ȡ�����ݱ�2��Ϣ
		 List<String> listBean2 = new ArrayList<String>();
	       try {
	    	   String encoding = "utf-8";
	           File sourceFile = new File("E:\\BY\\myapproval.sql");
	           FileInputStream fileInputStream = new FileInputStream(sourceFile);
	           //InputStreamReader��ת���������ֽ���ת��Ϊ�ַ���
	           InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);	        	           
	           int cc;
	           //strWord���������ȡ�ĵ���
	           String strWord="";
	           //intռ4���ֽڣ�charռ2���ֽ�
	           //inputStreamReader���ַ���������read()ÿ�ζ�ȡ�ĵ�λ���ַ�
	           while ((cc = inputStreamReader.read()) > 0) {
	             if(!((char) cc==' ')&&!((char) cc==';')){
	            	 String strWordTemp=(char) cc+"";  //(char) cc+""����char����ת��Ϊstring����
	            	 strWord=strWord+strWordTemp;
	             }else{
	            	 list.add(strWord);
	            	 strWord="";
	             }
	           }
	           inputStreamReader.close();
	           fileInputStream.close();
	           
	           String filePath1="";
	           String filePath2="";
	           for(int i=0;i<list.size();i++){
	        	   if(list.get(i).contains("CREATE")&&list.
	        			   get(i+1).contains("TABLE")){
	        		  //��������ݱ�ı������ȥ����#��Ϊ��ʶ
	        		  listBean.add(reMoveVar(list.get(i+2))+"#");	     
	        		  
	        	   }
	        	   if(list.get(i).contains("varchar")||list.get(i).contains("int")){
	        		 //��������ݱ���ֶ������ȥ
	        		  listBean.add(reMoveVar(list.get(i-1)));
	        	   }
	           }	
	           //����Ĵ����ǰ�listBean�����ݷֱ����listBean1��listBean2
	           int Tab=1;//���
	           for(int i=0;i<listBean.size();i++){	
	        	   if(listBean.get(i).contains("#")){
	        		   if(Tab==1){
	        			   listBean1.add(listBean.get(i));
	        			   String tem=listBean.get(i);
	        			   //ȥ��tem����Ǹ��ַ�#
	        			   tem=tem.substring(0, tem.length()-1);	        			  
		        		   filePath1=  writeFileName(tem);	
		        		   Tab=2;
	        		   }else{	        			   
	        			   listBean2.add(listBean.get(i));
	        			   String tem=listBean.get(i);
	        			   tem=tem.substring(0, tem.length()-1);	        			  
		        		   filePath2=  writeFileName(tem);	
		        		   Tab=3;
	        		   }
	        		   
	        	   }else{
	        		   if(Tab==2){
	        			   listBean1.add(listBean.get(i));
	        		   }else{
	        			   listBean2.add(listBean.get(i));
	        		   }
	        		   
	        	   }
	        	
	           }
	          //����ļ������ ����Ŀ���ļ���Ϊresutl.txt ׷�ӷ�ʽ
	           FileOutputStream fileOutputStream= new FileOutputStream("E:\\BY\\"+filePath1+".java",true);
	           OutputStreamWriter outputStreamWriter= new OutputStreamWriter(fileOutputStream);
	           //��ͨ��Writerд�����ݲ�̫���㣬��װ��BufferedWriter
	           BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
	          	        
	           String temp_var="";//�����
	           String temp_fun="";//�溯��
	           String temp_funG="";//�湹�캯��
	           for(int i=0;i<listBean1.size();i++){
	        	   System.out.println(">>"+listBean1.get(i));
	        	   if(listBean1.get(i).contains("#")){
	        		   String tem=listBean1.get(i);
        			   tem=tem.substring(0, tem.length()-1);	
        			   bufferedWriter.write(writeClassName(tem));
	        		   temp_funG=writeFun(tem);
	        	   }else{
	        		   temp_var=temp_var+writeVar(listBean1.get(i));
	        		   temp_fun=temp_fun+writeSetFunction(listBean1.get(i));
	        	   }
	        	
	           }
	           bufferedWriter.write(temp_var);
	           bufferedWriter.write(temp_funG);
	           bufferedWriter.write(temp_fun);
	           bufferedWriter.write("}");
	           bufferedWriter.close();
	           temp_var="";//�����
	           temp_fun="";//�溯��
	           temp_funG="";//�湹�캯��
	           
	           fileOutputStream= new FileOutputStream("E:\\BY\\"+filePath2+".java",true);
	           outputStreamWriter= new OutputStreamWriter(fileOutputStream);
	           bufferedWriter = new BufferedWriter(outputStreamWriter);
	           
	           for(int i=0;i<listBean2.size();i++){
	        	   System.out.println("listBean2>>"+listBean2.get(i));
	        	   if(listBean2.get(i).contains("#")){
	        		   String tem=listBean2.get(i);
        			   tem=tem.substring(0, tem.length()-1);	
        			   bufferedWriter.write(writeClassName(tem));
	        		   temp_funG=writeFun(tem);
	        	   }else{
	        		   System.out.println("temp_var>>"+temp_var);
	        		   temp_var=temp_var+writeVar(listBean2.get(i));
	        		   temp_fun=temp_fun+writeSetFunction(listBean2.get(i));
	        	   }
	        	
	           }
	           
	           bufferedWriter.write(temp_var);
	           bufferedWriter.write(temp_funG);
	           bufferedWriter.write(temp_fun);
	           bufferedWriter.write("}");	        
	           bufferedWriter.close();
	        	
	         
	          System.out.println(writeSetFunction("name"));
	          System.out.println(writeVar("name"));
	          System.out.println(writeFun("name"));
	          System.out.println(reMoveVar("'name'"));
	          System.out.println(writeFileName("nameee"));
	      
	           
	       } catch (Exception e) {
	           System.out.println(e);
	           e.getStackTrace();
	       }
	    }
	 
	 /***
	  * 
	  * д����
	  * @param str
	  * @return
	  */
	 public static String writeSetFunction(String str){
		 String tem=str.charAt(0)+"";
		 tem=str.replace(str.charAt(0), tem.toUpperCase().charAt(0));
		 System.out.println(str.replace(str.charAt(0), tem.toUpperCase().charAt(0)));
		 
		 String s1="public void set"+tem+"(String " +str+"){"+
				 "\r\n"+"\t  this."+str+"="+str+";\r\n"
				 +"}"+"\r\n";
		 
		 String s2="public String get"+tem+"(" +"){"+
	     "\r\n"+"\treturn this."+str+";\r\n"
		 +"}"+"\r\n";
	
		 return s1+s2;		 
	 }
	 
	 /***
	  * 
	  * д����
	  * @param str
	  * @return
	  */
     public static String writeVar(String str){
    	 String s1="private String "+str+"=\"\";\r\n";
    	 return s1;
     }
     /***
      * 
      * д���캯��
      * @param str
      * @return
      */
     public static String writeFun(String str){
    	 String tem=str.charAt(0)+"";
		 tem=str.replace(str.charAt(0), tem.toUpperCase().charAt(0));
    	 String s1="public "+tem+""+"Model(){\r\n" +
            "}\r\n";
    	 return s1;
     }
     /***
      * 
      * д��������ز���
      * @param str
      * @return
      */
     public static String writeClassName(String str){
    	 String tem=str.charAt(0)+"";
    	 tem=str.replace(str.charAt(0), tem.toUpperCase().charAt(0));
    	 String s1="public class"+tem+""+"Model(){\r\n" +
    			 "\r\n";
    	 return s1;
     }
     /***
      * 
      * д������
      * @param str
      * @return  StrModel
      */
     public static String writeFileName(String str){
    	 String tem=str.charAt(0)+"";
    	 tem=str.replace(str.charAt(0), tem.toUpperCase().charAt(0));
    	 String s1=tem+""+"Model";
    	 return s1;
     }
     /***
      * 
      * ȥ����һ�������һ�������ַ�
      * @param str
      * @return
      */
     public static String reMoveVar(String str){  	    	
    	 return str.substring(1, str.length()-1);
     }
     
     /***
      * 
      * ��һ����д�����һ��ȥ��
      * @param str
      * @return
      */
     public static String reMoveLastAndReplaceFirst(String str){  
    	 String tem=str.charAt(0)+"";
    	 tem=str.replace(str.charAt(0), tem.toUpperCase().charAt(0));
    	 return tem.substring(0, str.length()-1);
     }     

}


