package com.ofd.app.http;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ofd.app.entities.Message;
import com.ofd.app.services.AbstractConnectionManager;
import com.ofd.app.services.XmppConnectionService;

public class HttpConnectionManager extends AbstractConnectionManager {

	public HttpConnectionManager(XmppConnectionService service) {
		super(service);
	}

	private List<HttpConnection> connections = new CopyOnWriteArrayList<HttpConnection>();

	public HttpConnection createNewConnection(Message message) {
		HttpConnection connection = new HttpConnection(this);
		connection.init(message);
		this.connections.add(connection);
		return connection;
	}

	public void finishConnection(HttpConnection connection) {
		this.connections.remove(connection);
	}
}
