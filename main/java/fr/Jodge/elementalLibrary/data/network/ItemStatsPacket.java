package fr.Jodge.elementalLibrary.data.network;

import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.ItemHelper;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.stats.ItemStats;
import fr.Jodge.elementalLibrary.data.stats.MonsterStats;
import fr.Jodge.elementalLibrary.function.JLog;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemStatsPacket implements IMessage
{
	ItemStats stats;
	
	public ItemStatsPacket(){}

	public ItemStatsPacket(ItemStack stack, MinecraftServer server)
	{
		JLog.info("New request for item data : " + stack.getDisplayName());
		this.stats = new ItemStats(stack, server);
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		NBTTagCompound itemTag = BufUtils.readTag(buf);
		ItemStack stack = ItemStack.loadItemStackFromNBT(itemTag);
		stats = new ItemStats(stack, Minecraft.getMinecraft().thePlayer.getServer());
				
		for(Class<? extends IElementalWritable> clazz : Main.constante.ITEM_STATS)
		{
			// we create an new object base. Function on createByString is suppose to made a new object !
			IElementalWritable object;
			try 
			{
				object = clazz.newInstance();
				object.fromByte(buf);
				
				// we add stats here
				stats.add(clazz, object);
			} 
			catch (Throwable throwable) 
			{
				String text = "Can't create value from " + clazz;
				JLog.crashReport(throwable, text);
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		NBTTagCompound itemTag = ((ItemStack)stats.obj).writeToNBT(new NBTTagCompound());
		BufUtils.writeTag(buf, itemTag);
		// ElementalConstante.PLAYER_STATS is suppose to have every IElementalWritable that can be write in buffer
		for(Class<? extends IElementalWritable> clazz : Main.constante.ITEM_STATS)
		{	
			IElementalWritable obj = stats.getStat(clazz);
			obj.toByte(buf);
		}

	}
	
	public static class Handler implements IMessageHandler<ItemStatsPacket, IMessage>
	{

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(ItemStatsPacket message, MessageContext ctx) 
		{
			ItemHelper.registerItem((ItemStack) message.stats.obj, message.stats);
			return null;
		}
		
	}

}