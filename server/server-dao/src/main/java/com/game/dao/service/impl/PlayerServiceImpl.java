package com.game.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.game.dao.domain.Player;
import com.game.dao.mapper.PlayerMapper;
import com.game.dao.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author w101
 * @description 针对表【player】的数据库操作Service实现
 * @createDate 2023-01-29 11:26:37
 */
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements PlayerService {
    private final PlayerMapper playerMapper;

    @Override
    public List<Player> queryPlayersByAccountId(String accountId) {
        QueryWrapper<Player> playerQueryWrapper = new QueryWrapper<>();
        playerQueryWrapper.eq("account_id",accountId);
        return list(playerQueryWrapper);
    }

    @Override
    public List<Player> queryPlayerByName(String name) {
        QueryWrapper<Player> playerQueryWrapper = new QueryWrapper<>();
        playerQueryWrapper.eq("name",name);
        return list(playerQueryWrapper);
    }

    @Override
    public int insert(Player player) {
       return playerMapper.insert(player);
    }
}




