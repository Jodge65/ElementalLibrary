package fr.Jodge.elementalLibrary.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.network.AskMonsterStatsPacket;
import fr.Jodge.elementalLibrary.data.stats.AbstractStats;
import fr.Jodge.elementalLibrary.data.stats.MonsterStats;
import fr.Jodge.elementalLibrary.function.JLog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class MonsterHelper extends DataHelper
{
	public static void initMonster(EntityLivingBase target) 
	{	
		if(target.hasCustomName() || !Main.constante.DEFAULT_STATS.containsKey(target.getClass()))
		{
			if(!target.worldObj.isRemote) // server side
			{			
				MonsterStats stats = new MonsterStats(target);
				DataHelper.initEntityMatrix(target, stats);
			}
			else // client side
			{
				Main.constante.STATS_SOCKET.sendToServer(new AskMonsterStatsPacket(target.getEntityId()));
			}
		}
		else
		{
			AbstractStats stats = Main.constante.DEFAULT_STATS.get(target.getClass());
			DataHelper.initEntityMatrix(target, stats);
		}
	}

}
