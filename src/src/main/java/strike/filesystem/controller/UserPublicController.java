package strike.filesystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import strike.filesystem.dto.CreateOrLoginUserDTO;
import strike.filesystem.exception.BusinessException;
import strike.filesystem.exception.UserAlreadyExistException;
import strike.filesystem.service.UserService;
import strike.filesystem.service.auth.UserAuthenticationService;

@RestController
@RequestMapping("/public/user")
public class UserPublicController {

  private final UserService userService;
  private final UserAuthenticationService authentication;

  @Autowired
  public UserPublicController(
      final UserService userService, final UserAuthenticationService authentication) {
    this.userService = userService;
    this.authentication = authentication;
  }

  @PostMapping("/register")
  public @ResponseBody String register(@RequestBody CreateOrLoginUserDTO createOrLoginUserDTO)
          throws BusinessException {
    userService.save(createOrLoginUserDTO.getUsername(), createOrLoginUserDTO.getPassword());

    return login(createOrLoginUserDTO);
  }

  @PostMapping("/login")
  public @ResponseBody String login(@RequestBody CreateOrLoginUserDTO createOrLoginUserDTO) throws BusinessException {
    return authentication.login(createOrLoginUserDTO.getUsername(), createOrLoginUserDTO.getPassword());
  }
}
