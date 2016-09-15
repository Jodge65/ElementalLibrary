package fr.Jodge.elementalLibrary.data.network;

import fr.Jodge.elementalLibrary.data.register.Variable;
import fr.Jodge.elementalLibrary.log.JLog;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
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
		if(buf.readBoolean()) // need to recreate stack
		{
			String itemName = BufUtils.readUTF8String(buf);
			
			ItemStack stack = new ItemStack(new Item().setUnlocalizedName(itemName));
			
			if(buf.readBoolean()) // stack as custom matrix name
			{
				String matrixName = BufUtils.readUTF8String(buf);
				stack.getTagCompound().setString(Variable.DEFAULT_MATRIX_KEY, matrixName);
			}

		}
		else // can read stack from nbt
		{
			NBTTagCompound itemTag = BufUtils.readTag(buf);
			
			this.stack = ItemStack.loadItemStackFromNBT(itemTag);
			if(stack == null)
				JLog.error("Somethink wrong happen when try to load item for NBTTag...");
		}

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if(Item.getByNameOrId(tags.getString("id")) == null)
		{
			buf.writeBoolean(true);
			BufUtils.writeUTF8String(buf, stack.getItem().getUnlocalizedName());
			
			if(tags.hasKey(Variable.DEFAULT_MATRIX_KEY))
			{
				buf.writeBoolean(true);
				String matrixName = tags.getString(Variable.DEFAULT_MATRIX_KEY);
				BufUtils.writeUTF8String(buf, matrixName);
			}
			else
			{
				buf.writeBoolean(false);
			}
		}
		else
		{
			buf.writeBoolean(false);
			BufUtils.writeTag(buf, stack.writeToNBT(new NBTTagCompound()));

		}
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
