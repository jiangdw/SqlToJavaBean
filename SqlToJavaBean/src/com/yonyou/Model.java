package com.yonyou;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileSystemView;

import com.sun.corba.se.impl.orb.ParserTable.TestAcceptor1;

public class Model {
	UI view = new UI();
	ModelRelease test = new ModelRelease();
	// 文件选择对象
	JFileChooser chooser = new JFileChooser();

	private static int flag = 1;

	public Model() {

		view.setVisible(true);
		view.chooseFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == view.chooseFileButton) {
					chooseDirectory();
				}
			}
		});

		/**
		 * 输出监听器
		 */
		view.outputButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == view.outputButton) {
					outputDirectory();
				}
			}
		});
		
		/**
		 * 确认键监听器
		 */

		view.confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == view.confirmButton) {
					dosome();
				}
			}
		});
		
		/**
		 * 主表键监听器
		 */
		view.mainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == view.mainButton) {
					chooseMutilFiles("main");
				}
			}
		});
		
		/**
		 * 子表键监听器
		 */
		view.subButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == view.subButton) {
					chooseMutilFiles("sub");
				}
			}
		});
		
		/**
		 * 确认键监听器
		 */
		view.confirm2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == view.confirm2) {
					doMainOrSub();
				}
			}
		});
		
	}

	
	
	
	
	protected void doMainOrSub() {
		String []mainStr ={ view.mainTextField.getText()};
		String []subStr = view.subTextField.getText().split(" ");
		
		System.out.println(subStr.length);
		test.writeMainOrSub(mainStr,subStr);
		
		
		
	}





	protected void chooseMutilFiles(String mainOrSub) {
		if (flag == 1) {
			FileSystemView fsv = FileSystemView.getFileSystemView();
			System.out.println(fsv.getHomeDirectory()); // 得到桌面路径
			chooser.setCurrentDirectory(fsv.getHomeDirectory());
			flag = 2;
		} else {
			chooser.setCurrentDirectory(chooser.getSelectedFile());
		}
		chooser.setMultiSelectionEnabled(true);
		chooser.setApproveButtonText("确定");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// 只能选择文件
		chooser.setDialogTitle("选择要处理的***.java文件");

		int result = chooser.showOpenDialog(chooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File []fileArray = chooser.getSelectedFiles();
			// 获取选择目录的绝对路径
			System.out.println(fileArray.length);
			int fileLength = fileArray.length;
			String classNameStr = "";
			for(int i = 0; i < fileLength; i++){
				File file = fileArray[i];
				String filepath = file.getPath();
				classNameStr+=file.getName()+" ";
				System.out.println("文件路径： "+filepath);
				test.readFileJava(file,mainOrSub);
			}
			
			if(mainOrSub.equals("main")){
				view.mainTextField.setText(classNameStr);
			}else{
				view.subTextField.setText(classNameStr);
				
			}
		}
	}



	protected void fixTable() {
		int i = 0;
		for (TClass t : test.listTable) {
			t.toString();
		}
		view.tNamelable.setText(test.getTableName(test.listTable, i));
		String str = view.tNameTextField.getText();
		if(str.equals("")){
			test.fixTableName(test.listTable, i, str);
		}
		i++;
	}

	public void outputDirectory() {
		// 文件选择对象
		//JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(chooser.getSelectedFile());
		chooser.setApproveButtonText("确定");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只能选择文件
		chooser.setDialogTitle("选择输出的目录");
		int result = chooser.showOpenDialog(chooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			// 获取选择目录的绝对路径
			String filepath = file.getAbsolutePath();
			view.outputTextFiled.setText(filepath);
		}
	}

	// 选择文件目录
	public void chooseDirectory() {
		if (flag == 1) {
			FileSystemView fsv = FileSystemView.getFileSystemView();
			System.out.println(fsv.getHomeDirectory()); // 得到桌面路径
			chooser.setCurrentDirectory(fsv.getHomeDirectory());
			flag = 2;
		} else {
			chooser.setCurrentDirectory(chooser.getSelectedFile());
		}
		chooser.setMultiSelectionEnabled(true);
		chooser.setApproveButtonText("确定");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// 只能选择文件
		chooser.setDialogTitle("选择要处理的**.sql文件");

		int result = chooser.showOpenDialog(chooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file[] = chooser.getSelectedFiles();
			// 获取选择目录的绝对路径
			for(int i = 0;i < file.length;i++){
				String filepath = file[i].getAbsolutePath();
				if (!filepath.endsWith(".sql")) {
					showMessage("请选择sql文件");
					chooseDirectory();
				}
				if (filepath != null) {
					test.readFile(filepath);
				}
				
			}
			
			view.inputTextFiled.setText(file[0].getPath());// 将文件路径设到JTextField
			
			
			
			view.tNameTextField.setText(test.listTable.get(0).getTableName());
			
		}
	}

	
	public void nextTable(){
		if(test.listTable.size()==0){
			view.cNametextField.setText("没有表了");
			view.tNameTextField.setText("没有表了");
		}else{
			view.tNameTextField.setText(test.listTable.get(0).getTableName());
			view.cNametextField.setText("");
		}
		
	}
	
	public void dosome() {
		
		System.out.println("size+" + test.listTable.size());
		if (test.listTable.size() != 0) {
			String parentClass = view.comboBox.getSelectedItem().toString();
			// System.out.println("parentClass-------------------" +
			// parentClass);
			String className = view.cNametextField.getText();
			if (className.equals("")) {
				showMessage("请输入类名！");
			}

			// String file = view.inputTextFiled.getText();

			/*
			 * if (file != null) { test.readFile(file); }
			 */
			String outfile = view.outputTextFiled.getText();
			if (!outfile.equals("")) {
				outfile = outfile + "\\";
				System.out.println("outfile-----------" + outfile);
				test.writeFile(outfile, parentClass, className);
				test.listTable.remove(0);
			} else {
				outfile = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
				outfile = outfile + "\\";
				System.out.println(outfile);
				test.writeFile(outfile, parentClass, className);
				test.listTable.remove(0);
			}
		}
		System.out.println(test.listTable.size());
		nextTable();

	}

	/*
	 * 显示信息
	 */

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

}
