/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.turnedslayer.darkcraft.help.render.icon;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

/**
 * Extension of {@link TextureAtlasSprite} to allow common operations like clipping and offset.<br>
 * Allows to have an IIcon that is not registered, but depending on a registered one.
 *
 * @author Ordinastie
 *
 */
public class DarkIcon extends TextureAtlasSprite
{
    /**
     * Width of the global texture sheet.
     */
    protected int sheetWidth;
    /**
     * Height of the global texture sheet.
     */
    protected int sheetHeight;

    /**
     * Is the icon flipped on the horizontal axis.
     */
    protected boolean flippedU = false;
    /**
     * Is the icon flipped on the vertical axis.
     */
    protected boolean flippedV = false;
    /**
     * Rotation value (clockwise)
     */
    protected int rotation = 0;
    /**
     * Lists of DarkIcon depending on this one.
     */
    protected Set<DarkIcon> dependants = new HashSet<DarkIcon>();

    public DarkIcon()
    {
        super("");
        maxU = 1;
        maxV = 1;
    }

    public DarkIcon(String name)
    {
        super(name);
    }

    public DarkIcon(DarkIcon baseIcon)
    {
        super(baseIcon.getIconName());
        baseIcon.addDependant(this);
    }

    /**
     * Adds a {@link DarkIcon} to be dependant on this one. Will call {@link #initIcon(DarkIcon, int, int, int, int, boolean)} when
     * stiched to the sheet.
     *
     * @param icon the icon
     */
    public void addDependant(DarkIcon icon)
    {
        dependants.add(icon);
    }

    //#region getters/setters
    /**
     * Sets the size in pixel of this {@link DarkIcon}.
     *
     * @param width the width
     * @param height the height
     */
    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public float getMinU()
    {
        return this.flippedU ? maxU : minU;
    }

    @Override
    public float getMaxU()
    {
        return this.flippedU ? minU : maxU;
    }

    @Override
    public float getMinV()
    {
        return this.flippedV ? maxV : minV;
    }

    @Override
    public float getMaxV()
    {
        return this.flippedV ? minV : maxV;
    }

    /**
     * Sets this {@link DarkIcon} to be flipped.
     *
     * @param horizontal whether to flip horizontally
     * @param vertical whether to flip vertically
     * @return this {@link DarkIcon}
     */
    public DarkIcon flip(boolean horizontal, boolean vertical)
    {
        flippedU = horizontal;
        flippedV = vertical;
        return this;
    }

    /**
     * @return true if this {@link DarkIcon} is flipped horizontally.
     */
    public boolean isFlippedU()
    {
        return flippedU;
    }

    /**
     * @return true if this {@link DarkIcon} is flipped vertically.
     */
    public boolean isFlippedV()
    {
        return flippedV;
    }

    /**
     * @return true fi this {@link DarkIcon} is rotated.
     */
    public boolean isRotated()
    {
        return rotation != 0;
    }

    /**
     * Sets the rotation for this {@link DarkIcon}. The icon will be rotated <b>rotation</b> x 90 degrees clockwise.
     *
     * @param rotation the rotation
     */
    public void setRotation(int rotation)
    {
        this.rotation = rotation;
    }

    /**
     * @return the rotation for this {@link DarkIcon}.
     */
    public int getRotation()
    {
        return rotation;
    }

    //#end getters/setters

    /**
     * Initializes this {@link DarkIcon}. Called from the icon this one depends on, copying the <b>baseIcon</b> values.
     *
     * @param baseIcon the base icon
     * @param width the width
     * @param height the height
     * @param x the x
     * @param y the y
     * @param rotated the rotated
     */
    protected void initIcon(DarkIcon baseIcon, int width, int height, int x, int y, boolean rotated)
    {
        copyFrom(baseIcon);
    }

