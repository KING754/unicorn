package com.game.constant;

/**
 * @author KING
 * @date 2022/12/29
 */
public class NettyConstant {
    public static final int SIGN_LEN = 32;
    public static final int LENGTH_FIELD_LEN = 2;
    public static final int MSG_ID_LEN = 2;
    public static final int CODE_LEN = 2;
    public static final int TIMESTAMP_LEN = 8;
    public static final int SEQUENCE_ID_LEN = 4;

    public static final String DEFAULT_STRING_CHARSET_NAME = "UTF-8";

    public static final int MSG_HEAD_LEN = (MSG_ID_LEN+CODE_LEN+TIMESTAMP_LEN+SEQUENCE_ID_LEN+SIGN_LEN+ LENGTH_FIELD_LEN);
    public static final int SIGN_HEAD_LEN = MSG_ID_LEN+CODE_LEN+TIMESTAMP_LEN+SEQUENCE_ID_LEN;

    public static final short MAX_MSG_LEN = Short.MAX_VALUE;


    public static final int INNER_LENGTH_FIELD_LEN = 2;
    public static final int INNER_MSG_ID_LEN = 2;
    public static final int INNER_CODE_LEN = 2;
    public static final int MSG_PLAYER_ID = ServerConstant.PLAYER_ID_LEN;


    public static final int INNER_MSG_HEAD_LEN = (INNER_LENGTH_FIELD_LEN
                                                    +INNER_CODE_LEN
                                                    +INNER_MSG_ID_LEN
                                                    +MSG_PLAYER_ID);




}
