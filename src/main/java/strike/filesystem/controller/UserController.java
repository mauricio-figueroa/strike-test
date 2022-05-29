package strike.filesystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import strike.filesystem.model.User;
import strike.filesystem.service.UserService;
import strike.filesystem.service.auth.UserAuthenticationService;

@RestController
public class UserController {

  private final UserService userService;
  private final UserAuthenticationService authentication;

  @Autowired
  public UserController(
      final UserService userService, final UserAuthenticationService authentication) {
    this.userService = userService;
    this.authentication = authentication;
  }

  @PostMapping("/public/users/register")
  public @ResponseBody String register(
      @RequestParam("username") final String username,
      @RequestParam("password") final String password) {
    final User user = userService.save(username, password);

    return login(username, password);
  }

  @PostMapping("/public/users/login")
  public @ResponseBody String login(
      @RequestParam("username") final String username,
      @RequestParam("password") final String password) {
    return authentication
        .login(username, password)
        .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
  }

  @GetMapping("/private/current")
  public User getCurrent(@AuthenticationPrincipal final User user) {
    return user;
  }

  @GetMapping("/private/logout")
  public ResponseEntity<?> logout(@AuthenticationPrincipal final User user) {
    authentication.logout(user);
    return ResponseEntity.ok().build();
  }
}
