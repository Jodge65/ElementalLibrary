package fr.Jodge.elementalLibrary.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import fr.Jodge.elementalLibrary.common.damage.ElementalMatrix;
import fr.Jodge.elementalLibrary.common.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.common.data.IElementalWritable;
import fr.Jodge.elementalLibrary.common.data.PlayerStatsPacket;
import fr.Jodge.elementalLibrary.common.function.JLog;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ElementalConstante 
{
	public static final int DATA_ATK = 0;
	public static final int DATA_RES = 1;

	
	public static List<Map<Class, DataParameter>> STATS;
	public static List<Pair<Integer, IElementalWritable>> PLAYER_STATS;

	/** map of each matrix for entity */
	public static Map<Class, DataParameter> ENTITY_ATK_KEY;
	public static Map<Class, DataParameter> ENTITY_RESIST_KEY;

	
	public static SimpleNetworkWrapper PLAYER_STATS_SOCKET;
	public static String PLAYER_STATS_ID = Main.MODID + ".playerStats";
	public static void onPreLoad()
	{
		JLog.info(" --- PREINIT --- ");
		
		PLAYER_STATS_SOCKET = NetworkRegistry.INSTANCE.newSimpleChannel(PLAYER_STATS_ID);
		PLAYER_STATS_SOCKET.registerMessage(PlayerStatsPacket.Handler.class, PlayerStatsPacket.class, 0, Side.CLIENT);
		JLog.info("New socket have been create for PLAYER_STATS_SOCKET");

		ENTITY_ATK_KEY = new HashMap<Class, DataParameter>();
		JLog.info("New map have been create for ENTITY_ATK_KEY");

		ENTITY_RESIST_KEY = new HashMap<Class, DataParameter>();
		JLog.info("New map have been create for ENTITY_RESIST_KEY");

		PLAYER_STATS = new ArrayList<Pair<Integer, IElementalWritable>>();
		JLog.info("New list have been create for PLAYER_STATS");
		PLAYER_STATS.add(new ImmutablePair<Integer, IElementalWritable>(DATA_ATK, new ElementalMatrix()));
		PLAYER_STATS.add(new ImmutablePair<Integer, IElementalWritable>(DATA_RES, new ElementalMatrix()));
		
		STATS = new ArrayList<Map<Class, DataParameter>>();
		STATS.add(ENTITY_ATK_KEY);
		STATS.add(ENTITY_RESIST_KEY);
		
		
	}
	
	public static void onLoad()
	{
		JLog.info(" --- INIT --- ");

	}
	
	public static void onAfterLoad()
	{
		JLog.info(" --- POSTINIT --- ");

	}

	
	public static DataParameter getDataKeyForEntity(EntityLivingBase target, int dataType) 
	{
		Class key = target.getClass();
		Map<Class, DataParameter> currentMap;
		
		if(STATS.size() >= dataType)
		{
			currentMap = STATS.get(dataType);
		}
		else
		{
			JLog.warning("The data type " + dataType + " is undefined...");
			return null;
		}
		
		if(currentMap.containsKey(key))
		{
			return currentMap.get(key);
		}
		else
		{
			DataParameter value = EntityDataManager.createKey(target.getClass(), ElementalDataSerializers.ELEMENTAL_MATRIX);
			currentMap.put(key, value);
			
			JLog.info("New key have ben create for entity " + target.getClass() + ". Id use is : " + value.getId() + ".");
			
			return value;
		}
		
			
	}
}
