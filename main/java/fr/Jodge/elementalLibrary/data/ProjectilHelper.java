package fr.Jodge.elementalLibrary.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.network.AskMonsterStatsPacket;
import fr.Jodge.elementalLibrary.data.stats.AbstractStats;
import fr.Jodge.elementalLibrary.data.stats.MonsterStats;
import fr.Jodge.elementalLibrary.log.JLog;

public class ProjectilHelper extends DataHelper
{
	public static void initArrowProjectil(EntityArrow arrow) 
	{	
		JLog.info("Ask for EntityArrow stats");
		Entity entityTarget = arrow.shootingEntity;
		if(entityTarget instanceof EntityLivingBase)
		{
			EntityLivingBase target = (EntityLivingBase) entityTarget;
			if(target.hasCustomName() || !Main.constante.DEFAULT_STATS.containsKey(target.getClass()))
			{
				if(!target.worldObj.isRemote) // server side
				{
					MonsterStats stats = new MonsterStats(target);
					DataHelper.initEntityMatrix(arrow, stats);
				}
				else // client side
				{
					Main.constante.STATS_SOCKET.sendToServer(new AskMonsterStatsPacket(target.getEntityId()));
				}
			}
			else
			{
				AbstractStats stats = Main.constante.DEFAULT_STATS.get(target.getClass());
				DataHelper.initEntityMatrix(arrow, stats);
			}
		}
	}
}