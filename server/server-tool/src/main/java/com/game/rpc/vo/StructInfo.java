package com.game.rpc.vo;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KING
 * @date 2023/01/05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StructInfo {
    private String name;
    private String firstLowerName;
    private String comment;
    private List<StructFieldInfo> fieldInfos;
    private String lengthExpress;

    private boolean isEnumType;

    public void setName(String name){
        this.name = name;
        this.firstLowerName = StrUtil.lowerFirst(name);
    }

    public void addField(StructFieldInfo fieldInfo) {
        if (fieldInfo == null) {
            return;
        }
        if (fieldInfos == null) {
            fieldInfos = new ArrayList<>();
        }
        fieldInfos.add(fieldInfo);
    }

    public void setLengthExpress(){
        if(fieldInfos == null || fieldInfos.isEmpty()){
            lengthExpress = "0";
            return ;
        }
        for (StructFieldInfo field: fieldInfos) {
            if(StrUtil.isEmpty(lengthExpress)){
                lengthExpress = field.getType().getLength()+"";
            }else{
                lengthExpress += ("+"+field.getType().getLength());
            }
        }
    }

//    public int getStructTotalLength(){
//        int totalLen = 0;
//        if(fieldInfos == null || fieldInfos.isEmpty()){
//            return totalLen;
//        }
//
//        for (StructFieldInfo field: fieldInfos) {
//            if(field == null){
//                continue;
//            }
//            totalLen += field.getType().getLength();
//        }
//        return totalLen;
//    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("name:").append(name).append("\n");
        buffer.append("comment:").append(comment).append("\n");
        buffer.append("total LEN:").append(lengthExpress).append("\n");
        for (StructFieldInfo field : fieldInfos) {
            buffer.append(field.toString()).append("\n");
        }
        return buffer.toString();
    }
}
