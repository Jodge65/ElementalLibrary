package fr.Jodge.elementalLibrary.data;

import java.lang.reflect.InvocationTargetException;

import scala.Int;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.network.BufUtils;
import fr.Jodge.elementalLibrary.data.register.ElementalConstante;
import fr.Jodge.elementalLibrary.function.JLog;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ElementalDataSerializers extends DataSerializers
{
	/** Serialize for object ElementalMatrix */
	public static final DataSerializer<IElementalWritable> ELEMENTAL_SERIALIZER = new DataSerializer<IElementalWritable>()
	{
		@Override
		public void write(PacketBuffer buf, IElementalWritable value)
	    {
			BufUtils.writeClass(buf, value.getClass());
			value.toByte(buf);
	    }
		
	    @Override
		public IElementalWritable read(PacketBuffer buf)
	    {
	    	Class unknowClass = BufUtils.readClass(buf);
	    	IElementalWritable obj;
			try 
			{
				obj = (IElementalWritable) unknowClass.getConstructor().newInstance();
				obj.fromByte(buf);
		   		return obj;
			}
			catch (Throwable throwable) 
			{
				String text = "Can't create new instance from " + unknowClass;
				JLog.crashReport(throwable, text);
				return null;
			}
	    }
	    
	   @Override
	public DataParameter<IElementalWritable> createKey(int id)
	   {
		   return new DataParameter(id, this);
	   }
	};
			
	static
	{
		// don't forget to register Jodge ! Don't rage again if he don't work !
		registerSerializer(ELEMENTAL_SERIALIZER);
	}
}
