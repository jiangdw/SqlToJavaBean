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
//InputStream和Reader是所有输入流的基类。
//区别：InputStream面向字节，而Reader面向字符
public class MyTest {

	 public static void main(String[] args) {	
		 //list保存读取的所有单词
		 List<String> list = new ArrayList<String>();
		 //listBean保存读取的所有数据表名和数据字段
		 List<String> listBean = new ArrayList<String>();
		//listBean1保存读取的数据表1信息
		 List<String> listBean1 = new ArrayList<String>();
		//listBean2保存读取的数据表2信息
		 List<String> listBean2 = new ArrayList<String>();
	       try {
	    	   String encoding = "utf-8";
	           File sourceFile = new File("E:\\BY\\myapproval.sql");
	           FileInputStream fileInputStream = new FileInputStream(sourceFile);
	           //InputStreamReader是转换流，将字节流转化为字符流
	           InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);	        	           
	           int cc;
	           //strWord用来保存读取的单词
	           String strWord="";
	           //int占4个字节，char占2个字节
	           //inputStreamReader是字符流，调用read()每次读取的单位是字符
	           while ((cc = inputStreamReader.read()) > 0) {
	             if(!((char) cc==' ')&&!((char) cc==';')){
	            	 String strWordTemp=(char) cc+"";  //(char) cc+""代表char类型转化为string类型
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
	        		  //这里把数据表的表名存进去，以#作为标识
	        		  listBean.add(reMoveVar(list.get(i+2))+"#");	     
	        		  
	        	   }
	        	   if(list.get(i).contains("varchar")||list.get(i).contains("int")){
	        		 //这里把数据表的字段名存进去
	        		  listBean.add(reMoveVar(list.get(i-1)));
	        	   }
	           }	
	           //下面的代码是把listBean中数据分别存入listBean1和listBean2
	           int Tab=1;//标记
	           for(int i=0;i<listBean.size();i++){	
	        	   if(listBean.get(i).contains("#")){
	        		   if(Tab==1){
	        			   listBean1.add(listBean.get(i));
	        			   String tem=listBean.get(i);
	        			   //去掉tem最后那个字符#
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
	          //获得文件输出流 假设目标文件名为resutl.txt 追加方式
	           FileOutputStream fileOutputStream= new FileOutputStream("E:\\BY\\"+filePath1+".java",true);
	           OutputStreamWriter outputStreamWriter= new OutputStreamWriter(fileOutputStream);
	           //普通的Writer写入内容不太方便，包装成BufferedWriter
	           BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
	          	        
	           String temp_var="";//存变量
	           String temp_fun="";//存函数
	           String temp_funG="";//存构造函数
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
	           temp_var="";//存变量
	           temp_fun="";//存函数
	           temp_funG="";//存构造函数
	           
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
	  * 写函数
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
	  * 写变量
	  * @param str
	  * @return
	  */
     public static String writeVar(String str){
    	 String s1="private String "+str+"=\"\";\r\n";
    	 return s1;
     }
     /***
      * 
      * 写构造函数
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
      * 写类名和相关部分
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
      * 写函数名
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
      * 去掉第一个和最后一个多余字符
      * @param str
      * @return
      */
     public static String reMoveVar(String str){  	    	
    	 return str.substring(1, str.length()-1);
     }
     
     /***
      * 
      * 第一个大写和最后一个去掉
      * @param str
      * @return
      */
     public static String reMoveLastAndReplaceFirst(String str){  
    	 String tem=str.charAt(0)+"";
    	 tem=str.replace(str.charAt(0), tem.toUpperCase().charAt(0));
    	 return tem.substring(0, str.length()-1);
     }     

}


