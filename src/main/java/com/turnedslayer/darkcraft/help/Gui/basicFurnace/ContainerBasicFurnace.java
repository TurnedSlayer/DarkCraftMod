package com.turnedslayer.darkcraft.help.Gui.basicFurnace;

import com.turnedslayer.darkcraft.blocks.tiles.TileDarkBasicFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;


public class ContainerBasicFurnace extends Container
{

    private TileDarkBasicFurnace tileDarkBasicFurnace;
    public int getEnergyStored;
    public int lastEnergyStored;

    public ContainerBasicFurnace(InventoryPlayer par1InventoryPlayer, TileDarkBasicFurnace par2TileDarkBasicFurnace)
    {
        this.tileDarkBasicFurnace = par2TileDarkBasicFurnace;
        this.addSlotToContainer(new Slot(par2TileDarkBasicFurnace, 0, 56, 17));
        //this.addSlotToContainer(new Slot(par2TileDarkBasicFurnace, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileDarkBasicFurnace, 1, 116, 35));
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
    }


    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (p_82846_2_ != 1 && p_82846_2_ != 0)
            {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (p_82846_2_ >= 3 && p_82846_2_ < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (p_82846_2_ >= 30 && p_82846_2_ < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(p_82846_1_, itemstack1);
        }

        return itemstack;
    }
/*


    @Override
    public void addCraftingToCrafters(ICrafting iCrafting)
    {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.tileDarkBasicFurnace.energy);

    }

    */
/**
     * Looks for changes made in the container, sends them to every listener.
     *//*

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastEnergyStored != this.tileDarkBasicFurnace.energy)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileDarkBasicFurnace.energy);
                System.out.println("crafters");
            }

        }

        this.lastEnergyStored = this.tileDarkBasicFurnace.energy;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int valueType, int updatedValue)
    {
        if (valueType == 0)
        {
            this.tileDarkBasicFurnace.energy = updatedValue;
        }

       */
/* if (p_75137_1_ == 0)
        {
            this.tileFurnace.furnaceCookTime = p_75137_2_;
        }

        if (p_75137_1_ == 1)
        {
            this.tileFurnace.furnaceBurnTime = p_75137_2_;
        }

        if (p_75137_1_ == 2)
        {
            this.tileFurnace.currentItemBurnTime = p_75137_2_;
        }*//*

    }
*/

}