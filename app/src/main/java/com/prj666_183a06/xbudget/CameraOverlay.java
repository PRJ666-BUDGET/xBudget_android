package com.prj666_183a06.xbudget;

import android.graphics.Canvas;

import com.prj666_183a06.xbudget.camera.GraphicOverlay;

import java.util.List;

public class CameraOverlay extends GraphicOverlay.Graphic {
    public CameraOverlay(GraphicOverlay overlay) {
        super(overlay);
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }
}
