package fr.Jodge.elementalLibrary.common.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.Jodge.elementalLibrary.common.damage.ElementalMatrix;
import fr.Jodge.elementalLibrary.common.damage.IMonsterMatrix;

public class DataEvent 
{
	@SubscribeEvent(priority = EventPriority.NORMAL)
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
			PlayerHelper.initPlayer((EntityPlayer) target);
		}
		else
		{
			// each kind of entity alive that is not a player
			ElementalMatrix resistMatrix;
			ElementalMatrix atkMatrix;
			
			if(target instanceof IMonsterMatrix)
			{
				atkMatrix = ((IMonsterMatrix)target).getAtkMatrix();
				resistMatrix = ((IMonsterMatrix)target).getResistMatrix();
			}
			else // if it's a monster that not include ElementLibrary
			{
				atkMatrix = new ElementalMatrix(0.9F);
				resistMatrix = new ElementalMatrix();
				atkMatrix.autoUpdateAtk(target);
				resistMatrix.autoUpdateResist(target);
			}
			
			DataHelper.initEntityMatrix((EntityLivingBase)target, atkMatrix, resistMatrix);
		}
		
	}
	
}
