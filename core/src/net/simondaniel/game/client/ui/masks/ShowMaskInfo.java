package net.simondaniel.game.client.ui.masks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class ShowMaskInfo {
	
	public List<String> getMissingFields() {
		List<String> result = new ArrayList<String>();

		try {
			for(Field f : this.getClass().getDeclaredFields()) {
				if(f.get(this) == null) {
					result.add(f.getName());
				}
			}
			
				
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void clear() {
		
	}
}
