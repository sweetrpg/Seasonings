package com.sweetrpg.seasonings.handler;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import com.sweetrpg.seasonings.core.Seasonings;
import com.sweetrpg.seasonings.network.message.MessageSyncConfigs;
import com.sweetrpg.seasonings.network.message.MessageSyncSeasonCycle;

public class PacketHandler
{
    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(Seasonings.MOD_ID);

    public static void init()
    {
        instance.registerMessage(MessageSyncSeasonCycle.class, MessageSyncSeasonCycle.class, 3, Side.CLIENT);
        instance.registerMessage(MessageSyncConfigs.class, MessageSyncConfigs.class, 4, Side.CLIENT);
    }
}
