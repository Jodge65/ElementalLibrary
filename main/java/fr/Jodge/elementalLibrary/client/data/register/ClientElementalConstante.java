package fr.Jodge.elementalLibrary.client.data.register;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.model.ModelLoader;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.ItemHelper;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.log.JLog;
import fr.Jodge.elementalLibrary.server.data.register.CommonElementalConstante;

/**
 * Client class for Constante
 * @author Jodge
 *
 */
public class ClientElementalConstante extends CommonElementalConstante
{
	/**
	 * What you need to do : 
	 * - Refers each kind of existing stats
	 */
	@Override
	public void onPreLoad()
	{
		super.onPreLoad();
	}
	
	/**
	 * What you need to do : 
	 * - add/remove element
	 */
	@Override
	public void onLoad()
	{
		super.onLoad();
				
	}
	
	/**
	 * 
	 */
	@Override
	public void onAfterLoad()
	{
		super.onAfterLoad();
	}	

	/**
	 * purge old data
	 */
	@Override
	public void onClientExit()
	{
		JLog.info(" --- START PURGE --- ");
		
		DEFAULT_STATS.clear();
		DEFAULT_ITEM_STATS.clear();
		JLog.info("Default stats clear.");
		
		Element.reset();
		JLog.info("Element clear.");

		JLog.info(" --- END PURGE --- ");
	}
	
	
	/**
	 * Nothing special to do on client side, but needed for integrated server...
	 */
	@Override
	public void onServerStart(MinecraftServer server)
	{
		super.onServerStart(server);
	}
	
	@Override
	public void registerTexture(Item item)
	{
		registerTexture(item, 0);
	}
	
	@Override
	public void registerTexture(Item item, int metadata)
	{
		String name = ItemHelper.getUnlocalizedName(item);
		ModelLoader.setCustomModelResourceLocation (item , metadata, new ModelResourceLocation (Main.MODID + ":" + name, "inventory"));
	}

}
