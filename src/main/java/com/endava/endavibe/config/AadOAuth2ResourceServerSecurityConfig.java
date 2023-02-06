package com.endava.endavibe.config;

import com.azure.spring.cloud.autoconfigure.aad.AadResourceServerWebSecurityConfigurerAdapter;
import com.endava.endavibe.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AadOAuth2ResourceServerSecurityConfig extends AadResourceServerWebSecurityConfigurerAdapter {
    private final AppUserService appUserService;

    @Value("${client.url}")
    private String clientUrl;

    private static final String[] WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/login**",
            "/error**"
    };

    private static final List<String> METHODS_ALLOWED = List.of(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name()
    );

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/subscriber/unsubscribe/**").anonymous()
                .and().authorizeRequests()
                .antMatchers("/user/appUsers/totalNumber").anonymous()
                .and().authorizeRequests()
                .antMatchers(WHITELIST).permitAll()
                 /* *
                 *  TODO Role Hierarchy ADMIN -> MANAGER -> SUBSCRIBER
                 *  TODO See if is possible to assign privileges direct from the database
                 * */
                 /* Project */
                .antMatchers(HttpMethod.GET,"/project/**").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.PATCH,"/project/**").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.POST,"/project/**").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.PUT,"/project/**").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.DELETE,"/project/**").hasAnyAuthority("ADMIN","MANAGER")
                /* Question */
                .antMatchers(HttpMethod.GET,"/question/**").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.PATCH,"/question/**").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.POST,"/question/**").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.PUT,"/question/**").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.DELETE,"/question/**").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.DELETE,"/question/**").hasAnyAuthority("ADMIN","MANAGER")
                /* Reporting */
                .antMatchers(HttpMethod.GET,"/reporting/**").hasAnyAuthority("ADMIN","MANAGER","SUBSCRIBER")
                /* AppUser */
                .antMatchers(HttpMethod.GET,"/user/{uuid}").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.GET,"/user/project/{projectUuid}").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers(HttpMethod.PATCH,"/user/{uuid}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"/user/add").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/user/appUsers").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/user/appUsers/{uuid}").hasAuthority("ADMIN")
                /* Quiz */
                .antMatchers(HttpMethod.GET,"/quiz/{uuid}").hasAuthority("TEAM_MEMBER")
                .antMatchers(HttpMethod.POST,"/quiz").hasAuthority("TEAM_MEMBER")
                .mvcMatchers("/**").authenticated()
                .and()
                .authorizeRequests()
                .and()
                .authorizeRequests(requests -> requests.anyRequest().authenticated())
                .addFilterBefore(accessTokenFilter(appUserService),  BasicAuthenticationFilter.class);
    }


    @Bean
    AccessTokenFilter accessTokenFilter(AppUserService appUserService) {
        return new AccessTokenFilter(appUserService);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedMethods(METHODS_ALLOWED);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
