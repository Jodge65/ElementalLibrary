package fr.Jodge.elementalLibrary.common.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import fr.Jodge.elementalLibrary.common.ElementalConstante;
import fr.Jodge.elementalLibrary.common.damage.ElementalMatrix;
import fr.Jodge.elementalLibrary.common.function.JLog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PlayerHelper extends DataHelper
{

	public static void initPlayer(EntityPlayer target) 
	{
		//JLog.write("### Entity : "  + target.getName() + ":" + target.getEntityId() + ", instance of " + target.getClass());
		if(!target.worldObj.isRemote) // server side
		{
			PlayerStats stats;

			stats = new PlayerStats(target);
			
			ElementalMatrix atkMatrix = (ElementalMatrix)stats.getStat(ElementalConstante.DATA_ATK);
			ElementalMatrix resistMatrix = (ElementalMatrix)stats.getStat(ElementalConstante.DATA_RES);
			DataHelper.initEntityMatrix(target, atkMatrix, resistMatrix);
			//ElementalConstante.PLAYER_STATS_SOCKET.sendToAll(new PlayerStatsPacket(stats));
		}
		else // client side
		{
			ElementalConstante.PLAYER_STATS_SOCKET.sendToServer(new AskPlayerStatsPacket(target.getEntityId()));
		}

	}

}
