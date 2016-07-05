package fr.Jodge.elementalLibrary.common.data;

import scala.Int;
import fr.Jodge.elementalLibrary.common.damage.ElementalMatrix;
import fr.Jodge.elementalLibrary.common.function.JLog;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ElementalDataSerializers extends DataSerializers
{
	/** Serialize for object ElementalMatrix */
	public static final DataSerializer<ElementalMatrix> ELEMENTAL_MATRIX = new DataSerializer<ElementalMatrix>()
	{
		public void write(PacketBuffer buf, ElementalMatrix value)
	    {
			// 
			value.toByte(buf);

	    }
		
	    public ElementalMatrix read(PacketBuffer buf)
	    {
	    	// 
       	
	   		ElementalMatrix obj = new ElementalMatrix();
	   		return (ElementalMatrix) obj.fromByte(buf);
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
