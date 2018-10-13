package com.prj666_183a06.xbudget;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.lang.Object;

public class OCRParser implements Detector.Processor<TextBlock> {
    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {

    }
}
