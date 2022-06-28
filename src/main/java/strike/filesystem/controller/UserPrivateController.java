package strike.filesystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strike.filesystem.model.User;
import strike.filesystem.service.auth.UserAuthenticationService;

@RestController
@RequestMapping("/user")
public class UserPrivateController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserPrivateController.class);

  private final UserAuthenticationService authentication;

  @Autowired
  public UserPrivateController(final UserAuthenticationService authentication) {
    this.authentication = authentication;
  }

  @GetMapping("/current")
  public User getCurrent(@AuthenticationPrincipal final User user) {
    return user;
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(@AuthenticationPrincipal final User user) {

    LOGGER.info("Logout user {}", user.getId());

    authentication.logout(user);
    return ResponseEntity.ok().build();
  }
}
