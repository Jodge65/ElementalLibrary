package fr.Jodge.elementalLibrary.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.entity.PlayerStats;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.network.AskPlayerStatsPacket;
import fr.Jodge.elementalLibrary.function.JLog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerHelper extends DataHelper
{
	/** List on allPlayer. /!\ Empty client side  */
	public static Map<UUID, PlayerHelper> allPlayer = new HashMap<UUID, PlayerHelper>();

	public PlayerStats stats;
	public EntityPlayer player;
	
	
	/**
	 * Create new player.
	 * @param target <i>EntityPlayer</i> target entity
	 */
	protected PlayerHelper(EntityPlayer target)
	{
		this.player = target;
		
		if(!target.worldObj.isRemote) // server side
		{
			allPlayer.put(player.getUniqueID(), this);
			stats = new PlayerStats(target);
			DataHelper.initEntityMatrix(target, stats);
		}
		else // client side
		{
			Main.constante.STATS_SOCKET.sendToServer(new AskPlayerStatsPacket(target.getUniqueID()));
		}
	}
	
	/**
	 * Create a new player and pass through all security
	 * @param target <i>EntityPlayer</i> target entity
	 * @return instance of PlayerHelper for this player
	 */
	public static PlayerHelper createNewPlayer(EntityPlayer target)
	{
		return new PlayerHelper(target);
	}
	
	/**
	 * Return PlayerHelper for asked Entity. <br>
	 * On Client Side a new playerHelper will be create each time <br>
	 * On Server Side, if player exist, then is current playerHelper will be send. If not, it will be create
	 * @param target <i>EntityPlayer</i> target entity
	 * @return instance of PlayerHelper for this player
	 */
	public static PlayerHelper getPlayerHelper(EntityPlayer target)
	{
		UUID uuid = target.getUniqueID();

		if(!target.worldObj.isRemote) // server side
		{
			if(allPlayer.containsKey(uuid))
			{
				// server side, try to get value in list.
				return allPlayer.get(uuid);
			}
		}
		
		// if client side, or if server but value not exist then return new.
		return createNewPlayer(target);
	}

	/**
	 * Force save of current PlayerStats and remove from list current player.
	 * @return <i>boolean</i> return false if something wrong happen on save.
	 */
	public boolean exit()
	{
		boolean itWork = save();
		PlayerHelper.allPlayer.remove(player.getUniqueID());
		return itWork;
	}
	
	/**
	 * Force save of current PlayerStats.
	 * @return <i>boolean</i> return false if something wrong happen.
	 */
	public boolean save()
	{
		return stats.save();
	}
	

	
}
