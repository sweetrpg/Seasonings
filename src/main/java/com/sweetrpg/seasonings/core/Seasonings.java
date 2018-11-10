package com.sweetrpg.seasonings.core;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import com.sweetrpg.seasonings.command.SSCommand;
import com.sweetrpg.seasonings.init.ModBlocks;
import com.sweetrpg.seasonings.init.ModConfig;
import com.sweetrpg.seasonings.init.ModFertility;
import com.sweetrpg.seasonings.init.ModHandlers;
import com.sweetrpg.seasonings.init.ModItems;

@Mod(modid = Seasonings.MOD_ID, version = Seasonings.MOD_VERSION, name = Seasonings.MOD_NAME, dependencies = "required-after:forge@[1.0.0.0,)")
public class Seasonings
{
    public static final String MOD_NAME = "Seasonings";
    public static final String MOD_ID = "seasonings";
    public static final String MOD_VERSION = "@MOD_VERSION@";

    @Instance(MOD_ID)
    public static Seasonings instance;

    @SidedProxy(clientSide = "com.sweetrpg.seasonings.core.ClientProxy", serverSide = "com.sweetrpg.seasonings.core.CommonProxy")
    public static CommonProxy proxy;

    public static Logger logger = LogManager.getLogger(MOD_ID);
    public static File configDirectory;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configDirectory = new File(event.getModConfigurationDirectory(), "seasonings");

        ModConfig.preInit(configDirectory);
        ModBlocks.init();
        ModItems.init();
        ModHandlers.init();

        proxy.registerRenderers();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ModConfig.init(configDirectory);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	ModFertility.init();
    	ModHandlers.postInit();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new SSCommand());
    }
}
