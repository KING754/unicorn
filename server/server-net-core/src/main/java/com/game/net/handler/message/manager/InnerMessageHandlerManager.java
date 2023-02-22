package com.game.net.handler.message.manager;

import com.game.net.handler.message.InnerMessageHandler;
import com.game.net.annotation.HandlerTrigger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author KING
 * @date 2023/02/15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InnerMessageHandlerManager extends BaseHandlerManager<InnerMessageHandler> {
    private final ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        super.clearHandler();
        Collection<InnerMessageHandler> handlers = applicationContext.getBeansOfType(InnerMessageHandler.class).values();

        for (InnerMessageHandler handler : handlers) {
            HandlerTrigger trigger = handler.getClass().getAnnotation(HandlerTrigger.class);
            if (trigger != null) {
                short msgId = (short)trigger.msgId();
                super.putHandler(msgId,handler);
            }
        }
    }
}
