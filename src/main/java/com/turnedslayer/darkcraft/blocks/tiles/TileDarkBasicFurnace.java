package com.turnedslayer.darkcraft.blocks.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

public class TileDarkBasicFurnace extends TileDarkBase implements IEnergyHandler
{


    //Furnace

    //Inventory
    private ItemStack[] inventory = new ItemStack[2];

    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && entityplayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }



    //Energy
    public EnergyStorage EnergyStorage = new EnergyStorage(10000);


    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int rec = this.EnergyStorage.receiveEnergy(maxReceive, simulate);
        this.markDirty();
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return rec;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return EnergyStorage.extractEnergy(maxExtract, false);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return EnergyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return EnergyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }



    //NBT
    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        EnergyStorage.readFromNBT(nbt);

        super.readFromNBT(nbt);
        NBTTagList invList = nbt.getTagList("inventory", 10);
        for (int i=0; i<invList.tagCount(); i++)
        {
            NBTTagCompound itemTag = invList.getCompoundTagAt(i);
            int slot = itemTag.getByte("Slot") & 255;
            if(slot>=0 && slot<this.inventory.length)
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        EnergyStorage.writeToNBT(nbt);
    }


}