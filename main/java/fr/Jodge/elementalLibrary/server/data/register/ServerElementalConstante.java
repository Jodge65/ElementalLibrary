package fr.Jodge.elementalLibrary.server.data.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.datasync.DataParameter;
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
import fr.Jodge.elementalLibrary.data.register.CommonElementalConstante;
import fr.Jodge.elementalLibrary.data.register.Register;
import fr.Jodge.elementalLibrary.function.JLog;

/**
 * Server class for Constante
 * @author Jodge
 *
 */
public class ServerElementalConstante extends CommonElementalConstante
{
	/**
	 * What you need to do : 
	 * - Refers each kind of existing stats
	 */
	@Override
	public void onPreLoad()
	{
		JLog.info("Welcom Server Side");
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

	@Override
	public void onServerStart()
	{
		JLog.info(" --- SERVER START --- ");
		VanillaInitialization.initElement();
	}
	
	/**
	 * send data to client
	 */
	@Override
	public void onClientJoin()
	{
		
	}
	
	/**
	 * Nothing to do on server side
	 */
	@Override
	public void onClientExit(){}
	
	


}
