package fr.Jodge.elementalLibrary.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.MonsterHelper;
import fr.Jodge.elementalLibrary.data.PlayerHelper;
import fr.Jodge.elementalLibrary.data.ProjectilHelper;
import fr.Jodge.elementalLibrary.data.network.InitDamageSourcePacket;
import fr.Jodge.elementalLibrary.data.network.InitElementPacket;
import fr.Jodge.elementalLibrary.log.JLog;

public class DataEvent
{
	// this need to be trigger faster cause of time needed to send packet to client
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSpawn(EntityJoinWorldEvent event)
	{
		Entity target = event.getEntity();
		/*if(!(target instanceof EntityLivingBase))
		{
			// ignore each entity that this not something alive
			return;
		}
		else */
		
		if(target instanceof EntityFallingBlock) return; // prevent from generation freeze... I hope...
		if(target instanceof EntityXPOrb) return; // prevent from multi kill freeze
		
		if(target instanceof EntityPlayer)
		{
			// player data need to be saved somewhere to be used again
			PlayerHelper.getPlayerHelper((EntityPlayer) target);
		}
		else if(target instanceof EntityLivingBase)
		{
			// monster data was store server side. Whit this, it's possible to personalize data for each map !
			MonsterHelper.initMonster((EntityLivingBase)target);
		}
		else if(target instanceof EntityArrow)
		{
			// monster data was store server side. Whit this, it's possible to personalize data for each map !
			ProjectilHelper.initArrowProjectil((EntityArrow)target);
		}
		else
		{
			// hummm
			JLog.info("Unknown Entity type : " + target.getClass());
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
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onInteract (EntityInteract event)
	{
		ItemStack itemStack = event.getItemStack();
		if(itemStack != null)
		{
			Item item = itemStack.getItem();
			if(item == Items.NAME_TAG) // item instanceof ItemNameTag
			{
				Entity entity = event.getTarget();
				if(entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer))
				{
					((ItemNameTag)item).itemInteractionForEntity(itemStack, event.getEntityPlayer(), (EntityLivingBase)entity, event.getHand());
					MonsterHelper.initMonster((EntityLivingBase) entity);
					event.setCanceled(true);
				}
			}
		}
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
		// Send data SERVER SIDE
		JLog.info("Client Connecting");
		Packet packetElement = Main.constante.STATS_SOCKET.getPacketFrom(new InitElementPacket());
		Packet packetDamageSource = Main.constante.STATS_SOCKET.getPacketFrom(new InitDamageSourcePacket());
		event.getManager().sendPacket(packetElement);
		event.getManager().sendPacket(packetDamageSource);

	}
}
