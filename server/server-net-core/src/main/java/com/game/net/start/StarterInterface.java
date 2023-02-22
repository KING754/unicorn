package com.game.net.start;

/**
 * @author KING
 * @date 2023/02/10
 */
public interface StarterInterface {
    boolean initDispatcher();

    boolean initTrigger();

    boolean initSocketHandler();

    boolean initInitializer();

    boolean initServerOrClient();

    default boolean initAllComponent() {
        if (!this.initDispatcher()
                || !this.initSocketHandler()
                || !this.initInitializer()
                || !this.initServerOrClient()) {
            return false;
        }
        return true;
    }

    boolean start();
}
