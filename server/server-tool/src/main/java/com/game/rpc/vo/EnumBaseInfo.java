package com.game.rpc.vo;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.game.rpc.RpcConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KING
 * @date 2023/01/03
 */
@Data
@Slf4j
@ToString
@NoArgsConstructor
public class EnumBaseInfo {
    private String name;
    private int value;
    private String valueStr;
    private String comment;

    private boolean canZero;

    private static final int ARR_LEN = 2;

    public EnumBaseInfo(String line) {
        String[] msgIdNameAndValueArr = line.split("=");
        this.name = msgIdNameAndValueArr[0].trim();

        String idAndCommentStr = msgIdNameAndValueArr[1];
        this.parseValueAndComment(idAndCommentStr);

        this.canZero = false;
    }

    private static final String LINE_END_FLAG = ";";

    private void parseValueAndComment(String idAndCommentStr) {
        String[] valueAndCommentArr = idAndCommentStr.split(RpcConstant.COMMENT_START_FLAG);
        String enumValueStr = valueAndCommentArr[0].trim();
        if (enumValueStr.endsWith(LINE_END_FLAG)) {
            enumValueStr = enumValueStr.substring(0, enumValueStr.length() - 1);
        }
        int enumValue = NumberUtil.parseInt(enumValueStr.trim());
        if (!canZero && enumValue == 0) {
            log.error("enum value error.canZero:{},value:{}", canZero, enumValueStr);
        }
        if (enumValue < 0 || enumValue > Short.MAX_VALUE) {
            log.error("enum value error.value:{}", enumValueStr);
            return;
        }

        this.value = enumValue;
        this.valueStr = enumValueStr;

        if (valueAndCommentArr.length == ARR_LEN) {
            this.comment = valueAndCommentArr[1].trim();
        }
    }

    public boolean check() {
        if (StrUtil.isEmpty(name)) {
            return false;
        }
        if (!canZero && value == 0) {
            return false;
        }
        return value >= 0;
    }

}
