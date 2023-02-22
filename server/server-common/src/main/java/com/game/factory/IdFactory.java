package com.game.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author KING
 * @date 2023/02/16
 */

@Slf4j
@Component
public class IdFactory {
    private AtomicLong idFactoryTemp = new AtomicLong(1);

    public String getId(){
        long id = idFactoryTemp.getAndIncrement();
        return String.format("%064d", id);
    }

    public static void main(String[] args) {
        System.out.println(new IdFactory().getId());
    }


}
