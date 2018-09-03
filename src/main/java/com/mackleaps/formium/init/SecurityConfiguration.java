package com.mackleaps.formium.init;

import com.mackleaps.formium.security.utils.CustomJdbcTokenRepositoryImpl;
import com.mackleaps.formium.security.utils.CustomPassEncoder;
import com.mackleaps.formium.service.auth.AuthServiceInterface;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SecurityConfiguration.class);
    private AuthServiceInterface authService;
    private AuthenticationSuccessHandler successHandler;
    private DataSource dataSource;

    @Autowired
    public SecurityConfiguration (AuthenticationSuccessHandler successHandler,
                                  AuthServiceInterface authService,
                                  @Qualifier("dataSource") DataSource dataSource) {

        this.authService = authService;
        this.successHandler = successHandler;
        this.dataSource = dataSource;
    }

    private static final String[] AUTH_WHITELIST = {
            "/resources/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                .antMatcher("/**")
                .authorizeRequests()
                    .antMatchers("/api/**","/register*","/password/recovery*","/index")
                        .permitAll()
                    .antMatchers("/system/**")
                        .hasRole("RESEARCHER")
                    .antMatchers("/answer/**")
                        .hasRole("EMPLOYEE")
                .and()
	                .formLogin()
	                    .loginPage("/login")
	                    .failureUrl("/login?error=true")
	                    .successHandler(successHandler)
	                    .usernameParameter("email").passwordParameter("password")
	                    .permitAll()

                .and()
                    .logout()
                    .logoutSuccessUrl("/login?logout=true")
                    .permitAll()
                    .invalidateHttpSession(true)

                .and()
	                .rememberMe()
	                    .rememberMeParameter("remember")
	                    .tokenRepository(persistenceTokenBean())
	                    .tokenValiditySeconds(60*60*24*7)

                .and()
	                .sessionManagement()
	                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	                    .maximumSessions(1)
	                    .expiredUrl("/login")
	                .and()
	                    .sessionFixation().none()
                    
                .and()
                	.exceptionHandling().accessDeniedPage("/403")
                	
                .and()
                	.csrf();
        }

    @Bean
    public PersistentTokenRepository persistenceTokenBean() {
    	CustomJdbcTokenRepositoryImpl repoToken = new CustomJdbcTokenRepositoryImpl();
    	repoToken.setCreateTableOnStartup(true);
    	repoToken.setDataSource(dataSource);
    	return repoToken;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPassEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthProv = new DaoAuthenticationProvider();
        daoAuthProv.setUserDetailsService(authService);
        daoAuthProv.setPasswordEncoder(passwordEncoder());
        return daoAuthProv;
    }

}
