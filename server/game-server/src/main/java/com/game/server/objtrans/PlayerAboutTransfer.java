package com.game.server.objtrans;

import com.game.dao.domain.Player;
import com.game.rpc.message.st.common.Common;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author KING
 * @date 2023/01/31
 */
@Mapper
public interface PlayerAboutTransfer {
    PlayerAboutTransfer INSTANCE = Mappers.getMapper(PlayerAboutTransfer.class);

    @Mappings(
            {
                    @Mapping(source = "id",target = "uqId"),
                    @Mapping(source = "name",target = "nickname")
            }
    )
    Common.Player dbPlayerToPb(Player dbPlayer);



    List<Common.Player> dbPlayerListToPbList(List<Player> dbPlayer);
}
