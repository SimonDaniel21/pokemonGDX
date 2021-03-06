package net.simondaniel.game.client.ui.masks;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.simondaniel.GameConfig;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.NamingDialog;
import net.simondaniel.game.client.ui.NamingDialog.ButtonOption;
import net.simondaniel.game.client.ui.NamingDialog.Entry;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.game.client.ui.UImaskHandler;
import net.simondaniel.network.client.PlayClient;
import net.simondaniel.network.client.Request.AccountActivationC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.MessageS;

public class LoginMask extends UImask<LoginMaskInfo> {

	PlayClient client;
	
	TextField name_tf, pw_tf;
	TextButton login_bttn;

	Label serverName;

	CheckBox remindPW;

	GameMenu gameMenu;
	ServerSelection server_select;
	NamingDialog nd;
	public NamingDialog activation;


	ServerSelection serverSelection;

	final Dialog loggingInDialog;

	public LoginMask(Skin s, UImaskHandler ui) {
		super(new LoginMaskInfo(), s, ui);
		

		loggingInDialog = new Dialog("info", s);
		loggingInDialog.text("logging in...");

		nd = new NamingDialog("enter email", s, ButtonOption.OK_CANCEL, new Entry("email:", "istmieegal@gmail.com"),
				new Entry("repeat password:", "pw1")) {
			@Override
			public void updateValues(String[] values) {
				if (!values[1].equals(pw_tf.getText())) {
					System.err.println("reached");
					InfoDialog.show("passwords dont match!", getStage());
				} else {
					client.authService.registerNewUser(name_tf.getText(), values[1], values[0]);
					deactivateUntilResponse();
				}
			}
		};
		activation = new NamingDialog("Activation", s, ButtonOption.OK, new Entry("code:", "")) {
			@Override
			public void updateValues(String[] values) {
				info.client.authService.activateAccount(name_tf.getText(), values[0]);
				deactivateUntilResponse();
			}
		};
		serverName = new Label(null, s);
		add(serverName).colspan(2);
		row();
		add("name:").width(200);
		name_tf = new TextField("", s);
		name_tf.selectAll();
		// getStage().setKeyboardFocus(name_tf);
		name_tf.setCursorPosition(name_tf.getText().length());
		add(name_tf).width(300);
		row();
		add("Passwort:");
		// development
	
		pw_tf = new TextField("", s);
		add(pw_tf).width(200);
		row();
		remindPW = new CheckBox("passwort merken", s);
		add(remindPW);
		login_bttn = new TextButton("login", s);
		login_bttn.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (isActive())
					loginRequest();
				// deactivateUntilResponse();
			}
		});
		add(login_bttn).row();
		TextButton tb = new TextButton("back", s);
		tb.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(isActive()) {
					client.close();
					client.authService.deactivate();
					server_select.info.autoConnect = false;
					switchTo(server_select);
				}
			}
		});
		add(tb);

		TextButton register = new TextButton("register", s, "small");
		register.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (isActive())
					nd.show(getStage());
			}
		});
		add(register).bottom().right();

		addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ENTER) {
					if (isActive())
						loginRequest();
				}
				return false;
			}
		});
	}
	
	

	public void loginRequest() {
		loggingInDialog.show(getStage());
		deactivateUntilResponse();
		
		client.authService.login(name_tf.getText(), pw_tf.getText());
		System.out.println("try login woth " + name_tf.getText());
	}

	@Override
	public void enter() {
		client = info.client;
		client.authService.activate();
		
		serverName.setText(info.client.SERVER_NAME);
		name_tf.setText(GameConfig.gameConfig.LAST_LOGIN_NAME);
		name_tf.selectAll();
		name_tf.setCursorPosition(name_tf.getText().length());

		String pw = GameConfig.gameConfig.LAST_LOGIN_PASSWORD;
		
		if (pw.equals("null"))
			pw = "";
		else if(info.autoLogin){
			pw_tf.setText(pw);
			loginRequest();
			
			System.err.println("auto login deaktiviert");
		}
		
		pw_tf.setText(pw);
		remindPW.setChecked(GameConfig.gameConfig.KEEP_PASSWORD);
		name_tf.selectAll();
		getStage().setKeyboardFocus(name_tf);
		if (PokemonGDX.CONFIGURATION == LaunchConfiguration.LOGGED_IN) {
			loginRequest();
		}
	}
	
	private void doLogin() {
		GameConfig.gameConfig.writeAsString("last_loginName", name_tf.getText());
		
		if (remindPW.isChecked()) {
			GameConfig.gameConfig.writeAsString("last_loginPassword", pw_tf.getText());
		} else {
			GameConfig.gameConfig.writeAsString("last_loginPassword", "null");
		}
		GameConfig.gameConfig.writeAsBool("keep_password", remindPW.isChecked());

		PokemonGDX.game.client = info.client;
		gameMenu.info.client = info.client;
		switchTo(gameMenu);
	}

	@Override
	public void act(float delta) {
		
		MessageS rr = client.authService.registerRequest.consume();
		MessageS aar = client.authService.activateAccountRequest.consume();
		
		LoginS p = client.authService.loginRequest.consume();
		
		if(p != null) {
			
			if (p.response.equals("success")) {
				doLogin();

			} else {
				InfoDialog id = new InfoDialog(p.response, getSkin());
				id.show(getStage());
			}
			loggingInDialog.hide();
			reActivateUI();
		}
		
		if(rr != null) {
			reActivateUI();
			if(rr.type == 0) {
				InfoDialog.showError(rr.message, getStage());
			}
			
			if(rr.type == 1) {
				activation.show(getStage());
			}
		}
		
		if(aar != null) {
			reActivateUI();
			if(aar.type == 0) {
				InfoDialog.showError(aar.message, getStage());
			}
			
			if(aar.type == 2) {
				InfoDialog.show(aar.message, stage);
			}
			
			if(aar.type == 3) {
				InfoDialog.showError(aar.message, stage);
			}
		}
		
		
		super.act(delta);
	}

	@Override
	public void leave() {
		client.authService.deactivate();
		client = null;
	}



	@Override
	public void afterInit() {
		gameMenu = stage.game_menu_mask;
		server_select = stage.server_select_mask;
	}

}
