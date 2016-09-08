package fossilsarcheology.server.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class FemursItem extends ItemArmor {
    public FemursItem(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlot) {
        super(material, renderIndex, equipmentSlot);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (stack.getItem() == FAItemRegistry.INSTANCE.skullHelmet || stack.getItem() == FAItemRegistry.INSTANCE.ribCage || stack.getItem() == FAItemRegistry.INSTANCE.feet) {
            return "fossil:textures/armor/bone_1.png";
        }
        if (stack.getItem() == FAItemRegistry.INSTANCE.femurs) {
            return "fossil:textures/armor/bone_2.png";
        }
        return "fossil:textures/armor/bone_2.png";
    }
}