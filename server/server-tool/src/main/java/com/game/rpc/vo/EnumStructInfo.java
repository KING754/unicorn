package com.game.rpc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author KING
 * @date 2023/01/05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnumStructInfo extends StructInfo{
    private Collection<ValueShortEnumInfo> enums;


}
