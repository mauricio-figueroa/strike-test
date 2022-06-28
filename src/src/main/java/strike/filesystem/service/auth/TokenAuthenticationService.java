package strike.filesystem.service.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import strike.filesystem.exception.BusinessException;
import strike.filesystem.exception.PasswordDontMatchException;
import strike.filesystem.exception.UserNotFoundException;
import strike.filesystem.model.User;
import strike.filesystem.service.UserService;

@Service
public class TokenAuthenticationService implements UserAuthenticationService {

  private final TokenService tokenService;
  private final UserService userService;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  private static Map<String, String> CURRENT_USERS = new HashMap<>();

  @Autowired
  public TokenAuthenticationService(
      final TokenService tokenService,
      final UserService users,
      final BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.tokenService = tokenService;
    this.userService = users;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public String login(final String username, final String password) throws BusinessException {
    final Optional<User> userOpt = userService.findByUsername(username);

    if (userOpt.isEmpty()) {
      throw UserNotFoundException.create();
    }

    User user = userOpt.get();

    if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
      throw PasswordDontMatchException.create();
    }

    final String token = tokenService.expiring(ImmutableMap.of("username", username));

    CURRENT_USERS.put(username, token);
    return token;
  }

  @Override
  public Optional<User> findByToken(final String token) {

    final Optional<User> usernameOpt =
        Optional.of(tokenService.verify(token))
            .map(map -> map.get("username"))
            .flatMap(userService::findByUsername);

    if (usernameOpt.isPresent()) {
      final User user = usernameOpt.get();
      if (CURRENT_USERS.get(user.getUsername()) != null) {
        return usernameOpt;
      }
    }

    return Optional.empty();
  }

  @Override
  public void logout(final User user) {
    CURRENT_USERS.remove(user.getUsername());
  }
}
