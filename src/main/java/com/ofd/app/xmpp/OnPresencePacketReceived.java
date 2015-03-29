package com.ofd.app.xmpp;

import com.ofd.app.entities.Account;
import com.ofd.app.xmpp.stanzas.PresencePacket;

public interface OnPresencePacketReceived extends PacketReceived {
	public void onPresencePacketReceived(Account account, PresencePacket packet);
}
