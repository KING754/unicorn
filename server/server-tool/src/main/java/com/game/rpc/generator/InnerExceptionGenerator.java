package com.game.rpc.generator;

import cn.hutool.core.io.FileUtil;
import com.game.rpc.message.inner.InnerRPC;
import com.game.rpc.vo.ExceptionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KING
 * @date 2023/01/06
 */
@Slf4j
public class InnerExceptionGenerator extends ExceptionGenerator {
    private static final String HANDLER_TEMPLATE_NAME = "template/InnerException.vm";
    private static final String SAVE_PATH = "Y:\\game\\gameServer\\server\\server-exception\\src\\main\\java\\com\\game\\exception\\inner\\";


    public void genException() throws IOException {
        InnerRPC.EN_INNER_CODE[] msgIds = InnerRPC.EN_INNER_CODE.values();
        List<InnerRPC.EN_INNER_CODE> codeList = new ArrayList<>();
        for (InnerRPC.EN_INNER_CODE code:msgIds) {
            if(!code.equals(InnerRPC.EN_INNER_CODE.UNRECOGNIZED) && code.getNumber() >0){
                codeList.add(code);
            }
        }

        this.genInnerException(codeList);
    }

    public void genInnerException(List<InnerRPC.EN_INNER_CODE> codeList) throws IOException {
        if (codeList == null || codeList.isEmpty()) {
            return;
        }

        for (InnerRPC.EN_INNER_CODE code : codeList) {
            String tempPath = SAVE_PATH;
            ExceptionInfo exceptionInfo = this.buildExceptionFromCode(code);
            if (exceptionInfo == null) {
                continue;
            }
            VelocityContext ctx = new VelocityContext();
            ctx.put("ex", exceptionInfo);

            if(!FileUtil.exist(tempPath)){
                FileUtil.mkdir(tempPath);
            }

            String fileFullPath = tempPath + exceptionInfo.getName() + ".java";
            if (!FileUtil.exist(fileFullPath)) {
                super.writeFile(ctx, HANDLER_TEMPLATE_NAME, fileFullPath);
            } else {
                log.warn("{} exist!!!", exceptionInfo.getName());
            }
        }
    }

    protected ExceptionInfo buildExceptionFromCode(InnerRPC.EN_INNER_CODE code) {
        String msgName = super.getExceptionName(code.name());
        return new ExceptionInfo(msgName,code.name());
    }

//    private String getExceptionName(String codeName) {
//        String exceptionName = codeName;
//        exceptionName = exceptionName.replaceFirst("E_", "");
//        exceptionName = exceptionName.replace("_ERROR","");
//        exceptionName = exceptionName.toLowerCase();
//        List<String> nameParts = StrUtil.split(exceptionName, "_");
//        StringBuffer nameBuffer = new StringBuffer();
//        for (String namePart : nameParts) {
//            namePart = StrUtil.upperFirst(namePart);
//            nameBuffer.append(namePart);
//        }
//        nameBuffer.append("Exception");
//        return nameBuffer.toString();
//    }

}
