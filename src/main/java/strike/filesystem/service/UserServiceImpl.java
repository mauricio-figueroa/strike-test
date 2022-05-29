package strike.filesystem.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import strike.filesystem.model.AppUserRole;
import strike.filesystem.model.User;
import strike.filesystem.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  private static final String USER_NOT_FOUND_MSG = "User with email %s not found";

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserServiceImpl(
      final UserRepository userRepository, final BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
  }

  @Override
  public User save(final String username, final String password) {

    User user =
        new User(username, bCryptPasswordEncoder.encode(password), AppUserRole.USER, false, true);
    userRepository.save(user);
    return user;
  }

  @Override
  public Optional<User> findByUsername(final String username) {
    return userRepository.findByUsername(username);
  }
}
