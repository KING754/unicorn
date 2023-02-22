package com.game.rpc.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.game.rpc.RpcConstant;
import com.game.rpc.StructIntFieldType;
import com.game.rpc.vo.StructFieldTypeInfo;
import com.game.rpc.vo.StructInfo;
import com.game.rpc.vo.EnumStructInfo;
import com.game.rpc.vo.StructFieldInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author KING
 * @date 2023/01/04
 */

@Slf4j
public class StructGenerator extends BaseGenerator {
    private static final int MIN_LINES_COUNT = 2;
    private static final String NUMBER_FIELD_FLAG = "int";
    private static final String STRING_FIELD_FLAG = "char";

    private static final String SAVE_PATH = "Y:\\game\\gameServer\\server\\server-rpc-model\\src\\main\\java\\com\\game\\rpc\\message\\struct\\";

//    private static final String SAVE_PATH = "Y:\\game\\gameServer\\server\\server-tool\\src\\main\\java\\com\\game\\rpc\\test\\";


    private static final String CODE_TEMPLATE_NAME = "template/Struct.vm";
    private static final String ENUM_TEMPLATE_NAME = "template/EnumStruct.vm";

    private static HashMap<String, StructInfo> ALL_STRUCT = new HashMap<>();
    private static HashMap<String, List<String>> NOT_FOUND_STRUCT = new HashMap<>();

    public void genAllStruct() throws IOException {
        List<File> allStructFileList = this.getAllStructFile();
        List<StructInfo> allStruct = new ArrayList<>();
        for (File file : allStructFileList) {
            List<StructInfo> allFileStruct = this.dealOneFile(file);
            if (allFileStruct == null) {
                log.error("parse file have error.file:{}", file.getName());
                return;
            }
            if (!allFileStruct.isEmpty()) {
                allStruct.addAll(allFileStruct);
            }
        }

        boolean dealNotFoundRs = this.dealNotFoundStruct();
        if (!dealNotFoundRs) {
            log.error("not found any struct.detail in the before logs.");
            return;
        }

        for (StructInfo struct : allStruct) {
            if (struct instanceof EnumStructInfo) {
                this.writeEnumStruct((EnumStructInfo) struct);
            } else {
                this.writeNormalStruct(struct);
            }
        }
    }

    private List<StructInfo> dealOneFile(File structFile) throws IOException {
        log.info("deal struct file:{}", structFile.getName());

        List<String> allFileLine = super.readFileContent(structFile);

        List<StructInfo> fileAllStruct = new ArrayList<>();
        List<String> oneStructLines = new ArrayList<>();

        boolean isStart = false;
        for (String line : allFileLine) {
            if (StrUtil.isEmpty(line)) {
                continue;
            }
            line = line.trim();
            if (StrUtil.isEmpty(line)) {
                continue;
            }
            if (isStartLine(line)) {
                oneStructLines.add(line);
                isStart = true;
            } else if (isEndLine(line)) {
                isStart = false;
                oneStructLines.add(line);
                StructInfo struct = this.dealOneStructLines(oneStructLines, structFile.getName());
                if (struct == null) {
                    log.error("deal one struct error.{}", structFile.getAbsolutePath());
                    return null;
                }
                if (ALL_STRUCT.containsKey(struct.getName())) {
                    log.error("duplicate struct.{}", struct.getName());
                    return null;
                }
                ALL_STRUCT.put(struct.getName(), struct);
                fileAllStruct.add(struct);
                oneStructLines.clear();
            } else if (isStart) {
                oneStructLines.add(line);
            }
        }
        return fileAllStruct;
    }

    private StructInfo dealOneStructLines(List<String> oneStructLines, String filePath) throws IOException {
        if (!isRightStructLines(oneStructLines)) {
            log.error("the struct lines format error.");
            return null;
        }
        if (this.isEnumStruct(oneStructLines.get(0))) {
            return new EnumShortStructGenerator().getEnumStruct(oneStructLines, filePath);
        } else {
            return this.parseNormalStruct(oneStructLines);
        }

    }

    private StructInfo parseNormalStruct(List<String> oneStructLines) {
        StructInfo structInfo = new StructInfo();
        for (String line : oneStructLines) {
            line = line.replaceAll("\\t", "    ");
            if (isStartLine(line)) {
                String name = this.parseStructName(line);
                if (StrUtil.isEmpty(name)) {
                    log.error("parse struct name error.start line:{}", line);
                    return null;
                }
                structInfo.setName(StrUtil.upperFirst(name));

                String comment = this.parseLineComment(line);
                if (!StrUtil.isEmpty(comment)) {
                    structInfo.setComment(comment);
                }
            } else if (!isEndLine(line)) {
                StructFieldInfo field = this.parseField(line, structInfo.getName());
                if (field == null) {
                    log.error("parse struct field error.field line:{}", line);
                    return null;
                }
                structInfo.addField(field);
            }
        }

        return structInfo;
    }

