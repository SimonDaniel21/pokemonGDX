package net.simondaniel.game.client.ui.masks;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch.Config;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.GameConfig;
import net.simondaniel.GameMode;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.MyRandom;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.NamingDialog;
import net.simondaniel.game.client.ui.NamingDialog.ButtonOption;
import net.simondaniel.game.client.ui.NamingDialog.Entry;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.MyListener;
import net.simondaniel.network.client.Request.AccountActivationC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.server.Response.EndConnectionS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.screens.MainMenuScreen;

public class LoginMask extends UImask<LoginMaskInfo>{

	TextField name_tf, pw_tf;
	TextButton login_bttn;
	
	Label serverName;
	
	CheckBox remindPW;
	
	GameMenu gameMenu;
	
	NamingDialog nd, activation;
	
	MyListener listener;

	ServerSelection serverSelection;
	
	final Dialog loggingInDialog;
	
	public LoginMask(Skin s) {
		super(new LoginMaskInfo(), s);
		gameMenu = new GameMenu(s);
		listener = new LoginMaskListener();
		
		loggingInDialog = new Dialog("info", s);
		loggingInDialog.text("logging in...");
		
		nd = new NamingDialog("enter email", s, ButtonOption.OK_CANCEL, new Entry("email:", ""), new Entry("repeat password:", "")) {
			@Override
			public void updateValues(String[] values) {
				if(!values[1].equals(pw_tf.getText())) {
					System.err.println("reached");
					InfoDialog.show("passwords dont match!", getStage());
				}
				else {
					RegisterUserC p = new RegisterUserC();
					p.name = name_tf.getText();
					p.pw = values[1];
					p.email = values[0];
					info.client.send(p);
					deactivateUntilResponse();
				}
			}
		};
		activation = new NamingDialog("Activation", s, ButtonOption.OK, new Entry("code:", "")) {
			@Override
			public void updateValues(String[] values) {
				AccountActivationC p = new AccountActivationC();
				p.name = name_tf.getText();
				p.code = values[0];
				info.client.send(p);
				deactivateUntilResponse();
			}
		};
		serverName =  new Label(null, s);
		add(serverName).colspan(2);
		row();
		add("name:").width(200);
		name_tf = new TextField(GameConfig.gameConfig.LAST_LOGIN_NAME, s);
		name_tf.selectAll();
		//getStage().setKeyboardFocus(name_tf);
		name_tf.setCursorPosition(name_tf.getText().length());
		add(name_tf).width(300);
		row();
		add("Passwort:");
		//development
		String pw = GameConfig.gameConfig.LAST_LOGIN_PASSWORD;
		if(pw.equals("null")) pw = "";
		pw_tf = new TextField(pw, s);
		add(pw_tf).width(200);
		row();
		remindPW = new CheckBox("passwort merken", s);
		add(remindPW);
		login_bttn = new TextButton("login", s);
		login_bttn.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(isActive())
					loginRequest(name_tf.getText(), pw_tf.getText());
				//deactivateUntilResponse();
			}
		});
		add(login_bttn).row();
		TextButton tb = new TextButton("back", s);
		tb.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				goBack();	
			}
		});
		add(tb);
		
		TextButton register = new TextButton("register", s, "small");
		register.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(isActive())
					nd.show(getStage());
			}
		});
		add(register).bottom().right();
		
		addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ENTER) {
					if(isActive())
						loginRequest(name_tf.getText(), pw_tf.getText());
				}
				return false;
			}
		});
	}
	
	public void loginRequest(String name, String pw) {
		loggingInDialog.show(getStage());
		GameClient gc = info.client;
		gc.sendLoginRequest(name, pw);
		deactivateUntilResponse();

//		boolean loggedIn = gc.waitForVerification(2000);
//		if (!loggedIn) {
//			InfoDialog id = new InfoDialog(gc.errorMsg, skin);
//			id.show(getStage());
//		} else {
//			GameConfig.gameConfig.writeAsString("last_loginName", name);
//			if(remindPW.isChecked()) {
//				GameConfig.gameConfig.writeAsString("last_loginPassword", pw);
//			}
//			else {
//				GameConfig.gameConfig.writeAsString("last_loginPassword", "null");
//			}
//			GameConfig.gameConfig.writeAsBool("keep_password", remindPW.isChecked());
//			GameConfig.gameConfig.save();
//			
//			d.addAction(Actions.sequence(Actions.fadeOut(0.4f), Actions.hide()));
//			PokemonGDX.game.client = gc;
//			gameMenu.getInfo().client = gc;
//			switchTo(gameMenu);
//						
//			
//			//stage.add(new Friendlist(skin));
//		}
//		d.hide();

	}

	@Override
	public void enter() {
		info.client.addMyListener(listener);
		serverName.setText(info.client.SERVER_NAME);
		//name_tf.setText("blader108");
		name_tf.selectAll();
		name_tf.setCursorPosition(name_tf.getText().length());
		
		String pw = GameConfig.gameConfig.LAST_LOGIN_PASSWORD;
		if(pw.equals("null")) pw = "";
		pw_tf.setText(pw);
		remindPW.setChecked(GameConfig.gameConfig.KEEP_PASSWORD);
		name_tf.selectAll();
		getStage().setKeyboardFocus(name_tf);
		if(PokemonGDX.CONFIGURATION == LaunchConfiguration.LOGGED_IN) {
			loginRequest("user " + MyRandom.random.nextInt(1000), "development");
		}
	}

	@Override
	public void act(float delta) {
		info.client.handlePacketBuffer();
		if(info.client.isLoginFinished()){
			if(info.client.isLoggedIn()) {
					GameConfig.gameConfig.writeAsString("last_loginName", name_tf.getText());
					if(remindPW.isChecked()) {
						GameConfig.gameConfig.writeAsString("last_loginPassword", pw_tf.getText());
					}
					else {
						GameConfig.gameConfig.writeAsString("last_loginPassword", "null");
					}
					GameConfig.gameConfig.writeAsBool("keep_password", remindPW.isChecked());
					GameConfig.gameConfig.save();
					
					PokemonGDX.game.client = info.client;
					gameMenu.getInfo().client = info.client;
					switchTo(gameMenu);
					reActivateUI();
					
			}
			else{
				InfoDialog id = new InfoDialog(info.client.errorMsg, getSkin());
				info.client.resetLogin();
				id.show(getStage());
				//System.out.println("showing login error");
			}
			loggingInDialog.hide();
			reActivateUI();
		}
		super.act(delta);
	}
	
	@Override
	public void leave() {
		info.client.removeMyListener(listener);
	}
	
	public class LoginMaskListener implements MyListener{

		@Override
		public void received(Connection c, Object o) {
			
			if(o instanceof EndConnectionS) {
				reActivateUI();
				ServerSelection s = new ServerSelection(getSkin());
				s.getInfo().greetingMessage = "lost connection to server...";
				switchTo(s);
				
				System.out.println("go back");
			}
			
			if(o instanceof MessageS) {
				System.out.println("received response");
				MessageS p = (MessageS) o;
				if(p.type == 1) {
					reActivateUI();
					activation.show(getStage());
				}
				else if(p.type == 0) {
					reActivateUI();
					InfoDialog.show(p.message, getStage());
					loggingInDialog.hide();
				}
					
			}
		}
		
	}
}
