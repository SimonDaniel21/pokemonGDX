package editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.DefaultSingleSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.simondaniel.pokes.PokemonType;

import javax.swing.JList;
import javax.swing.SwingConstants;

public class TypeChooseDialog extends JDialog {

	private static TypeChooseDialog d = new TypeChooseDialog();

	private final JPanel contentPanel = new JPanel();

	DefaultListModel<String> model;
	JList<String> list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TypeChooseDialog dialog = new TypeChooseDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TypeChooseDialog() {
		setModal(true);
		setBounds(100, 100, 131, 309);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		model = new DefaultListModel<String>();

		for (PokemonType t : PokemonType.values()) {
			model.addElement(t.name);
		}
		model.addElement("none");
		contentPanel.setLayout(null);

		list = new JList<String>(model);
		list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		list.setBounds(12, 12, 100, 211);
		contentPanel.add(list);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			JDialog ref = this;
			{
				JButton okButton = new JButton("OK");
				okButton.setHorizontalAlignment(SwingConstants.LEFT);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						ref.setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	public static PokemonType chooseType() {
		d.list.setSelectedIndex(d.model.getSize()-1);
		d.setVisible(true);
		
		return PokemonType.fromString(d.list.getSelectedValue());
	}
}
