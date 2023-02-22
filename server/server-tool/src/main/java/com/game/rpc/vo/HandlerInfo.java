package com.game.rpc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author KING
 * @date 2023/01/06
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HandlerInfo {
    private String name;
    private String msgEnumName;
    private String reqType;
    private String respType;
    private String importObjStr;
    private String reqTypeFull;
    private String respTypeFull;
    private String packageStr;
}
