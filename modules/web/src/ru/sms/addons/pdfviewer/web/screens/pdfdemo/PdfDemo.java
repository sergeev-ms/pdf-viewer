package ru.sms.addons.pdfviewer.web.screens.pdfdemo;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Layout;
import ru.sms.addons.pdfviewer.web.toolkit.ui.addons.wtpdfviewer.WTPdfViewer;

import javax.inject.Inject;
import java.io.InputStream;

@UiController("demo_PdfDemo")
@UiDescriptor("pdf-demo.xml")
public class PdfDemo extends Screen {

    @Inject
    private VBoxLayout pdfBox;
    @Inject
    private CheckBox download;

    private WTPdfViewer pdfViewer;

    @Subscribe
    public void onInit(InitEvent event) {
        pdfViewer = new WTPdfViewer();
        pdfViewer.setSizeFull();

        String filename = "ru/sms/addons/pdfviewer/web/screens/pdfdemo/compressed.tracemonkey-pldi-09.pdf";
        InputStream dataStream = getClass().getClassLoader().getResourceAsStream(filename);
//        pdfViewer.setPassword("123");
        pdfViewer.setResource(new StreamResource(new InputStreamSource(dataStream), filename));

        download.setValue(pdfViewer.isDownloadVisible());
        download.addValueChangeListener(valueChangeEvent ->
                pdfViewer.setDownloadVisible(valueChangeEvent.getValue()));

        pdfBox.unwrap(Layout.class).addComponent(pdfViewer);
    }

    @Subscribe("downloadBtn")
    public void onDownloadBtnClick(Button.ClickEvent event) {
        pdfViewer.download();
    }

    @Subscribe("lastPageBtn")
    public void onLastPageBtnClick(Button.ClickEvent event) {
        pdfViewer.lastPage();
    }

    @Subscribe("toggleHandToolBtn")
    public void onToggleHandToolBtnClick(Button.ClickEvent event) {
//        pdfViewer.toggleHandTool();
//        pdfViewer.setZoomWithoutModifiers(true);
    }

    public static class InputStreamSource implements StreamResource.StreamSource {

        private final InputStream data;

        public InputStreamSource(InputStream data) {
            super();
            this.data = data;
        }

        @Override
        public InputStream getStream() {
            return data;
        }
    }
}