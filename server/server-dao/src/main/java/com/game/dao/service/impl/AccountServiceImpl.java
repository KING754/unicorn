package com.game.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.game.dao.domain.Account;
import com.game.dao.mapper.AccountMapper;
import com.game.dao.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author w101
 * @description 针对表【account】的数据库操作Service实现
 * @createDate 2023-01-29 11:26:37
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    private final AccountMapper accountMapper;

    public Account getAccountByName(String name) {
        Account account = accountMapper.queryByAccountName(name);
        System.out.println(account);

        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_name", name);
        Account account1 = accountMapper.selectOne(queryWrapper);
        return account1;
    }

    public int insertAccount(Account account) {
        return accountMapper.insert(account);
    }
}




