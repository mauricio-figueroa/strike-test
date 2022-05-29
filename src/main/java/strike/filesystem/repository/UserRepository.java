package strike.filesystem.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import strike.filesystem.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByUsername(final String username);
}
