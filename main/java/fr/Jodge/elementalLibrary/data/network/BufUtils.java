package fr.Jodge.elementalLibrary.data.network;

import fr.Jodge.elementalLibrary.log.ElementalCrashReport;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraftforge.fml.common.network.ByteBufUtils;

public class BufUtils extends ByteBufUtils
{
	/**
     * Read a UUID  from the byte buffer.
     *
     * @param from <i>ByteBuf</i> The buffer to read from
     * @return <i>UUID</i> The UUID
     */
    public static UUID readUUID(ByteBuf from)
    {
    	String stringUUID = ByteBufUtils.readUTF8String(from);
        return UUID.fromString(stringUUID);
    }

    /**
     * Write a UUID to the buffer.
     * 
     * @param to <i>ByteBuf</i> the buffer to write in
     * @param <i>UUID</i> The UUID to write
     */
    public static void writeUUID(ByteBuf to, UUID uuid)
    {
    	String stringUUID = uuid.toString();
    	ByteBufUtils.writeUTF8String(to, stringUUID);
    }
    
    
    /**
     * @param from <i>ByteBuf</i> The buffer to read from
     * @return <i>Class</i> the class
     */
    public static Class readClass(ByteBuf from)
    {
    	String stringClass = ByteBufUtils.readUTF8String(from);
    	Class returnValue = null;
    	try 
    	{
			returnValue = Class.forName(stringClass);
		} 
    	catch (Throwable throwable) 
    	{
    		String text = "And error occure when try to make a class whit string : " + stringClass;
    		ElementalCrashReport.crashReport(throwable, text);
		}
    	
    	return returnValue;
    }
    
    /**
     * 
     * @param to <i>ByteBuf</i> the buffer to write in
     * @param c <i>Class</i> the class
     */
    public static void writeClass(ByteBuf to, Class c)
    {
    	String stringClass = c.getName();
    	ByteBufUtils.writeUTF8String(to, stringClass);
    }
}
