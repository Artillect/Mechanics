package com.pfaeff_and_cdkrot.util;

import java.util.List;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.pfaeff_and_cdkrot.ForgeMod;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Utility
{
	//a BIG utility class
	@Deprecated
	private static veci3 dirVectorsi[];
	private static java.lang.reflect.Method EntityFallMethod;
	
	static
	{
		dirVectorsi = new veci3[]
		{
		new veci3(0, -1, 0), new veci3(0, +1, 0),
		new veci3(0, 0, -1), new veci3(0, 0, +1),
		new veci3(-1, 0, 0), new veci3(+1, 0, 0)
		};
		try
		{
			EntityFallMethod = Entity.class.
					getDeclaredMethod("fall", float.class);
			EntityFallMethod.setAccessible(true);
		} catch (Exception e)
		{
			try
			{
				EntityFallMethod = Entity.class.
						getDeclaredMethod("func_70069_a", float.class);
				EntityFallMethod.setAccessible(true);
			}
			catch (Exception ex)
			{
				throw new RuntimeException("FATAL INIT EXCEPTION", ex);
			}
		}
	}
	
	//Get side opposite to i
    public static int getOppositeSide(int i)
    {
    	switch(i) {
	    	case 0: return 1; //UP: DOWN
	    	case 1: return 0; //DOWN: UP
	    	case 2: return 3; //NORTH (Z_NEG): SOUTH (Z_POS)
	    	case 3: return 2; //SOUTH (Z_POS): NORTH (Z_NEG)
	    	case 4: return 5; //EAST  (X_NEG): WEST  (X_POS)
	    	case 5: return 4; //WEST  (X_POS): EAST  (X_NEG)
    	}
    	return 0;//illegal argument
    }
    
    /**
     * This will return suggested metadata(Side) for block which placed on side
     */
    public static int getMetadataForBlockSidePlaced(float rotationYaw)
    {
        //int l = MathHelper.floor_double((double)((rotationYaw * 4F) / 360F) + 0.5D) & 3;
        int l = MathHelper.floor_double((double)(rotationYaw / 90F) + 0.5D) & 3;//four values
        
		//if (l == 0)
			//return 2;
		if (l == 1)
			return 5;
		if (l == 2) 
			return 3;
		if (l == 3) 
			return 4;
		return 2;
    }
    
    /**
     * This will return suggested metadata(Side) for block which placed on any side
     * Mostly copypaste of BlockPistonBase.determineOrientation
     */
    public static int getMetadataForBlockAnyPlaced(World world, int x, int y, int z, EntityLivingBase entity)
    {
        if (MathHelper.abs((float)entity.posX - (float)x) < 2.0F && MathHelper.abs((float)entity.posZ - (float)z) < 2.0F)
        {
            double d0 = entity.posY + 1.82D - (double)entity.yOffset;

            if (d0 - (double)y > 2.0D)
            {
                return 1;
            }

            if ((double)y - d0 > 0.0D)
            {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }
    /**
     * Get a directional vector for specified meta (side);
     * @param meta
     * @return
     */
    @Deprecated
    public static veci3 getDirectionVectorFori(int meta)
    {
    	return ((meta>=0)&&(meta<=6)) ? dirVectorsi[meta] : null;
    }
    
    /**
     * if use veci3 as a x,y,z container it can be required to swap components to get
     * components of one vec strict more than of other.
     * Performes the routine
     * @param tomin vec getting min
     * @param tomax vec getting max
     */
    public static void SwapVectorsComponentsi(veci3 tomin, veci3 tomax)
    {
    	int t;
    	if (tomin.x > tomax.x)
    	{
    		t=tomin.x; tomin.x = tomax.x; tomax.x = t;
    	}
    	if (tomin.y > tomax.y)
    	{
    		t=tomin.y; tomin.y = tomax.y; tomax.y = t;
    	}
    	if (tomin.z > tomax.z)
    	{
    		t=tomin.z; tomin.z = tomax.z; tomax.z = t;
    	}
    }
    
    public static AxisAlignedBB SelectPoolBasingOnVectorAndInc(veci3 base, dirvec dirvec)
    {
    	if (dirvec.isFacingNegative)
    		return AxisAlignedBB.getBoundingBox(base.x+dirvec.x, base.y+dirvec.y, base.z+dirvec.z,
    							base.x, base.y, base.z);
    	else
    		return AxisAlignedBB.getBoundingBox(base.x, base.y, base.z,
    				base.x+dirvec.x, base.y+dirvec.y, base.z+dirvec.z);
    }
    
    public static int getDefaultDirectionsMeta(World world, int x, int y, int z)
    {    	
    	int front = world.getBlockId(x, y, z - 1);
    	int back = world.getBlockId(x, y, z + 1);
    	int left = world.getBlockId(x - 1, y, z);
    	int right = world.getBlockId(x + 1, y, z);
    	int meta = 3;

    	if (Block.opaqueCubeLookup[front] && !Block.opaqueCubeLookup[back])
    		meta = 3;

    	if (Block.opaqueCubeLookup[back] && !Block.opaqueCubeLookup[front])
    		meta = 2;

    	if (Block.opaqueCubeLookup[left] && !Block.opaqueCubeLookup[right])
    		meta = 5;

    	if (Block.opaqueCubeLookup[right] && !Block.opaqueCubeLookup[left])
    		meta = 4;

    	return meta;
    }
    
    public static vecd3 vecFromEntity(Entity e)
    {
    	return new vecd3(e.posX, e.posY, e.posZ);
    }
    
    public static void doEntityFall(Entity e)
    {
    	try
		{
			EntityFallMethod.invoke(e, e.fallDistance);
		} catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
    }
    
    public static EnumFacing getDirectionVectorInVanillaFormat(int side)
    {
    	return EnumFacing.getFront(side);
    }
    /**
     * Loads File as String array.
     * @param file path to file as in classloader.loadresource
     * @param encoding file encoding
     * @return String array containing file's text or null if fails.
     */
	public static String[] loadFileAsStringArray(String file, String encoding)
	{
		InputStream in = ForgeMod.class.getClassLoader().getResourceAsStream(file);
		if (in == null)
		{
			ForgeMod.modLogger.warning("[FLoader] Failed loading file "+file+" with encoding "+encoding);
			return null;
		}
		List<String> list = new ArrayList<String>();
		Scanner s = new Scanner(in, encoding);
		if (s.hasNext())//at least 1 line
			s.nextLine();//first line is skipped because of possible encoding problem
		while (s.hasNextLine())
		{
			String l = s.nextLine();
			if (l.startsWith("#")||l.equals(""))
				continue;//ignored
			list.add(l);
		}
		s.close();
		System.out.println("load scs"+list.size());
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * Returns random object from list or null, if empty
	 * @param list
	 * @param r
	 * @return
	 */
	public static <T> T randomFromList(List<T> list, Random r)
	{
		return list.size()==0 ? null : list.get(r.nextInt(list.size()));
	}
}