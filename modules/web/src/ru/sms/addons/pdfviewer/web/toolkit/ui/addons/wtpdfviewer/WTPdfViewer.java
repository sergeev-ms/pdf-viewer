package ru.sms.addons.pdfviewer.web.toolkit.ui.addons.wtpdfviewer;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerClientRpc;
import ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerServerRpc;
import ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerState;

import java.io.IOException;

// This is the server-side UI component that provides public API 
// for WTPdfViewer
@JavaScript({ "l10n.js", "pdf.worker.js", "pdf.js", "pdf.viewer.js" })
public class WTPdfViewer extends com.vaadin.ui.AbstractComponent {

  private static final long serialVersionUID = -8979560729307918782L;
  private static final Logger log = LoggerFactory.getLogger(WTPdfViewer.class);

  // To process events from the client, we implement and register ServerRpc
  private WTPdfViewerServerRpc rpc = new WTPdfViewerServerRpc() {

    private static final long serialVersionUID = 7777987530546614587L;

    @Override
    public void onError(String error) {
      log.warn("Client side returned an error: " + error);
    }
  };

  public WTPdfViewer() {
    registerRpc(rpc);
  }

  // We must override getState() to cast the state to WTPdfViewerState
  @Override
  protected WTPdfViewerState getState() {
    return (WTPdfViewerState) super.getState();
  }

  @Override
  protected WTPdfViewerState getState(boolean markAsDirty) {
    return (WTPdfViewerState) super.getState(markAsDirty);
  }

  public void setResource(StreamResource sourceFile) {
    setResource("resourceFile", sourceFile);
  }

  public StreamResource getResource() {
    return (StreamResource) getResource("resourceFile");
  }

  public String getPassword() {
    return getState(false).password;
  }

  public void setPassword(String password) {
    getState().password = password;
  }

  public void firstPage() {
    WTPdfViewerClientRpc proxy = getRpcProxy(WTPdfViewerClientRpc.class);
    proxy.firstPage();
  }

  public void lastPage() {
    WTPdfViewerClientRpc proxy = getRpcProxy(WTPdfViewerClientRpc.class);
    proxy.lastPage();
  }

  public void previousPage() {
    WTPdfViewerClientRpc proxy = getRpcProxy(WTPdfViewerClientRpc.class);
    proxy.previousPage();
  }

  public void nextPage() {
    WTPdfViewerClientRpc proxy = getRpcProxy(WTPdfViewerClientRpc.class);
    proxy.nextPage();
  }

  public void setPage(int page) {
    WTPdfViewerClientRpc proxy = getRpcProxy(WTPdfViewerClientRpc.class);
    proxy.setPage(page);
  }

  public void setShowPreviousViewOnLoad(boolean showPreviousViewOnLoad) {
    WTPdfViewerClientRpc proxy = getRpcProxy(WTPdfViewerClientRpc.class);
    proxy.setShowPreviousViewOnLoad(showPreviousViewOnLoad);
  }

  public void download() {
    WTPdfViewerClientRpc proxy = getRpcProxy(WTPdfViewerClientRpc.class);
    proxy.download();
  }

  public void print() {
    WTPdfViewerClientRpc proxy = getRpcProxy(WTPdfViewerClientRpc.class);
    proxy.print();
  }

  public void toggleHandTool() {
    WTPdfViewerClientRpc proxy = getRpcProxy(WTPdfViewerClientRpc.class);
    proxy.toggleHandTool();
  }

  public boolean isZoomWithoutModifiers() {
    return getState(false).zoomWithoutModifiers;
  }

  public void setZoomWithoutModifiers(boolean withoutModifiers) {
    if (withoutModifiers != isZoomWithoutModifiers())
      getState().zoomWithoutModifiers = withoutModifiers;
  }

  @Override
  public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
    DownloadStream stream = null;
    String[] parts = path.split("/", 2);
    String key = parts[0];
    String requestedFilename = parts[1];

    VaadinSession session = getSession();
    session.lock();
    try {
      ConnectorResource resource = (ConnectorResource) getResource(key);
      if (resource == null) {
        return false;
      }
      stream = resource.getStream();
    } finally {
      session.unlock();
    }

    try {
      /**
       * When client side attempts to switch between multiple different pdfs quickly, quicker then the pdf can load,  
       * following order of events can happen:
       * setResource("one.pdf");
       * setResource("two.pdf");
       *
       * handleConnectorRequest(..., one.pdf); <- in this case one.pdf resource is no longer available, only two.pdf is
       * 										 <- it is better to return nothing and let client show error then return wrong pdf 
       *                                       <- however in pdf viewer we expect the second handleConnectorRequest to come shortly
       * handleConnectorRequest(..., two.pdf);
       */
      if (requestedFilename.equals(stream.getFileName())) {
        stream.writeResponse(request, response);
      } else {
        log.warn("Obsolete pdf file requested. The stream is not available anymore, sending nothing.");
        log.warn("Requested filename: " + requestedFilename + " available filename: " + stream.getFileName());
      }
    } catch (IOException ex) {
      /**
       * When client side attempts to switch between multiple different pdfs quickly, quicker then the pdf can load,  
       * the exception could happen in any of the files. Most error happen because client side aborted old requests
       * and issued new request for newer pdf. In that case, we can safely ignore the error and just log it.
       */
      String currentFilename = currentFilenameForErrorHanding(session);
      if (isEqual(requestedFilename, currentFilename)) {
        // This is case when last request failed and pdf in browser has nothing to show.
        // Throw
        // the exception so that the reset of application error handler can act on it.
        throw ex;
      }

      log.warn("Exception while downloading file. The file was already obsolete, ignoring exception.", ex);
      log.warn("File that failed: " + requestedFilename + " current filename: " + stream.getFileName());
    }
    return true;
  }

  private boolean isEqual(String str1, String str2) {
    if (str1 == null) {
      return str2 == null;
    }
    return str1.equals(str2);
  }

  protected String currentFilenameForErrorHanding(VaadinSession session) {
    session.lock();
    try {
      StreamResource currentResource = getResource();
      String currentFilename = currentResource == null ? null : currentResource.getFilename();
      return currentFilename;
    } finally {
      session.unlock();
    }
  }

  public boolean isDownloadVisible() {
    return getState(false).downloadVisible;
  }

  public void setDownloadVisible(boolean downloadVisible) {
    if (isDownloadVisible() != downloadVisible) {
      getState().downloadVisible = downloadVisible;
    }
  }

  public boolean isPrintVisible() {
    return getState(false).printVisible;
  }

  public void setPrintVisible(boolean printVisible) {
    if (isPrintVisible() != printVisible) {
      getState().printVisible = printVisible;
    }
  }
}
