package com.game.rpc.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KING
 * @date 2023/01/03
 */
@Data
@Slf4j
@ToString
@NoArgsConstructor
public class ValueShortEnumInfo extends EnumBaseInfo {
    private short shortValue;

    public ValueShortEnumInfo(String line) {
        super(line);
        this.shortValue = (short) super.getValue();
    }

    @Override
    public boolean check(){
        if(!super.check()){
            return false;
        }

        return super.getValue() <= Short.MAX_VALUE;

    }

}
