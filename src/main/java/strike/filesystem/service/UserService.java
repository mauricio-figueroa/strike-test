package strike.filesystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

import strike.filesystem.exception.UserAlreadyExistException;
import strike.filesystem.model.User;

public interface UserService extends UserDetailsService {

  User save(final String username, final String password) throws UserAlreadyExistException;

  Optional<User> findByUsername(final String username);

  List<User> findByUsernames(final List<String> usernames);
}
