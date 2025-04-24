package admin.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import admin.entity.AccountUser;

public interface AccountUserRepository extends CrudRepository<AccountUser, Long>{
    Optional<AccountUser> findByUsername(String username);
}
