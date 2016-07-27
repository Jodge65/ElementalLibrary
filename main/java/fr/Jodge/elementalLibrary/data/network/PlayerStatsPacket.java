package fr.Jodge.elementalLibrary.data.network;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;

import fr.Jodge.elementalLibrary.ElementalConstante;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.PlayerHelper;
import fr.Jodge.elementalLibrary.data.entity.PlayerStats;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.function.JLog;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		
		for(Pair<Class, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
		{
			// this is use to access the correct createByString method
			IElementalWritable obj = coupleOfValue.getValue();
			
			// we create an new object base. Function on createByString is suppose to made a new object !
			IElementalWritable newObject;
			try 
			{
				newObject = obj.getClass().getConstructor().newInstance();
		   		newObject.fromByte(buf);
				
				// we add stats here
				stats.add(coupleOfValue.getKey(), newObject);
			} 
			catch (Throwable throwable) 
			{
				String text = "Can't create value from " + obj.getClass();
				JLog.crashReport(throwable, text);
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		BufUtils.writeUUID(buf, stats.uuid);

		// ElementalConstante.PLAYER_STATS is suppose to have every IElementalWritable that can be write in buffer
		for(Pair<Class, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
		{	
			IElementalWritable obj = stats.get(coupleOfValue.getKey());
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
