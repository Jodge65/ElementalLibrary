package fr.Jodge.elementalLibrary.client.data.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.entity.AbstractStats;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.network.AskMonsterStatsPacket;
import fr.Jodge.elementalLibrary.data.network.AskPlayerStatsPacket;
import fr.Jodge.elementalLibrary.data.network.MonsterStatsPacket;
import fr.Jodge.elementalLibrary.data.network.PlayerStatsPacket;
import fr.Jodge.elementalLibrary.data.register.Register;
import fr.Jodge.elementalLibrary.function.JLog;
import fr.Jodge.elementalLibrary.server.data.register.CommonElementalConstante;
import fr.Jodge.elementalLibrary.server.data.register.VanillaInitialization;

/**
 * Client class for Constante
 * @author Jodge
 *
 */
public class ClientElementalConstante extends CommonElementalConstante
{
	/**
	 * What you need to do : 
	 * - Refers each kind of existing stats
	 */
	@Override
	public void onPreLoad()
	{
		super.onPreLoad();
	}
	
	/**
	 * What you need to do : 
	 * - add/remove element
	 */
	@Override
	public void onLoad()
	{
		super.onLoad();
				
	}
	
	/**
	 * 
	 */
	@Override
	public void onAfterLoad()
	{
		super.onAfterLoad();
	}	

	/**
	 * purge old data
	 */
	@Override
	public void onClientExit()
	{
		JLog.info(" --- START PURGE --- ");
		DEFAULT_STATS.clear();
		JLog.info("Default stats clear.");
		Element.reset();
		JLog.info("Element clear.");

		JLog.info(" --- END PURGE --- ");
	}
	
	
	/**
	 * Nothing special to do on client side, but needed for integrated server...
	 */
	@Override
	public void onServerStart(MinecraftServer server)
	{
		super.onServerStart(server);
	}
	
}
