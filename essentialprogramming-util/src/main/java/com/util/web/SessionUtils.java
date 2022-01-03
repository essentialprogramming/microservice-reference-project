package com.util.web;

import lombok.val;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static <T> T getAttribute(HttpServletRequest request, String attributeName) {
        T ret = null;
        val session = getSession(request);
        val mutex = getMutex(request);
        if (mutex != null) {
            synchronized (mutex) {
                ret = (T) session.getAttribute(attributeName);
            }
        }
        return ret;
    }

    /**
     *
     * @param request
     * @param attributeName
     * @param value
     * @return
     */
    public static void setAttribute(HttpServletRequest request, String attributeName, Object value) {
        val session = getSession(request);
        val mutex = getMutex(request);
        if (mutex != null) {
            synchronized (mutex) {
                session.setAttribute(attributeName, value);
            }
        }
    }

    /**
     *
     * @param request
     * @return
     */
    private static Object getMutex(HttpServletRequest request) {
        val session = getSession(request);

        if (session != null) {
            val mutex = WebUtils.getSessionMutex(session);
            return mutex;
        }

        return null;
    }

    private static HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }
}
