package com.jds.jn.gui.dialogs;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jds.jn.Jn;
import com.jds.jn.rconfig.RValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class ProgramSettingsDialog extends JDialog
{
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTabbedPane _tabbedPane1;
	private JCheckBox _useTrayCheckBox;
	private JCheckBox _savePacketsInDecodeCheckBox;
	private JSlider _mainVisible;

	public ProgramSettingsDialog()
	{
		super(Jn.getInstance());
		setLocationRelativeTo(null);
		setLocationByPlatform(true);
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onCancel();
			}
		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				onCancel();
			}
		});

		contentPane.registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		load();

		setSize(600, 400);
	}

	public void load()
	{
		_useTrayCheckBox.setSelected(RValues.USE_TRAY.asBoolean());
		_savePacketsInDecodeCheckBox.setSelected(RValues.SAVE_AS_DECODE.asBoolean());
		_mainVisible.setValue((int) (RValues.MAIN_VISIBLE.asFloat() * 100F));
	}

	private void onOK()
	{
		RValues.USE_TRAY.setVal(_useTrayCheckBox.isSelected());
		RValues.SAVE_AS_DECODE.setVal(_savePacketsInDecodeCheckBox.isSelected());
		RValues.MAIN_VISIBLE.setVal(_mainVisible.getValue() / 100F);
		Jn.getInstance().updateVisible();
		dispose();
	}

	private void onCancel()
	{
		dispose();
	}

	{
		// GUI initializer generated by IntelliJ IDEA GUI Designer
		// >>> IMPORTANT!! <<<
		// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$()
	{
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
		panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		buttonOK = new JButton();
		buttonOK.setText("OK");
		panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		buttonCancel = new JButton();
		this.$$$loadButtonText$$$(buttonCancel, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Cancel"));
		panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		_tabbedPane1 = new JTabbedPane();
		_tabbedPane1.setTabPlacement(1);
		panel3.add(_tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		_tabbedPane1.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("General"), panel4);
		final JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel6 = new JPanel();
		panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel7 = new JPanel();
		panel7.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel6.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		_useTrayCheckBox = new JCheckBox();
		this.$$$loadButtonText$$$(_useTrayCheckBox, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("UseTray"));
		panel7.add(_useTrayCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer2 = new Spacer();
		panel7.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		_savePacketsInDecodeCheckBox = new JCheckBox();
		this.$$$loadButtonText$$$(_savePacketsInDecodeCheckBox, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("SavePacketsInDecode"));
		panel7.add(_savePacketsInDecodeCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer3 = new Spacer();
		panel5.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final JPanel panel8 = new JPanel();
		panel8.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		_tabbedPane1.addTab(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("GUISettings"), panel8);
		final JPanel panel9 = new JPanel();
		panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel8.add(panel9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panel9.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Visible")));
		_mainVisible = new JSlider();
		_mainVisible.setPaintLabels(false);
		_mainVisible.setPaintTicks(true);
		_mainVisible.setSnapToTicks(false);
		_mainVisible.setValueIsAdjusting(false);
		panel9.add(_mainVisible, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer4 = new Spacer();
		panel8.add(spacer4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	private void $$$loadButtonText$$$(AbstractButton component, String text)
	{
		StringBuffer result = new StringBuffer();
		boolean haveMnemonic = false;
		char mnemonic = '\0';
		int mnemonicIndex = -1;
		for (int i = 0; i < text.length(); i++)
		{
			if (text.charAt(i) == '&')
			{
				i++;
				if (i == text.length())
				{
					break;
				}
				if (!haveMnemonic && text.charAt(i) != '&')
				{
					haveMnemonic = true;
					mnemonic = text.charAt(i);
					mnemonicIndex = result.length();
				}
			}
			result.append(text.charAt(i));
		}
		component.setText(result.toString());
		if (haveMnemonic)
		{
			component.setMnemonic(mnemonic);
			component.setDisplayedMnemonicIndex(mnemonicIndex);
		}
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return contentPane;
	}
}
