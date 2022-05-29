package strike.filesystem.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuppressWarnings("checkstyle:FinalLocalVariable")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditableEntity extends BaseEntity {

  private static final long serialVersionUID = -32L;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime creationDate;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime modificationDate;

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public LocalDateTime getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(final LocalDateTime modificationDate) {
    this.modificationDate = modificationDate;
  }
}
