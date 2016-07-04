package fr.Jodge.elementalLibrary.common.data;

import scala.Int;
import fr.Jodge.elementalLibrary.common.damage.ElementalMatrix;
import fr.Jodge.elementalLibrary.common.function.JLog;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public class ElementalDataSerializers extends DataSerializers
{
	/** Serialize for object ElementalMatrix */
	public static final DataSerializer<ElementalMatrix> ELEMENTAL_MATRIX = new DataSerializer<ElementalMatrix>()
	{
		public void write(PacketBuffer buf, ElementalMatrix value)
	    {
			// we use the string to know the content and create, so we just write this on buffer.
	       	buf.writeString(value.write());
	    }
		
	    public ElementalMatrix read(PacketBuffer buf)
	    {
	    	// the buf give us a string. The constructor can read the line and understand. What else ?
	       	String value = buf.readStringFromBuffer(buf.capacity());
	       	
	   		ElementalMatrix object = new ElementalMatrix(value);
	   		return object;
	   }
	    
	   public DataParameter<ElementalMatrix> createKey(int id)
	   {
		   return new DataParameter(id, this);
	   }
	};
			
	static
	{
		// don't forget to register Jodge ! Don't rage again if he don't work !
		registerSerializer(ELEMENTAL_MATRIX);
	}
}
