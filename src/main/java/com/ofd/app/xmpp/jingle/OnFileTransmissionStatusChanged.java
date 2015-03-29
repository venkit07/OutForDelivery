package com.ofd.app.xmpp.jingle;

import com.ofd.app.entities.DownloadableFile;

public interface OnFileTransmissionStatusChanged {
	public void onFileTransmitted(DownloadableFile file);

	public void onFileTransferAborted();
}
