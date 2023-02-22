package com.game.dao.service;

import com.game.dao.domain.Account;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author w101
* @description 针对表【account】的数据库操作Service
* @createDate 2023-01-29 11:26:37
*/
public interface AccountService extends IService<Account> {
    public Account getAccountByName(String name);

    public int insertAccount(Account account);
}
