package com.game.server.validator;

import cn.hutool.core.util.StrUtil;

/**
 * @author KING
 * @date 2023/01/31
 */
public class BaseValidator {
    /**
     * 字符串长度验证
     * @param str input str
     * @param minLen >= minLen
     * @param maxLen <= maxLen
     * @return
     */
    public boolean validatorStrLen(String str,int minLen,int maxLen){
        if(StrUtil.isEmpty(str)){
            if(minLen == 0){
                return true;
            }else{
                return false;
            }
        }

        int strLen = str.length();
        return (strLen >= minLen && strLen <= maxLen);
    }
}
