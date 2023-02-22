package com.game.rpc.generator;

import cn.hutool.core.util.StrUtil;
import com.game.rpc.vo.EnumStructInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @author KING
 * @date 2023/01/03
 */
@Slf4j
public class EnumShortStructGenerator extends EnumGenerator {
    private String name;
    private String comment;

    public EnumStructInfo getEnumStruct(List<String> oneEnumStructAllLines, String rpcFilePath) throws IOException {
        String name = super.parseStructName(oneEnumStructAllLines.get(0));
        if(StrUtil.isEmpty(name)){
            return null;
        }

        String comment = super.parseLineComment(oneEnumStructAllLines.get(0));
        this.name = name;
        this.comment = comment;
        super.setEnumValueType(EnumGenerator.VALUE_SHORT_TYPE);
        boolean isOk = super.dealAllEnumLines(rpcFilePath, oneEnumStructAllLines);
        if (!isOk) {
            return null;
        }
        EnumStructInfo enumStructInfo = new EnumStructInfo();
        enumStructInfo.setName(this.name);
        enumStructInfo.setComment(this.comment);
        enumStructInfo.setEnums(super.getShortEnumInfo());
        enumStructInfo.setEnumType(true);

        return enumStructInfo;

//        this.writeMsgIdFile(String name);
    }


//    private void writeMsgIdFile() throws IOException {
//        Collection<ValueShortEnumInfo> msgIdInfos = super.getShortEnumInfo();
//        VelocityContext ctx = new VelocityContext();
//        ctx.put("name",this.name);
//        ctx.put("comment",this.comment);
//        ctx.put("msgIdList", msgIdInfos);
//
//        super.writeFile(ctx, CODE_TEMPLATE_NAME, MSG_ID_SAVE_FILE_NAME);
//    }


}
