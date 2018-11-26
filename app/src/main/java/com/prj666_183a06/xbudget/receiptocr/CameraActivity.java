package com.prj666_183a06.xbudget.receiptocr;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.prj666_183a06.xbudget.R;
import com.prj666_183a06.xbudget.camera.CameraSourcePreview;
import com.prj666_183a06.xbudget.camera.GraphicOverlay;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.android.gms.vision.Frame.ROTATION_90;

public final class CameraActivity extends AppCompatActivity {
    private static final String TAG = "OcrCaptureActivity";

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // Constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String TextBlockObject = "String";

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay<CameraOverlay> graphicOverlay;

    // Helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private TextRecognizer textRecognizer;

    private ImageView image;

    private Bitmap bitmap;
    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.cameraactivity);

        preview = (CameraSourcePreview) findViewById(R.id.preview);
        graphicOverlay = (GraphicOverlay<CameraOverlay>) findViewById(R.id.graphicOverlay);
        image = (ImageView) findViewById(R.id.CameraImageView);

        // Set good defaults for capturing text.
        boolean autoFocus = true;
        boolean useFlash = false;

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }

        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        Snackbar.make(graphicOverlay, "Tap to capture receipt",
                Snackbar.LENGTH_SHORT)
                .show();

        FloatingActionButton fab = findViewById(R.id.cameraActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraAction();
            }
        });

    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };


        Snackbar.make(graphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean b = scaleGestureDetector.onTouchEvent(e);

        boolean c = gestureDetector.onTouchEvent(e);

        return b || c || super.onTouchEvent(e);
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // A text recognizer is created to find text.  An associated multi-processor instance
        // is set to receive the text recognition results, track the text, and maintain
        // graphics for each text block on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each text block.
        textRecognizer = new TextRecognizer.Builder(context).build();
        textRecognizer.setProcessor(new OCRParser(graphicOverlay));

        if (!textRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;


            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the text recognizer to detect small pieces of text.
        cameraSource =
                new CameraSource.Builder(getApplicationContext(), textRecognizer)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        //.setRequestedPreviewSize(100, 250)
                        .setRequestedFps(1.0f)
                        .setAutoFocusEnabled(true)
                        .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (preview != null) {
            preview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preview != null) {
            preview.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus,true);
            boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (cameraSource != null) {
            try {
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }



    private boolean cameraAction(){
        cameraSource.takePicture(null,
                new CameraSource.PictureCallback() {
                    public void onPictureTaken(byte[] data) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                        //scale image
                        Matrix matrix = new Matrix();
                        matrix.postScale((float) 0.5, (float) 0.5);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

                        //read elemenst as is.
                        //if no values found, rotate and try agian
                        //If none found again, say try again

                        //parse without rotation.
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items = textRecognizer.detect(frame);
                        //make lines into elements
                        boolean foundVal = false;
                        ArrayList<ReceiptElement> receiptElements = new ArrayList<>();
                        for (int i = 0; i < items.size(); ++i) {
                            TextBlock item = items.valueAt(i);
                            List<Line> lines = (List<Line>) item.getComponents();
                            for (int j = 0; j < lines.size(); ++j) {
                                Line line = lines.get(j);
                                if (line != null && line.getValue() != null) {
                                    receiptElements.add(new ReceiptElement(line));
                                    if (new ReceiptElement(line).inNumber()) {
                                        foundVal = true;
                                    }
                                }
                            }
                        }

                        if (!foundVal) {
                            Log.d("CameraLog:", "Rotating");
                            matrix = new Matrix();
                            matrix.postRotate(90);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

                            frame = new Frame.Builder().setBitmap(bitmap).build();
                            items = textRecognizer.detect(frame);
                            //make lines into elements
                            receiptElements = new ArrayList<>();
                            for (int i = 0; i < items.size(); ++i) {
                                TextBlock item = items.valueAt(i);
                                List<Line> lines = (List<Line>) item.getComponents();
                                for (int j = 0; j < lines.size(); ++j) {
                                    Line line = lines.get(j);
                                    if (line != null && line.getValue() != null) {
                                        receiptElements.add(new ReceiptElement(line));
                                        if (new ReceiptElement(line).inNumber()) {
                                            foundVal = true;
                                        }
                                    }
                                }
                            }
                            if (!foundVal) {
                                //if failed to read.
                                Log.d("CameraLog:", "Failed capture");
                                Snackbar.make(graphicOverlay, "Failed To read, Please try again",
                                        Snackbar.LENGTH_LONG)
                                        .show();
                                return;
                            }
                        /*
                        //rotate to portrait (Samsung Fix)
                        if (bitmap.getWidth() > bitmap.getHeight()) {
                            matrix = new Matrix();
                            matrix.postRotate(90);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                        }


                        //Parse Text
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                        SparseArray<TextBlock> items = textRecognizer.detect(frame);

                        //make lines into elements
                        ArrayList<ReceiptElement> receiptElements = new ArrayList<>();
                        for (int i = 0; i < items.size(); ++i) {
                            TextBlock item = items.valueAt(i);
                            List<Line> lines = (List<Line>) item.getComponents();
                            for (int j = 0; j < lines.size(); ++j) {
                                Line line = lines.get(j);
                                if (line != null && line.getValue() != null) {
                                    receiptElements.add(new ReceiptElement(line));
                                }
                            }
                        }
*/
                            //bubble sort elements
                            for (int i = 0; i < receiptElements.size(); i++) {
                                // Last i elements are already in place
                                for (int j = 0; j < receiptElements.size() - i - 1; j++) {
                                    if (receiptElements.get(j).getLine().getBoundingBox().top > receiptElements.get(j + 1).getLine().getBoundingBox().top) {
                                        Collections.swap(receiptElements, j, j + 1);
                                    }
                                }
                            }

                            //get all elements that are detected as 'total'
                            ArrayList<ReceiptElement> possibleTotals = new ArrayList<>();
                            for (int i = 0; i < receiptElements.size(); i++) {
                                if (receiptElements.get(i).isTotal()) {
                                    //find what number is associated
                                    int closestIndex = -1;
                                    double closestValue = 99999;
                                    int secondClosestIndex = -1;
                                    for (int j = 0; j < receiptElements.size(); j++) {
                                        if (receiptElements.get(j).inNumber()) {
                                            if (Math.abs(receiptElements.get(j).getLine().getBoundingBox().top) -
                                                    Math.abs(receiptElements.get(i).getLine().getBoundingBox().top)
                                                    < Math.abs(closestValue)) {
                                                secondClosestIndex = closestIndex;
                                                closestValue = receiptElements.get(j).getNumValue();
                                                closestIndex = j;
                                            }
                                        }
                                    }
                                    if (closestIndex != -1) {
                                        possibleTotals.add(receiptElements.get(closestIndex));
                                    }
                                    if (secondClosestIndex != -1) {
                                        possibleTotals.add(receiptElements.get(secondClosestIndex));
                                    }
                                }
                            }

                            //get largest of possible total
                            int largestTotalIndex = -1;
                            if (possibleTotals.size() > 0) {
                                largestTotalIndex = 0;
                                for (int i = 0; i < possibleTotals.size(); i++) {
                                    if (possibleTotals.get(i).getNumValue() > possibleTotals.get(largestTotalIndex).getNumValue()) {
                                        largestTotalIndex = i;
                                    }
                                }
                            }

                            ArrayList<Double> possibleValues = new ArrayList<>();
                            for (int i = 0; i < receiptElements.size(); i++) {
                                if (receiptElements.get(i).inNumber()) {
                                    possibleValues.add(receiptElements.get(i).getNumValue());
                                }
                            }

                            if (largestTotalIndex != -1) {
                                Log.d("CameraLog:", possibleTotals.get(largestTotalIndex).getValue());
                            } else {
                                Log.d("CameraLog:", "Not found");
                            }

                            for (int i = 0; i < possibleValues.size(); i++) {
                                Log.d("CameraLog:", Double.toString(possibleValues.get(i)));
                            }
                            if (possibleValues.size() == 0) {
                                Log.d("CameraLog:", "No values");
                            }

                            //Sanitize values and sent to form
                            Intent myIntent = new Intent(CameraActivity.this, ReceiptFormActivity.class);
                            //remove unusualy large values
                            //backwards due to index changes on remove7
                            for(int i = possibleValues.size() -1; i >= 0; i--){
                                if(possibleValues.get(i) > 999){
                                    possibleValues.remove(i);
                                }
                            }
                            if(possibleValues.size() > 0){
                                myIntent.putExtra("EXTRA_COST_ARR", possibleValues);
                            }
                            if(largestTotalIndex != -1){
                                myIntent.putExtra("EXTRA_COST", possibleTotals.get(largestTotalIndex).getNumValue());
                            } else {
                                myIntent.putExtra("EXTRA_COST", Collections.max(possibleValues));
                            }
                            CameraActivity.this.startActivity(myIntent);
                        }
                    }
                });
        return false;
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
//            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
            return false;
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            if (cameraSource != null) {
                //cameraSource.doZoom(detector.getScaleFactor());
            }
        }
    }
}