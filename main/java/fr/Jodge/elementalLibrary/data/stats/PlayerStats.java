package fr.Jodge.elementalLibrary.data.stats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jline.internal.Log;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.server.MinecraftServer;
import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.function.JLog;

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
