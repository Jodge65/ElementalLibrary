package fr.Jodge.elementalLibrary.data.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AskMonsterStatsPacket implements IMessage
{
	int idEntity;
	
	public AskMonsterStatsPacket(){}
	
	public AskMonsterStatsPacket(int id)
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

	public static class Handler implements IMessageHandler<AskMonsterStatsPacket, MonsterStatsPacket>
	{
		@Override
		public MonsterStatsPacket onMessage(AskMonsterStatsPacket message, MessageContext ctx) 
		{
			int id = message.idEntity;
			Entity entity = ctx.getServerHandler().playerEntity.worldObj.getEntityByID(id);
			
			// in case entity is null, return null. Don't need to answer in this case.
			if(entity == null)
				return null;
			else
				return new MonsterStatsPacket((EntityLivingBase) entity);
		}
	}
}
