package com.game.rpc.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.game.rpc.message.st.Code;
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
public class ExceptionGenerator extends BaseGenerator {
    private static final String HANDLER_TEMPLATE_NAME = "template/Exception.vm";
    private static final String SAVE_PATH = "Y:\\game\\gameServer\\server\\server-exception\\src\\main\\java\\com\\game\\exception\\logic\\";


    public void genException() throws IOException {
        Code.En_Code[] msgIds = Code.En_Code.values();
        List<Code.En_Code> codeList = new ArrayList<>();
        for (Code.En_Code code:msgIds) {
            if(!code.equals(Code.En_Code.UNRECOGNIZED) && code.getNumber() >0){
                codeList.add(code);
            }
        }

        this.genException(codeList);
    }

    protected void genException(List<Code.En_Code> codeList) throws IOException {
        if (codeList == null || codeList.isEmpty()) {
            return;
        }

        for (Code.En_Code code : codeList) {
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

    protected ExceptionInfo buildExceptionFromCode(Code.En_Code code) {
        String msgName = this.getExceptionName(code.name());
        return new ExceptionInfo(msgName,code.name());
    }

    protected String getExceptionName(String codeName) {
        String exceptionName = codeName;
        exceptionName = exceptionName.replaceFirst("E_", "");
        exceptionName = exceptionName.replace("_ERROR","");
        exceptionName = exceptionName.toLowerCase();
        List<String> nameParts = StrUtil.split(exceptionName, "_");
        StringBuffer nameBuffer = new StringBuffer();
        for (String namePart : nameParts) {
            namePart = StrUtil.upperFirst(namePart);
            nameBuffer.append(namePart);
        }
        nameBuffer.append("Exception");
        return nameBuffer.toString();
    }

}
