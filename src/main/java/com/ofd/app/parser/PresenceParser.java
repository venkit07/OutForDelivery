package com.ofd.app.parser;

import java.util.ArrayList;

import com.ofd.app.crypto.PgpEngine;
import com.ofd.app.entities.Account;
import com.ofd.app.entities.Contact;
import com.ofd.app.entities.Conversation;
import com.ofd.app.entities.MucOptions;
import com.ofd.app.entities.Presences;
import com.ofd.app.generator.PresenceGenerator;
import com.ofd.app.services.XmppConnectionService;
import com.ofd.app.xml.Element;
import com.ofd.app.xmpp.OnPresencePacketReceived;
import com.ofd.app.xmpp.jid.Jid;
import com.ofd.app.xmpp.stanzas.PresencePacket;

public class PresenceParser extends AbstractParser implements
		OnPresencePacketReceived {

	public PresenceParser(XmppConnectionService service) {
		super(service);
	}

	public void parseConferencePresence(PresencePacket packet, Account account) {
		PgpEngine mPgpEngine = mXmppConnectionService.getPgpEngine();
		final Conversation conversation = packet.getFrom() == null ? null : mXmppConnectionService.find(account, packet.getFrom().toBareJid());
		if (conversation != null) {
			final MucOptions mucOptions = conversation.getMucOptions();
			boolean before = mucOptions.online();
			int count = mucOptions.getUsers().size();
			final ArrayList<MucOptions.User> tileUserBefore = new ArrayList<>(mucOptions.getUsers().subList(0,Math.min(mucOptions.getUsers().size(),5)));
			mucOptions.processPacket(packet, mPgpEngine);
			final ArrayList<MucOptions.User> tileUserAfter = new ArrayList<>(mucOptions.getUsers().subList(0,Math.min(mucOptions.getUsers().size(),5)));
			if (!tileUserAfter.equals(tileUserBefore)) {
				mXmppConnectionService.getAvatarService().clear(conversation);
			}
			if (before != mucOptions.online() || (mucOptions.online() && count != mucOptions.getUsers().size())) {
				mXmppConnectionService.updateConversationUi();
			} else if (mucOptions.online()) {
				mXmppConnectionService.updateMucRosterUi();
			}
		}
	}

	public void parseContactPresence(PresencePacket packet, Account account) {
		PresenceGenerator mPresenceGenerator = mXmppConnectionService
				.getPresenceGenerator();
		if (packet.getFrom() == null) {
			return;
		}
		final Jid from = packet.getFrom();
		String type = packet.getAttribute("type");
		Contact contact = account.getRoster().getContact(packet.getFrom());
		if (type == null) {
			String presence;
			if (!from.isBareJid()) {
				presence = from.getResourcepart();
			} else {
				presence = "";
			}
			int sizeBefore = contact.getPresences().size();
			contact.updatePresence(presence,
					Presences.parseShow(packet.findChild("show")));
			PgpEngine pgp = mXmppConnectionService.getPgpEngine();
			if (pgp != null) {
				Element x = packet.findChild("x", "jabber:x:signed");
				if (x != null) {
					Element status = packet.findChild("status");
					String msg;
					if (status != null) {
						msg = status.getContent();
					} else {
						msg = "";
					}
					contact.setPgpKeyId(pgp.fetchKeyId(account, msg,
							x.getContent()));
				}
			}
			boolean online = sizeBefore < contact.getPresences().size();
			updateLastseen(packet, account, false);
			mXmppConnectionService.onContactStatusChanged.onContactStatusChanged(contact, online);
		} else if (type.equals("unavailable")) {
			if (from.isBareJid()) {
				contact.clearPresences();
			} else {
				contact.removePresence(from.getResourcepart());
			}
			mXmppConnectionService.onContactStatusChanged
					.onContactStatusChanged(contact, false);
		} else if (type.equals("subscribe")) {
			if (contact.getOption(Contact.Options.PREEMPTIVE_GRANT)) {
				mXmppConnectionService.sendPresencePacket(account,
						mPresenceGenerator.sendPresenceUpdatesTo(contact));
			} else {
				contact.setOption(Contact.Options.PENDING_SUBSCRIPTION_REQUEST);
			}
		}
		Element nick = packet.findChild("nick",
				"http://jabber.org/protocol/nick");
		if (nick != null) {
			contact.setPresenceName(nick.getContent());
		}
		mXmppConnectionService.updateRosterUi();
	}

	@Override
	public void onPresencePacketReceived(Account account, PresencePacket packet) {
		if (packet.hasChild("x", "http://jabber.org/protocol/muc#user")) {
			this.parseConferencePresence(packet, account);
		} else if (packet.hasChild("x", "http://jabber.org/protocol/muc")) {
			this.parseConferencePresence(packet, account);
		} else {
			this.parseContactPresence(packet, account);
		}
	}

}
