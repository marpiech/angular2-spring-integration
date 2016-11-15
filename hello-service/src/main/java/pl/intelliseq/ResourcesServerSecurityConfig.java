package pl.intelliseq;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by Marcin Piechota
 */
@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class ResourcesServerSecurityConfig implements ResourceServerConfigurer {

    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("hello-service");
    }

    public void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/**").authenticated()
                    .antMatchers(HttpMethod.GET, "/hello").hasAuthority("USER");
                    //.antMatchers(HttpMethod.POST, "/foo").hasAuthority("FOO_WRITE");
                    //you can implement it like this, but I show method invocation security on write
        }



}
