package com.game.exception.logic;

import com.game.exception.LogicException;
import com.game.rpc.message.inner.InnerRPC;
import com.game.rpc.message.st.Code;

/**
* @author KING
* @date 2023/2/15 18:42:43
*/
public class PlayerNameDuplicateException extends LogicException {
    public PlayerNameDuplicateException(String message) {
        super(InnerRPC.EN_INNER_CODE.E_INNER_SUSS, Code.En_Code.E_PLAYER_NAME_DUPLICATE_ERROR, message);
    }
}
