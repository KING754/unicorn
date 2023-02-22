package com.game.server.validator;

import com.game.constant.ServerConstant;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/01/31
 */
@Component
public class ValidatorAccount extends BaseValidator{
    public boolean validatorAccountName(String name){
        return super.validatorStrLen(name, ServerConstant.ACCOUNT_NAME_MIN_LEN,ServerConstant.ACCOUNT_NAME_MAX_LEN);
    }

    public boolean validatorAccountPwd(String password){
        return super.validatorStrLen(password, ServerConstant.ACCOUNT_PWD_MIN_LEN,ServerConstant.ACCOUNT_PWD_MAX_LEN);
    }

    public boolean validatorPlayerName(String playerName){
        return super.validatorStrLen(playerName, ServerConstant.PLAYER_NAME_MIN_LEN,ServerConstant.PLAYER_NAME_MAX_LEN);
    }
}
