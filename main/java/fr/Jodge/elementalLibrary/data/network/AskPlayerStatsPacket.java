package fr.Jodge.elementalLibrary.data.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AskPlayerStatsPacket implements IMessage
{
	UUID uuidEntity;
	
	public AskPlayerStatsPacket(){}
	
	public AskPlayerStatsPacket(UUID id)
	{
		this.uuidEntity = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.uuidEntity = BufUtils.readUUID(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		BufUtils.writeUUID(buf, uuidEntity);
	}

	public static class Handler implements IMessageHandler<AskPlayerStatsPacket, PlayerStatsPacket>
	{
		// on Server
		@Override
		public PlayerStatsPacket onMessage(AskPlayerStatsPacket message, MessageContext ctx) 
		{
			UUID uuid = message.uuidEntity;
			EntityPlayer player = ctx.getServerHandler().playerEntity.worldObj.getPlayerEntityByUUID(uuid);
			
			// in case player is null, return null. Don't need to answer in this case.
			if(player == null)
				return null;
			else
				return new PlayerStatsPacket(player);

		}
	}
}