    private boolean isEnumStruct(String firstLine) {
        firstLine = firstLine.trim();
        return this.isEnumStartLine(firstLine);
    }


    private boolean dealNotFoundStruct() {
        if (NOT_FOUND_STRUCT.isEmpty()) {
            return true;
        }
        for (Map.Entry<String, List<String>> entry : NOT_FOUND_STRUCT.entrySet()) {
            String notFoundStructName = entry.getKey();
            StructInfo structInfo = ALL_STRUCT.get(notFoundStructName);
            if (structInfo == null) {
                log.error("not found {} at final.", notFoundStructName);
                return false;
            }
            if(!structInfo.isEnumType()){
                log.info("found {} at final.but not enum struct, no need deal", notFoundStructName);
                continue;
            }
            List<String> needTheStructList = entry.getValue();
            for (String needSetLengthStructName : needTheStructList) {
                StructInfo needSetLengthStruct = ALL_STRUCT.get(needSetLengthStructName);
                if (needSetLengthStruct == null) {
                    log.error("not found the need reset struct:{}", needSetLengthStructName);
                    return false;
                }
                boolean isRest = false;
                for (StructFieldInfo fieldInfo : needSetLengthStruct.getFieldInfos()) {
                    if (!fieldInfo.getType().getType().equals(notFoundStructName)) {
                        continue;
                    }
                    fieldInfo.getType().setStructTypeName(structInfo.getName());
                    isRest = true;
                }
                if (!isRest) {
                    return false;
                }
            }
        }
        return true;
    }

    private StructFieldInfo parseField(String fieldLine, String structName) {
        fieldLine = fieldLine.trim();
        String[] fieldSplitArray = StrUtil.splitToArray(fieldLine, " ");

        String fieldName = this.getNameStrFromArray(fieldSplitArray);
        if (StrUtil.isEmpty(fieldName)) {
            log.error("parse field name error.line:{}", fieldLine);
            return null;
        }
        fieldName = StrUtil.lowerFirst(fieldName);
        String firstUpName = StrUtil.upperFirst(fieldName);

        String comment = this.parseLineComment(fieldLine);

        StructFieldTypeInfo typeInfo = this.parseFileType(fieldSplitArray[0], structName);
        if (typeInfo == null) {
            log.error("parse field type error.line:{}", fieldLine);
            return null;
        }

        return new StructFieldInfo(fieldName, firstUpName, comment, typeInfo);
    }

    private String getNameStrFromArray(String[] lineArray) {
        if (lineArray == null || lineArray.length == 0) {
            return null;
        }
        for (int i = 1; i < lineArray.length; i++) {
            String lineSplitStr = lineArray[i];
            if (StrUtil.isEmpty(lineSplitStr)) {
                continue;
            }
            lineSplitStr = lineSplitStr.trim();
            if (StrUtil.isEmpty(lineSplitStr)) {
                continue;
            }

            if (isCommentStr(lineSplitStr)) {
                continue;
            }
            return lineSplitStr.replace(";", "").trim();
        }
        return null;
    }


    private StructFieldTypeInfo parseFileType(String typeInfo, String structName) {
        typeInfo = typeInfo.trim();
        if (isListField(typeInfo)) {
            return this.getListFieldType(typeInfo,structName);
        } else if (isNumberField(typeInfo)) {
            return this.getIntFieldType(typeInfo);
        } else if (isStringField(typeInfo)) {
            return this.getStringFieldType(typeInfo);
        } else {
            return this.getStructFieldType(typeInfo, structName);
        }
    }

    private StructFieldTypeInfo getListFieldType(String typeInfo,String structName) {
        typeInfo = typeInfo.replace("[]","").trim();
        StructFieldTypeInfo type = this.parseFileType(typeInfo,structName);
        if (type == null) {
            log.error("get int field type error.str:{}", typeInfo);
            return null;
        }
        type.setStructTypeName(type.getStructTypeName());
        type.setChildType(type.getType());
        type.setType(StructIntFieldType.ListType.getType());
        return type;
    }

    private StructFieldTypeInfo getIntFieldType(String typeInfo) {
        StructIntFieldType type = StructIntFieldType.getFieldType(typeInfo);
        if (type == null) {
            log.error("get int field type error.str:{}", typeInfo);
            return null;
        }
        return new StructFieldTypeInfo(type);
    }

