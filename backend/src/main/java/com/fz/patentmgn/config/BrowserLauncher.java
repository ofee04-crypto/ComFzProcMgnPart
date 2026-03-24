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
                log.info("伺服器準備完畢，嘗試啟動預設瀏覽器導向至: {}", url);
                Desktop.getDesktop().browse(new URI(url));
            } else {
                log.info("Desktop API 未支援，嘗試以 native CMD 喚醒瀏覽器");
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            }
        } catch (Exception e) {
            log.error("無法自動開啟瀏覽器，請手動打開 {}", url, e);
        }
    }
}
