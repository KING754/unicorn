package com.game.exception.logic;

import com.game.exception.LogicException;
import com.game.rpc.message.inner.InnerRPC;
import com.game.rpc.message.st.Code;

/**
* @author KING
* @date 2023/2/16 19:30:46
*/
public class UserConnectionNotFoundException extends LogicException {
    public UserConnectionNotFoundException(String message) {
        super(InnerRPC.EN_INNER_CODE.E_INNER_SUSS, Code.En_Code.E_USER_CONNECTION_NOT_FOUND, message);
    }
}
