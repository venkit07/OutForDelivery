package com.ofd.app.xmpp.jingle;

import com.ofd.app.entities.Account;
import com.ofd.app.xmpp.PacketReceived;
import com.ofd.app.xmpp.jingle.stanzas.JinglePacket;

public interface OnJinglePacketReceived extends PacketReceived {
	public void onJinglePacketReceived(Account account, JinglePacket packet);
}
