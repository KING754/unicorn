package com.game.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.game.dao.domain.Player;

import java.util.List;

/**
* @author w101
* @description 针对表【player】的数据库操作Service
* @createDate 2023-01-29 11:26:37
*/
public interface PlayerService extends IService<Player> {
    public List<Player> queryPlayersByAccountId(String accountId);

    public List<Player> queryPlayerByName(String name);

    public int insert(Player player);
}
