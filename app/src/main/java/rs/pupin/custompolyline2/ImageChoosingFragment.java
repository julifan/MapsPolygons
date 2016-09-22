package rs.pupin.custompolyline2;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;


public class ImageChoosingFragment extends Fragment {
    static interface ImageListener {
        void imageChosen(String path, Bitmap image);
    }

    private ImageListener listener;

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;

    private static final int PICK_IMAGE_REQUEST = 1;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    //private static final String VIDEO_STORAGE_KEY = "viewvideo";
    //private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
    //private VideoView mVideoView;
    //private Uri mVideoUri;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    public ImageChoosingFragment() {
        //required empty constructor
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return "CustomPolyline2";
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MapsActivity.REQUEST_CAMERA);
            } else {
                storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

                if (storageDir != null) {
                    if (!storageDir.mkdirs()) {
                        if (!storageDir.exists()) {
                            Log.d("CameraSample", "failed to create directory");
                            return null;
                        }
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        Log.d("CameraSample", "everything works");
        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) && (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        //mImageView.setImageBitmap(bitmap);
        //send the activity the image and the path where it is stored
        //path: mCurrentPhotoPath
        listener.imageChosen(mCurrentPhotoPath, bitmap);
        //mVideoUri = null;
        mImageView.setVisibility(View.VISIBLE);
        //mVideoView.setVisibility(View.INVISIBLE);
        Log.d("IMAGE", "set image");
        Log.d("IMAGE", mCurrentPhotoPath);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }


    private void dispatchTakePictureIntent(int actionCode) {


        // Check permission for CAMERA
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            // Callback onRequestPermissionsResult interceptado na Activity MainActivity
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.CAMERA},
                    MapsActivity.REQUEST_CAMERA);
        } else {
            // permission has been granted, continue as usual

            //Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(captureIntent, 0);
            //}


            switch (actionCode) {
                case ACTION_TAKE_PHOTO_B:
                    File f = null;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        f = setUpPhotoFile();
                        mCurrentPhotoPath = f.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        mCurrentPhotoPath = null;
                    }

                    startActivityForResult(takePictureIntent, actionCode);
                    break;
                case ACTION_TAKE_PHOTO_S:
                    Intent intent = new Intent();
                    // Show only images, no videos or anything else
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    // Always show the chooser (if there are multiple options available)
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    startActivityForResult(intent, actionCode);
                    break;
                default:
                    break;
            } // switch

            //else nothing happens
        }

    }

    /*private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
    }*/

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        //mImageView.setImageBitmap(mImageBitmap);
        //mVideoUri = null;
        mImageView.setVisibility(View.VISIBLE);
        //mVideoView.setVisibility(View.INVISIBLE);

    }

    public void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();

            mCurrentPhotoPath = null;
        }

    }

    private void handleCameraVideo(Intent intent) {
        //mVideoUri = intent.getData();
        //mVideoView.setVideoURI(mVideoUri);
        mImageBitmap = null;
        //mVideoView.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.INVISIBLE);
    }

    Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
            };

    Button.OnClickListener mTakePicSOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                }
            };

    /*Button.OnClickListener mTakeVidOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakeVideoIntent();
                }
            };*/

    /**
     * Called when the activity is first created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image_choosing, container, false);

        mImageView = (ImageView) v.findViewById(R.id.imageView1);
        //mVideoView = (VideoView) findViewById(R.id.videoView1);
        mImageBitmap = null;
        //mVideoUri = null;

        Button picBtn = (Button) v.findViewById(R.id.gallery);
        setBtnListenerOrDisable(
                picBtn,
                mTakePicOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        Button picSBtn = (Button) v.findViewById(R.id.camera);
        setBtnListenerOrDisable(
                picSBtn,
                mTakePicSOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        /*Button vidBtn = (Button) v.findViewById(R.id.btnIntendV);
        setBtnListenerOrDisable(
                vidBtn,
                mTakeVidOnClickListener,
                MediaStore.ACTION_VIDEO_CAPTURE
        );*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        return v;
    }


    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == Activity.RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO_B

            case ACTION_TAKE_PHOTO_S: {
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

                    Uri uri = data.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        //TODO hier an die activity weiterreichen.
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                        String filename = "/sdcard/" + getFileName_CustomFormat() + ".mp4";
                        copyInputStreamToFile(inputStream, new File(filename));
                        listener.imageChosen(filename, bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            } // ACTION_TAKE_PHOTO_S
        } // switch
    }

    private String getFileName_CustomFormat() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    private void copyInputStreamToFile( InputStream in, File file ) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*// Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null));
        super.onSaveInstanceState(outState);
    }*/

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );

    }*/

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and
     * responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(getActivity(), intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    btn.getText());
            btn.setClickable(false);
        }
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            this.listener = (ImageListener) a;
        }
    }
    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            this.listener = (ImageListener) activity;
        }
    }

}