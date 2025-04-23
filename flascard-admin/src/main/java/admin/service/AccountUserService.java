package admin.service;

import java.util.List;

import admin.dto.AccountUserDTO;
import admin.entity.AccountUser;

public interface AccountUserService {

    Iterable<AccountUserDTO> findAll();
    AccountUser findAccountUserById(String id);
}
