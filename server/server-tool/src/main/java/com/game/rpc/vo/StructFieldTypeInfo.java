package com.game.rpc.vo;

import com.game.rpc.StructIntFieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author KING
 * @date 2023/01/05
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StructFieldTypeInfo {
    private String type;
    private Object length;
    private Object defaultValue;

    private String structTypeName;
    private String childType;

    public StructFieldTypeInfo(StructIntFieldType numberField){
        this.type = numberField.getType();
        this.length = numberField.getLength();
        this.defaultValue = numberField.getDefaultValue();
        this.structTypeName = numberField.getType();
    }


    public StructFieldTypeInfo(StructIntFieldType numberField,Object length){
        this.type = numberField.getType();
        this.length = length;
        this.defaultValue = numberField.getDefaultValue();
        this.structTypeName = numberField.getType();
    }
}
