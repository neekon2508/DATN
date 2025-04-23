package admin.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import admin.dto.AccountUserDTO;
import admin.dto.CardSetDTO;
import admin.entity.AccountUser;

@Service
public class RestAccountUserService implements AccountUserService{

    private RestTemplate restTemplate;

    @Autowired
    public RestAccountUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${app.cors.origins}")
    private String corsOrigins;
    
    @Override
    public Iterable<AccountUserDTO> findAll() {
       return Arrays.asList(restTemplate.getForObject(corsOrigins+"/api/account_user/get_all", AccountUserDTO[].class));

    }

    @Override
    public AccountUserDTO findAccountUserById(String id) {
       return restTemplate.getForObject(corsOrigins+"/api/account_user/get/{id}", AccountUserDTO.class, id);
    }

    @Override
    public AccountUser addAccountUser(AccountUser accountUser) {
        return restTemplate.postForObject(corsOrigins+"/api/account_user/create", accountUser, AccountUser.class);
    }

}
