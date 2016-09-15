package fr.Jodge.elementalLibrary.data.stats;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.ItemHelper;
import fr.Jodge.elementalLibrary.data.register.Variable;
import fr.Jodge.elementalLibrary.log.JLog;

public class ItemStats extends AbstractStats
{	
	public static final String folder = "itemdata\\";
	
	public String itemName;
	
	public ItemStats(String itemName)
	{
		super(Main.constante.ITEM_STATS);
		this.itemName = itemName;
	}
	
	public ItemStats(ItemStack stack, MinecraftServer server) 
	{
		this(ItemHelper.getUnlocalizedName(stack));
		this.obj = stack;

		String url = folder;
		data = null;

		// check if entity has custom name tag
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey(Variable.DEFAULT_MATRIX_KEY))
			{
				url += stack.getTagCompound().getString(Variable.DEFAULT_MATRIX_KEY) + ElementalConfiguration.EXTENTION;
				data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
				if(!data.exists())
				{
					JLog.info("Custom File " + data.getAbsolutePath() + " not exist. Defautl file will be used");
					data = null;
				}
				else
				{
					JLog.info("Custom File " + data.getAbsolutePath() + " exist. It will be used.");
				}
			}
		}

		if(data == null)
		{
			url = folder + this.itemName + ElementalConfiguration.EXTENTION;
			data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
		}

		JLog.info("Make item Stats for " + obj.getClass() + " by file.");
		makeByFile();
	}
	
}
