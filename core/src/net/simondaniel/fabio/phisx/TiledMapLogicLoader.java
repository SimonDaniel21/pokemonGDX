package net.simondaniel.fabio.phisx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class TiledMapLogicLoader {
	
	private static final String COLLISION_TILESET = "CollisionTileset.tsx";

	/**
	 * liest eine TMX Datei und gibt ein Array mit den Collision Daten aus
	 * hierbei werden auch die TileIDs �bersetzt, oder null wenn der Layer 
	 * nicht mit diesem Namen existiert
	 * @param path
	 * @return
	 */
	public static LogicMap loadCollisionDataFromXML(String path) {
		XmlReader reader = new XmlReader();
		Element root = reader.parse(Gdx.files.internal(path));
		
		Element e = null;	// das Collision Layer Element der XML File
		Array<Element> ea = root.getChildrenByName("layer");
		for(Element ee : ea) {
			if(ee.getAttribute("name").equals("collision"))
				e = ee;
		}
		if(e == null)
			return null;
		
		Element e2 = null;	// Das Collisions Tileset
		Array<Element> ea2 = root.getChildrenByName("tileset");
		for(Element ee : ea2) {
			if(ee.getAttribute("source").equals(COLLISION_TILESET))
				e2 = ee;
		}
		if(e2 == null)
			return null;
		int firstGID = e2.getIntAttribute("firstgid");
		
		int w= e.getIntAttribute("width");
		int h = e.getIntAttribute("height");
		LogicMap lm = new LogicMap(w, h);
		
		String data = e.get("data").replaceAll(System.lineSeparator(), "").replaceAll("\r", "");
		String[] sa = data.split(",");
		
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int index1 = x +  y 	 *w;
				int index2 = x + (h -y-1)*w;
				int id = Integer.valueOf(sa[index2]) - firstGID;
				int v = 0;
				if(id == 0)
					v = 1;
				lm.getData()[index1] = v;
			}
		}
	
		return lm;
	}
}
