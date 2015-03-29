package com.ofd.app.xmpp.stanzas.streammgmt;

import com.ofd.app.xmpp.stanzas.AbstractStanza;

public class RequestPacket extends AbstractStanza {

	public RequestPacket(int smVersion) {
		super("r");
		this.setAttribute("xmlns", "urn:xmpp:sm:" + smVersion);
	}

}
