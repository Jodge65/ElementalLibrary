package fr.Jodge.elementalLibrary.data.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.stats.MonsterStats;
import fr.Jodge.elementalLibrary.log.ElementalCrashReport;
import fr.Jodge.elementalLibrary.log.JLog;

public class MonsterStatsPacket implements IMessage
{
	MonsterStats stats;
	
	public MonsterStatsPacket(){}

	public MonsterStatsPacket(EntityLivingBase entity)
	{
		JLog.info("New request for monster data : " + entity.getName());
		this.stats = new MonsterStats(entity);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		int id = buf.readInt();
		stats = new MonsterStats(id);
		stats.isDefaultStats = buf.readBoolean();
		//stats.entity = (EntityLivingBase) Minecraft.getMinecraft().theWorld.getEntityByID(id);
		
		for(Class<? extends IElementalWritable> clazz : Main.constante.MONSTER_STATS)
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
				ElementalCrashReport.crashReport(throwable, text);
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(stats.id);
		buf.writeBoolean(stats.isDefaultStats);

		// ElementalConstante.PLAYER_STATS is suppose to have every IElementalWritable that can be write in buffer
		for(Class<? extends IElementalWritable> clazz : Main.constante.MONSTER_STATS)
		{	
			IElementalWritable obj = stats.getStat(clazz);
			obj.toByte(buf);
		}

	}
	
	public static class Handler implements IMessageHandler<MonsterStatsPacket, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MonsterStatsPacket message, MessageContext ctx) 
		{
			// first think needed : take entity
			int idEntity = message.stats.id;
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(idEntity);
			
			// if entity exist / is loaded
			if(entity != null)
			{
				DataHelper.initEntityMatrix(entity, message.stats);
			}
			else
			{
				JLog.warning("Id " + idEntity + " don't give any monster..." );
			}
			return null;
		}
		
	}

}
