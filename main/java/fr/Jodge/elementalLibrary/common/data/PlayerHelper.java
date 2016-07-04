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
	public static final String folder = "playerdata\\";
	public static final String extention = ".eld";
	
	public static void initPlayer(EntityPlayer target) 
	{

		if(!target.worldObj.isRemote) // server side
		{
			MinecraftServer server = target.getServer();
			String url = folder + target.getUniqueID() + extention;

			File data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
			PlayerStats stats;

			stats = new PlayerStats(data, target.getUniqueID());
			
			ElementalMatrix atkMatrix = (ElementalMatrix)stats.getStat(ElementalConstante.DATA_ATK);
			ElementalMatrix resistMatrix = (ElementalMatrix)stats.getStat(ElementalConstante.DATA_RES);
			DataHelper.initEntityMatrix(target, atkMatrix, resistMatrix);
			
			ElementalConstante.PLAYER_STATS_SOCKET.sendToAll(new PlayerStatsPacket(stats));
		}
		else // client side
		{
			/*atkMatrix = new ElementalMatrix();
			resistMatrix = new ElementalMatrix();
			atkMatrix.autoUpdateAtk(target);
			resistMatrix.autoUpdateResist(target);*/
		}

	}

}
