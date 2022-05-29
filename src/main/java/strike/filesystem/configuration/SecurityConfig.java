package strike.filesystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .antMatchers("/h2-console**")
        .permitAll()
        .antMatchers("/h2-console")
        .permitAll()
        .antMatchers("/api/**")
        .authenticated()
        .and()
        .httpBasic(withDefaults())
        .csrf()
        .disable()
        .headers()
        .frameOptions()
        .disable();
    return http.build();
  }
}
