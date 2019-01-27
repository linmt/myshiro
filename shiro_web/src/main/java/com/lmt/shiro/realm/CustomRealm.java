package com.lmt.shiro.realm;

import com.lmt.dao.UserDao;
import com.lmt.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by 张洲徽 on 2019/1/25.
 */
public class CustomRealm extends AuthorizingRealm{

    @Resource
    private UserDao userDao;

//    Map<String,String> userMap=new HashMap<String, String>(16);
//    {
//        userMap.put("zhang","195d91be1e3ba6f1c857d46f24c5a454");
//        super.setName("customRealm");
//    }

    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.从认证信息获取用户名
        String userName = (String)principalCollection.getPrimaryPrincipal();
        //模拟从数据库或缓存中获取角色数据
        Set<String> roles=getRolesByUserName(userName);
        //模拟从数据库或缓存中获取权限数据
        Set<String> permissions=getPermissionsByUserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String userName) {
        Set<String> sets=new HashSet<String>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    private Set<String> getRolesByUserName(String userName) {
        List<String> list=userDao.getRolesByUserName(userName);
        Set<String> sets=new HashSet<String>(list);
        return sets;
    }

    //认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //1.利用主体传过来的认证信息获取用户名
        String userName = (String)authenticationToken.getPrincipal();

        //2.通过用户名到数据库获取凭证
        String password=getPasswordByUserName(userName);
        if(password==null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo=
                new SimpleAuthenticationInfo(userName,password,"customRealm");

        //返回对象之前要设置盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        return authenticationInfo;
    }

    private String getPasswordByUserName(String userName) {
        //访问数据库
        User user=userDao.getUserByUserName(userName);
        if(user!=null){
            return user.getPassword();
        }
        return null;
    }

    public static void main(String[] args){
        Md5Hash md5Hash=new Md5Hash("123","zhang");
        System.out.println(md5Hash);
    }
}
