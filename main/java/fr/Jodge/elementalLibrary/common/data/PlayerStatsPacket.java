package fr.Jodge.elementalLibrary.common.data;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;

import fr.Jodge.elementalLibrary.common.ElementalConstante;
import fr.Jodge.elementalLibrary.common.damage.ElementalMatrix;
import fr.Jodge.elementalLibrary.common.function.JLog;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
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
		this.stats = new PlayerStats(entity);
	}
	
	public PlayerStatsPacket(PlayerStats stats)
	{
		this.stats = stats;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		stats = new PlayerStats(buf.readInt());
		
		for(Pair<Integer, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
		{
			// this is use to access the correct createByString method
			IElementalWritable obj = coupleOfValue.getValue();
			
			// we create an new object base. Function on createByString is suppose to made a new object !
			IElementalWritable newObject = obj.fromByte(buf);
			
			// we add stats here
			stats.add(coupleOfValue.getKey(), newObject);

		}
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(stats.idEntity);

		// ElementalConstante.PLAYER_STATS is suppose to have every IElementalWritable that can be write in buffer
		for(Pair<Integer, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
		{	
			IElementalWritable obj = stats.value.get(coupleOfValue.getKey());
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
			int idEntity = message.stats.idEntity;
			Entity player = Minecraft.getMinecraft().theWorld.getEntityByID(idEntity);;
			
			// if entity exist / is loaded
			if(player != null)
			{
				// check if it's a plyer (it's suppose to be a player each time...
				if(player instanceof EntityPlayer)
				{
					DataHelper.initEntityMatrix((EntityPlayer)player,
							(ElementalMatrix)message.stats.getStat(ElementalConstante.DATA_ATK), 
							(ElementalMatrix)message.stats.getStat(ElementalConstante.DATA_RES));
				}
				else
				{
					JLog.warning("Id " + idEntity + " was give to " + player.getName() + " which is instance of " + player.getClass());
				}
			}
			else
			{
				JLog.warning("Id " + idEntity + " don't give any entity..." );
			}
			return null;
		}
		
	}

}
