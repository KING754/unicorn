package com.game.gateway.service;

import com.game.config.ServerTypeEnum;
import com.game.constant.ServerConstant;
import com.game.net.conn.ConnectionManager;
import com.game.rpc.message.inner.InnerRPC;
import com.game.vo.ConnectionIdInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.temporal.ValueRange;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KING
 * @date 2023/02/15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogicConnectionManager extends ConnectionManager {

    /**
     * Map<serverType,Map<serverId,Map<connId,ctx>>>, logic connection cache
     */
    public static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ChannelHandlerContext>>> LOGIC_SERVICE_CONN_MAP = new ConcurrentHashMap<>();

    /**
     * Map<serverType_connId,current connection count>, logic connection cache
     * 本来准备考虑移到redis,多个缓存共同维护.
     * 后来想想,如果每个gateway都能保持自己转发是平衡,那么总体上也是平衡的.
     * 所以不放到缓存了.
     * Map<serverType,Map<serverId,Map<connId,Count>>>
     **/
    public static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, AtomicInteger>>> LOGIC_SERVICE_CONN_COUNT = new ConcurrentHashMap<>();


    /**
     * 各个类型可以处理的消息ID区间
     */
    public static final ConcurrentHashMap<Integer, ValueRange> LOGIC_SERVICE_MSG_ID_RANGE = new ConcurrentHashMap<>();

    /**
     * 玩家登录时,按负载分配好各个服务器的连接.此处保存玩家的连接关系,
     * Map<playerId,Map<serverType,ctx>>
     */
    public static final ConcurrentHashMap<String, ConcurrentHashMap<Integer, ChannelHandlerContext>> PLAYER_LOGIC_MAPPING = new ConcurrentHashMap<>();


    public boolean isHaveTheConnId(int serverType, int serverId, int connId) {
        if (!LOGIC_SERVICE_CONN_MAP.containsKey(serverType)) {
            return false;
        }
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ChannelHandlerContext>> innerMap1 = LOGIC_SERVICE_CONN_MAP.get(serverType);
        if (!innerMap1.containsKey(serverId)) {
            return false;
        }
        ConcurrentHashMap<Integer, ChannelHandlerContext> innerMap2 = innerMap1.get(serverId);
        return innerMap2.containsKey(connId);
    }


    public void putLogicService(InnerRPC.EN_Server_type pbServiceType) {
        int typeNum = pbServiceType.getNumber();
        if (LOGIC_SERVICE_MSG_ID_RANGE.containsKey(typeNum)) {
            return;
        }

        ServerTypeEnum type = ServerTypeEnum.getTypeByPbType(pbServiceType);
        ValueRange range = ValueRange.of(type.getMinMsgId(), type.getMaxMsgId());
        LOGIC_SERVICE_MSG_ID_RANGE.put(type.getType().getNumber(), range);
        log.info("add logic service type:{},range:{}", pbServiceType.getDescriptorForType(), range);
    }

    public void putConnection(int serverType, int serverId, int connId, ChannelHandlerContext ctx) {
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ChannelHandlerContext>> oneTypeAllServerIdMap = LOGIC_SERVICE_CONN_MAP.computeIfAbsent(serverType, k -> new ConcurrentHashMap<>());
        ConcurrentHashMap<Integer, ChannelHandlerContext> oneServerAllConnMap = oneTypeAllServerIdMap.computeIfAbsent(serverId, k -> new ConcurrentHashMap<>());
        oneServerAllConnMap.put(connId, ctx);
        log.info("add conn type:{},id={},connId:{}", serverType, serverId, connId);

        this.increaseConnectionCount(ctx);
    }

    public boolean isHaveDealSameMsgIdOtherServerType(InnerRPC.EN_Server_type pbType) {
        ServerTypeEnum type = ServerTypeEnum.getTypeByPbType(pbType);
        return this.isHaveDealSameMsgIdOtherServerType(type);
    }

    public boolean checkServerTypeCfg() {
        for (InnerRPC.EN_Server_type pbType : InnerRPC.EN_Server_type.values()) {
            ServerTypeEnum type = ServerTypeEnum.getTypeByPbType(pbType);
            if (type == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isHaveDealSameMsgIdOtherServerType(ServerTypeEnum serverType) {
        if (LOGIC_SERVICE_MSG_ID_RANGE.isEmpty()) {
            return false;
        }

        ValueRange inputRange = ValueRange.of(serverType.getMinMsgId(),serverType.getMaxMsgId());

        for (Map.Entry<Integer, ValueRange> inMapTypeRangeEntry : LOGIC_SERVICE_MSG_ID_RANGE.entrySet()) {
            int inMapServerType = inMapTypeRangeEntry.getKey();
            if(inMapServerType == serverType.getType().getNumber()){
                continue;
            }

            ValueRange inMapRange = inMapTypeRangeEntry.getValue();
            if(inMapRange == null){
                continue;
            }
            if(inMapRange.getMinimum() > inputRange.getMaximum() || inMapRange.getMaximum() < inputRange.getMinimum()){
                continue;
            }
            return true;
        }
        return false;
    }

//    private boolean getDealSameMsgIdAllServer(ServerTypeEnum type) {
//        if (LOGIC_SERVICE_MSG_ID_RANGE.isEmpty()) {
//            return null;
//        }
//
//        RangeUtil.
//
//        List<ValueRange> sortedList = new ArrayList<>(LOGIC_SERVICE_MSG_ID_RANGE.values());
//        sortedList.add(ValueRange.of(type.getMinMsgId(), type.getMaxMsgId()));
//
//        Collections.sort(sortedList, Comparator.comparing(ValueRange::getMinimum));
//
//        boolean hasOverlap = false;
//        for (int i = 1; i < sortedList.size(); i++) {
//            ValueRange prev = sortedList.get(i - 1);
//            ValueRange current = sortedList.get(i);
//            if (prev.getMaximum() >= current.getMinimum()) {
//                hasOverlap = true;
//                break;
//            }
//        }
//
//        return !hasOverlap;
//        return false;
//    }

    public Integer getServerTypeByMsgId(int msgId) {
        for (Map.Entry<Integer, ValueRange> entry : LOGIC_SERVICE_MSG_ID_RANGE.entrySet()) {
            int minMsgId = (int) entry.getValue().getMinimum();
            int maxMsgId = (int) entry.getValue().getMaximum();

            if (msgId >= minMsgId && msgId <= maxMsgId) {
                return entry.getKey();
            }
        }
        return null;
    }

    public ChannelHandlerContext getMinLoadLogicConn(int needServerType) {
        // 假设要查找最小值对应的三个键值为 outerKey, midKey, innerKey
        int minVal = Integer.MAX_VALUE;
        int serverType = -1, serverId = -1, connId = -1;
        for (Map.Entry<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, AtomicInteger>>> entry1 : LOGIC_SERVICE_CONN_COUNT.entrySet()) {
            int inMapServerType = entry1.getKey();
            if (inMapServerType != needServerType) {
                continue;
            }

            ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, AtomicInteger>> midMap = entry1.getValue();
            for (Map.Entry<Integer, ConcurrentHashMap<Integer, AtomicInteger>> entry2 : midMap.entrySet()) {
                int inMapServerId = entry2.getKey();
                ConcurrentHashMap<Integer, AtomicInteger> innerMap = entry2.getValue();
                for (Map.Entry<Integer, AtomicInteger> entry3 : innerMap.entrySet()) {
                    int inMapConnId = entry3.getKey();
                    AtomicInteger val = entry3.getValue();
                    if (val.get() < minVal) {
                        minVal = val.get();
                        serverType = inMapServerType;
                        serverId = inMapServerId;
                        connId = inMapConnId;
                    }
                }
            }
        }

        ChannelHandlerContext ctx = this.getLogicConnByInfo(serverType, serverId, connId);
        if (ctx == null) {
            ctx = this.getDefaultLogicConn(serverType);
        }
        return ctx;
    }

    public ChannelHandlerContext getDefaultLogicConn(int serverType) {
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ChannelHandlerContext>> midMap = LOGIC_SERVICE_CONN_MAP.get(serverType);
        if (midMap != null) {
            // 遍历中间层 ConcurrentHashMap，返回第一个非空的 ChannelHandlerContext 对象
            for (ConcurrentHashMap<Integer, ChannelHandlerContext> innerMap : midMap.values()) {
                if (!innerMap.isEmpty()) {
                    return innerMap.values().iterator().next();
                }
            }
        }
        return null;
    }

    public ChannelHandlerContext getLogicConnByInfo(int serverType, int serverId, int connId) {
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ChannelHandlerContext>> midMap = LOGIC_SERVICE_CONN_MAP.get(serverType);
        if (midMap != null) {
            ConcurrentHashMap<Integer, ChannelHandlerContext> innerMap = midMap.get(serverId);
            if (innerMap != null) {
                return innerMap.get(connId);
            }
        }
        return null;
    }

    public ChannelHandlerContext getLogicConnByInfo(ConnectionIdInfo connIdInfo) {
        return this.getLogicConnByInfo(connIdInfo.getServerType(), connIdInfo.getServerId(), connIdInfo.getConnId());
    }

    public void increaseConnectionCount(ChannelHandlerContext ctx) {
        AtomicInteger connectionCount = this.getLogicConnCounter(ctx);
        connectionCount.incrementAndGet();
    }

    public void reduceConnectionCount(ChannelHandlerContext ctx) {
        AtomicInteger connectionCount = this.getLogicConnCounter(ctx);
        int newCount = connectionCount.decrementAndGet();
        if (newCount < 0) {
            connectionCount.set(0);
        }
    }


    private AtomicInteger getLogicConnCounter(ChannelHandlerContext ctx) {
        int serverType = ctx.attr(AttributeKey.<Integer>valueOf(ServerConstant.NETTY_CTX_SERVER_TYPE_ATTR_KEY)).get();
        int serverId = ctx.attr(AttributeKey.<Integer>valueOf(ServerConstant.NETTY_CTX_SERVER_ID_ATTR_KEY)).get();
        int connectionId = ctx.attr(AttributeKey.<Integer>valueOf(ServerConstant.NETTY_CTX_CONNECTION_ID_ATTR_KEY)).get();

        ConcurrentHashMap<Integer, AtomicInteger> connectionCountMap = LOGIC_SERVICE_CONN_COUNT
                .computeIfAbsent(serverType, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(serverId, k -> new ConcurrentHashMap<>());

        return connectionCountMap.computeIfAbsent(connectionId, k -> new AtomicInteger());
    }

}
