package com.game.rpc;

/**
 * @author KING
 * @date 2023/01/05
 */
public enum StructIntFieldType {
    int8("Byte", 8,1),
    int16("Short", 16,11),
    int32("Integer", 32,1111),
    int64("Long", 64,1111_1111),
    StringType("String",0,"defaultStr"),
    EnumType("Enum",0,"defaultStr"),
    ListType("List",0,null);

    private String type;
    private int length;
    private Object defaultValue;

    StructIntFieldType(String type, int length,Object defaultValue) {
        this.type = type;
        this.length = length;
        this.defaultValue = defaultValue;
    }

    public String getType(){
        return this.type;
    }

    public int getLength(){
        return this.length;
    }

    public Object getDefaultValue(){
        return this.defaultValue;
    }

    public static StructIntFieldType getFieldType(String typeStr) {
        for (StructIntFieldType type : StructIntFieldType.values()) {
            if(type.name().equals(typeStr)){
                return type;
            }
        }
        return null;
    }
}
