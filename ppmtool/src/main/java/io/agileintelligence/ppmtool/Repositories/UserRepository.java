package io.agileintelligence.ppmtool.Repositories;

import io.agileintelligence.ppmtool.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User getById(Long id);
}
