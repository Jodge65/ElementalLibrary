package fr.Jodge.elementalLibrary.event;

import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraftforge.fml.relauncher.Side;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.MonsterHelper;
import fr.Jodge.elementalLibrary.data.PlayerHelper;
import fr.Jodge.elementalLibrary.data.entity.AbstractStats;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.network.InitElementPacket;
import fr.Jodge.elementalLibrary.data.network.PlayerStatsPacket;
import fr.Jodge.elementalLibrary.function.JLog;

public class DataEvent
{
	// this need to be trigger faster cause of time needed to send packet to client
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSpawn(EntityJoinWorldEvent event)
	{
		Entity target = event.getEntity();
		if(!(target instanceof EntityLivingBase))
		{
			// ignore each entity that this not something alive
			return;
		}
		else if(target instanceof EntityPlayer)
		{
			// player data need to be saved somewhere to be used again
			PlayerHelper.getPlayerHelper((EntityPlayer) target);
		}
		else
		{
			// monster data was store server side. Whit this, it's possible to personalize data for each map !
			MonsterHelper.initMonster((EntityLivingBase)target);
		}
	}
	
    public void onServerStopping(FMLServerStoppingEvent event) 
	{
        if(FMLCommonHandler.instance().getSide() == Side.SERVER) // Server only 
        {
            // save everything
        	for (PlayerHelper helper : PlayerHelper.allPlayer.values()) 
        	{
        	    if(!helper.save())
        	    {
        	    	JLog.alert("Current Helper for " + helper.player.getName() + " has trouble and can't be save...");
        	    }
        	}
        	// not needed on server only cause game was close after, but needed on integrated server.
        	PlayerHelper.allPlayer.clear();
        }
    }
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
		// save player data
		EntityPlayer player = event.player;
		if(!player.worldObj.isRemote) // server side
		{
			PlayerHelper helper = PlayerHelper.getPlayerHelper(player);
			helper.exit();
		}
	}
	
	public void onInteract (EntityInteractSpecific event)
	{
		// TODO add NameTag compatibility
	}
	
	@SubscribeEvent
	public void onClientDisconnectionFromServer(ClientDisconnectionFromServerEvent event) 
	{
        // purge constant
		Main.constante.onClientExit();
	}
	
	@SubscribeEvent
	public void onClientConnectToServer(ServerConnectionFromClientEvent event)
	{
		JLog.info("Client Connecting");
		Packet packetIn = Main.constante.STATS_SOCKET.getPacketFrom(new InitElementPacket());
		event.getManager().sendPacket(packetIn);
		// give data (SERVER SIDE)
		Main.constante.onClientJoin();
	}
}
