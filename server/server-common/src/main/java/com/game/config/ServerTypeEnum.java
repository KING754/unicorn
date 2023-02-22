package com.game.config;

import com.game.rpc.message.inner.InnerRPC;

import static com.game.rpc.message.st.MsgId.EN_MessageId;

;

/**
 * @author KING
 * @date 2023/02/15
 */
public enum ServerTypeEnum {
    GATE_WAY(InnerRPC.EN_Server_type.SERVER_TYPE_GATEWAY, (short) 0, (short) 0),
    LOGIN(InnerRPC.EN_Server_type.SERVER_TYPE_LOGIN, (short) EN_MessageId.E_LOGIN_VALUE, (short) EN_MessageId.E_LOGIN_END_VALUE),
    LOBBY(InnerRPC.EN_Server_type.SERVER_TYPE_LOBBY, (short) EN_MessageId.E_LOBBY_START_VALUE, (short) EN_MessageId.E_LOBBY_END_VALUE),
    ROOM_MANAGER(InnerRPC.EN_Server_type.SERVER_ROOM_MANAGER, (short) EN_MessageId.E_ROOM_START_VALUE, (short) EN_MessageId.E_ROOM_END_VALUE),
    LOGIN_AND_LOBBY(InnerRPC.EN_Server_type.SERVER_LOGIN_AND_LOBBY, (short) EN_MessageId.E_LOGIN_VALUE, (short) EN_MessageId.E_LOBBY_END_VALUE);

    private final InnerRPC.EN_Server_type type;
    private final short minMsgId;
    private final short maxMsgId;

    ServerTypeEnum(InnerRPC.EN_Server_type type, short minMsgId, short maxMsgId) {
        this.type = type;
        this.minMsgId = minMsgId;
        this.maxMsgId = maxMsgId;
    }

    public InnerRPC.EN_Server_type getType() {
        return type;
    }

    public int getMinMsgId() {
        return minMsgId;
    }

    public short getMaxMsgId() {
        return maxMsgId;
    }

    public static ServerTypeEnum getTypeByPbType(InnerRPC.EN_Server_type serverType) {
        ServerTypeEnum[] allServerType = ServerTypeEnum.values();
        for (ServerTypeEnum type : allServerType) {
            if (type.getType().getNumber() == serverType.getNumber()) {
                return type;
            }
        }
        return null;
    }

}

