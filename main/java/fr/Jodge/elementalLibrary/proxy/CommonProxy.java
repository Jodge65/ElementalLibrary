package fr.Jodge.elementalLibrary.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CommonProxy
{
	public void registerRender()
	{
	}

	public void registerTexture(Object obj, int metadata, String name, String modid)
	{
	}

	public void registerTexture(Object obj, String name, String modid)
	{
	}

	public void multiTexture(Item item, ResourceLocation[] chaine)
	{
	}

	public boolean isClientSide(){return false;}
	public boolean isServerSide(){return !isClientSide();}
	
}
