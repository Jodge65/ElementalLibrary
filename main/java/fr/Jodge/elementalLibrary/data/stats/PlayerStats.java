package fr.Jodge.elementalLibrary.data.stats;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.Main;

public class PlayerStats extends AbstractStats
{	
	public UUID uuid;
	
	public static final String folder = "playerdata\\";
	
	public PlayerStats(UUID uuid) 
	{
		super(Main.constante.PLAYER_STATS);
		this.uuid = uuid;
	}
	
	public PlayerStats(EntityPlayer entity) 
	{
		this(entity.getUniqueID());
		this.obj = entity;
		MinecraftServer server = entity.getServer();
		String url = folder + entity.getUniqueID() + ElementalConfiguration.EXTENTION;
		data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
		makeByFile();
	}

}
