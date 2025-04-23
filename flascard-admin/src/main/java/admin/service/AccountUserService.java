package admin.service;

import admin.dto.AccountUserDTO;
import admin.entity.AccountUser;


public interface AccountUserService {

    Iterable<AccountUserDTO> findAll();
    AccountUserDTO findAccountUserById(String id);
    AccountUser addAccountUser(AccountUser accountUser);
}
