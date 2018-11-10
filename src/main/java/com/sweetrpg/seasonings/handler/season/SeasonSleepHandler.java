/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package com.sweetrpg.seasonings.handler.season;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import com.sweetrpg.seasonings.api.config.SeasonsOption;
import com.sweetrpg.seasonings.api.config.SyncedConfig;
import com.sweetrpg.seasonings.season.SeasonSavedData;

public class SeasonSleepHandler 
{
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == Phase.START && event.side == Side.SERVER)
        {
            WorldServer world = (WorldServer)event.world;

            //Called before all players are awoken for the next day
            if (world.areAllPlayersAsleep())
            {
                SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
                long timeDiff = 24000L - ((world.getWorldInfo().getWorldTime() + 24000L) % 24000L);
                seasonData.seasonCycleTicks += timeDiff;
                seasonData.markDirty();
                SeasonHandler.sendSeasonUpdate(world);
            }
        }
    }
}
