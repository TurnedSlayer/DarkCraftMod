package com.turnedslayer.darkcraft.blocks.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by TurnedSlayer.
 */
public class TileDarkBasicFurnace extends TileDarkBase implements IInventory, IEnergyHandler {
    private TileDarkBasicFurnace tilef;
    public EnergyStorage storage = new EnergyStorage(10000);
    private String localizedName;
    private static final int[] inventory_top = new int[]{0};
    private static final int[] inventory_sides = new int[]{1};
    private static final int[] inventory_bottom = new int[]{2,1};
    public int furnaceSpeed = 10;
    public int rfPerUse = 1000;
    public int burnTime;
    public int currentItemSmeltingTime;
    public int smeltingTime;
    private ItemStack[] inventory = new ItemStack[2];
    private String field_145958_o;
    protected int capacity;
    public int energy = this.storage.getEnergyStored();
    public int maxRF = 10000;
    protected byte state;



    public TileDarkBasicFurnace()
    {
        super();






    }


    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeCustomNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readCustomNBT(pkt.func_148857_g());
    }

    public ItemStack decrStackSize(int i, int j) {
        if(this.inventory[i]!=null){
            ItemStack itemstack;
            if(this.inventory[i].stackSize<=j){
                itemstack=this.inventory[i];
                this.inventory[i]=null;
                return itemstack;
            }else{
                itemstack=this.inventory[i].splitStack(j);
                if(this.inventory[i].stackSize==0){
                    this.inventory[i]=null;
                }
                return itemstack;
            }
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int i) {
        if(this.inventory[i]!=null){
            ItemStack itemstack=this.inventory[i];
            this.inventory[i]=null;
            return itemstack;
        }
        return null;
    }



    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        this.inventory[par1] = par2ItemStack;

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
        return this.inventory[par1];
    }

    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    public boolean isSmelting(){
        return this.burnTime>0;
    }



        private boolean canSmelt(){

            if (this.storage.getEnergyStored() <= 500) return false;

            if (this.inventory[0] == null)
            {
                return false;
            }
            else
            {
                ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]);
                if (itemstack == null) return false;
                if (this.inventory[1] == null) return true;
                if (!this.inventory[1].isItemEqual(itemstack)) return false;
                int result = inventory[1].stackSize + itemstack.stackSize;

                return result <= getInventoryStackLimit() && result <= this.inventory[1].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
            }
        }




            private void smeltItem(){
                if(this.canSmelt()){
                    ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]);
                    this.storage.modifyEnergyStored(-500);
                    if(this.inventory[1]==null){
                        this.inventory[1]=itemstack.copy();
                    }else if(this.inventory[1].isItemEqual(itemstack)){
                        this.inventory[1].stackSize+=itemstack.stackSize;
                    }
                    this.inventory[0].stackSize--;

                    if(this.inventory[0].stackSize<=0){
                        this.inventory[0]=null;
                    }
                }
            }



    public void updateEntity()
    {
        //boolean flag = this.smeltingTime > 0;
        //boolean flag1 = false;


        boolean hasRF = this.storage.getEnergyStored() > 0;
        boolean sendUpdate = false;


        if (!this.worldObj.isRemote)
        {


            // If the state has changed, catch that something changed
            if (hasRF != this.storage.getEnergyStored() > 0)
            {
                sendUpdate = true;
            }
        }


        if (!this.worldObj.isRemote)
        {


                if (this.canSmelt())
                {
                    ++this.smeltingTime;

                    if (this.smeltingTime == furnaceSpeed)
                    {
                        this.smeltingTime = 0;
                        this.smeltItem();

                        sendUpdate = true;

                    }
                }
                else
                {
                    this.smeltingTime = 0;
                }


        }

        if (sendUpdate)
        {
            this.markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            //System.out.println("Block update");
            //this.state = this.storage.getEnergyStored() > 0 ? (byte) 1 : (byte) 0;
            //this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.state);
            //PacketHandler.INSTANCE.sendToAllAround(new MessageTileEntityAludel(this, inventory[OUTPUT_INVENTORY_INDEX]), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, (double) this.xCoord, (double) this.yCoord, (double) this.zCoord, 128d));
            //this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        }
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
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

    public void readCustomNBT(NBTTagCompound nbt)
    {
        storage.readFromNBT(nbt);
    }


    public void writeCustomNBT(NBTTagCompound nbt)
    {
        storage.writeToNBT(nbt);
    }
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        NBTTagList invList = new NBTTagList();
        for(int i=0; i<this.inventory.length; i++)
            if(this.inventory[i] != null)
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(itemTag);
                invList.appendTag(itemTag);
            }
        nbt.setTag("inventory", invList);
    }


    @Override
    public boolean canConnectEnergy(ForgeDirection from) {

        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {

        int rec = this.storage.receiveEnergy(maxReceive, simulate);
        this.markDirty();
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return rec;
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
