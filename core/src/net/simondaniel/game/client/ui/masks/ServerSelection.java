package net.simondaniel.game.client.ui.masks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import net.simondaniel.Config;
import net.simondaniel.GameConfig;
import net.simondaniel.util.MyColor;
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.NamingDialog;
import net.simondaniel.game.client.ui.NamingDialog.ButtonOption;
import net.simondaniel.game.client.ui.NamingDialog.Entry;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.game.client.ui.UImaskHandler;
import net.simondaniel.network.client.PlayClient;

public class ServerSelection extends UImask<ServerSelectionInfo>{
	
	Skin skin;
	
	private PlayClient client;
	
	public LoginMask loginMask;
	
	boolean connecting;
	private String answer;
	
	private CheckBox remindServer;
	
	private String host;
	
	public ServerSelection(Skin s, UImaskHandler ui) {
		super(new ServerSelectionInfo(), s, ui);
		
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
					if(isActive()) {
						tryToConnectTo(name, ip);
					}
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
			final String name = sa[0];
			final String ip = sa[1];
			final TextButton startServerbBttn = new TextButton(name, skin);
			startServerbBttn.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(isActive()) {
						tryToConnectTo(name, ip);
					}
				}
				
			});
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
		row();
		
		remindServer = new CheckBox("automatisch verbinden", s);
		add(remindServer);
	}
	
	private Dialog tryToConnectDialog;
	private Label connectingLabel;
	
	/**  connects to server async and shows info
	 * 
	 * @param name
	 * @param ip
	 */
	private void tryToConnectTo(final String name, final String host) {
		connectingLabel.setText("connecting to " + name + "...");
		this.host = host;
		tryToConnectDialog.show(getStage());
		tryToConnectDialog.setOrigin(Align.center);
		deactivateUntilResponse();
		
		new Thread(new Runnable() {	
			@Override
			public void run() {
				answer = client.connectBlocking(host);
				reActivateUI();
			}
		}).start();;
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
		client = info.client;
		String auto = GameConfig.gameConfig.AUTO_SERVER_HOST;
		if(!auto.equals("null")) {
			tryToConnectTo("auto_server", auto);
		}
	}

	@Override
	public void leave() {
		client = null;
		//GameConfig.gameConfig.writeAsBool("use_auto_server", remindServer.isChecked());
		String autoServer = remindServer.isChecked() ? host : "null";
		GameConfig.gameConfig.writeAsString("auto_server_host", autoServer);
		GameConfig.gameConfig.save();
	}
	
	@Override
	public void act(float delta) {
		
		if(answer != null) {
			if(answer.equals("connected")) {
				System.out.println("info " + loginMask.getInfo());
				loginMask.getInfo().client = client;
				switchTo(loginMask);
			}
			else {
				InfoDialog.show(MyColor.dye(Color.RED, "error"), answer, getStage());
			}
			tryToConnectDialog.hide();
			answer = null;
		}
		
		
		super.act(delta);
	}

	@Override
	public void afterInit() {
		loginMask = stage.login_mask;
	}
}
