package fr.Jodge.elementalLibrary.data.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AskItemStatsPacket implements IMessage
{
	ItemStack stack;
	
	public AskItemStatsPacket(){}
	
	public AskItemStatsPacket(ItemStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		NBTTagCompound itemTag = BufUtils.readTag(buf);
		this.stack = ItemStack.loadItemStackFromNBT(itemTag);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		BufUtils.writeTag(buf, stack.writeToNBT(new NBTTagCompound()));
	}

	public static class Handler implements IMessageHandler<AskItemStatsPacket, ItemStatsPacket>
	{
		@Override
		public ItemStatsPacket onMessage(AskItemStatsPacket message, MessageContext ctx) 
		{
			MinecraftServer server = ctx.getServerHandler().playerEntity.getServer();
			return new ItemStatsPacket(message.stack, server);
		}
	}
}
