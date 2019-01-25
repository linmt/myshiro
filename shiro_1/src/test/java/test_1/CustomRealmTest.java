package test_1;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import realm.CustomRealm;

/**
 * Created by 张洲徽 on 2019/1/25.
 */
public class CustomRealmTest {
    @Test
    public void testAuthentication(){
        CustomRealm customRealm=new CustomRealm();

        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        HashedCredentialsMatcher matcher=new HashedCredentialsMatcher();
        //指定使用的加密算法
        matcher.setHashAlgorithmName("md5");
        //指定加密的次数
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);

        //2.主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("zhang","123");

        subject.login(token);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());
        subject.checkRole("admin");
        subject.checkPermissions("user:delete","user:add");
    }
}
