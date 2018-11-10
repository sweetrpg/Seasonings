/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package com.sweetrpg.seasonings.season;

import com.google.common.base.Preconditions;
import com.sweetrpg.seasonings.api.config.SeasonsOption;
import com.sweetrpg.seasonings.api.config.SyncedConfig;
import com.sweetrpg.seasonings.api.season.ISeasonState;
import com.sweetrpg.seasonings.api.season.Season;

public final class SeasonTime implements ISeasonState
{
    public static final SeasonTime ZERO = new SeasonTime(0);
    public final int time;
    
    public SeasonTime(int time)
    {
        Preconditions.checkArgument(time >= 0, "Time cannot be negative!");
        this.time = time;
    }

    @Override
    public int getDayDuration()
    {
        return SyncedConfig.getIntValue(SeasonsOption.DAY_DURATION);
    }

    @Override
    public int getSubSeasonDuration()
    {
        return getDayDuration() * SyncedConfig.getIntValue(SeasonsOption.SUB_SEASON_DURATION);
    }

    @Override
    public int getSeasonDuration()
    {
        return getSubSeasonDuration() * 3;
    }

    @Override
    public int getCycleDuration()
    {
        return getSubSeasonDuration() * Season.SubSeason.values().length;
    }
    
    @Override
    public int getSeasonCycleTicks() 
    {
        return this.time;
    }

    @Override
    public int getDay()
    {
        return this.time / getDayDuration();
    }

    @Override
    public Season.SubSeason getSubSeason()
    {
        int index = (this.time / getSubSeasonDuration()) % Season.SubSeason.values().length;
        return Season.SubSeason.values()[index];
    }

    @Override
    public Season getSeason()
    {
        return this.getSubSeason().getSeason();
    }

    @Override
    public Season.TropicalSeason getTropicalSeason()
    {
        int index = ((((this.time / getSubSeasonDuration()) + 11) / 2) + 5) % Season.TropicalSeason.values().length;
        return Season.TropicalSeason.values()[index];
    }
}
