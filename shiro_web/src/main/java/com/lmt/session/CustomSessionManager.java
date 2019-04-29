package com.lmt.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * Created by 张洲徽 on 2019/4/29.
 */
public class CustomSessionManager extends DefaultSessionManager {
    //sessionKey中存储着request对象，把session放到request对象里面，这样不用每次都去redis中读取
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = this.getSessionId(sessionKey);
        ServletRequest request=null;

        if(sessionKey instanceof WebSessionKey) {
            request=((WebSessionKey)sessionKey).getServletRequest();
        }
        if(request != null && sessionId != null) {
            Session session=(Session) request.getAttribute(sessionId.toString());
            if(session != null){
                return session;
            }
        }
        Session session=super.retrieveSession(sessionKey);
        if(request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(),session);
        }
        return session;
    }
}
