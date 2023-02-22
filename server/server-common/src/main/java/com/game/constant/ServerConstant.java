package com.game.constant;

/**
 * @author KING
 * @date 2023/01/31
 */
public class ServerConstant {
    public static final int PLAYER_ID_LEN = 64;
    public static final int ACCOUNT_NAME_MIN_LEN = 6;
    public static final int ACCOUNT_NAME_MAX_LEN = 20;

    public static final int ACCOUNT_PWD_MIN_LEN = 6;
    public static final int ACCOUNT_PWD_MAX_LEN = 20;

    public static final int PLAYER_NAME_MIN_LEN = 6;
    public static final int PLAYER_NAME_MAX_LEN = 20;

    public static final int PLAYER_INIT_LEVEL = 1;
    public static final int PLAYER_INIT_GOLD = 200;


    public static final String MESSAGE_DEFAULT_SIGN = "00000000000000000000000000000000";
    public static final String MESSAGE_DEFAULT_PLAYER_ID = "0000000000000000000000000000000000000000000000000000000000000000";


    public static final String NETTY_CTX_SERVER_TYPE_ATTR_KEY = "serverType";
    public static final String NETTY_CTX_SERVER_ID_ATTR_KEY = "serverId";
    public static final String NETTY_CTX_CONNECTION_ID_ATTR_KEY = "connectionId";

    public static final String PLAYER_NETTY_CTX_SERVER_TYPE_CONN_KEY = "serverType_";

    public static final String PLAYER_NETTY_CTX_PLAYER_ID_KEY = "playerId";

    public static String getPlayerServerTypeConnAttrKey(int serverType){
        return PLAYER_NETTY_CTX_SERVER_TYPE_CONN_KEY+serverType;
    }

}
