package net.simondaniel.network.protocols;

import com.esotericsoftware.kryonet.Connection;
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.masks.LoginMask;
import net.simondaniel.game.client.ui.masks.ServerSelection;
import net.simondaniel.network.chanels.MessageChannel;
import net.simondaniel.network.chanels.Protocol;
import net.simondaniel.network.client.ChanelListener;
import net.simondaniel.network.client.ChanelListenerList;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.GameClient.State;
import net.simondaniel.network.server.Response.EndConnectionS;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.MessageS;

public class LoginProtocol extends Protocol{

	

	public LoginProtocol(MessageChannel chanel) {
		super(chanel);
		
	}

	public static class ClientListener extends ChanelListener {

		private LoginMask mask;
		
		public ClientListener(MessageChannel channel, LoginMask m, ChanelListenerList list) {
			super(channel, false, list);
			this.mask = m;
		}

		@Override
		protected void channelReceive(Connection c, Object o) {

			if (o instanceof LoginS) {
				LoginS p = (LoginS) o;
				GameClient client = mask.getInfo().client;
				System.out.println("client received login answer: " + p.response);
				client.errorMsg = p.response;
				client.verify(p.response.equals("success"));
				if (client.state == State.DECLINED) {
					// c.close();
				}
			}
			if (o instanceof EndConnectionS) {
				mask.reActivateUI();
				ServerSelection s = new ServerSelection(mask.getSkin());
				s.getInfo().greetingMessage = "lost connection to server...";
				mask.switchTo(s);

				System.out.println("go back");
			}

			if (o instanceof MessageS) {
				System.out.println("received response");
				MessageS p = (MessageS) o;
				if (p.type == 1) {
					mask.reActivateUI();
					mask.activation.show(mask.getStage());
				} else if (p.type == 0) {
					mask.reActivateUI();
					InfoDialog.show(p.message, mask.getStage());
					mask.hide();
				}

			}
		}
	}
	
	public static class ServerListener extends ChanelListener {

		private LoginMask mask;
		
		public ServerListener(MessageChannel channel, LoginMask m, ChanelListenerList list) {
			super(channel, false, list);
			this.mask = m;
		}

		@Override
		protected void channelReceive(Connection c, Object o) {

			if (o instanceof LoginS) {
				LoginS p = (LoginS) o;
				GameClient client = mask.getInfo().client;
				System.out.println("client received login answer: " + p.response);
				client.errorMsg = p.response;
				client.verify(p.response.equals("success"));
				if (client.state == State.DECLINED) {
					// c.close();
				}
			}
			if (o instanceof EndConnectionS) {
				mask.reActivateUI();
				ServerSelection s = new ServerSelection(mask.getSkin());
				s.getInfo().greetingMessage = "lost connection to server...";
				mask.switchTo(s);

				System.out.println("go back");
			}

			if (o instanceof MessageS) {
				System.out.println("received response");
				MessageS p = (MessageS) o;
				if (p.type == 1) {
					mask.reActivateUI();
					mask.activation.show(mask.getStage());
				} else if (p.type == 0) {
					mask.reActivateUI();
					InfoDialog.show(p.message, mask.getStage());
					mask.hide();
				}

			}
		}
	}

}
