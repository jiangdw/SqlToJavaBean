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
	// �ļ�ѡ�����
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
		 * ���������
		 */
		view.outputButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == view.outputButton) {
					outputDirectory();
				}
			}
		});
		
		/**
		 * ȷ�ϼ�������
		 */

		view.confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == view.confirmButton) {
					dosome();
				}
			}
		});
		
		/**
		 * �����������
		 */
		view.mainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == view.mainButton) {
					chooseMutilFiles("main");
				}
			}
		});
		
		/**
		 * �ӱ��������
		 */
		view.subButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == view.subButton) {
					chooseMutilFiles("sub");
				}
			}
		});
		
		/**
		 * ȷ�ϼ�������
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
			System.out.println(fsv.getHomeDirectory()); // �õ�����·��
			chooser.setCurrentDirectory(fsv.getHomeDirectory());
			flag = 2;
		} else {
			chooser.setCurrentDirectory(chooser.getSelectedFile());
		}
		chooser.setMultiSelectionEnabled(true);
		chooser.setApproveButtonText("ȷ��");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// ֻ��ѡ���ļ�
		chooser.setDialogTitle("ѡ��Ҫ�����***.java�ļ�");

		int result = chooser.showOpenDialog(chooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File []fileArray = chooser.getSelectedFiles();
			// ��ȡѡ��Ŀ¼�ľ���·��
			System.out.println(fileArray.length);
			int fileLength = fileArray.length;
			String classNameStr = "";
			for(int i = 0; i < fileLength; i++){
				File file = fileArray[i];
				String filepath = file.getPath();
				classNameStr+=file.getName()+" ";
				System.out.println("�ļ�·���� "+filepath);
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
		// �ļ�ѡ�����
		//JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(chooser.getSelectedFile());
		chooser.setApproveButtonText("ȷ��");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// ֻ��ѡ���ļ�
		chooser.setDialogTitle("ѡ�������Ŀ¼");
		int result = chooser.showOpenDialog(chooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			// ��ȡѡ��Ŀ¼�ľ���·��
			String filepath = file.getAbsolutePath();
			view.outputTextFiled.setText(filepath);
		}
	}

	// ѡ���ļ�Ŀ¼
	public void chooseDirectory() {
		if (flag == 1) {
			FileSystemView fsv = FileSystemView.getFileSystemView();
			System.out.println(fsv.getHomeDirectory()); // �õ�����·��
			chooser.setCurrentDirectory(fsv.getHomeDirectory());
			flag = 2;
		} else {
			chooser.setCurrentDirectory(chooser.getSelectedFile());
		}
		chooser.setMultiSelectionEnabled(true);
		chooser.setApproveButtonText("ȷ��");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// ֻ��ѡ���ļ�
		chooser.setDialogTitle("ѡ��Ҫ�����**.sql�ļ�");

		int result = chooser.showOpenDialog(chooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file[] = chooser.getSelectedFiles();
			// ��ȡѡ��Ŀ¼�ľ���·��
			for(int i = 0;i < file.length;i++){
				String filepath = file[i].getAbsolutePath();
				if (!filepath.endsWith(".sql")) {
					showMessage("��ѡ��sql�ļ�");
					chooseDirectory();
				}
				if (filepath != null) {
					test.readFile(filepath);
				}
				
			}
			
			view.inputTextFiled.setText(file[0].getPath());// ���ļ�·���赽JTextField
			
			
			
			view.tNameTextField.setText(test.listTable.get(0).getTableName());
			
		}
	}

	
	public void nextTable(){
		if(test.listTable.size()==0){
			view.cNametextField.setText("û�б���");
			view.tNameTextField.setText("û�б���");
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
				showMessage("������������");
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
	 * ��ʾ��Ϣ
	 */

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

}
