package fr.Jodge.elementalLibrary.data.register;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.network.datasync.DataParameter;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.stats.AbstractStats;
import fr.Jodge.elementalLibrary.data.stats.ItemStats;

public class Variable 
{
	// FROM HERE, FIX
	/** Map of each kind of existing stats */
	public static Map<Class, Map<Class, DataParameter>> STATS;
	
	/** List of stats available for player. */
	public static List<Class<? extends IElementalWritable>> PLAYER_STATS;
	/** List of stats available for monster. */
	public static List<Class<? extends IElementalWritable>> MONSTER_STATS;
	/** List of stats available for item. Some stats can be empty like shield that only work on armor */
	public static List<Class<? extends IElementalWritable>> ITEM_STATS;
	
	/** default stats socket use by this mod */
	public static SimpleNetworkWrapper STATS_SOCKET;
	public static String STATS_ID = Main.MODID;
	
	// FROM HERE, CHANGE DURING EXECUTE
	/** Save Default Stats to use it again instead of ask server/read file  */
	public static Map<Class, AbstractStats> DEFAULT_STATS;

	/** Save Default Stats only for item to use it again instead of ask server/read file  */
	public static Map<String, ItemStats> DEFAULT_ITEM_STATS;

	/** Default element or each kind of damage source */
	public static Map<String, Element> DEFAULT_ELEMENT_DAMAGE_SOURCES;
	
	/** */
	public static Map<Class, Map<Class, IElementalWritable>> VALUE_REGISTER;

}
