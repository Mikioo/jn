package com.jds.jn.gui.panels.viewpane.packetlist.sub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jds.jn.config.RValues;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.profiles.NetworkProfile;
import com.jds.jn.network.profiles.NetworkProfiles;
import com.jds.jn.parser.datatree.DataTreeNode;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.formattree.ForPart;
import com.jds.jn.parser.formattree.Part;
import com.jds.jn.parser.parttypes.PartType;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.protocoltree.PacketFamilly;
import com.jds.jn.protocol.protocoltree.PacketInfo;
import com.jds.jn.session.Session;
import com.jds.jn.util.Bundle;
import com.jds.swing.XCheckedButton;
import com.jds.swing.XJPopupMenu;

/**
 * @author VISTALL
 * @date 21.09.2009
 * Time: 14:00:20
 * Thanks: Ulysses R. Ribeiro
 */
public class FormForSearch extends JPanel
{
	public static final String NONE = "<none>";

	private JPanel main;
	private JTextField _findText;
	private JButton _searchBtn;

	private JComboBox _partSelect;
	private JComboBox _operatorSelect;
	private JTextField _operatorEqual;
	private JLabel _statusLabel;
	private ViewPane _pane;

	private static final String[] MATH_OPERATORS = {
			"==",
			"!=",
			">",
			">=",
			"<",
			"<="
	};
	private static final String[] STRING_OPERATORS = {
			"equals",
			"equalsIgnoreCase"
	};

	private Map<String, PacketInfo> _formats = new HashMap<String, PacketInfo>();

	private XJPopupMenu _menuFindSimple = new XJPopupMenu();

	public FormForSearch(ViewPane pane)
	{
		_pane = pane;
		$$$setupUI$$$();

		_findText.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				_searchBtn.setEnabled(!_findText.getText().trim().equals(""));
				_menuFindSimple.removeItems();

				Set<String> set = new TreeSet<String>();
				for(PacketInfo packetInfo : _formats.values())
				{
					if(packetInfo.getName().startsWith(_findText.getText()))
					{
						set.add(packetInfo.getName());
					}
				}

				if(!set.isEmpty())
				{
					_menuFindSimple.setPopupSize(_findText.getWidth(), set.size() > 10 ? 200 : 20 * set.size());
					_menuFindSimple.setFocusable(false);

					int i = 0;
					for(final String str : set)
					{
						XCheckedButton item = new XCheckedButton(str);
						item.setSize(_findText.getWidth(), 20);
						item.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								_findText.setText(str);

								checkPartFields();
							}
						});

						_menuFindSimple.add(item);

