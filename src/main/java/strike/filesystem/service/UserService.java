package strike.filesystem.service;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

import strike.filesystem.model.User;

public interface UserService extends UserDetailsService {

  User save(final String username, final String password);

  Optional<User> findByUsername(final String username);
}