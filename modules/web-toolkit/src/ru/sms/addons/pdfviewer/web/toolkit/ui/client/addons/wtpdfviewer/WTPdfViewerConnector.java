package ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer;

import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import ru.sms.addons.pdfviewer.web.toolkit.ui.addons.wtpdfviewer.WTPdfViewer;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@SuppressWarnings("serial")
@Connect(WTPdfViewer.class)
public class WTPdfViewerConnector extends AbstractComponentConnector {

	// ServerRpc is used to send events to server. Communication implementation
	// is automatically created here
	WTPdfViewerServerRpc rpc = RpcProxy.create(WTPdfViewerServerRpc.class, this);

	public WTPdfViewerConnector() {

		// To receive RPC events from server, we register ClientRpc implementation 
		registerRpc(WTPdfViewerClientRpc.class, new WTPdfViewerClientRpc() {

			@Override
			public void firstPage() {
				getWidget().firstPage();
			}

			@Override
			public void lastPage() {
				getWidget().lastPage();
			}

			@Override
			public void nextPage() {
				getWidget().nextPage();
			}

			@Override
			public void previousPage() {
				getWidget().previousPage();
			}

			@Override
			public void setPage(int page) {
				getWidget().setPage(page);
			}
			
			@Override
			public void setShowPreviousViewOnLoad(boolean showPreviousViewOnLoad) {
				getWidget().setShowPreviousViewOnLoad(showPreviousViewOnLoad);
			}

			@Override
			public void download() {
				getWidget().download();
			}

			@Override
			public void print() {
				getWidget().print();
			}

			@Override
			public void toggleHandTool() {
				getWidget().toggleHandTool();
			}
		});
		
		getWidget().setErrorListener(error -> rpc.onError(error));
	}

	// We must implement getWidget() to cast to correct type 
	// (this will automatically create the correct widget type)
	@Override
	public WTPdfViewerWidget getWidget() {
		return (WTPdfViewerWidget) super.getWidget();
	}

	// We must implement getState() to cast to correct type
	@Override
	public WTPdfViewerState getState() {
		return (WTPdfViewerState) super.getState();
	}

	@OnStateChange("resources")
	void updateResource() {
		getWidget().setResourceFile(getResourceUrl("resourceFile"), getState().password);
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		if (stateChangeEvent.hasPropertyChanged("downloadVisible")) {
			getWidget().setDownloadVisible(getState().downloadVisible);
		}
		if (stateChangeEvent.hasPropertyChanged("printVisible")) {
			getWidget().setPrintVisible(getState().printVisible);
		}
	}
}
