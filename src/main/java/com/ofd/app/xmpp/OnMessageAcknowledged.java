package com.ofd.app.xmpp;

import com.ofd.app.entities.Account;

public interface OnMessageAcknowledged {
	public void onMessageAcknowledged(Account account, String id);
}