    /**
     * Offsets this {@link DarkIcon} by a specified amount. <b>offsetX</b> and <b>offsetY</b> are specified in pixels.
     *
     * @param offsetX the x offset
     * @param offsetY the y offset
     * @return this {@link DarkIcon}
     */
    public DarkIcon offset(int offsetX, int offsetY)
    {
        initSprite(sheetWidth, sheetHeight, getOriginX() + offsetX, getOriginY() + offsetY, isRotated());
        return this;
    }

    /**
     * Clips this {@link DarkIcon}. <b>offsetX</b>, <b>offsetY</b>, <b>width</b> and <b>height</b> are specified in pixels.
     *
     * @param offsetX the x offset
     * @param offsetY the y offset
     * @param width the width
     * @param height the height
     * @return this {@link DarkIcon}
     */
    public DarkIcon clip(int offsetX, int offsetY, int width, int height)
    {
        this.width = width + (useAnisotropicFiltering ? 16 : 0);
        this.height = height + (useAnisotropicFiltering ? 16 : 0);
        offset(offsetX, offsetY);

        return this;
    }

    /**
     * Clips this {@link DarkIcon}. <b>offsetXFactor</b>, <b>offsetYFactor</b>, <b>widthFactor</b> and <b>heightFactor</b> are values
     * from zero to one.
     *
     * @param offsetXFactor the x factor for offset
     * @param offsetYFactor the y factor for offset
     * @param widthFactor the width factor
     * @param heightFactor the height factor
     * @return this {@link DarkIcon}
     */
    public DarkIcon clip(float offsetXFactor, float offsetYFactor, float widthFactor, float heightFactor)
    {
        if (useAnisotropicFiltering)
        {
            width -= 16;
            height -= 16;
        }

        int offsetX = Math.round(width * offsetXFactor);
        int offsetY = Math.round(height * offsetYFactor);

        width = Math.round(width * widthFactor);
        height = Math.round(height * heightFactor);

        if (useAnisotropicFiltering)
        {
            width += 16;
            height += 16;
        }

        offset(offsetX, offsetY);

        return this;
    }

    /**
     * Called when the part represented by this {@link DarkIcon} is stiched to the texture. Sets most of the icon fields.
     *
     * @param width the width
     * @param height the height
     * @param x the x
     * @param y the y
     * @param rotated the rotated
     */
    @Override
    public void initSprite(int width, int height, int x, int y, boolean rotated)
    {
        this.sheetWidth = width;
        this.sheetHeight = height;
        super.initSprite(width, height, x, y, rotated);
        for (DarkIcon dep : dependants)
            dep.initIcon(this, width, height, x, y, rotated);
    }

    /**
     * Copies the values from {@link DarkIcon base} to this {@link DarkIcon}.
     *
     * @param base the icon to copy from
     */
    public void copyFrom(DarkIcon base)
    {
        super.copyFrom(base);
        this.useAnisotropicFiltering = base.useAnisotropicFiltering;
        this.sheetWidth = base.sheetWidth;
        this.sheetHeight = base.sheetHeight;
        this.flippedU = base.flippedU;
        this.flippedV = base.flippedV;
    }

    /**
     * Creates a new {@link DarkIcon} from this <code>DarkIcon</code>.
     *
     * @return the new {@link DarkIcon}
     */
    public DarkIcon copy()
    {
        DarkIcon icon = new DarkIcon();
        icon.copyFrom(this);
        return icon;
    }

    /**
     * Attempts to register this {@link DarkIcon} to the {@link TextureMap}. If an {@link IIcon} is already registered with this name,
     * that registered icon will be returned instead.
     *
     * @param register the TextureMap
     * @return this {@link DarkIcon} if not already registered, otherwise, the DarkIcon already inside the registry.
     */
    public DarkIcon register(TextureMap register)
    {
        TextureAtlasSprite icon = register.getTextureExtry(getIconName());
        if (icon != null)
            return (DarkIcon) icon;

        register.setTextureEntry(getIconName(), this);
        return this;
    }
}
