package com.lmt.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by 张洲徽 on 2019/1/29.
 */
//传多个role进来，满足其中一个即可
public class RolesOrFilter extends AuthorizationFilter{
    protected boolean isAccessAllowed(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            Object o
    ) throws Exception {
        Subject subject=getSubject(servletRequest,servletResponse);
        String[] roles=(String[]) o;
        if (roles==null||roles.length==0){
            return true;
        }
        for (String role:roles){
            if(subject.hasRole(role)){
                return true;
            }
        }
        return false;
    }
}
