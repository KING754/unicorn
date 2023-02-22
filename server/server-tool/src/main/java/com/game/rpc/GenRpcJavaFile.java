package com.game.rpc;

import com.game.rpc.generator.ExceptionGenerator;
import com.game.rpc.generator.InnerExceptionGenerator;
import com.game.rpc.message.st.MsgId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KING
 * @date 2023/01/03
 */
public class GenRpcJavaFile {
    public static void main(String[] args) throws IOException {
//        MsgIdGenerator msgIdGenerator = new MsgIdGenerator();
//        msgIdGenerator.genMsgId();
//
//        new CodeGenerator().genCode();
//
//        StructGenerator structGenerator = new StructGenerator();
//        structGenerator.genAllStruct();
//        List<MsgId.EN_MessageId> msgIdList = GenRpcJavaFile.readAllMsgId();
//        new HandlerClientGenerator().genHandler(msgIdList);
//        new HandlerServerGenerator().genHandler(msgIdList);
        new ExceptionGenerator().genException();
        new InnerExceptionGenerator().genException();
    }


    private static List<MsgId.EN_MessageId> readAllMsgId(){
        MsgId.EN_MessageId[] msgIds = MsgId.EN_MessageId.values();
        List<MsgId.EN_MessageId> msgIdList = new ArrayList<>();
        for (MsgId.EN_MessageId msgId:msgIds) {
            if(!msgId.equals(MsgId.EN_MessageId.UNRECOGNIZED) && msgId.getNumber() >0){
               msgIdList.add(msgId);
            }
        }
        return msgIdList;
    }
}
