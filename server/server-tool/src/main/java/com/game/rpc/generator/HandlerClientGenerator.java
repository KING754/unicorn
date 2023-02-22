package com.game.rpc.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.game.rpc.message.st.MsgId;
import com.game.rpc.vo.HandlerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author KING
 * @date 2023/01/06
 */
@Slf4j
public class HandlerClientGenerator extends BaseGenerator {
    private static final String HANDLER_TEMPLATE_NAME = "template/HandlerClient.vm";
    private static final String SAVE_PATH = "Y:\\game\\gameServer\\server\\test-client\\src\\main\\java\\com\\game\\client\\handler";

    //    private static final String SAVE_PATH = "Y:\\game\\gameServer\\server\\server-tool\\src\\main\\java\\com\\game\\rpc\\test\\";
    protected Set<Class<?>> clsSet;

    public HandlerClientGenerator() {
        clsSet = ClassUtil.scanPackage();
    }

    public void genHandler(List<MsgId.EN_MessageId> msgIdList) throws IOException {
        this.baseGenHandler(msgIdList, SAVE_PATH, HANDLER_TEMPLATE_NAME);
    }

    protected void baseGenHandler(List<MsgId.EN_MessageId> msgIdList, String savePath, String templateName) throws IOException {
        if (msgIdList == null || msgIdList.isEmpty()) {
            return;
        }

        for (MsgId.EN_MessageId msgId : msgIdList) {
            String tempPath = savePath;
            HandlerInfo handler = this.buildHandlerFromMsgId(msgId);
            if (handler == null) {
                continue;
            }
            VelocityContext ctx = new VelocityContext();
            ctx.put("handler", handler);

            if(!StrUtil.isEmpty(handler.getPackageStr())){
                String packagePath = handler.getPackageStr().replace(".","\\");
                tempPath += packagePath;
            }
            tempPath += "\\";
            if(!FileUtil.exist(tempPath)){
                FileUtil.mkdir(tempPath);
            }

            String fileFullPath = tempPath + handler.getName() + ".java";
            if (!FileUtil.exist(fileFullPath)) {
                super.writeFile(ctx, templateName, fileFullPath);
            } else {
                log.warn("{} exist!!!", handler.getName());
            }
        }
    }

    protected HandlerInfo buildHandlerFromMsgId(MsgId.EN_MessageId msgId) {
        String msgName = this.getMsgName(msgId.name());

        String handlerName = this.getHandlerName(msgName);
        String reqType = this.getReqName(msgName);
        String respType = this.getRespName(msgName);

        Class reqCls = this.getClass(reqType);
        Class respCls = this.getClass(respType);
        String importObjStr = "";
        String reqTypeFull = "";
        String respTypeFull = "";
        String packageStr = "";

        if(reqCls != null){
            importObjStr = reqCls.getEnclosingClass().getName();
            reqTypeFull = reqCls.getEnclosingClass().getSimpleName()+"."+reqCls.getSimpleName();
            packageStr = reqCls.getPackageName();
            packageStr = packageStr.replace("com.game.rpc.message.st","");
        }else{
            log.warn("not found {}",reqType);
            reqType = "";
        }
        if(respCls != null){
            importObjStr = respCls.getEnclosingClass().getName();
            respTypeFull = respCls.getEnclosingClass().getSimpleName()+"."+respCls.getSimpleName();
            packageStr = respCls.getPackageName();
            packageStr = packageStr.replace("com.game.rpc.message.st","");
        }else{
            log.warn("not found {}",respType);
            respType = "";
        }


        return new HandlerInfo(handlerName, msgId.name(), reqType, respType, importObjStr, reqTypeFull, respTypeFull,packageStr);
    }

    private String getHandlerName(String msgIdName) {
        return msgIdName += "Handler";
    }

    private String getReqName(String msgIdName) {
        return msgIdName += "Req";
    }

    private String getRespName(String msgIdName) {
        return msgIdName += "Resp";
    }

    private String getMsgName(String msgIdName) {
        String handlerName = msgIdName;
        handlerName = handlerName.replaceFirst("E_", "");
        handlerName = handlerName.toLowerCase();
        List<String> nameParts = StrUtil.split(handlerName, "_");
        StringBuffer nameBuffer = new StringBuffer();
        for (String namePart : nameParts) {
            namePart = StrUtil.upperFirst(namePart);
            nameBuffer.append(namePart);
        }
        return nameBuffer.toString();
    }

    private Class getClass(String classSimpleName) {
        if (clsSet == null || clsSet.isEmpty()) {
            return null;
        }
        for (Class cls : clsSet) {
            String name = cls.getName();
            if(!name.startsWith("com.game.rpc")){
                continue;
            }
            String simpleName = cls.getSimpleName();
            if(simpleName.equals(classSimpleName)){
                return cls;
            }
        }
        return null;
    }
}
