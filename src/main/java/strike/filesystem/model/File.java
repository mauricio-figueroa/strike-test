package strike.filesystem.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file")
public class File extends BaseAuditableEntity {

  @OneToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(name = "name")
  private String name;

  @Lob
  @Column(name = "file_blob", columnDefinition = "BLOB")
  private byte[] file;

  @ManyToMany(cascade = CascadeType.MERGE)
  @JoinTable(
      name = "file_user",
      joinColumns = {@JoinColumn(name = "file_id")},
      inverseJoinColumns = {@JoinColumn(name = "user_id")})
  private Set<User> allowedUsers = new HashSet<>();

  public File(final User user, final String name, final byte[] file) {
    this.user = user;
    this.name = name;
    this.file = file;
  }

  public File() {

  }

  public void addAllowedUsers(final User user) {
    if (this.allowedUsers == null) {
      allowedUsers = new HashSet<>();
    }
    allowedUsers.add(user);
  }
}
