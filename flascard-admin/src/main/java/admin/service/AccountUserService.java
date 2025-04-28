package admin.service;

import admin.dto.AccountUserDTO;
import admin.dto.CardSetDTO;
import admin.entity.AccountUser;


public interface AccountUserService {

    Iterable<AccountUserDTO> findAll();
    AccountUserDTO findAccountUserDTOById(Long id);
    AccountUser addAccountUser(AccountUser accountUser);
    AccountUserDTO addCardSetToAccountUser(Long id, CardSetDTO cardSetDTO);
}
