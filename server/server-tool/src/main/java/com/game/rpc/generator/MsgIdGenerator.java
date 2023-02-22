package com.game.rpc.generator;

import com.game.rpc.RpcConstant;
import com.game.rpc.vo.ValueShortEnumInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.util.Collection;

/**
 * @author KING
 * @date 2023/01/03
 */
@Slf4j
public class MsgIdGenerator extends EnumGenerator {
    public static final String MSG_ID_FILE_NAME = "msgId.txt";
    private static final String MSG_ID_SAVE_FILE_NAME = "Y:\\game\\gameServer\\server\\server-rpc-model\\src\\main\\java\\com\\game\\rpc\\message\\MsgId.java";
    private static final String CODE_TEMPLATE_NAME = "template/MsgId.vm";

    public void genMsgId() throws IOException {
        super.setEnumValueType(EnumGenerator.VALUE_SHORT_TYPE);
        String rpcFilePath = RpcConstant.RPC_BASE_FILE_PATH + MSG_ID_FILE_NAME;

        log.info("deal:{}",rpcFilePath);
        boolean isOk = super.readEnumFile(rpcFilePath);
        if (!isOk) {
            return;
        }
        this.writeMsgIdFile();
    }


    private void writeMsgIdFile() throws IOException {
        Collection<ValueShortEnumInfo> msgIdInfos = super.getShortEnumInfo();
        VelocityContext ctx = new VelocityContext();
        ctx.put("msgIdList", msgIdInfos);

        super.writeFile(ctx, CODE_TEMPLATE_NAME, MSG_ID_SAVE_FILE_NAME);
    }




}
