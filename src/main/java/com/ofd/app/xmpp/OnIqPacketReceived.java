package com.ofd.app.xmpp;

import com.ofd.app.entities.Account;
import com.ofd.app.xmpp.stanzas.IqPacket;

public interface OnIqPacketReceived extends PacketReceived {
	public void onIqPacketReceived(Account account, IqPacket packet);
}
