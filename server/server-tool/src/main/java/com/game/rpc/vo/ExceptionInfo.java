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
public class ExceptionInfo {
    private String name;
    private String codeEnumName;
}
