package com.game.rpc.generator;

import com.game.rpc.message.st.MsgId;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @author KING
 * @date 2023/01/06
 */
@Slf4j
public class HandlerServerGenerator extends HandlerClientGenerator {
    private static final String HANDLER_TEMPLATE_NAME = "template/HandlerServer.vm";
    private static final String SAVE_PATH = "Y:\\game\\gameServer\\server\\game-server\\src\\main\\java\\com\\game\\server\\handler";

//    private static final String SAVE_PATH = "Y:\\game\\gameServer\\server\\server-tool\\src\\main\\java\\com\\game\\rpc\\test\\";


    public void genHandler(List<MsgId.EN_MessageId> msgIdList) throws IOException {
        this.baseGenHandler(msgIdList,SAVE_PATH,HANDLER_TEMPLATE_NAME);
    }

}
