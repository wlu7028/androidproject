package com.logbook.logbookapp.OCRVINResource;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

/**
 * Created by me on 10/2/2016.
 */
public class OcrDetectorProcessor  implements Detector.Processor<TextBlock>{
    public OcrDetectorProcessor(){

    }
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());
            }

        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {

    }
}
