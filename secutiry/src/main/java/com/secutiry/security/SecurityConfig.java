package com.secutiry.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.secutiry.security.filters.CustomAuthenticationFilter;
import com.secutiry.security.filters.CustomAuthorizationFilter;
import com.secutiry.services.UserService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(super.authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/login", "/api/token/refresh", "api/ping").permitAll();

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users", "/api/roles", "/api/users/**/add-role").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/roles").hasAuthority("ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.GET ,"/api/users/**").hasAnyAuthority("USER", "ADMIN");

        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(customAuthenticationFilter);
    }
    
}
