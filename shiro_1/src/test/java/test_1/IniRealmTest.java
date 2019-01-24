package test_1;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Created by 张洲徽 on 2019/1/24.
 */
public class IniRealmTest {
    @Test
    public void testAuthentication(){
        IniRealm iniRealm=new IniRealm("classpath:shiro.ini");
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        //2.主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("Tom","123");
        subject.login(token);

        subject.checkRole("admin");

        subject.checkPermission("user:view");
    }
}
