package strike.filesystem.service.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import strike.filesystem.service.UserService;
import strike.filesystem.model.User;

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

  @Override
  public Optional<String> login(final String username, final String password) {
    final Optional<User> userOpt = userService.findByUsername(username);

    userOpt.filter(
        user -> Objects.equals(bCryptPasswordEncoder.encode(password), user.getPassword()));
    final Optional<String> response =
        userOpt.map(user -> tokenService.expiring(ImmutableMap.of("username", username)));

    CURRENT_USERS.put(username, response.get());
    return response;
  }

  @Override
  public Optional<User> findByToken(final String token) {

    final Optional<User> usernameOpt = Optional.of(tokenService.verify(token))
            .map(map -> map.get("username"))
            .flatMap(userService::findByUsername);

    if(usernameOpt.isPresent()){
      final User user = usernameOpt.get();
      if (CURRENT_USERS.get(user.getUsername())!= null){
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
