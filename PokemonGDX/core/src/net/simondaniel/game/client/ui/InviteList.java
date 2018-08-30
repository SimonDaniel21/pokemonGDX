package net.simondaniel.game.client.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.simondaniel.MyColor;
import net.simondaniel.game.client.ui.Entry.EntryType;

public class InviteList extends List<Entry>{

	public InviteList(Skin skin) {
		super(skin);
	}

	public String getSelectedName() {
		if(getSelected() == null) return null;
		return getSelected().getName();
	}

	public void set(String ... s) {
		clearItems();
		for(String ss : s) addName(ss);
	}
	
	public void addName(String name) {
		getItems().add(new Entry(name));
	}

	public void setPending(String name) {
		for(Entry e : getItems()) {
			if(e.getName().equals(name)) {
				e.setState(EntryType.PENDING);
			}	
		}
	}
	
}

class Entry{
	private String name, coloredString;
	private EntryType state;
	
	public Entry(String name) {
		this(name, EntryType.NORMAL);
	}
	
	public Entry(String name, EntryType type) {
		this.name = name;
		setState(type);
	}
	
	public void setState(EntryType type) {
		state = type;
		switch (state) {
		case PENDING:
			coloredString = MyColor.dye(Color.ORANGE, name);
			break;

		case ACCEPTED:
			coloredString = MyColor.dye(Color.GREEN, name);
			break;
			
		case DECLINED:
			coloredString = MyColor.dye(Color.RED, name);
			break;
		default:
			coloredString = name;
			break;
		}
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return coloredString;
	}
	
	public static enum EntryType{
		NORMAL, PENDING, ACCEPTED, DECLINED;
	}
}