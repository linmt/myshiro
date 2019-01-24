package test_1;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by 张洲徽 on 2019/1/24.
 */
public class AuthenticationTest {

    SimpleAccountRealm simpleAccountRealm=new SimpleAccountRealm();

    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("Tom","123");
    }

    @Test
    public void testAuthentication(){
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //2.主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("Tom","123");
        subject.login(token);

        subject.logout();

        System.out.println("isAuthenticated:"+subject.isAuthenticated());
    }

}
