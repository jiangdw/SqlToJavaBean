package com.yonyou;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileSystemView;

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
		 * ��һ���ļ�����
		 */
		
		view.NextButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == view.NextButton) {
					fixTable();
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
		
		view.multiSelectMainComboBox.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				System.out.println("���ѡ���ֵ��" + view.multiSelectMainComboBox.getSelectedItemsString());
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
		
		view.multiSelectSubComboBox.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				System.out.println("�ӱ�ѡ���ֵ��" + view.multiSelectSubComboBox.getSelectedItemsString());
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
		
		
		
		
		
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
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// ֻ��ѡ��Ŀ¼
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
		view.multiSelectSubComboBox.addItem("1");
		view.multiSelectSubComboBox.addItem("2");
		view.multiSelectSubComboBox.addItem("3");
		view.multiSelectSubComboBox.addItem("4");
		
		
		if (flag == 1) {
			FileSystemView fsv = FileSystemView.getFileSystemView();
			System.out.println(fsv.getHomeDirectory()); // �õ�����·��
			chooser.setCurrentDirectory(fsv.getHomeDirectory());
			flag = 2;
		} else {
			chooser.setCurrentDirectory(chooser.getSelectedFile());
		}
		chooser.setMultiSelectionEnabled(false);
		chooser.setApproveButtonText("ȷ��");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// ֻ��ѡ��Ŀ¼
		chooser.setDialogTitle("ѡ��Ҫ�����**.sql�ļ�");

		int result = chooser.showOpenDialog(chooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			// ��ȡѡ��Ŀ¼�ľ���·��
			String filepath = file.getAbsolutePath();
			if (!filepath.endsWith(".sql")) {
				showMessage("��ѡ��sql�ļ�");
				chooseDirectory();
			}
			view.inputTextFiled.setText(filepath);// ���ļ�·���赽JTextField
		}
	}

	public void dosome() {
		String parentClass = view.comboBox.getSelectedItem().toString();
		System.out.println("parentClass-------------------" + parentClass);
		String className = view.cNametextField.getText();
		if (className.equals("")) {
			showMessage("������������");
		}

		String file = view.inputTextFiled.getText();
		if (file != null) {
			test.readFile(file);
			test.selectDBFiled();
		}
		String outfile = view.outputTextFiled.getText();
		if (!outfile.equals("")) {
			outfile = outfile + "\\";
			System.out.println("outfile-----------" + outfile);
			test.writeFile(outfile, parentClass, className);
		} else {
			outfile = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
			outfile = outfile + "\\";
			System.out.println(outfile);
			test.writeFile(outfile, parentClass, className);
		}
	}

	/*
	 * ��ʾ��Ϣ
	 */

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

}
