package test_1;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Created by 张洲徽 on 2019/1/24.
 */
public class JdbcRealmTest {
    DruidDataSource datasource=new DruidDataSource();

    {
        datasource.setUrl("jdbc:mysql://127.0.0.1:3306/shiro");
        datasource.setUsername("root");
        datasource.setPassword("root");
    }
    @Test
    public void testAuthentication(){
        JdbcRealm jdbcRealm=new JdbcRealm();
        jdbcRealm.setDataSource(datasource);
        //权限的开关
        jdbcRealm.setPermissionsLookupEnabled(true);

        //自定义的表
        String sql="select password from test_user where username=?";
        jdbcRealm.setAuthenticationQuery(sql);

        String roleSql="select role_name from test_user_roles where username = ?";
        jdbcRealm.setUserRolesQuery(roleSql);

        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //2.主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("zhang","123");
        subject.login(token);

        System.out.println("isAuthenticated:"+subject.isAuthenticated());
        System.out.println("hasRole:"+subject.hasRole("admin"));
        subject.checkPermission("user:select");
    }
}
