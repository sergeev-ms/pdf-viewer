package ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer;

import com.vaadin.shared.communication.ClientRpc;

// ClientRpc is used to pass events from server to client
// For sending information about the changes to component state, use State instead
public interface WTPdfViewerClientRpc extends ClientRpc {

	void firstPage();

	void lastPage();

	void nextPage();

	void previousPage();

	void setPage(int page);

	void setShowPreviousViewOnLoad(boolean showPreviousViewOnLoad);

	void download();

	void print();

	void toggleHandTool();

    void setZoomWithoutModifiers(boolean withNoModifiers);
}
