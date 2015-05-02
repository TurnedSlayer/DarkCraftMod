package com.turnedslayer.darkcraft.blocks.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by TurnedSlayer.
 */
public class TileDarkBasicFurnace extends TileEntity implements IInventory, IEnergyHandler {
    private TileDarkBasicFurnace tilef;
    public EnergyStorage storage = new EnergyStorage(10000);
    private String localizedName;
    private static final int[] furnaceItemStacks_top = new int[]{0};
    private static final int[] furnaceItemStacks_sides = new int[]{1};
    private static final int[] furnaceItemStacks_bottom = new int[]{2,1};
    public int furnaceSpeed = 10;
    public int rfPerUse = 1000;
    public int burnTime;
    public int currentItemSmeltingTime;
    public int smeltingTime;
    private ItemStack[] furnaceItemStacks = new ItemStack[2];
    private String field_145958_o;
    protected int capacity;
    public int energy;
    public int maxRF = 10000;



    public TileDarkBasicFurnace()
    {
        super();



    }



    public ItemStack decrStackSize(int i, int j) {
        if(this.furnaceItemStacks[i]!=null){
            ItemStack itemstack;
            if(this.furnaceItemStacks[i].stackSize<=j){
                itemstack=this.furnaceItemStacks[i];
                this.furnaceItemStacks[i]=null;
                return itemstack;
            }else{
                itemstack=this.furnaceItemStacks[i].splitStack(j);
                if(this.furnaceItemStacks[i].stackSize==0){
                    this.furnaceItemStacks[i]=null;
                }
                return itemstack;
            }
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int i) {
        if(this.furnaceItemStacks[i]!=null){
            ItemStack itemstack=this.furnaceItemStacks[i];
            this.furnaceItemStacks[i]=null;
            return itemstack;
        }
        return null;
    }



    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        this.furnaceItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && entityplayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return false;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.field_145958_o : "ContainerBasicFurnace";
    }

    public int getRFStored()
    {
        return this.storage.getEnergyStored() * 10 / 200;
    }




    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.furnaceItemStacks[par1];
    }

    public int getSizeInventory()
    {
        return this.furnaceItemStacks.length;
    }

    public boolean isSmelting(){
        return this.burnTime>0;
    }



        private boolean canSmelt(){

            if (this.storage.getEnergyStored() <= 500) return false;

            if (this.furnaceItemStacks[0] == null)
            {
                return false;
            }
            else
            {
                ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
                if (itemstack == null) return false;
                if (this.furnaceItemStacks[1] == null) return true;
                if (!this.furnaceItemStacks[1].isItemEqual(itemstack)) return false;
                int result = furnaceItemStacks[1].stackSize + itemstack.stackSize;

                return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[1].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
            }
        }




            private void smeltItem(){
                if(this.canSmelt()){
                    ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
                    this.storage.modifyEnergyStored(-500);
                    if(this.furnaceItemStacks[1]==null){
                        this.furnaceItemStacks[1]=itemstack.copy();
                    }else if(this.furnaceItemStacks[1].isItemEqual(itemstack)){
                        this.furnaceItemStacks[1].stackSize+=itemstack.stackSize;
                    }
                    this.furnaceItemStacks[0].stackSize--;

                    if(this.furnaceItemStacks[0].stackSize<=0){
                        this.furnaceItemStacks[0]=null;
                    }
                }
            }



    public void updateEntity()
    {
        boolean flag = this.smeltingTime > 0;
        boolean flag1 = false;



        if (!this.worldObj.isRemote)
        {


                if (this.canSmelt())
                {
                    ++this.smeltingTime;

                    if (this.smeltingTime == furnaceSpeed)
                    {
                        this.smeltingTime = 0;
                        this.smeltItem();
                        flag1 = true;

                    }
                }
                else
                {
                    this.smeltingTime = 0;
                }


        }
    }


/*
public void updateEntity()
{
    boolean flag = this.furnaceBurnTime > 0;
    boolean flag1 = false;

    if (this.furnaceBurnTime > 0)
    {
        --this.furnaceBurnTime;
    }

    if (!this.worldObj.isRemote)
    {
        if (this.furnaceBurnTime == 0 && this.canSmelt())
        {
            this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

            if (this.furnaceBurnTime > 0)
            {
                flag1 = true;

                if (this.furnaceItemStacks[1] != null)
                {
                    --this.furnaceItemStacks[1].stackSize;

                    if (this.furnaceItemStacks[1].stackSize == 0)
                    {
                        this.furnaceItemStacks[1] = furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
                    }
                }
            }
        }

        if (this.isBurning() && this.canSmelt())
        {
            ++this.furnaceCookTime;

            if (this.furnaceCookTime == 200)
            {
                this.furnaceCookTime = 0;
                this.smeltItem();
                flag1 = true;
            }
        }
        else
        {
            this.furnaceCookTime = 0;
        }

        if (flag != this.furnaceBurnTime > 0)
        {
            flag1 = true;
            BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
    }

    if (flag1)
    {
        this.markDirty();
    }
}
*/

    /*
    public void updateEntity(){
        boolean flag = this.burnTime > 0;
        boolean flag1 = false;


        if (!this.worldObj.isRemote)
        {



            if (this.canGrind())
            {
                this.currentItemSmeltingTime = storage.getEnergyStored();

            }

            if (this.isSmelting() && this.canGrind())
            {


                ++this.smeltingTime;

                if (this.smeltingTime == 200)
                {
                    this.smeltingTime = 0;
                    this.grindItem();
                    flag1 = true;
                }
            }
            else
            {
                this.smeltingTime = 0;
            }

            if (flag != this.smeltingTime > 0)
            {
                flag1 = true;
                blockDarkBasicFurnace.updateBlockState(this.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }
    */



    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        storage.readFromNBT(nbt);

    }
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        storage.writeToNBT(nbt);
    }





    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();

        writeToNBT(nbtTag);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }


    @Override
    public boolean canConnectEnergy(ForgeDirection from) {

        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {

        return storage.receiveEnergy(maxReceive, false);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {

        return storage.extractEnergy(maxExtract, false);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {

        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {

        return storage.getMaxEnergyStored();
    }

    public int modifyEnergyStored(int energy) {

        return 0;
    }
}
