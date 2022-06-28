package strike.filesystem.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod", "checkstyle:finallocalvariable"})
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity {

  private static final long serialVersionUID = -32L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version private Long version;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(final Long version) {
    this.version = version;
  }
}
