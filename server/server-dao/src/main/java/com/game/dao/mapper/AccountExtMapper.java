package com.game.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.game.dao.domain.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author w101
* @description 针对表【account】的数据库操作Mapper
* @createDate 2023-01-29 11:26:37
* @Entity com.game.dao.domain.Account
*/
public interface AccountExtMapper extends BaseMapper<Account> {
    @Select("select * from account where account_name=#{accountName}")
    public Account queryByAccountName(@Param("accountName") String accountName);
}




