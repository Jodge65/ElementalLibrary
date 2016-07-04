package fr.Jodge.elementalLibrary.common.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jline.internal.Log;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.network.datasync.DataParameter;
import fr.Jodge.elementalLibrary.common.ElementalConstante;
import fr.Jodge.elementalLibrary.common.function.JLog;

public class PlayerStats 
{
	/** integer refers to one of int ElementalConstante.CONSTANTE */
	public Map<Integer, IElementalWritable> value;
	public UUID idEntity;
	
	/** private state that refer to file success (or not) */
	private boolean succes;
	
	public PlayerStats(UUID id) 
	{
		value = new HashMap<Integer, IElementalWritable>();
		succes = true;
		this.idEntity = id;

	}
	/**
	 * read the file and adapt value
	 * @param data <i>File</i> file that refers to the player.
	 */
	public PlayerStats(File data, UUID id) 
	{
		this(id);
		
		
		if(!data.exists())
		{
			// if file not exist, then create new player
			CreateNewPlayer(data);
		}

		// when file exist : read it !
		ReadFromFile(data);
		if(succes)
		{
			JLog.info("Succefuly load data for player.");
		}
		else
		{
			Log.warn("Something wrong happen. Player data will be reset.");
			succes = true;
			CreateNewPlayer(data);
		}

		
	}
	
	
	public PlayerStats add(Integer key, IElementalWritable obj)
	{
		value.put(key, obj);
		return this;
	}
	
	
	/**
	 * Try to use all default constructor available in ElementalConstante.PLAYER_STATS
	 * @param data <i>File</i>
	 */
	protected void CreateNewPlayer(File data) 
	{
		for(Pair<Integer, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
		{

			try 
			{
				String text = coupleOfValue.getValue().write();
				value.put(coupleOfValue.getKey(), coupleOfValue.getValue().createByString(text));
			}  
			catch (Exception e) 
			{
				JLog.error("Can't create and use default constructor for " + coupleOfValue.getValue());
			}
		}
		
		save(data);
	}

	/**
	 * Initialize current object whit value read in file
	 * @param data <i>File</i>
	 */
	protected void ReadFromFile(File data) 
	{
		ObjectInputStream readStream = null;
		try 
		{
			readStream = new ObjectInputStream(new FileInputStream(data));
		} 
		catch (IOException e) 
		{
			succes = false;
			JLog.error("Can't create an ObjectOutputStream for file " + data.getName());
		}
		
		if(succes)
		{
			for(Pair<Integer, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
			{
				String text = null;
				try 
				{
					IElementalWritable obj = coupleOfValue.getValue();
					text = readStream.readUTF();
					IElementalWritable newObject = obj.createByString(text);
					add(coupleOfValue.getKey(), newObject);
				} 
				catch (IOException e)
				{
					JLog.error("Can't create current object with " + text);
				}
			}
		}

	}


	/**
	 * Save the current value in the file
	 * @param data <i>File</i>
	 */
	public void save(File data) 
	{
		// if data already exist, delete.
		if(data.exists())
		{
			data.delete();
		}

		// start save
		try
		{
			data.createNewFile();
		}
		catch (IOException e)
		{
			succes = false;
			JLog.warning("File " + data.getAbsolutePath() + " can't be create.");
		}
		
		// if save is create
		if(succes)
		{
			ObjectOutputStream writeStream = null;

			try 
			{
				writeStream = new ObjectOutputStream(new FileOutputStream(data));
			} 
			catch (IOException e) 
			{
				succes = false;
				JLog.error("Can't create an ObjectOutputStream for file " + data.getName());
			}
			
			// if writer is ready
			if(succes)
			{
				for(Pair<Integer, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
				{		
					
					try 
					{
						IElementalWritable obj = value.get(coupleOfValue.getKey());
						writeStream.writeUTF(obj.write());
					} 
					catch (IOException e)
					{
						JLog.warning("Can't write for current object " + coupleOfValue.getValue().getClass() + " the object must implements IElementalWritable");
					}
				}
				
				if(succes)
				{
					try 
					{
						writeStream.flush();
						writeStream.close();
					} 
					catch (IOException e) 
					{
						succes = false;
						JLog.error("Can't save file " + data.getName());
					}
					if(succes)
					{
						JLog.info("Succefuly save data for player at " + data.getName());

					}
				}
			}
		}
		
	} // end of save

	public Object getStat(int key)
	{
		return value.getOrDefault(key, null);
	}
	
	public String toString()
	{
		String text = "";
		
		for(Pair<Integer, IElementalWritable> coupleOfValue : ElementalConstante.PLAYER_STATS)
		{
			text += getStat(coupleOfValue.getKey()).toString() + "\n";
		}
		
		return text;
	}
}