						if(++i >= 10)
						{
							break;
						}
					}

					_menuFindSimple.show(_findText, 0, _findText.getY() + _findText.getHeight());
				}
				else
				{
					_menuFindSimple.setVisible(false);
				}

				checkPartFields();
			}
		});

		_operatorEqual.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				checkOperatorEqualValue();
			}
		});

		_searchBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				RValues.LAST_SEARCH.setVal(_findText.getText());

				search();
			}
		});

		_partSelect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				checkOperators();
			}
		});

		buildPacketCache();

		checkPartFields();
		checkOperators();

		_searchBtn.setEnabled(!_findText.getText().trim().equals(""));
	}

	private void checkOperators()
	{
		_operatorSelect.removeAllItems();

		Object selected = _partSelect.getSelectedItem();
		if(selected != null && selected != NONE)
		{
			Part part = (Part) selected;

			for(String str : (part.getType().getValueType() == PartType.PartValueType.STRING ? STRING_OPERATORS : MATH_OPERATORS))
			{
				_operatorSelect.addItem(str);
			}

			_operatorSelect.setSelectedIndex(0);

			checkOperatorEqualValue();
		}

		_operatorSelect.setEnabled(selected != null && selected != NONE);
		_operatorEqual.setEnabled(selected != null && selected != NONE);

		checkOperatorEqualValue();
	}

	private void checkOperatorEqualValue()
	{
		boolean passed = true;
		checkLoop:
		{
			PacketInfo packetInfo = _formats.get(_findText.getText());
			if(packetInfo == null)
			{
				break checkLoop;
			}

			Object selectedPart = _partSelect.getSelectedItem();
			if(!(selectedPart instanceof Part))
			{
				break checkLoop;
			}

			if(((Part) selectedPart).getType().getValueType() == PartType.PartValueType.DIGITAL)
			{
				try
				{
					Long.decode(_operatorEqual.getText());
				}
				catch(NumberFormatException ex)
				{
					passed = false;
				}
			}
		}

		_searchBtn.setEnabled(passed);
		setStatusLabel(passed ? null : "IncorrectValue", Color.RED);
	}

	private void checkPartFields()
	{
		PacketInfo packetInfo = _formats.get(_findText.getText());
		_partSelect.removeAllItems();
		if(packetInfo != null)
		{
			_partSelect.addItem(NONE);

			for(Part part : packetInfo.getDataFormat().getMainBlock().getParts())
			{
				if(part instanceof ForPart)
				{
					continue;
				}
				_partSelect.addItem(part);
			}
		}

		_partSelect.setEnabled(packetInfo != null);
		_operatorSelect.setEnabled(packetInfo != null);
		_operatorEqual.setEnabled(packetInfo != null);
	}

	public void search()
	{
		Object selected = _partSelect.getSelectedItem();
		if(selected == null || selected == NONE)
		{
			if(_pane.getDecryptedPacketListPane().getModel().searchPacket(_pane, _findText.getText()))
			{
				setStatusLabel("Found", Color.GREEN);
			}
			else
			{
				setStatusLabel("NotFound", Color.RED);
			}
		}
		else
		{
			int index = search0(false);

			if(index >= 0)
			{
				JTable pt = _pane.getDecryptedPacketListPane().getPacketTable();
				pt.setAutoscrolls(true);
				pt.getSelectionModel().setSelectionInterval(index, index);
				pt.scrollRectToVisible(pt.getCellRect(index, 0, true));

				setStatusLabel("Found", Color.GREEN);
			}
			else
			{
				setStatusLabel("NotFound", Color.RED);
			}
		}
	}

	private void buildPacketCache()
	{
		_formats.clear();

		if(_pane.getSession() == null)
		{
			return;
		}

		Protocol currentProto = _pane.getSession().getProtocol();
		getAllFormatsName(currentProto);
	}

	private void getAllFormatsName(Protocol p)
	{
		for(PacketFamilly a : p.getFamilies())
		{
			if(a != null)
			{
				for(PacketInfo pi : a.getFormats().values())
				{
					_formats.put(pi.getName(), pi);
				}
			}
		}
	}

	private void createUIComponents()
	{
		main = this;
		_findText = new JTextField(RValues.LAST_SEARCH.asString());
	}

	public void setStatusLabel(String text, Color color)
	{
		if(text != null)
		{
			_statusLabel.setText(Bundle.getString(text));
			_statusLabel.setForeground(color);
		}
		else
		{
			_statusLabel.setText("");
		}
	}

	private int search0(boolean prev)
	{
		Session session = _pane.getSession();
		NetworkProfile profile = NetworkProfiles.getInstance().active();

		if(session == null || profile == null)
		{
			return -1;
		}

		List<DecryptedPacket> packets = session.getDecryptPackets();

		PacketInfo packetInfo = _formats.get(_findText.getText());
		Part selectedPart = (Part) _partSelect.getSelectedItem();

		int size = packets.size();

		Object value = null;
		if(selectedPart.getType().getValueType() == PartType.PartValueType.STRING)
		{
			value = _operatorEqual.getText();
		}
		else
		{
			try
			{
				value = Long.decode(_operatorEqual.getText());
			}
			catch(NumberFormatException e)
			{
				value = Long.MIN_VALUE;
			}
		}

		int prevResult = prev ? -1 : search0(true);
		int startIndex = _pane.getDecryptedPacketListPane().getPacketTable().getSelectedRow();
		if(startIndex == prevResult)
		{
			startIndex++;
		}

		for(int i = startIndex; i < size; i++)
		{
			DecryptedPacket gp = packets.get(i);
			PacketInfo format = gp.getPacketInfo();
			if(format != packetInfo || gp.hasError())
			{
				continue;
			}

			DataTreeNode p = gp.getRootNode().getPartByName(selectedPart.getName());
			if(!(p instanceof VisualValuePart))
			{
				continue;
			}

			Object partValue = ((VisualValuePart) p).getValue();

			switch(selectedPart.getType().getValueType())
			{
				case DIGITAL:
					Long longValue = ((Number) partValue).longValue();
					Long selectedLongValue = ((Number) value).longValue();
					switch(_operatorSelect.getSelectedIndex())
					{
						case 0: // ==
							if(selectedLongValue.equals(longValue))
							{
								return i;
							}
							break;
						case 1: // !=
							if(!selectedLongValue.equals(longValue))
							{
								return i;
							}
							break;
						case 2: // >
							if(selectedLongValue > longValue)
							{
								return i;
							}
							break;
						case 3: // >=
							if(selectedLongValue > longValue)
							{
								return i;
							}
							break;
						case 4: // <
							if(selectedLongValue < longValue)
							{
								return i;
							}
							break;
						case 5: // <=
							if(selectedLongValue <= longValue)
							{
								return i;
							}
							break;
					}
					break;
				case STRING:
					switch(_operatorSelect.getSelectedIndex())
					{
						case 0: // equals
							if(value.equals(partValue))
							{
								return i;
							}
							break;
						case 1: // equalsIgnoreCase
							if(((String) value).equalsIgnoreCase((String) partValue))
							{
								return i;
							}
							break;
					}
					break;
			}
		}

		return -1;
	}

	public JTextField getFindText()
	{
		return _findText;
	}

	public JTextField getOperatorEqual()
	{
		return _operatorEqual;
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
		createUIComponents();
		main.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
		main.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Status"));
		panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel2.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		_statusLabel = new JLabel();
		_statusLabel.setForeground(new Color(-52429));
		this.$$$loadLabelText$$$(_statusLabel, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("NotFound"));
		panel2.add(_statusLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_searchBtn = new JButton();
		_searchBtn.setBorderPainted(true);
		_searchBtn.setEnabled(false);
		_searchBtn.setFocusPainted(true);
		_searchBtn.setIcon(new ImageIcon(getClass().getResource("/com/jds/jn/resources/images/find.png")));
		_searchBtn.setText("");
		panel2.add(_searchBtn, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panel1.add(_findText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		_partSelect = new JComboBox();
		_partSelect.setEnabled(false);
		panel3.add(_partSelect, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
		_operatorSelect = new JComboBox();
		_operatorSelect.setEnabled(false);
		panel3.add(_operatorSelect, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
		_operatorEqual = new JTextField();
		_operatorEqual.setEnabled(false);
		panel3.add(_operatorEqual, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	private void $$$loadLabelText$$$(JLabel component, String text)
	{
		StringBuffer result = new StringBuffer();
		boolean haveMnemonic = false;
		char mnemonic = '\0';
		int mnemonicIndex = -1;
		for(int i = 0; i < text.length(); i++)
		{
			if(text.charAt(i) == '&')
			{
				i++;
				if(i == text.length())
				{
					break;
				}
				if(!haveMnemonic && text.charAt(i) != '&')
				{
					haveMnemonic = true;
					mnemonic = text.charAt(i);
					mnemonicIndex = result.length();
				}
			}
			result.append(text.charAt(i));
		}
		component.setText(result.toString());
		if(haveMnemonic)
		{
			component.setDisplayedMnemonic(mnemonic);
			component.setDisplayedMnemonicIndex(mnemonicIndex);
		}
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return main;
	}

}
