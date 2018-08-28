package net.simondaniel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

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
	
	public static enum Atlas{
		PIKACHU("gfx/atlases/pika/pika_atlas2.atlas"),
		SQUIRTLE("gfx/atlases/squirtle/squirtle.atlas");
		
		public final String path;
		public final AssetDescriptor<TextureAtlas> assetDescriptor;
		
		private Atlas(String path) {
			this.path = path;
			assetDescriptor = new AssetDescriptor<TextureAtlas>(Gdx.files.internal(path), TextureAtlas.class);
		}
	}
}
