package com.ofd.app.xmpp.stanzas.csi;

import com.ofd.app.xmpp.stanzas.AbstractStanza;

public class ActivePacket extends AbstractStanza {
	public ActivePacket() {
		super("active");
		setAttribute("xmlns", "urn:xmpp:csi:0");
	}
}
