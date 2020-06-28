package ru.sms.addons.pdfviewer.web.bootstrap;

import com.haulmont.cuba.web.sys.CubaBootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import org.jsoup.nodes.Element;


public class CustomBootstrapListener extends CubaBootstrapListener {
    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        super.modifyBootstrapPage(response);

        Element head = response.getDocument().getElementsByTag("head").get(0);

        String pathToLocale = "./VAADIN/pdf-js-locale/locale/ru/viewer.properties";

        Element resource = response.getDocument().createElement("link");
        resource.attr("type", "application/l10n");
        resource.attr("href", pathToLocale);
        head.appendChild(resource);

    }
}