package strike.filesystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import strike.filesystem.model.User;
import strike.filesystem.service.UserService;
import strike.filesystem.service.auth.UserAuthenticationService;

@RestController("/users")
public class UserPrivateController {

  private final UserService userService;
  private final UserAuthenticationService authentication;

  @Autowired
  public UserPrivateController(
      final UserService userService, final UserAuthenticationService authentication) {
    this.userService = userService;
    this.authentication = authentication;
  }

  @GetMapping("/current")
  public User getCurrent(@AuthenticationPrincipal final User user) {
    return user;
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(@AuthenticationPrincipal final User user) {
    authentication.logout(user);
    return ResponseEntity.ok().build();
  }
}
