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
import net.simondaniel.network.client.PlayClient;
import net.simondaniel.network.client.Request.AccountActivationC;
import net.simondaniel.network.client.Request.RegisterUserC;

public class LoginMask extends UImask<LoginMaskInfo> {

	PlayClient client;
	
	TextField name_tf, pw_tf;
	TextButton login_bttn;

	Label serverName;

	CheckBox remindPW;

	GameMenu gameMenu;

	NamingDialog nd;
	public NamingDialog activation;


	ServerSelection serverSelection;

	final Dialog loggingInDialog;

	public LoginMask(Skin s) {
		super(new LoginMaskInfo(), s);
		gameMenu = new GameMenu(s);

		loggingInDialog = new Dialog("info", s);
		loggingInDialog.text("logging in...");

		nd = new NamingDialog("enter email", s, ButtonOption.OK_CANCEL, new Entry("email:", ""),
				new Entry("repeat password:", "")) {
			@Override
			public void updateValues(String[] values) {
				if (!values[1].equals(pw_tf.getText())) {
					System.err.println("reached");
					InfoDialog.show("passwords dont match!", getStage());
				} else {
					RegisterUserC p = new RegisterUserC();
					p.name = name_tf.getText();
					p.pw = values[1];
					p.email = values[0];
					//info.client.send(MessageChannel.initialChannel, p);
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
				//info.client.send(MessageChannel.initialChannel, p);
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
				client.close();
				client.authService.deactivate();
				goBack();
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
		else {
			//loginRequest();
			
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

	@Override
	public void act(float delta) {
		
		if(client.authService.response.isReady()) {
			//System.err.println("response ready " + client.authService.response.consume().response);
			String r = client.authService.response.consume();
			
			if (r.equals("success")) {
				GameConfig.gameConfig.writeAsString("last_loginName", name_tf.getText());
				if (remindPW.isChecked()) {
					GameConfig.gameConfig.writeAsString("last_loginPassword", pw_tf.getText());
				} else {
					GameConfig.gameConfig.writeAsString("last_loginPassword", "null");
				}
				GameConfig.gameConfig.writeAsBool("keep_password", remindPW.isChecked());
				GameConfig.gameConfig.save();

				PokemonGDX.game.client = info.client;
				gameMenu.getInfo().client = info.client;
				switchTo(gameMenu);

			} else {
				InfoDialog id = new InfoDialog(r, getSkin());
				id.show(getStage());
				// System.out.println("showing login error");
			}
			loggingInDialog.hide();
			reActivateUI();
		}
		
		super.act(delta);
	}

	@Override
	public void leave() {
		client.authService.deactivate();
		client = null;
	}

}
