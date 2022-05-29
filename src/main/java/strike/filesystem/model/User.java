package strike.filesystem.model;

import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User extends BaseAuditableEntity implements UserDetails {

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "app_user_role", nullable = false)
  @Enumerated(EnumType.STRING)
  private AppUserRole appUserRole;

  @Column(name = "locked", nullable = false)
  private Boolean locked;

  @Column(name = "enabled", nullable = false)
  private Boolean enabled;

  public User(final String username, final String password, final AppUserRole appUserRole, final Boolean locked, final Boolean enabled) {
    this.username = username;
    this.password = password;
    this.appUserRole = appUserRole;
    this.locked = locked;
    this.enabled = enabled;
  }

  public User() {
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
    return Collections.singleton(authority);
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
