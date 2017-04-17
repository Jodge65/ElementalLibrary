package fr.Jodge.elementalLibrary.area;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.google.common.collect.Lists;


public class AreaHelper
{
	/**
	 *  * an entity can be put instead of this 2. In this case Height can't be give : it will be adapt if entity is sneaking or not.
	 * @param pos1 (BlockPos or 3 double) *
	 * @param pos2 (BlockPos or 3 double) *
	 * 
	 * @param rayon (double) ran of circle
	 * @param height (double - optional) height of circle. If empty : 1.0D (one block)
	 * @return(AxisAlignedBB) minecraftArea (cirle)
	 */
	public static AxisAlignedBB circleArea(double x1, double y1, double z1, double x2, double y2, double z2, double rayon, double height)
	{
		return new AxisAlignedBB(x1, y1 + height, z1, x2 + 1.0D, y2 + height + 1.0D, z2 + 1.0D).expand(rayon, 0.0D, rayon);
	}
	public static AxisAlignedBB circleArea(double x1, double y1, double z1, double x2, double y2, double z2, double rayon)
	{
		return circleArea(x1, y1, z1, x2, y2, z2, rayon, 1.0D);
	}
	public static AxisAlignedBB circleArea(BlockPos pos1, BlockPos pos2, double rayon, double height)
	{
		return circleArea(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos1.getY(), pos1.getZ(), rayon, height);
	}
	public static AxisAlignedBB circleArea(BlockPos pos1, BlockPos pos2, double rayon)
	{
		return circleArea(pos1, pos2, rayon, 1.0D);
	}
	public static AxisAlignedBB circleArea(BlockPos pos1, double rayon)
	{
		return circleArea(pos1, pos1, rayon, 1.0D);
	}
	public static AxisAlignedBB circleArea(BlockPos pos1, double rayon, double height)
	{
		return circleArea(pos1.getX(), pos1.getY(), pos1.getZ(), pos1.getX(), pos1.getY(), pos1.getZ(), rayon, height);
	}
	public static AxisAlignedBB circleArea(double x1, double y1, double z1, double rayon)
	{
		return circleArea(x1, y1, z1, x1, y1, z1, rayon, 1.0D);
	}
	public static AxisAlignedBB circleArea(double x1, double y1, double z1, double rayon, double height)
	{
		return circleArea(x1, y1, z1, x1, y1, z1, rayon, height);
	}
	public static AxisAlignedBB circleArea(Entity entity, double rayon)
	{
		if (entity.isSneaking())
			return circleArea(entity.posX, entity.posY, entity.posZ, entity.posX, entity.posY, entity.posZ, rayon, 0.5D);
		else
			return circleArea(entity.posX, entity.posY, entity.posZ, entity.posX, entity.posY, entity.posZ, rayon);
	}

	/**
	 * 
	 * @param pos1 (BlockPos or 3 double) 
	 * @param pos2 (BlockPos or 3 double) 
	 * @param height (double - optional) height of circle. If empty : 1.0D (one block)
	 * @return(AxisAlignedBB) minecraftArea (square)
	 */
	public static AxisAlignedBB squareArea(double x1, double y1, double z1, double x2, double y2, double z2, double height)
	{
		return new AxisAlignedBB(x1, y1 + height, z1, x2 + 1.0D, y2 + height + 1.0D, z2 + 1.0D);
	}
	public static AxisAlignedBB squareArea(BlockPos pos1, BlockPos pos2, double height)
	{
		return squareArea(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos1.getY(), pos1.getZ(), height);
	}
	public static AxisAlignedBB squareArea(BlockPos pos1, BlockPos pos2)
	{
		return squareArea(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos1.getY(), pos1.getZ(), 1.0D);
	}
	public static AxisAlignedBB squareArea(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		return squareArea(x1, y1, z1, x2, y2, z2, 1.0D);
	}
	
	/**
	 * 
	 * @param player (EntityPlayer) player which use the function
	 * @param hitArea (AxisAligneBB) area of effect
	 * @return (List) all entity in area
	 */
	public static List getEntityInArea(EntityPlayer player, AxisAlignedBB hitArea)
	{
		return player.worldObj.getEntitiesWithinAABBExcludingEntity(player, hitArea);
	}
	public static List getEntityInArea(EntityPlayer player, double range)
	{
		return player.worldObj.getEntitiesWithinAABBExcludingEntity(player, circleArea(player, range));
	}	
	
	/**
	 * add the sub function, which is not available on basic function....
	 * @param vector (Vec3)
	 * @param multiplier (double)
	 * @return Vec3)
	 */
    public static Vec3d subVector(Vec3d vector, double multiplier)
    {
    	while(multiplier > 1.0D)
    	{
    		vector.addVector(vector.xCoord, 0, vector.zCoord);
    		multiplier --;
    	}
    	if(multiplier != 0)
    	{
    		vector.addVector(vector.xCoord * multiplier, 0, vector.zCoord * multiplier);
    	}
        return vector;
    }
	
    
	public static List getEntitySquareArea(EntityPlayer player, double range)
	{
        ArrayList<Entity> listToFill = Lists.newArrayList();
		BlockPos playerPos = player.getPosition();
		
		double hyp = Math.sqrt(2 * Math.pow(range, 2));
		Vec3d entityView = subVector(player.getLook(1.0F).normalize(), hyp);
		
		double xVector = entityView.xCoord * hyp;
		//double yVector = entityView.yCoord * hyp;
		double zVector = entityView.zCoord * hyp;
		
		double xDebut = playerPos.getX() - entityView.xCoord;
		double yDebut = playerPos.getY();// - entityView.yCoord;
		double zDebut = playerPos.getZ() - entityView.zCoord;
		
		double xFinal = xVector + playerPos.getX();
		double yFinal = playerPos.getY() + 1.0D;
		double zFinal = zVector + playerPos.getZ();

		AxisAlignedBB area = new AxisAlignedBB(xDebut, yDebut, zDebut, xFinal, yFinal, zFinal);

		List entityList = getEntityInArea(player, area);
	
		for (int i = 0; i < entityList.size(); i++)
		{
			if ((entityList.get(i) instanceof EntityLivingBase))
			{
				Entity entity = (Entity) entityList.get(i);
				if(player.getDistanceToEntity(entity) < range)
				{
					listToFill.add(entity);
				}
			}
		}
		
		return listToFill;
	}
	
}
