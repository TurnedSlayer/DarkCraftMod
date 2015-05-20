package com.turnedslayer.darkcraft.help.Gui.basicFurnace;

import com.turnedslayer.darkcraft.blocks.tiles.TileDarkBasicFurnace;
import com.turnedslayer.darkcraft.libs.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiBasicFurnace extends GuiContainer
{
    TileDarkBasicFurnace tileDarkBasicFurnace;
    private static final ResourceLocation backgroundimage = new ResourceLocation(References.MODID.toLowerCase() + ":" + "textures/gui/Dark Furnace.png");
    int rf;


    public GuiBasicFurnace(InventoryPlayer inventoryPlayer, TileDarkBasicFurnace tileDarkBasicFurnace)
    {
        super(new ContainerBasicFurnace(inventoryPlayer, tileDarkBasicFurnace));
        xSize = 176;
        ySize = 164;
        this.tileDarkBasicFurnace=tileDarkBasicFurnace;

        //this.DarkFurnace = tileDarkBasicFurnace;
        //rf = this.tileFurnace.getRFStored();

    }

   // @Override
    @SideOnly(Side.CLIENT)
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(backgroundimage);
        int var4 = (this.width - this.xSize)/2;
        int var5 = (this.height - this.ySize)/2;
        this.drawTexturedModalRect(var4, var5,0,0,this.xSize,this.ySize);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

       // if(this.tileFurnace.getEnergyStored())

       // rf = this.tileDarkBasicFurnace.storage.getEnergyStored();
        //System.out.println(this.tileDarkBasicFurnace.storage.getEnergyStored());
        //this.drawTexturedModalRect(k + 7, l + 4, 176, 31,(rf / 10000 * 67) , 8);
        int stored = (int)(46*(tileDarkBasicFurnace.storage.getEnergyStored()/(float)tileDarkBasicFurnace.storage.getMaxEnergyStored()));
        this.drawGradientRect(guiLeft+157,guiTop+22+(46-stored), guiLeft+164,guiTop+68, 0xffb51500, 0xff600b00);

    }

    @SideOnly(Side.CLIENT)
    protected void drawGuiContainerBackgroundLayer()
    {
        this.mc.getTextureManager().bindTexture(backgroundimage);
        this.drawTexturedModalRect(176, 39, 20, 244, 100, 39);
    }

    @Override
    protected void drawGradientRect(int x0, int y0, int x1, int y1, int colour0, int colour1)
    {
        float f = (float)(colour0>>24&255)/255F;
        float f1 = (float)(colour0>>16&255)/255F;
        float f2 = (float)(colour0>>8&255)/255F;
        float f3 = (float)(colour0&255) / 255F;
        float f4 = (float)(colour1>>24&255)/255F;
        float f5 = (float)(colour1>>16&255)/255F;
        float f6 = (float)(colour1>>8&255)/255F;
        float f7 = (float)(colour1&255)/255F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex((double)x1, (double)y0, 0);
        tessellator.addVertex((double)x0, (double)y0, 0);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double)x0, (double)y1, 0);
        tessellator.addVertex((double)x1, (double)y1, 0);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }








}