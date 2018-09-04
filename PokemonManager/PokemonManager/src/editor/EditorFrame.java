package editor;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.JobAttributes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.xml.sax.helpers.XMLReaderFactory;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import net.simondaniel.pokes.PokemonType;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class EditorFrame extends JFrame {
	
	List<PokemonData> pokes = new ArrayList<PokemonData>();

	private JPanel contentPane;
	private final JScrollPane scrollPane = new JScrollPane();
	
	DefaultListModel<String> model;
	JList<String> list;
	JLabel lblName;
	JLabel typ1;
	JLabel pokedex;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditorFrame frame = new EditorFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EditorFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		scrollPane.setBounds(12, 46, 150, 166);
		contentPane.add(scrollPane);
		
		pokedex = new JLabel("");
		pokedex.setBounds(272, 75, 118, 15);
		contentPane.add(pokedex);
		
		JLabel lblTyp = new JLabel("typ1:");
		lblTyp.setBounds(180, 102, 70, 15);
		contentPane.add(lblTyp);
		
		JLabel typ1 = new JLabel("");
		typ1.setBounds(272, 102, 118, 15);
		contentPane.add(typ1);
		
		JLabel lblTyp_1 = new JLabel("typ2:");
		lblTyp_1.setBounds(180, 129, 70, 15);
		contentPane.add(lblTyp_1);
		
		JLabel typ2 = new JLabel("");
		typ2.setBounds(272, 129, 118, 15);
		contentPane.add(typ2);
		
		JButton btnChooseXmlFile = new JButton("choose xml file");
		btnChooseXmlFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("Pokemon file","xml"));
				chooser.setCurrentDirectory(new File("/home/simon/git/pokemonGDX/PokemonGDX/core/assets"));
				chooser.showOpenDialog(null);
				
				File selected = chooser.getSelectedFile();
				
				if(selected != null)
					load(chooser.getSelectedFile());
			}
		});
		btnChooseXmlFile.setBounds(12, 9, 150, 25);
		contentPane.add(btnChooseXmlFile);
		

		String[] sa = new String[2];
		sa[0] = "bulb";
		sa[1] = "pika";
		model = new DefaultListModel<>();
		list = new JList<String>(model);
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println(list.getSelectedValue());
				for(PokemonData d : pokes) {
					if(d.nameField.getValue().equals(list.getSelectedValue())) {
						lblName.setText(d.nameField.toString());
						pokedex.setText(d.pokedexField.toString());
						typ1.setText(d.typ1Field.toString());
						if(d.typ2Field.getValue() == null) {
							typ2.setText("none");
						}
						else {
							typ2.setText(d.typ2Field.toString());
						}
					}
				}
				
			}
		});
		scrollPane.setViewportView(list);
		
		JLabel lbl0 = new JLabel("name:");
		lbl0.setBounds(180, 48, 70, 15);
		contentPane.add(lbl0);
		
		lblName = new JLabel("");
		lblName.setBounds(272, 48, 118, 15);
		contentPane.add(lblName);
		
		JLabel lblPokedex = new JLabel("pokedex:");
		lblPokedex.setBounds(180, 75, 70, 15);
		contentPane.add(lblPokedex);
		
		JButton btnAdd = new JButton("add");
		final JFrame ref = this;
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String n = JOptionPane.showInputDialog(ref, "enter Name");
				if(n == null) return;
				if(n.equals("")) {
					JOptionPane.showMessageDialog(ref, "you have to enter a name",
							"add error", JOptionPane.ERROR_MESSAGE);
					return;
				}
					
				PokemonData data = new PokemonData();
				data.nameField.set(n);
				pokes.add(data);
				model.addElement(n);
			}
		});
		btnAdd.setBounds(12, 224, 150, 25);
		contentPane.add(btnAdd);
		
		JButton btnSave = new JButton("save");
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(321, 224, 117, 25);
		contentPane.add(btnSave);
		
		JButton button_0 = new JButton("");
		button_0.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(list.isSelectionEmpty()) return;
				String n = JOptionPane.showInputDialog(ref, "enter new name");
				if(n == null) return;
				if(n.equals("")) {
					JOptionPane.showMessageDialog(ref, "you have to enter a name",
							"add error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				getFromName(list.getSelectedValue()).nameField.set(n);
				model.set(list.getSelectedIndex(), n);
				lblName.setText(n);
			}
		});
		button_0.setBounds(408, 46, 30, 17);
		contentPane.add(button_0);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(list.isSelectionEmpty()) return;
				String n = JOptionPane.showInputDialog(ref, "enter new pokedex");
				if(n == null) return;
				if(n.equals("")) {
					JOptionPane.showMessageDialog(ref, "you have to enter a number",
							"add error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				getFromName(list.getSelectedValue()).pokedexField.set(Integer.valueOf(n));
				pokedex.setText(n);
			}
		});
		button.setBounds(408, 75, 30, 17);
		contentPane.add(button);
		
		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(list.isSelectionEmpty()) return;
				PokemonType t = TypeChooseDialog.chooseType();
				if(t == null) return;
				getFromName(list.getSelectedValue()).typ1Field.set(t);
				typ1.setText(t.name);
			}
		});
		button_1.setBounds(408, 100, 30, 17);
		contentPane.add(button_1);
		
		JButton button_2 = new JButton("");
		button_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(list.isSelectionEmpty()) return;
				PokemonType t = TypeChooseDialog.chooseType();
				
				getFromName(list.getSelectedValue()).typ2Field.set(t);
				if(t != null) {
					typ2.setText(t.name);
				}
				else {
					typ2.setText("none");
				}
			}
		});
		button_2.setBounds(408, 127, 30, 17);
		contentPane.add(button_2);
		
		
		load(new File("/home/simon/git/pokemonGDX/PokemonGDX/core/assets/Pokemons.xml"));
	}
	
	private void save() {
		for(PokemonData d : pokes) {
			if(!d.isValid()) {
				JOptionPane.showMessageDialog(this, d.nameField.getValue() +" is not valid",
						"save error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		if(JOptionPane.showConfirmDialog(this, "are you sure?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
			return;
		}
		Element root = new Element("Pokemon", null);
		for(PokemonData d : pokes) {
			Element p = new Element(d.nameField.getValue(), root);
			Element name = new Element("name", p);
			name.setText(d.nameField.getValue());
			p.addChild(name);
			Element id = new Element("id", p);
			id.setText(d.pokedexField.getValue() + "");
			p.addChild(id);
			Element typ1 = new Element("firstType", p);
			typ1.setText(d.typ1Field.getValue().name);
			p.addChild(typ1);
			if(!(d.typ2Field.getValue() == null)) {
				Element typ2 = new Element("secondType", p);
				typ2.setText(d.typ2Field.getValue() + "");
				p.addChild(typ2);
			}
			
			root.addChild(p);
		}
		String s = root.toString();
		s = makePretty(s);
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String xml = header + "\n" + s;
		FileHandle fh = new FileHandle(new File("/home/simon/git/pokemonGDX/PokemonGDX/core/assets/Pokemons.xml"));
		fh.writeString(xml, false);
		System.out.println(s);
		
	}
	
	private String makePretty(String xml) {
		xml = makeShort(xml, "name");
		xml = makeShort(xml, "id");
		xml = makeShort(xml, "firstType");
		xml = makeShort(xml, "secondType");
		return xml;
	}
	
	private String makeShort(String s, String child) {
		s = s.replaceAll("<" + child + ">\n\t\t\t", "<" + child + ">");
		s = s.replaceAll("\n\t\t</" + child + ">", "</" + child + ">");
		return s;
	}
	
	private PokemonData getFromName(String name) {
		for(PokemonData d : pokes) {
			if(d.nameField.getValue().equals(name))
				return d;
		}
				
		return null;
	}
	
	private void load(File f) {
		XmlReader reader = new XmlReader();
		Element root = reader.parse(new FileHandle(f));
		String[] sa = new String[root.getChildCount()];
		for(int i = 0; i < sa.length; i++) {
			Element e = root.getChild(i);
			PokemonData data = new PokemonData();
			data.nameField.set(e.get("name"));
			data.pokedexField.set(e.getInt("id"));
			data.typ1Field.set(PokemonType.fromString(e.get("firstType")));
			if(e.hasChild("secondType"))
				data.typ2Field.set(PokemonType.fromString(e.get("secondType")));
			pokes.add(data);
			sa[i] = e.get("name");
			model.addElement(e.get("name"));
		}
		
	}
}
