package net.simondaniel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import net.simondaniel.pokes.Pokemon;

public class Assets {

	public static final AssetManager manager = new AssetManager();
	
	public static void load() {
		for(Atlas a : Atlas.values()) {
			AssetDescriptor<TextureAtlas> ad = new AssetDescriptor<TextureAtlas>(Gdx.files.internal(a.path), TextureAtlas.class);
			manager.load(ad);
		}
		for(Texture a : Texture.values()) {
			manager.load(a.path, com.badlogic.gdx.graphics.Texture.class);
		}
		loadPokemonAtlases();
		manager.finishLoading();
	}
	public static void dispose() {
		manager.dispose();
	}
	
	public static enum Texture{
		TEST("badlogic.jpg");
	
		public final String path;
		public final AssetDescriptor<Texture> assetDescriptor;
		
		private Texture(String path) {
			this.path = path;
			assetDescriptor = new AssetDescriptor<Texture>(Gdx.files.internal(path), Texture.class);
		}
	}
	
	private static void loadPokemonAtlases() {
		for(Pokemon p : Pokemon.pokemon) {
			FileHandle fh = Gdx.files.internal(p.getAtlasPath());
			if(!fh.exists()) continue;
			
			AssetDescriptor<TextureAtlas> ad = new AssetDescriptor<TextureAtlas>(fh, TextureAtlas.class);
			manager.load(ad);
		}
	}
		
	public static enum Atlas{
		PIKACHU("gfx/pokemon/pikachu/pika_atlas2.atlas"),
		SQUIRTLE("gfx/pokemon/squirtle/squirtle.atlas");
		
		public final String path;
		public final AssetDescriptor<TextureAtlas> assetDescriptor;
		
		private Atlas(String path) {
			this.path = path;
			assetDescriptor = new AssetDescriptor<TextureAtlas>(Gdx.files.internal(path), TextureAtlas.class);
		}
	}
}
