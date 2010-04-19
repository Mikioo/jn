package com.jds.jn.gui.dialogs;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jds.jn.Jn;
import com.jds.swing.JPicturePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 20.09.2009
 * Time: 13:36:10
 */
public class AboutDialog extends JWindow
{
	private JPanel contentPane;
	private JPicturePanel JPicturePanel;

	public AboutDialog()
	{
		super(Jn.getInstance());
		$$$setupUI$$$();
		add(contentPane);
		try
		{
			JPicturePanel.setImage(ImageIO.read(getClass().getResource("/jds/jn/resources/images/logo.png")));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				dispose();
			}

			@Override
			public void mousePressed(MouseEvent e)
			{

			}

			@Override
			public void mouseReleased(MouseEvent e)
			{

			}

			@Override
			public void mouseEntered(MouseEvent e)
			{

			}

			@Override
			public void mouseExited(MouseEvent e)
			{

			}
		});

		JPicturePanel.setPreferredSize(JPicturePanel.getImageSize());
		setSize(JPicturePanel.getImageSize().width, JPicturePanel.getImageSize().height + 30);
		setLocationRelativeTo(null);
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
		contentPane.setLayout(new BorderLayout(0, 0));
		JPicturePanel = new JPicturePanel();
		contentPane.add(JPicturePanel, BorderLayout.NORTH);
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel1, BorderLayout.SOUTH);
		final JLabel label1 = new JLabel();
		label1.setText("Author: VISTALL");
		panel1.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("Company: J Develop Station © 2009");
		panel1.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final Spacer spacer2 = new Spacer();
		panel1.add(spacer2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return contentPane;
	}
}
