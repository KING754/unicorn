package com.game.rpc.vo;

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
public class StructFieldInfo {
    private String name;
    private String firstUpName;
    private String comment;
    private StructFieldTypeInfo type;

}
