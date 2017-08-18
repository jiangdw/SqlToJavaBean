package com.yonyou;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

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
	}

	public void outputDirectory() {
		// 文件选择对象
		//JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(chooser.getSelectedFile());
		chooser.setApproveButtonText("确定");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只能选择目录
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
		chooser.setMultiSelectionEnabled(false);
		chooser.setApproveButtonText("确定");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// 只能选择目录
		chooser.setDialogTitle("选择要处理的**.sql文件");

		int result = chooser.showOpenDialog(chooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			// 获取选择目录的绝对路径
			String filepath = file.getAbsolutePath();
			if (!filepath.endsWith(".sql")) {
				showMessage("请选择sql文件");
				chooseDirectory();
			}
			view.inputTextFiled.setText(filepath);// 将文件路径设到JTextField
		}
	}

	public void dosome() {
		String parentClass = view.comboBox.getSelectedItem().toString();
		System.out.println("parentClass-------------------" + parentClass);
		String className = view.cNametextField.getText();
		if (className.equals("")) {
			showMessage("请输入类名！");
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
	 * 显示信息
	 */

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

}
