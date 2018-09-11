package net.simondaniel.game.client.ui.masks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import net.simondaniel.Config;
import net.simondaniel.GameConfig;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.MyRandom;
import net.simondaniel.fabio.GameMode;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.NamingDialog;
import net.simondaniel.game.client.ui.NamingDialog.ButtonOption;
import net.simondaniel.game.client.ui.NamingDialog.Entry;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.screens.MainMenuScreen;

public class ServerSelection extends UImask<ServerSelectionInfo>{
	
	Skin skin;
	GameClient gc;
	
	public LoginMask loginMask;
	
	public ServerSelection(Skin s) {
		super(new ServerSelectionInfo(), s);
		loginMask = new LoginMask(s);
		this.skin = getSkin();
		
		tryToConnectDialog = new Dialog("info", skin);
		connectingLabel = new Label("", skin);
		tryToConnectDialog.add(connectingLabel).center();
		
		add(new Label("recommended Servers", skin));
		row();
		
		for(String string : Config.gameConfig.SERVERS) {
			String[] sa = string.split(":");
			final String name = sa[0];
			final String ip = sa[1];
			final TextButton startServerbBttn = new TextButton(name, skin);
			startServerbBttn.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(isActive())
						tryToConnectTo(name, ip);
				}
				
			});
			add(startServerbBttn);
			Button b = new Button(skin);
			final NamingDialog nd = new NamingDialog("info", skin, ButtonOption.OK, 
					new Entry("name: ", name, false),
					new Entry("ip: ", ip, false)) {
				
				@Override
				public void updateValues(String[] values) {
				}
			};
			b.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					nd.show(getStage());
					System.out.println("pressed");
				}
				
			});
			add(b);
			row();
		}
		
		Label l = new Label("custom Servers", skin);
		l.setColor(Color.ORANGE);
		add(l);
		row();
		
		
		for(String string : Config.gameConfig.CUSTOM_SERVERS) {
			String[] sa = string.split(":");
			String name = sa[0];
			String ip = sa[1];
			final TextButton startServerbBttn = new TextButton(name, skin);
			add(startServerbBttn);
			Button b = new Button(skin);
			final NamingDialog nd = new NamingDialog("info", skin, 
					new Entry("name: ", name),
					new Entry("ip: ", ip)) {
				
				@Override
				public void updateValues(String[] values) {
					startServerbBttn.setText(values[0]);
				}
			};
			b.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					nd.show(getStage());
				}
				
			});
			add(b);
			row();
		}
		final Dialog d = new Dialog("", s);
		d.button("close").addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
			}
		
		});
		
		final NamingDialog nd = new NamingDialog("Add Server", skin, 
				new Entry("name: ", "enter name"),
				new Entry("ip: ", "enter ip")) {

					@Override
					public void updateValues(String[] values) {
						GameConfig.gameConfig.writeAppendToStringArray("custom_servers", values[0] + ":" + values[1], ",");
						GameConfig.gameConfig.save();
					}
			
		};
		TextButton addCustomServer = new TextButton("add", skin);
		addCustomServer.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				nd.show(getStage());
				//d.show(getStage());
			}
			
		});
		add(addCustomServer);
	}
	
	public void set(String lobbyName, String[][] others, GameMode mode, final GameClient gc, Skin skin) {
		
	}
	
	private Dialog tryToConnectDialog;
	private Label connectingLabel;
	
	/** sends a connectRequest and displays a waiting dialog while waiting for response
	 * 
	 * @param name
	 * @param ip
	 */
	private void tryToConnectTo(final String name, final String ip) {
		connectingLabel.setText("connecting to " + name + "...");
		tryToConnectDialog.show(getStage());
		tryToConnectDialog.setOrigin(Align.center);
		
		connectRequest(ip, name);
	}
	
	public void connectRequest(String ip, String name) {
		gc = new GameClient(ip, name);
		
		gc.sendConnectRequest();
		
		//tryToConnectDialog.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.hide()));
	
	}
	
	class ServerEntry extends Table{
		String servername, ipAdress;
		TextField tf_servername, tf_ipAdress;
		TextButton button;
		Button infobttn;
		
		NamingDialog nd;
		
		public ServerEntry(String serverName, String ip, final boolean editable, Skin skin) {
		
			nd = new NamingDialog("info", skin,
					new Entry("name: ", serverName)) {
				
				@Override
				public void updateValues(String[] values) {
					button.setText(values[0]);
				}
			};
			
			button = new TextButton(serverName, skin);
			add(button);
			infobttn = new Button(skin);
			infobttn.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		            nd.show(getStage());
		        }
		    });
			add(infobttn);
			add(new Button(skin, "minus"));
		}
	}

	@Override
	public void enter() {
		if(!info.greetingMessage.equals("")) {
			InfoDialog.show(info.greetingMessage, getStage());
		}
		if(PokemonGDX.CONFIGURATION == LaunchConfiguration.LOGGED_IN) {
			connectRequest("localhost", "AutoConnectServer");
		}
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(gc == null) return;
		System.out.println(gc.getState());
		if(gc.isConnectionFinished()) {
			if(gc.isConnected()) {
				System.err.println("isCOnnected");
				loginMask.getInfo().client = gc;
				switchTo(loginMask);
			}
			else {
				System.err.println("showing error");
				InfoDialog.show(gc.errorMsg, getStage());
				gc.resetConnection();
				System.out.println(gc.getState());
			}
			tryToConnectDialog.hide();
		}
	}
}
