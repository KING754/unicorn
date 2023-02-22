package com.game.rpc.generator;

import cn.hutool.core.util.StrUtil;
import com.game.rpc.vo.EnumBaseInfo;
import com.game.rpc.vo.ValueShortEnumInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author KING
 * @date 2023/01/04
 */
@Slf4j
@Data
public class EnumGenerator extends BaseGenerator {
    public static final int VALUE_SHORT_TYPE = 1;
    public static final int VALUE_INT_TYPE = 2;
    public HashMap<String, EnumBaseInfo> nameMap = new LinkedHashMap<>();
    public HashMap<Short, ValueShortEnumInfo> shortValueMap = new LinkedHashMap<>();
    public HashMap<Integer, EnumBaseInfo> integerValueMap = new LinkedHashMap<>();

    private int enumValueType;

    public boolean readEnumFile(String fullPathName) {
        List<String> enumContent = super.readEnumContent(fullPathName);
        return this.dealAllEnumLines(fullPathName, enumContent);
    }

    protected boolean dealAllEnumLines(String fullPathName, List<String> enumContent) {
        if (enumContent.isEmpty()) {
            log.error("{} enum content is empty.", fullPathName);
            return false;
        }

        for (String line : enumContent) {
            if (!this.parseLine(line)) {
                return false;
            }
        }
        return true;
    }

    private boolean parseLine(String lineStr) {
        lineStr = this.beforeParseLine(beforeParseLine(lineStr));
        if (lineStr == null) {
            return false;
        }
        if (this.isEnumStartLine(lineStr) || this.isEndLine(lineStr)) {
            return true;
        }

        if (enumValueType == VALUE_INT_TYPE) {
            return this.parseIntLine(lineStr);
        } else if (enumValueType == VALUE_SHORT_TYPE) {
            return this.parseShortLine(lineStr);
        } else {
            log.error("enum value type error.type:{}", this.enumValueType);
            return false;
        }
    }


    private boolean parseShortLine(String lineStr) {
        ValueShortEnumInfo info = new ValueShortEnumInfo(lineStr);
        if (!info.check() || !this.checkShortEnum(info)) {
            log.info("info check error.");
            return false;
        }

        this.cacheShortEnum(info);
        return true;
    }


    private boolean parseIntLine(String lineStr) {
        EnumBaseInfo info = new EnumBaseInfo(lineStr);
        if (!info.check() || this.checkIntEnum(info)) {
            log.info("info check error.");
            return false;
        }

        this.cacheIntEnum(info);
        return true;
    }

    private String beforeParseLine(String lintStr) {
        if (StrUtil.isEmpty(lintStr)) {
            log.error("shor enum line is null or empty");
            return null;
        }
        lintStr = lintStr.replace(" ", "");
        if (StrUtil.isEmpty(lintStr)) {
            log.error("after trim shor enum line is null or empty");
            return null;
        }
        return lintStr;
    }


    private boolean checkNameDuplicate(EnumBaseInfo info) {
        String name = info.getName();
        Integer value = info.getValue();
        if (this.nameMap.containsKey(name)) {
            log.error("duplicate name.name:{},old value:{},the value:{}", name, this.nameMap.get(name).getValue(), value);
            return false;
        }
        return true;
    }

    private boolean checkShortEnum(ValueShortEnumInfo info) {
        if (!this.checkNameDuplicate(info)) {
            return false;
        }

        short shortValue = info.getShortValue();
        if (this.shortValueMap.containsKey(shortValue)) {
            log.error("duplicate value.value:{},old name:{},the name:{}", shortValue, this.shortValueMap.get(shortValue).getName(), info.getName());
            return false;
        }
        return true;
    }


    private boolean checkIntEnum(EnumBaseInfo info) {
        if (!this.checkNameDuplicate(info)) {
            return false;
        }

        int shortValue = info.getValue();
        if (this.integerValueMap.containsKey(shortValue)) {
            log.error("duplicate value.value:{},old name:{},the name:{}", shortValue, this.integerValueMap.get(shortValue).getName(), info.getName());
            return false;
        }
        return true;
    }

    private void cacheShortEnum(ValueShortEnumInfo info) {
        nameMap.put(info.getName(), info);
        shortValueMap.put(info.getShortValue(), info);
    }

    private void cacheIntEnum(EnumBaseInfo info) {
        nameMap.put(info.getName(), info);
        integerValueMap.put(info.getValue(), info);
    }

    public Collection<ValueShortEnumInfo> getShortEnumInfo() {
        return shortValueMap.values();
    }

    public Collection<EnumBaseInfo> getIntEnumInfo() {
        return integerValueMap.values();
    }
}
