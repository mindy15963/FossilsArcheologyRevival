package fossilsarcheology.server.block;

import fossilsarcheology.server.handler.LocalizationStrings;
import fossilsarcheology.server.item.FAItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFern extends BlockBush {
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);

    public BlockFern() {
        super(Material.PLANTS);
        this.setTickRandomly(true);
        this.disableStats();
        this.setUnlocalizedName(LocalizationStrings.FERN_BLOCK_NAME);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
    }

    public static boolean checkUnderTree(World world, BlockPos pos) {
        for (int y = pos.getY(); y <= 128; ++y) {
            if (world.getBlockState(pos.up(y)).getMaterial() == Material.LEAVES) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    protected boolean canPlaceBlockOn(Block block) {
        return block == Blocks.GRASS;
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        super.updateTick(var1, var2, var3, var4, var5);
        int var6 = var1.getBlockMetadata(var2, var3, var4);

        if (checkUnderTree(var1, var2, var3, var4) && this.Cangrow(var6)) {
            // only the low ones grow and update the upper ones!
            if (var5.nextInt(10) > 1) {
                ++var6;// let it grow

                if (this.GetLv2(var6)) // gets level 2
                {
                    if (!var1.isAirBlock(var2, var3 + 1, var4))// not possible!
                    {
                        --var6;
                    } else// create new top block
                    {
                        var1.setBlock(var2, var3 + 1, var4, FABlockRegistry.INSTANCE.ferns);// fernUpper
                        var1.setBlockMetadataWithNotify(var2, var3 + 1, var4, this.CheckSubType(var6) == 0 ? var6 + 2 : var6 + 1, 2);
                    }
                } else if (this.HasLv2(var6)) {
                    if (var1.getBlock(var2, var3 + 1, var4) == FABlockRegistry.INSTANCE.ferns)// fernUpper
                    {
                        var1.setBlockMetadataWithNotify(var2, var3 + 1, var4, this.CheckSubType(var6) == 0 ? var6 + 2 : var6 + 1, 2); // update
                        // top
                        // block
                        // meta
                        // data
                    } else {
                        var6 = this.CheckSubType(var6) == 0 ? 3 : 10; // reset
                        // to last
                        // block
                        // one
                        // stage
                        // high
                    }
                }

				/*
                 * if (var6 == 1 && (new Random()).nextInt(10) < 5) { var6 += 7;
				 * }
				 */
                // System.out.println(String.valueOf(var6));
                var1.setBlockMetadataWithNotify(var2, var3, var4, var6, 2);
            }

			/*
             * else if (!this.lv2 && var5.nextInt(100) < 5) {
			 * this.breakBlock(var1, var2, var3, var4, this.blockID, var6); }
			 */
        }

        // var1.getBlock(var2, var3, var4);

        if (var6 % 7 >= 3) {
            for (int var8 = -1; var8 <= 1; ++var8) {
                for (int var9 = -1; var9 <= 1; ++var9) {
                    for (int var10 = -1; var10 <= 1; ++var10) {
                        if ((var8 != 0 || var10 != 0 || var9 != 0) && var1.getBlock(var2 + var8, var9 + var3 - 1, var4 + var10) == Blocks.grass && (var1.isAirBlock(var2 + var8, var9 + var3, var4 + var10) || var1.getBlock(var2 + var8, var9 + var3, var4 + var10) == Blocks.tallgrass) && checkUnderTree(var1, var2 + var8, var9 + var3, var4 + var10) && (new Random()).nextInt(10) <= 9) {
                            var1.setBlock(var2 + var8, var9 + var3, var4 + var10, FABlockRegistry.INSTANCE.ferns);
                            var1.setBlockMetadataWithNotify(var2 + var8, var9 + var3, var4 + var10, 8 * this.CheckSubType(var6), 2);
                        }
                    }
                }
            }
        }
    }

    public void fertilize(World var1, int var2, int var3, int var4)// ?????
    {
        var1.setBlockMetadataWithNotify(var2, var3, var4, 5 + 7 * (new Random()).nextInt(1), 2);
    }

    private float getGrowthRate(World var1, int var2, int var3, int var4) {
        float var5 = 1.0F;
        Block var6 = var1.getBlock(var2, var3, var4 - 1);
        Block var7 = var1.getBlock(var2, var3, var4 + 1);
        Block var8 = var1.getBlock(var2 - 1, var3, var4);
        Block var9 = var1.getBlock(var2 + 1, var3, var4);
        Block var10 = var1.getBlock(var2 - 1, var3, var4 - 1);
        Block var11 = var1.getBlock(var2 + 1, var3, var4 - 1);
        Block var12 = var1.getBlock(var2 + 1, var3, var4 + 1);
        Block var13 = var1.getBlock(var2 - 1, var3, var4 + 1);
        boolean var14 = var8 == FABlockRegistry.INSTANCE.ferns || var9 == FABlockRegistry.INSTANCE.ferns;
        boolean var15 = var6 == FABlockRegistry.INSTANCE.ferns || var7 == FABlockRegistry.INSTANCE.ferns;
        boolean var16 = var10 == FABlockRegistry.INSTANCE.ferns || var11 == FABlockRegistry.INSTANCE.ferns || var12 == FABlockRegistry.INSTANCE.ferns || var13 == FABlockRegistry.INSTANCE.ferns;

        for (int var17 = var2 - 1; var17 <= var2 + 1; ++var17) {
            for (int var18 = var4 - 1; var18 <= var4 + 1; ++var18) {
                Block var19 = var1.getBlock(var17, var3 - 1, var18);
                float var20 = 0.0F;

                if (var19 == Blocks.grass) {
                    var20 = 1.0F;

                    if (var1.getBlockMetadata(var17, var3 - 1, var18) > 0) {
                        var20 = 3.0F;
                    }
                }

                if (var17 != var2 || var18 != var4) {
                    var20 /= 4.0F;
                }

                var5 += var20;
            }
        }

        if (var16 || var14 && var15) {
            var5 /= 2.0F;
        }

        return var5;
    }

    @Override
    public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7) {
        super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, var6, var7);
    }

    @Override
    public Item getItemDropped(int var1, Random var2, int var3) {
        if (var2.nextInt(4) == 0) {
            return FAItemRegistry.INSTANCE.fernSeed;
        }
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random var1) {
        return 1;
    }

    /**
     * Can this block stay at this position. Similar to canPlaceBlockAt except
     * gets checked often with plants.
     */
    @Override
    public boolean canBlockStay(World var1, int var2, int var3, int var4) {
        boolean var5 = true;

        if (this.CheckLevel(var1.getBlockMetadata(var2, var3, var4))) {
            return var1.getBlock(var2, var3 - 1, var4) == FABlockRegistry.INSTANCE.ferns; // fernblock
            // below
        } else {
            var5 = var1.getBlock(var2, var3 - 1, var4) == Blocks.grass && checkUnderTree(var1, var2, var3, var4);// on
            // grass,
            // under
            // tree

            if (this.HasLv2(var1.getBlockMetadata(var2, var3, var4))) {
                var5 &= var1.getBlock(var2, var3 + 1, var4) == FABlockRegistry.INSTANCE.ferns; // and
                // has
                // the
                // upper
                // block
                // it
                // needs//fernUpper
            }

            return var5;
        }
    }

    public int CheckSubType(int var1) {
        return var1 > 7 ? 1 : 0;
    }

    public boolean CheckLevel(int var1)// false==down, true= up
    {
        return (var1 >= 6 && var1 <= 7) || var1 == 12;
    }

    public boolean Cangrow(int var1)// only the low ones grow and update the
    // upper ones!
    {
        return (var1 >= 0 && var1 < 5) || (var1 >= 8 && var1 < 11);
    }

    public boolean HasLv2(int var1) {
        return (var1 >= 4 && var1 <= 5) || (var1 >= 11 && var1 <= 11);
    }

    public boolean GetLv2(int var1)// the stages where it gets a second level
    {
        return (var1 == 4) || (var1 == 11);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(FAItemRegistry.INSTANCE.fernSeed);
    }

    @Override
    public int getBlockColor() {
        double var1 = 0.5D;
        double var3 = 1.0D;

        return ColorizerGrass.getGrassColor(var1, var3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int par1) {
        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    @Override
    public int colorMultiplier(IBlockAccess world, BlockPos pos) {
        return world.getBiomeGenForCoords(x, z).getBiomeFoliageColor(pos);
    }
}