    private StructFieldTypeInfo getStringFieldType(String typeInfo) {
        Integer length = this.getStringFieldLength(typeInfo);
        if (length == null) {
            log.error("get String field type error.str:{}", typeInfo);
            return null;
        }

        return new StructFieldTypeInfo(StructIntFieldType.StringType,length);
    }


    private StructFieldTypeInfo getStructFieldType(String typeInfo, String structName) {
        StructFieldTypeInfo fieldInfo = new StructFieldTypeInfo();
        fieldInfo.setType(typeInfo);
        fieldInfo.setStructTypeName(typeInfo);

        StructInfo structInfo = ALL_STRUCT.get(typeInfo);
        if (structInfo != null) {
            if (structInfo.isEnumType()) {
                fieldInfo.setType(StructIntFieldType.EnumType.getType());
            }
        } else {
            this.putNeedCheckStruct(typeInfo, structName);

        }
        fieldInfo.setLength(typeInfo+".TOTAL_LENGTH");

        return fieldInfo;
    }

    private void putNeedCheckStruct(String fieldType, String structName) {
        List<String> structNames = null;
        if (NOT_FOUND_STRUCT.containsKey(fieldType)) {
            structNames = NOT_FOUND_STRUCT.get(fieldType);
        }
        if (structNames == null) {
            structNames = new ArrayList<>();
        }
        structNames.add(structName);
        NOT_FOUND_STRUCT.put(fieldType, structNames);
    }

    private Integer getStringFieldLength(String typeInfo) {
        typeInfo = typeInfo.trim().replace("char[", "").replace("]", "").trim();
        int length = NumberUtil.parseInt(typeInfo);
        if (length <= 0 || length > RpcConstant.STRING_MAX_LENGTH) {
            log.error("string field length error.info:{}", typeInfo);
            return null;
        }
        return length;
    }

    private boolean isNumberField(String fieldLine) {
        return fieldLine.startsWith(NUMBER_FIELD_FLAG);
    }

    private boolean isStringField(String fieldLine) {
        return fieldLine.startsWith(STRING_FIELD_FLAG);
    }

    private boolean isListField(String fieldLine) {
        return fieldLine.indexOf("[]") > 0;
    }

    private boolean isRightStructLines(List<String> oneStructLines) {
        if (oneStructLines == null || oneStructLines.isEmpty()) {
            log.warn("one struct lines is null or empty.");
            return false;
        }
        if (oneStructLines.size() < MIN_LINES_COUNT) {
            log.warn("one struct lines size[{}] error.", oneStructLines.size());
            return false;
        }
        String firstLine = oneStructLines.get(0);
        if (!isStartLine(firstLine)) {
            log.error("the first line format error.{}", firstLine);
            return false;
        }
        String endLine = oneStructLines.get(oneStructLines.size() - 1);
        if (!isEndLine(endLine)) {
            log.error("the end line format error.{}", endLine);
            return false;
        }
        return true;
    }


    private boolean isCommentStr(String str) {
        return str.startsWith(RpcConstant.COMMENT_START_FLAG);
    }

    private List<File> getAllStructFile() {
        FileFilter fileFilter = file -> {
            String name = file.getName();
            if (name.equals(MsgIdGenerator.MSG_ID_FILE_NAME) || name.equals(CodeGenerator.CODE_FILE_NAME)) {
                return false;
            } else {
                return true;
            }
        };

        return FileUtil.loopFiles(RpcConstant.RPC_BASE_FILE_PATH, fileFilter);
    }

    public StructInfo getStructByName(String structName) {
        return ALL_STRUCT.get(structName);
    }

    private void writeNormalStruct(StructInfo struct) throws IOException {
        try {
            struct.setLengthExpress();
            VelocityContext ctx = new VelocityContext();
            String fileFullPath = SAVE_PATH + struct.getName() + ".java";
            ctx.put("struct", struct);
            super.writeFile(ctx, CODE_TEMPLATE_NAME, fileFullPath);
        } catch (Exception e) {
            log.error("write struct :{} error:\n", struct.getName(), e);
            System.exit(0);
        }

    }


    private void writeEnumStruct(EnumStructInfo enumStruct) throws IOException {
        VelocityContext ctx = new VelocityContext();
        String fileFullPath = SAVE_PATH + enumStruct.getName() + ".java";
        ctx.put("struct", enumStruct);
        super.writeFile(ctx, ENUM_TEMPLATE_NAME, fileFullPath);
    }
}
