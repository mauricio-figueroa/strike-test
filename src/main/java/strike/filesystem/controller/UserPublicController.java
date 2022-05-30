package strike.filesystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import strike.filesystem.exception.UserAlreadyExistException;
import service.UserService;
import service.auth.UserAuthenticationService;

@RestController
public class UserPublicController {

  private final UserService userService;
  private final UserAuthenticationService authentication;

  @Autowired
  public UserPublicController(
      final UserService userService, final UserAuthenticationService authentication) {
    this.userService = userService;
    this.authentication = authentication;
  }

  @PostMapping("/public/users/register")
  public @ResponseBody String register(
      @RequestParam("username") final String username,
      @RequestParam("password") final String password)
      throws UserAlreadyExistException {
    userService.save(username, password);

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
}
