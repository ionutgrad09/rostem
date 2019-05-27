package rostem.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import rostem.configuration.LoginHandlers.LoginAccessDeniedHandler;
import rostem.configuration.LoginHandlers.LoginFailureHanlder;
import rostem.configuration.LoginHandlers.LoginSuccessHandler;
import rostem.configuration.LoginHandlers.RestAuthenticationEntryPoint;
import rostem.service.authentication.AuthenticationService;

@Configuration
@EnableWebSecurity
@ComponentScan("rostem")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Callback for denied login
    private final LoginAccessDeniedHandler loginAccessDeniedHandler;

    // Callback for successful login
    private final LoginSuccessHandler successHandler;

    // Callback for failed login
    private final LoginFailureHanlder failureHandler;

    // Callback for Unauthorized
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    // Logic of authenticating a user
    private final AuthenticationService authenticationService;

    @Autowired
    public WebSecurityConfig(LoginAccessDeniedHandler loginAccessDeniedHandler,
            LoginSuccessHandler successHandler, LoginFailureHanlder failureHandler,
            RestAuthenticationEntryPoint restAuthenticationEntryPoint, AuthenticationService authenticationService) {
        super();
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        this.loginAccessDeniedHandler = loginAccessDeniedHandler;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public BadRequestFilter badRequestFilter(ObjectMapper objectMapper) {
        return new BadRequestFilter(objectMapper);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authenticationService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors()
                .and()
                    .csrf()
                    .disable()
                    .authorizeRequests()
                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(loginAccessDeniedHandler)
                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                    .antMatchers("/rostem/swagger-ui.html").permitAll()
                    .antMatchers("/rostem/login").permitAll()
                    .antMatchers("/rostem/admin/*").hasAuthority("ROLE_ADMIN")
                    .antMatchers("/rostem/*").authenticated()
                .and()
                    .formLogin()
                    .successHandler(successHandler)
                    .failureHandler(failureHandler)
                .and()
                    .httpBasic()
                .and()
                    .logout()
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                    .sessionManagement()
                    .sessionFixation().newSession()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/invalidSession.html")
                    .maximumSessions(3)
                    .expiredUrl("/sessionExpired.html");
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration
                .setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "HEAD", "DELETE", "PATCH", "PUT", "TRACE"));
        configuration.setAllowedHeaders(
                Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Request-With", "X-CLIENT-ID",
                        "X-CLIENT-SECRET"));
                        configuration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
