package ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer;

import com.vaadin.shared.communication.ServerRpc;

// ServerRpc is used to pass events from client to server
public interface WTPdfViewerServerRpc extends ServerRpc {

  public void onError(String error);
  
}
