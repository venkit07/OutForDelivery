package com.ofd.app.xmpp;

import com.ofd.app.entities.Account;
import com.ofd.app.xmpp.stanzas.MessagePacket;

public interface OnMessagePacketReceived extends PacketReceived {
	public void onMessagePacketReceived(Account account, MessagePacket packet);
}
