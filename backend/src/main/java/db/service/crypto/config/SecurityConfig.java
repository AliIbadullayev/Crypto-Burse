package db.service.crypto.config;

import db.service.crypto.security.oauth2.AuthenticationSuccess;
import db.service.crypto.security.oauth2.CustomOAuth2UserService;
import db.service.crypto.security.jwt.JwtConfigurer;
import db.service.crypto.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    final JwtTokenProvider jwtTokenProvider;

    final CustomOAuth2UserService oauthUserService;

    final AuthenticationSuccess authSuccess;

    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
    private static final String AUTH_ENDPOINT = "/api/v1/auth/**";
    private static final String USER_ENDPOINT = "/api/v1/users/**";


    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomOAuth2UserService oauthUserService, AuthenticationSuccess authSuccess) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oauthUserService = oauthUserService;
        this.authSuccess = authSuccess;
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_ENDPOINT).permitAll()
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .antMatchers(USER_ENDPOINT).authenticated()
                .anyRequest().permitAll()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(oauthUserService)
                .and()
                .successHandler(authSuccess)
        ;
    }

}
