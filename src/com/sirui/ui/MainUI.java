package com.sirui.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainUI extends JFrame {
	private static final String rootPath = System.getProperty("user.home");

	private JTextField crtNameTxt, crtSunTxt;
	private JTextArea text3;
	private JLabel crtNameLab;
	private JButton crtBtn, crtSunBtn, delBtn, delSunBtn, tlabel;
	private int height = 25;

	private JComboBox change;
	private JList<String> leftList;
	private String boxData[] = {};
	private DefaultComboBoxModel<String> model;
	private static Properties prop;
	private String savePath;
	private String basePath;
	private static boolean flag = true;
	private JPanel mPanel;

	static {
		prop = new Properties();
		try {
			BufferedReader buf = new BufferedReader(new FileReader(new File(
					rootPath + File.separator + ".config.properties")));
			prop.load(buf);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MainUI() {
		loadBoxData();
		setSize(new Dimension(1000, 700));
		setTitle("工作笔记");
		// setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(getOwner());
		setLayout(new BorderLayout(5, 5));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		addTop();
		addLeft();
		addCenter();
		addOnClick();
		setVisible(true);
	}

	private void loadBoxData() {
		if (savePath == null || "".equals(savePath))
			savePath = prop.getProperty("savePath");
		File f = new File(savePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		boxData = f.list();
	}

	public void addTop() {

		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		JPanel mmPanel = new JPanel();

		mmPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 1, 1));
		tlabel = new JButton();
		tlabel.setText("o");
		tlabel.setContentAreaFilled(false);
		mmPanel.add(tlabel);
		main.add(mmPanel, BorderLayout.NORTH);

		mPanel = new JPanel();
		mPanel.setVisible(false);
		mPanel.setLayout(new FlowLayout(0, 5, 5));
		crtNameTxt = new JTextField();
		crtNameTxt.setPreferredSize(new Dimension(240, height));
		crtNameLab = new JLabel();
		crtNameLab.setText("项目名称");

		crtBtn = new JButton();
		crtBtn.setPreferredSize(new Dimension(80, height));
		crtBtn.setText("创建");

		delBtn = new JButton();
		delBtn.setPreferredSize(new Dimension(80, height));
		delBtn.setText(" 删除项目");

		mPanel.add(crtNameLab);
		mPanel.add(crtNameTxt);
		mPanel.add(crtBtn);
		mPanel.add(delBtn);

		main.add(mPanel, BorderLayout.CENTER);
		getContentPane().add(main, BorderLayout.NORTH);
	}

	public void addLeft() {
		JPanel mPanel = new JPanel();
		int width = 200;

		mPanel.setPreferredSize(new Dimension(width, 0));
		mPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		change = new JComboBox(boxData);
		change.setPreferredSize(new Dimension(width - 10, 25));

		leftList = new JList<String>();
		leftList.setPreferredSize(new Dimension(width - 10, 0));
		leftList.setBackground(new Color(212, 212, 212));
		JScrollPane scroll = new JScrollPane(leftList);
		scroll.setPreferredSize(new Dimension(width, 400));

		JLabel label = new JLabel("新增分类");
		crtSunTxt = new JTextField();
		crtSunTxt.setPreferredSize(new Dimension(width - 20, height));
		crtSunBtn = new JButton(" 创建分类 ");

		delSunBtn = new JButton();
		delSunBtn.setPreferredSize(new Dimension(100, height));
		delSunBtn.setText(" 删除选中分类 ");

		mPanel.add(change);
		mPanel.add(new JLabel("分类"));
		mPanel.add(scroll);
		mPanel.add(label);
		mPanel.add(crtSunTxt);
		mPanel.add(crtSunBtn);
		mPanel.add(delSunBtn);
		getContentPane().add(mPanel, BorderLayout.WEST);
		basePath = savePath + File.separator + change.getSelectedItem();
		readloadJList();
	}

	public void addCenter() {
		JPanel mPanel = new JPanel();
		mPanel.setLayout(new BorderLayout());
		text3 = new JTextArea();
		text3.setVisible(false);
		mPanel.add(new JScrollPane(text3), BorderLayout.CENTER);
		getContentPane().add(mPanel, BorderLayout.CENTER);
	}

	private void readLoadComBox(JComboBox comBox) {
		comBox.setModel(new DefaultComboBoxModel(boxData));
	}

	private void readloadJList() {
		File f = new File(basePath);
		if (f.isDirectory()) {
			model = new DefaultComboBoxModel<String>(f.list());
			leftList.setModel(model);
		}
	}

	private void addOnClick() {
		tlabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mPanel.setVisible(flag);
				if (flag) {
					flag = false;
					tlabel.setText("c");
				} else {
					flag = true;
					tlabel.setText("o");
				}
			}
		});
		change.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String path = (String) change.getSelectedItem();
					basePath = savePath + File.separator + path;
					readloadJList();
					text3.setVisible(false);
				}
			}

		});

		leftList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					File f = new File(basePath + File.separator
							+ leftList.getSelectedValue());
					if (!f.isFile())
						return;
					try {
						StringBuffer sb = new StringBuffer();
						byte[] b = new byte[10];
						BufferedReader buf = new BufferedReader(new FileReader(
								f));
						String tmp;
						while ((tmp = buf.readLine()) != null) {
							sb.append(tmp);
							sb.append("\n");
						}
						text3.setText(sb.toString());
						text3.setVisible(true);
						buf.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		crtBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = crtNameTxt.getText();
				if (name == null || "".equals(name)) {
					errorMsg("请输入项目名称。");
					return;
				}

				if (!alertDialog("确认创建项目")) {
					return;
				}

				File f = new File(savePath + File.separator + name);
				if (!f.exists()) {
					f.mkdir();
					loadBoxData();
					readLoadComBox(change);
					basePath = savePath + File.separator
							+ change.getSelectedItem();
					readloadJList();
					crtNameTxt.setText("");
					text3.setVisible(false);
					successMsg("创建项目成功。");
				} else {
					errorMsg("对不起，项目已经存在。");
				}
			}
		});

		delBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object name = change.getSelectedItem();
				if (name == null) {
					return;
				}
				if (!alertDialog("确认删除[" + name + "]项目吗？")) {
					return;
				}
				File f = new File(savePath + File.separator + name);
				if (f.isDirectory()) {
					File[] tmp = f.listFiles();
					for (File t : tmp) {
						t.delete();
					}
				}
				f.delete();
				loadBoxData();
				readLoadComBox(change);
				basePath = savePath + File.separator + change.getSelectedItem();
				readloadJList();
				text3.setVisible(false);
				successMsg("删除成功。");
			}
		});

		crtSunBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = crtSunTxt.getText();
				if (change.getSelectedItem() == null)
					return;

				if (name == null || "".equals(name)) {
					errorMsg("请输入新分类名称。");
					return;
				}
				if (!alertDialog("确认在项目[" + change.getSelectedItem()
						+ "]下创建新分类[" + name + "]吗？")) {
					return;
				}
				basePath = savePath + File.separator + change.getSelectedItem();
				File f = new File(basePath + File.separator + name);
				if (!f.isFile()) {
					try {
						f.createNewFile();
						readloadJList();
						crtSunTxt.setText("");
						successMsg("创建分类成功。");
					} catch (IOException e1) {
						errorMsg(e1.getMessage());
					}
				} else {
					errorMsg("对不起,分类已经存在。");
				}
			}
		});

		delSunBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object changeName = change.getSelectedItem();
				Object listselectName = leftList.getSelectedValue();
				if (listselectName == null)
					return;

				if (!alertDialog("确认要删除项目[" + changeName + "]下的分类["
						+ listselectName + "]吗？")) {
					return;
				}

				File f = new File(savePath + File.separator + changeName
						+ File.separator + listselectName);
				f.delete();
				basePath = savePath + File.separator + change.getSelectedItem();
				readloadJList();
				text3.setVisible(false);
				successMsg("删除成功。");
			}
		});

		text3.addKeyListener(new KeyListener() {

			private String upKey;

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				upKey = KeyEvent.getKeyText(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				if ("Ctrl".equals(keyText) && "S".equals(upKey)) {
					saveFile();
				}
			}
		});
	}

	private void saveFile() {
		if (!alertDialog("确认保存修改内容？"))
			return;

		String path = savePath + File.separator + change.getSelectedItem()
				+ File.separator + leftList.getSelectedValue();
		File f = new File(path);
		if (!f.isFile()) {
			errorMsg("对不起，文件丢失。");
			return;
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(f);
			out.write(text3.getText().getBytes());
			out.flush();
			out.close();
			successMsg("保存成功。");
		} catch (Exception e) {
			errorMsg(e.getMessage());
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
	}

	private void errorMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "错误",
				JOptionPane.ERROR_MESSAGE);
	}

	private void successMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "提示",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private boolean alertDialog(String msg) {
		int ret = JOptionPane.showConfirmDialog(null, msg, "提示",
				JOptionPane.YES_NO_OPTION);
		if (ret == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}
}
