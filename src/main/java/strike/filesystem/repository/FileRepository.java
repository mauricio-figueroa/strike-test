package strike.filesystem.repository;

import org.springframework.data.repository.CrudRepository;

import strike.filesystem.model.File;

public interface FileRepository extends CrudRepository<File, Long> {}
