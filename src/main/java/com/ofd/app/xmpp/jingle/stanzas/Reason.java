package com.ofd.app.xmpp.jingle.stanzas;

import com.ofd.app.xml.Element;

public class Reason extends Element {
	private Reason(String name) {
		super(name);
	}

	public Reason() {
		super("reason");
	}
}
