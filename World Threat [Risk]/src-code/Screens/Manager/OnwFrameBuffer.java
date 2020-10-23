package com.strat7.game.Screens.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLOnlyTextureData;

/**
 * Created by Евгений on 15.09.2017.
 */

public class OnwFrameBuffer extends FrameBuffer {
    private Pixmap pixmap ;

    /** Creates a new FrameBuffer having the given dimensions and potentially a depth buffer attached. */
    public OnwFrameBuffer (Pixmap.Format format, int width, int height, boolean hasDepth) {
        this(format, width, height, hasDepth, false);
    }

    /** Creates a new FrameBuffer having the given dimensions and potentially a depth and a stencil buffer attached.
     *
     * @param format the format of the color buffer; according to the OpenGL ES 2.0 spec, only RGB565, RGBA4444 and RGB5_A1 are
     *           color-renderable
     * @param width the width of the framebuffer in pixels
     * @param height the height of the framebuffer in pixels
     * @param hasDepth whether to attach a depth buffer
     * @throws com.badlogic.gdx.utils.GdxRuntimeException in case the FrameBuffer could not be created */
    public OnwFrameBuffer (Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
        super(format, width, height, hasDepth, hasStencil);
        if(pixmap == null)
            pixmap = new Pixmap(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
    }


    @Override
    public Texture getColorBufferTexture() {
        return super.getColorBufferTexture();
    }

    @Override
    public Texture createColorTexture() {
        if(pixmap == null)
            pixmap = new Pixmap(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        Texture result = new Texture(pixmap);
        result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        result.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        return setColorBufferTexture(result);
    }

    public Texture setColorBufferTexture(Texture texture) {
        return colorTexture = texture;
    }

    public void clear() {
        getColorBufferTexture().draw(pixmap,0,0);
    }
}
