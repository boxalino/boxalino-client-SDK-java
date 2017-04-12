/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import static Helper.Common.EMPTY_STRING;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HASHIR
 */
public class ServletHttpContext extends HttpContext {

    public HttpServletRequest request;
    public HttpServletResponse response;

    public ServletHttpContext(String domain, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
        super(domain, request.getHeader("User-Agent"), request.getRemoteAddr(),
                request.getHeader("referer") == null ? EMPTY_STRING : new URI(request.getHeader("referer")).getPath(),
                request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI() + "?" + request.getQueryString());
        this.request = request;
        this.response = response;
    }

    private final int _VISITOR_COOKIE_TIME = 31536000;

    public String[] getSessionAndProfile(String sessionId, String profileId, String domain) {
        if (sessionId != null) {
            this.sessionId = sessionId;
        }
        if (profileId != null) {
            this.profileId = profileId;
        }
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
        if (domain == null || domain.equals(Helper.Common.EMPTY_STRING)) {
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
}
