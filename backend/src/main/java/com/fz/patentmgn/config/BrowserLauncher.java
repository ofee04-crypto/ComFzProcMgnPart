package com.fz.patentmgn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class BrowserLauncher implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(BrowserLauncher.class);

    @org.springframework.beans.factory.annotation.Value("${server.port:8080}")
    private String port;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String url = "http://localhost:" + port + "/";
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                log.info("Server Ready. Opening Browser to: {}", url);
                Desktop.getDesktop().browse(new URI(url));
            } else {
                log.info("Desktop API not supported, trying to open browser with native CMD");
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            }
        } catch (Exception e) {
            log.error("Failed to open browser, please open {} manually", url, e);
        }
    }
}
