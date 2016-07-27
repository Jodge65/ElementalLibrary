package fr.Jodge.elementalLibrary.proxy;

import fr.Jodge.elementalLibrary.function.JLog;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy
{
	public void registerRender()
	{
	}

	public void registerTexture(Object obj, int metadata, String name, String modid)
	{
		Item objectRender;
		if(obj instanceof Block)
		{
			objectRender = Item.getItemFromBlock((Block)obj);
		}
		else
		{
			objectRender = (Item) obj;
		}
			
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		mesher.register(objectRender, metadata, new ModelResourceLocation(modid + ":" + name, "inventory"));
	}
	public void registerTexture(Object obj, String name, String modid)
	{
		registerTexture(obj, 0, name, modid);
	}
	
	public void multiTexture(Item item, ResourceLocation[] chaine)
	{
		ModelBakery.registerItemVariants(item, chaine);
	}
	
	public boolean isClientSide(){return !super.isClientSide();}
	public boolean isServerSide(){return !isClientSide();}
}
