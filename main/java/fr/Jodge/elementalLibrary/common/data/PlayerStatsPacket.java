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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerStatsPacket implements IMessage
{
	PlayerStats stats;
	
	public PlayerStatsPacket()
	{
		
	}
	
	public PlayerStatsPacket(PlayerStats stats)
	{
		this.stats = stats;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		stats = new PlayerStats(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
		
		for(Pair<Integer, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
		{
			// this is use to access the correct createByString method
			IElementalWritable obj = coupleOfValue.getValue();
			
			// read a line in buffer
			
			// initialize the line based on bytebuffer
			String text = ByteBufUtils.readUTF8String(buf);
			
			// we create an new object base. Function on createByString is suppose to made a new object !
			IElementalWritable newObject = obj.createByString(text);
			
			// we add stats here
			stats.add(coupleOfValue.getKey(), newObject);

		}
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeUTF8String(buf, stats.idEntity.toString());

		// ElementalConstante.PLAYER_STATS is suppose to have every IElementalWritable that can be write in buffer
		for(Pair<Integer, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
		{	
			IElementalWritable obj = stats.value.get(coupleOfValue.getKey());
			ByteBufUtils.writeUTF8String(buf, obj.write());
		}

	}
	
	public static class Handler implements IMessageHandler<PlayerStatsPacket, IMessage>
	{

		@Override
		public IMessage onMessage(PlayerStatsPacket message, MessageContext ctx) 
		{
			UUID idEntity = message.stats.idEntity;
			
			List<EntityPlayer> players = null;
			
			// TODO find a better way...
			int i = 0;
			do
			{
				i++;
				if(i % 1000 == 0)
				{
					Minecraft mc = Minecraft.getMinecraft();
					if(mc != null)
					{
						WorldClient world = mc.theWorld;
						if(world != null)
						{
							players = world.playerEntities;
						}
					}

					if(i == 10000)
						break;
				}
				
				
			}		
			while(players == null);
			
			if(players != null)
			{
				for(EntityPlayer player : players)
				{
					if(player.getUniqueID().equals(idEntity))
					{
						DataHelper.initEntityMatrix(player,
								(ElementalMatrix)message.stats.getStat(ElementalConstante.DATA_ATK), 
								(ElementalMatrix)message.stats.getStat(ElementalConstante.DATA_RES));
					}
				}
				JLog.write(message.stats.toString());
			}

			return null;
		}
		
	}

}
