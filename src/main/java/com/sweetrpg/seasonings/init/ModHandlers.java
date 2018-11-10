/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package com.sweetrpg.seasonings.init;

import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.sweetrpg.seasonings.api.season.ISeasonColorProvider;
import com.sweetrpg.seasonings.api.season.SeasonHelper;
import com.sweetrpg.seasonings.config.BiomeConfig;
import com.sweetrpg.seasonings.handler.PacketHandler;
import com.sweetrpg.seasonings.handler.season.BirchColorHandler;
import com.sweetrpg.seasonings.handler.season.ProviderIceHandler;
import com.sweetrpg.seasonings.handler.season.RandomUpdateHandler;
import com.sweetrpg.seasonings.handler.season.SeasonHandler;
import com.sweetrpg.seasonings.handler.season.SeasonSleepHandler;
import com.sweetrpg.seasonings.handler.season.SeasonalCropGrowthHandler;
import com.sweetrpg.seasonings.handler.season.WeatherFrequencyHandler;
import com.sweetrpg.seasonings.season.SeasonTime;
import com.sweetrpg.seasonings.util.SeasonColourUtil;

public class ModHandlers
{
    private static final SeasonHandler SEASON_HANDLER = new SeasonHandler();

    public static void init()
    {
        PacketHandler.init();

        //Handlers for functionality related to seasons
        MinecraftForge.EVENT_BUS.register(SEASON_HANDLER);
        SeasonHelper.dataProvider = SEASON_HANDLER;
        MinecraftForge.EVENT_BUS.register(new RandomUpdateHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new ProviderIceHandler());
        MinecraftForge.EVENT_BUS.register(new SeasonSleepHandler());

        MinecraftForge.EVENT_BUS.register(new WeatherFrequencyHandler());
        MinecraftForge.EVENT_BUS.register(new SeasonalCropGrowthHandler());

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            registerSeasonColourHandlers();
        }
    }

    @SideOnly(Side.CLIENT)
    private static BiomeColorHelper.ColorResolver originalGrassColorResolver;
    @SideOnly(Side.CLIENT)
    private static BiomeColorHelper.ColorResolver originalFoliageColorResolver;

    @SideOnly(Side.CLIENT)
    private static void registerSeasonColourHandlers()
    {
        originalGrassColorResolver = BiomeColorHelper.GRASS_COLOR;
        originalFoliageColorResolver = BiomeColorHelper.FOLIAGE_COLOR;

        BiomeColorHelper.GRASS_COLOR = (biome, blockPosition) ->
        {
            SeasonTime calendar = new SeasonTime(SeasonHandler.clientSeasonCycleTicks);
            ISeasonColorProvider colorProvider = BiomeConfig.usesTropicalSeasons(biome) ? calendar.getTropicalSeason() : calendar.getSubSeason();
            return SeasonColourUtil.applySeasonalGrassColouring(colorProvider, biome, originalGrassColorResolver.getColorAtPos(biome, blockPosition));
        };

        BiomeColorHelper.FOLIAGE_COLOR = (biome, blockPosition) ->
        {
            SeasonTime calendar = new SeasonTime(SeasonHandler.clientSeasonCycleTicks);
            ISeasonColorProvider colorProvider = BiomeConfig.usesTropicalSeasons(biome) ? calendar.getTropicalSeason() : calendar.getSubSeason();
            return SeasonColourUtil.applySeasonalFoliageColouring(colorProvider, biome, originalFoliageColorResolver.getColorAtPos(biome, blockPosition));
        };
    }
    
    public static void postInit()
    {
    	if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
    		BirchColorHandler.init();
        }
    }
}
