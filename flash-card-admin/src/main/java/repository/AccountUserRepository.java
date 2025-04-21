package repository;

import org.springframework.data.repository.CrudRepository;

import entity.AccountUser;

public interface AccountUserRepository extends CrudRepository<AccountUser, Long>{
    AccountUser findByUsername(String username);
}
