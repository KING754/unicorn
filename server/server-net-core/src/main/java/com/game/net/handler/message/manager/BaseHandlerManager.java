package com.game.net.handler.message.manager;

import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KING
 * @date 2023/02/14
 */
public abstract class BaseHandlerManager<T> implements InitializingBean {
    private ConcurrentHashMap<Short, T> msgIdHandler = new ConcurrentHashMap<>();

    public void clearHandler(){
        msgIdHandler.clear();
    }

    public T getHandlerByMsgId(short msgId){
        return msgIdHandler.get(msgId);
    }

    public void putHandler(short msgId,T handler){
        msgIdHandler.put(msgId,handler);
    }

    public  ConcurrentHashMap<Short, T> getHandlerMap(){
        return this.msgIdHandler;
    }


    @Override
    public abstract void afterPropertiesSet() throws Exception;
}
