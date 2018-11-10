package com.sweetrpg.seasonings.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import com.sweetrpg.seasonings.api.config.SeasonsOption;
import com.sweetrpg.seasonings.api.config.SyncedConfig;
import com.sweetrpg.seasonings.api.season.Season;
import com.sweetrpg.seasonings.handler.season.SeasonHandler;
import com.sweetrpg.seasonings.season.SeasonSavedData;
import com.sweetrpg.seasonings.season.SeasonTime;

import java.util.List;

public class SSCommand extends CommandBase
{
    @Override
    public String getName()
    {
        return "seasonings";
    }

    @Override
    public List getAliases()
    {
        return Lists.newArrayList("ss");
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "commands.seasonings.usage";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.seasonings.usage");
        }
        else if ("setseason".equals(args[0]))
        {
            setSeason(sender, args);
        }
    }

    private void setSeason(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        Season.SubSeason newSeason = null;

        for (Season.SubSeason season : Season.SubSeason.values())
        {
            if (season.toString().toLowerCase().equals(args[1].toLowerCase()))
            {
                newSeason = season;
                break;
            }
        }

        if (newSeason != null)
        {
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(player.world);
            seasonData.seasonCycleTicks = SeasonTime.ZERO.getSubSeasonDuration() * newSeason.ordinal();
            seasonData.markDirty();
            SeasonHandler.sendSeasonUpdate(player.world);
            sender.sendMessage(new TextComponentTranslation("commands.seasonings.setseason.success", args[1]));
        }
        else
        {
            sender.sendMessage(new TextComponentTranslation("commands.seasonings.setseason.fail", args[1]));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "setseason");
        }

        return null;
    }
}
