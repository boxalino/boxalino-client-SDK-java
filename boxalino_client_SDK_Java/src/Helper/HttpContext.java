/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 *
 * @author HASHIR
 */
public class HttpContext extends HttpServlet {

    private int _VISITOR_COOKIE_TIME = 31536000;

    public String[] getSessionAndProfile(String sessionId, String profileId, String domain) {

        WebContext ctx = WebContextFactory.get();
        HttpServletRequest request = ctx.getHttpServletRequest();
        HttpServletResponse response = ctx.getHttpServletResponse();
        Cookie[] cookies = null;
        cookies = request.getCookies();
        if (sessionId != null && profileId != null) {
            return new String[]{sessionId, profileId};
        }
        if (cookies != null) {

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cems")) {
                    sessionId = cookie.getValue();
                } else {
                    sessionId = request.getSession().getId();
                }

                if (cookie.getName().equals("cemv")) {
                    profileId = cookie.getValue();
                } else {
                    profileId = request.getSession().getId();
                }

            }
        }
        if (domain == null || domain == Helper.Common.EMPTY_STRING) {
            Cookie cemsCookie = new Cookie("cems", sessionId);
            cemsCookie.setMaxAge(_VISITOR_COOKIE_TIME);
            response.addCookie(cemsCookie);

            Cookie cemvCookie = new Cookie("cemv", profileId);
            cemvCookie.setMaxAge(_VISITOR_COOKIE_TIME);
            response.addCookie(cemvCookie);

        } else {
            Cookie cemsCookie = new Cookie("cems", sessionId);
            cemsCookie.setMaxAge(_VISITOR_COOKIE_TIME);
            cemsCookie.setPath("/");
            cemsCookie.setDomain(domain);
            response.addCookie(cemsCookie);

            Cookie cemvCookie = new Cookie("cemv", profileId);
            cemvCookie.setMaxAge(_VISITOR_COOKIE_TIME);
            cemvCookie.setPath("/");
            cemvCookie.setDomain(domain);
            response.addCookie(cemvCookie);
        }

        return new String[]{sessionId, profileId};
    }

    public String getUserAgent() {
        WebContext ctx = WebContextFactory.get();
        HttpServletRequest request = ctx.getHttpServletRequest();
        return request.getHeader("User-Agent");
    }

    public String getIP() {
        WebContext ctx = WebContextFactory.get();
        HttpServletRequest request = ctx.getHttpServletRequest();
        return request.getRemoteAddr();
    }

    public String getReferer() throws URISyntaxException {
        WebContext ctx = WebContextFactory.get();
        HttpServletRequest request = ctx.getHttpServletRequest();
        return new URI(request.getHeader("referer")).getPath();
    }

    public String getCurrentUrl() {
        WebContext ctx = WebContextFactory.get();
        HttpServletRequest request = ctx.getHttpServletRequest();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI() + "?" + request.getQueryString();
    }

    public void responseWrite(String content) throws IOException {
        WebContext ctx = WebContextFactory.get();
        HttpServletResponse response = ctx.getHttpServletResponse();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(content);
    }
}
