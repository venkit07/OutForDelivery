package com.ofd.app.xmpp;

import com.ofd.app.entities.Contact;

public interface OnContactStatusChanged {
	public void onContactStatusChanged(final Contact contact, final boolean online);
}
