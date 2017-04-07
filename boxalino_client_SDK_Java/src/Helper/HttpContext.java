/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.util.UUID;

/**
 *
 * @author HASHIR
 */
public class HttpContext {

    private final int _VISITOR_COOKIE_TIME = 31536000;

    public static String sessionId;
    public static String profileId;
    private static String domain;
    private static String ip;
    private static String referer;
    private static String currentUrl;
    private static String htmlDebug = "";
    private static String userAgent = "";
    
     
    public HttpContext(String domain, String userAgent, String ip, String referer, String currentUrl) {
        this.sessionId = null;
        this.profileId = null;
        this.domain = domain;
        this.userAgent = userAgent;
        this.ip = ip;
        this.referer = referer;
        this.currentUrl = currentUrl;
    }

    public HttpContext(String sessionId, String profileId, String domain, String userAgent, String ip, String referer, String currentUrl) {
        this.sessionId = sessionId;
        this.profileId = profileId;
        this.domain = domain;
        this.userAgent = userAgent;
        this.ip = ip;
        this.referer = referer;
        this.currentUrl = currentUrl;
    }

    public String[] getSessionAndProfile(String sessionId, String profileId, String domain) {
        if (sessionId != null) {
            this.sessionId = sessionId;
        }
        if (profileId != null) {
            this.profileId = profileId;
        }
        if (this.sessionId == null) {
            this.sessionId = UUID.randomUUID().toString();
        }
        if (this.profileId == null) {
            this.profileId = UUID.randomUUID().toString();
        }
        return new String[]{this.sessionId, this.profileId};
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String getIP() {
        return this.ip;
    }

    public String getReferer() {
        return this.referer;
    }

    public String getCurrentUrl() {
        return this.currentUrl;
    }

    public String responseWrite(String write) {
        this.htmlDebug += write;
        return this.htmlDebug;
    }

    public String getHtmlDebug() {
        return this.htmlDebug;
    }
}
