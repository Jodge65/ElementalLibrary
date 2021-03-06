package fr.Jodge.elementalLibrary.data.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.PlayerHelper;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.stats.PlayerStats;
import fr.Jodge.elementalLibrary.log.ElementalCrashReport;
import fr.Jodge.elementalLibrary.log.JLog;

public class PlayerStatsPacket implements IMessage
{
	PlayerStats stats;
	
	public PlayerStatsPacket(){}

	public PlayerStatsPacket(EntityPlayer entity)
	{
		this.stats = PlayerHelper.getPlayerHelper(entity).stats;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		UUID uuid = BufUtils.readUUID(buf);

		stats = new PlayerStats(uuid);
		
		for(Class<? extends IElementalWritable> clazz : Main.constante.PLAYER_STATS)
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
		BufUtils.writeUUID(buf, stats.uuid);

		// ElementalConstante.PLAYER_STATS is suppose to have every IElementalWritable that can be write in buffer
		for(Class<? extends IElementalWritable> clazz : Main.constante.PLAYER_STATS)
		{	
			IElementalWritable obj = stats.get(clazz);
			obj.toByte(buf);
		}
	}
	
	public static class Handler implements IMessageHandler<PlayerStatsPacket, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(PlayerStatsPacket message, MessageContext ctx) 
		{
			// first think needed : take entity
			UUID uuidEntity = message.stats.uuid;
			EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(uuidEntity);
			
			// if entity exist / is loaded
			if(player != null)
			{
				DataHelper.initEntityMatrix(player, message.stats);
			}
			else
			{
				JLog.warning("UUID " + uuidEntity + " don't give any player..." );
			}
			return null;
		}
		
	}

}
