package fr.Jodge.elementalLibrary.data;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.network.AskItemStatsPacket;
import fr.Jodge.elementalLibrary.data.register.Register;
import fr.Jodge.elementalLibrary.data.register.Variable;
import fr.Jodge.elementalLibrary.data.stats.ItemStats;
import fr.Jodge.elementalLibrary.log.JLog;

public class ItemHelper extends DataHelper
{
	public static void initItem(ItemStack stack) 
	{	
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			JLog.info("New Item asked : " + stack.getDisplayName());
			Main.constante.STATS_SOCKET.sendToServer(new AskItemStatsPacket(stack));
		}
		else
		{
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			ItemStats stats = new ItemStats(stack, server);
			registerItem(stack, stats);
		}
	}
	
	public static void registerItem(ItemStack stack, ItemStats stats)
	{
		Register.addNewWeaponMatrix(stack, stats);
	}
	
	public static String getUnlocalizedName(Block b)
	{
		return getUnlocalizedName(Item.getItemFromBlock(b));
	}
	
	public static String getUnlocalizedName(Item i)
	{
		return getUnlocalizedName(i.getUnlocalizedName());
	}
	public static String getUnlocalizedName(ItemStack i)
	{
		return getUnlocalizedName(i.getItem());
	}
	public static String getUnlocalizedName(String s)
	{
		return s.substring(s.indexOf(".") + 1);
	}

	public static String getMatrixName(ItemStack stack) 
	{
		String itemName = ItemHelper.getUnlocalizedName(stack);
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey(Variable.DEFAULT_MATRIX_KEY))
			{
				itemName = stack.getTagCompound().getString(Variable.DEFAULT_MATRIX_KEY);
			}
		}
		return itemName;
	}
	
	
}
