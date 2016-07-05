package fr.Jodge.elementalLibrary.common.data;

import fr.Jodge.elementalLibrary.common.ElementalConstante;
import fr.Jodge.elementalLibrary.common.damage.ElementalMatrix;
import fr.Jodge.elementalLibrary.common.function.JLog;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AskPlayerStatsPacket  implements IMessage
{
	int idEntity;
	
	public AskPlayerStatsPacket(){}
	
	public AskPlayerStatsPacket(int id)
	{
		this.idEntity = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.idEntity = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(idEntity);
	}

	public static class Handler implements IMessageHandler<AskPlayerStatsPacket, PlayerStatsPacket>
	{
		@Override
		public PlayerStatsPacket onMessage(AskPlayerStatsPacket message, MessageContext ctx) 
		{
			int id = message.idEntity;
			Entity player = ctx.getServerHandler().playerEntity.worldObj.getEntityByID(id);
			
			if(player instanceof EntityPlayer)
				return new PlayerStatsPacket((EntityPlayer)player);
			else
				return null;
		}
		
	}

}
