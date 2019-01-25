package realm;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by 张洲徽 on 2019/1/25.
 */
public class CustomRealm extends AuthorizingRealm{
    Map<String,String> userMap=new HashMap<String, String>(16);
    {
//        userMap.put("zhang","123");
//        userMap.put("zhang","202cb962ac59075b964b07152d234b70");
        //加盐
        userMap.put("zhang","195d91be1e3ba6f1c857d46f24c5a454");
        super.setName("customRealm");
    }

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
        Set<String> sets=new HashSet<String>();
        sets.add("admin");
        sets.add("user");
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
                new SimpleAuthenticationInfo("zhang",password,"customRealm");

        //返回对象之前要设置盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("zhang"));

        return authenticationInfo;
    }

    private String getPasswordByUserName(String userName) {
        //模拟访问数据库
        return userMap.get(userName);
    }

    public static void main(String[] args){
        Md5Hash md5Hash=new Md5Hash("123","zhang");
        System.out.println(md5Hash);
    }
}
