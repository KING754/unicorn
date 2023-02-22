package com.game.server.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.game.constant.NettyConstant;
import com.game.constant.ServerConstant;
import com.game.dao.domain.Account;
import com.game.dao.domain.Player;
import com.game.dao.service.AccountService;
import com.game.dao.service.PlayerService;
import com.game.exception.logic.AccountNameDuplicateException;
import com.game.exception.logic.AccountNameException;
import com.game.exception.logic.AccountNotExistException;
import com.game.exception.logic.DbException;
import com.game.exception.logic.PasswordException;
import com.game.exception.logic.SystemException;
import com.game.rpc.message.st.common.Common;
import com.game.server.cache.ClientChannelManager;
import com.game.server.objtrans.PlayerAboutTransfer;
import com.game.server.validator.ValidatorAccount;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KING
 * @date 2023/01/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserService {
    public static AtomicInteger atomicInteger = new AtomicInteger(1);
    private final AccountService accountService;
    private final PlayerService playerService;
    private final ValidatorAccount accountValidator;
    private final ClientChannelManager channelManager;

    public boolean createAccount(ChannelHandlerContext ctx,String accountName, String password){
        Account account = accountService.getAccountByName(accountName);
        if(account != null){
            throw new AccountNameDuplicateException("Create Fail.");
        }
        boolean checkRs = accountValidator.validatorAccountName(accountName);
        if(!checkRs){
            throw new AccountNameException("Create Fail");
        }
        checkRs = accountValidator.validatorAccountPwd(password);
        if(!checkRs){
            throw new PasswordException("Create Fail");
        }

        Account newAccount = this.getNewAccount(accountName,password);
        try{
            int effectNum = accountService.insertAccount(newAccount);
            if(effectNum == 1){
                channelManager.addAccountConnection(newAccount.getId(),ctx);
                return true;
            }else{
                log.error("insert new account but fail.account:{},pwd:{},effectNum:{}",account,password,effectNum);
                throw new DbException("Create Fail");
            }
        }catch (Exception e){
            log.error("insert new account have error.account:{},pwd:{}",account,password,e);
            throw new SystemException("Create Fail");
        }
    }

    public List<Common.Player> login(ChannelHandlerContext ctx, String accountName, String password) {
        Account account = accountService.getAccountByName(accountName);
        if(account == null){
            throw new AccountNotExistException("Login Fail.");
        }
        String pass = account.getPassword();
        String inputPassMd5 = MD5.create().digestHex(password);
        if(!pass.equals(inputPassMd5)){
            throw new PasswordException("Login Fail.");
        }
        channelManager.addAccountConnection(account.getId(),ctx);
        List<Player> players = playerService.queryPlayersByAccountId(account.getId());
        if(players != null && !players.isEmpty()){
            List<Common.Player> pbPlayerList = PlayerAboutTransfer.INSTANCE.dbPlayerListToPbList(players);
            channelManager.addPlayerConnection(players.get(0).getId(),ctx);
            return pbPlayerList;
        }else {
            return null;
        }
    }



    public Common.Player createPlayer(ChannelHandlerContext ctx,String playerName){
        String accountId = channelManager.getAccountId(ctx);
        if(StrUtil.isEmpty(accountId)){
            throw new SystemException("not found channel accountId");
        }

        List<Player> players = playerService.queryPlayersByAccountId(accountId);
        if(players != null && !players.isEmpty()){          //allow one player
            throw new AccountNameDuplicateException("have.");
        }

        boolean checkRs = accountValidator.validatorPlayerName(playerName);
        if(!checkRs){
            throw new AccountNameException("Create Player Fail");
        }

        players = playerService.queryPlayerByName(playerName);
        if(players != null && !players.isEmpty()){          //allow one player
            throw new AccountNameDuplicateException("have.");
        }

        Player player = this.getNewPlayer(playerName,accountId);
        try{
            int effectNum = playerService.insert(player);
            if(effectNum == 1){
                Common.Player pbPlayer = PlayerAboutTransfer.INSTANCE.dbPlayerToPb(player);
                channelManager.addPlayerConnection(player.getId(),ctx);
                return pbPlayer;
            }else{
                log.error("insert new player but fail.effectNum:{},player:{}",effectNum,player);
                throw new DbException("Create Fail");
            }
        }catch (Exception e){
            log.error("insert new player but fail.player:{}",player,e);
            throw new SystemException("Create Fail");
        }
    }



    ///////////////////////////////////////private//////////////////////////////////
    private Account getNewAccount(String name,String password){
        Account newDbAccount = new Account();
        newDbAccount.setId(atomicInteger.getAndIncrement()+"");
        newDbAccount.setAccountName(name);
        newDbAccount.setPassword(MD5.create().digestHex(password, NettyConstant.DEFAULT_STRING_CHARSET_NAME));
        newDbAccount.setCreateTime(new Date());
        newDbAccount.setLastUpdate(new Date());

        return newDbAccount;
    }


    private Player getNewPlayer(String name,String accountId){
        Player player = new Player();
        player.setId(atomicInteger.getAndIncrement()+"");
        player.setAccountId(accountId);
        player.setName(name);
        player.setLevel(ServerConstant.PLAYER_INIT_LEVEL);
        player.setGold(ServerConstant.PLAYER_INIT_GOLD);
        player.setCreateTime(new Date());
        player.setLastUpdate(new Date());

        return player;
    }
}
