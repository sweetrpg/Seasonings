package com.sweetrpg.seasonings.handler.season;

import javax.annotation.Nullable;

import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.sweetrpg.seasonings.api.season.ISeasonColorProvider;
import com.sweetrpg.seasonings.config.BiomeConfig;
import com.sweetrpg.seasonings.init.ModConfig;
import com.sweetrpg.seasonings.season.SeasonTime;

public class BirchColorHandler
{
	public static void init()
    {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor()
	    {
	        public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
	        {
	            BlockPlanks.EnumType plankstype = (BlockPlanks.EnumType)state.getValue(BlockOldLeaf.VARIANT);
	            
	            if (plankstype == BlockPlanks.EnumType.SPRUCE)
	            {
	                return ColorizerFoliage.getFoliageColorPine();
	            }
	            else if (plankstype == BlockPlanks.EnumType.BIRCH)
	            {
	            	int birchColor = ColorizerFoliage.getFoliageColorBirch();
	            	
	            	if (worldIn != null && pos != null && ModConfig.seasons.changeBirchColour)
	            	{
	            		SeasonTime calendar = new SeasonTime(SeasonHandler.clientSeasonCycleTicks);
		                ISeasonColorProvider colorProvider = BiomeConfig.usesTropicalSeasons(worldIn.getBiome(pos)) ? calendar.getTropicalSeason() : calendar.getSubSeason();
		                birchColor = colorProvider.getBirchColor();
	            	}
	            	
	                return birchColor;
	            }
	            else
	            {
	                return worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
	            }
	        }
	    }, Blocks.LEAVES);
    }
}
