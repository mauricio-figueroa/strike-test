package strike.filesystem.configuration;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import service.auth.UserAuthenticationService;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  public final UserAuthenticationService userAuthenticationService;

  @Autowired
  public TokenAuthenticationProvider(
      @Lazy final UserAuthenticationService userAuthenticationService) {
    this.userAuthenticationService = userAuthenticationService;
  }

  @Override
  protected void additionalAuthenticationChecks(
      final UserDetails userDetails,
      final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
    // Nothing to do
  }

  @Override
  protected UserDetails retrieveUser(
      final String username, final UsernamePasswordAuthenticationToken authentication) {
    final Object token = authentication.getCredentials();
    return Optional.ofNullable(token)
        .map(String::valueOf)
        .flatMap(userAuthenticationService::findByToken)
        .orElseThrow(
            () ->
                new UsernameNotFoundException(
                    "Cannot find user with authentication token=" + token));
  }
}
