package com.future.myapplication11.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.future.myapplication11.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SurfaceViewCamera extends Activity implements View.OnClickListener {
    private String TAG = "";
    Button returnBack;
    Button tvswitchCamera;
    //启动摄像机
    private Camera mCamera;

    public boolean isopen_camara = false;
    private SurfaceView surfaceView;
    private SurfaceHolder mholder = null;
    private SurfaceCallback previewCallBack;
    private boolean isTakingPhoto;//是否正在拍照
    private int cameraPosition = 0;//0代表前置摄像头，1代表后置摄像头
    private Button btn_abcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        verifyPermission(new String[]{Manifest.permission.CAMERA});
        setContentView(R.layout.activity_main);
        // 预览控件
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        // 设置参数
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        returnBack = (Button) findViewById(R.id.return_back);

        returnBack.setOnClickListener(this);
        tvswitchCamera = (Button) findViewById(R.id.tvswitch_camera);
        tvswitchCamera.setOnClickListener(this);
    }

    public void verifyPermission(String[] permissions) {
        if (permissions != null) {
            List<String> lists = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {

                    }
                    lists.add(permissions[i]);
                }
            }
            if (lists.size() > 0) {
                String[] ps = new String[lists.size()];
                for (int i = 0; i < lists.size(); i++) {
                    ps[i] = lists.get(i);
                }
                ActivityCompat.requestPermissions(this, ps, 1);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void takePhoto() {
        if (!isopen_camara) {
            previewCallBack = new SurfaceCallback();
            surfaceView.getHolder().addCallback(previewCallBack);
        } else {
            autoTakePhoto();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            if (!isopen_camara) {
                previewCallBack = new SurfaceCallback();
                surfaceView.getHolder().addCallback(previewCallBack);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.return_back:
                finish();
                break;
            case R.id.tvswitch_camera:
                switchCamara();
                break;
        }
    }


    // 预览界面回调
    private final class SurfaceCallback implements SurfaceHolder.Callback {
        // 预览界面被创建
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                //1代表打开后置摄像头,0代表打开前置摄像头.
                mCamera = Camera.open(cameraPosition);// 打开摄像头
                setParams(holder, cameraPosition);
            } catch (Exception e) {
                e.printStackTrace();
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.lock();
                    mCamera.release();
                    mCamera = null;
                }
                finish();
                //Toast.makeText(getApplicationContext(), "该手机不支持自动拍照功能", Toast.LENGTH_LONG).show();
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            System.out.println("surfaceChanged");
            isopen_camara = true;
            //autoTakePhoto();
        }

        // 预览界面被销毁
        public void surfaceDestroyed(SurfaceHolder holder) {
            System.out.println("surfaceDestroyed");
            if (!isopen_camara)
                return;
            if (mCamera != null) {
                holder.removeCallback(this);
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;
            }
        }

    }

    public void reset() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        if (mCamera != null) {
            if (surfaceView != null && surfaceView.getHolder() != null && previewCallBack != null) {
                surfaceView.getHolder().removeCallback(previewCallBack);
            }
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;

            mCamera = Camera.open(cameraPosition);
            if (null != mholder)
                setParams(mholder, cameraInfo.facing);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            if (surfaceView != null && surfaceView.getHolder() != null && previewCallBack != null) {
                surfaceView.getHolder().removeCallback(previewCallBack);
            }
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    private void setParams(SurfaceHolder mySurfaceView, int postion) {
        try {
            int PreviewWidth = 0;
            int PreviewHeight = 0;
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);//获取窗口的管理器
            /*PreviewWidth = display.();
            PreviewHeight = display.getHeight();*/

            Camera.Parameters parameters = mCamera.getParameters();
            // 选择合适的预览尺寸
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

            // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
            if (sizeList.size() > 1) {
                Iterator<Camera.Size> itor = sizeList.iterator();
                while (itor.hasNext()) {
                    Camera.Size cur = itor.next();
                    /*if (cur.width >= PreviewWidth
                            && cur.height >= PreviewHeight) {*/
                    if (cur.width >= PreviewWidth
                            && cur.height >= PreviewHeight) {
                        PreviewWidth = cur.width;
                        PreviewHeight = cur.height;
                        break;
                    }
                }
            }
            parameters.setPreviewSize(PreviewWidth, PreviewHeight); //获得摄像区域的大小
//            parameters.setPreviewSize(PreviewWidth, PreviewHeight); //获得摄像区域的大小
            //parameters.setPreviewFrameRate(3);//每秒3帧  每秒从摄像头里面获得3个画面
            //parameters.setPreviewFpsRange(3,);
            List<int[]> list = parameters.getSupportedPreviewFpsRange();
            int[] v = null;
            int index = 0;
            int min = 0;
            for (int i = 0; i < list.size(); i++) {
                v = list.get(i);
                if (v[0] > min) {
                    min = v[0];
                    index = i;
                }
            }
            parameters.setPreviewFpsRange(list.get(index)[0], list.get(index)[1]);
            parameters.setPictureFormat(PixelFormat.JPEG);//设置照片输出的格式
            parameters.set("jpeg-quality", 85);//设置照片质量
            parameters.setPictureSize(PreviewWidth, PreviewHeight);//设置拍出来的屏幕大小
            parameters.setRotation(180); //Java部分
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);
            } else {
                parameters.set("orientation", "landscape");
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }

//            if (Build.VERSION.SDK_INT >= 24) {
//                if (postion == 0) {
//                    mCamera.setDisplayOrientation(90);
//                } else {
//                    mCamera.setDisplayOrientation(270);
//                }
//            } else {
//                mCamera.setDisplayOrientation(90);
//            }
            mCamera.setParameters(parameters);//把上面的设置 赋给摄像头
            mCamera.setPreviewDisplay(mySurfaceView);//把摄像头获得画面显示在SurfaceView控件里面
            mholder = mySurfaceView;
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {

                }
            });
            mCamera.startPreview();//开始预览
            //   mPreviewRunning = true;
        } catch (IOException e) {
            Log.e("tag", e.toString());
        }
    }

    Handler handler = null;

    private void autoTakePhoto() {
        // 拍照前需要对焦 获取清析的图片
        if (null == mCamera) return;
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

                if (true && isopen_camara) {
                    // 对焦成功
                    //    Toast.makeText(MainActivity.this, "对焦成功 !!",Toast.LENGTH_SHORT).show();
                    if (!isTakingPhoto) {
                        isTakingPhoto = true;
                        handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mCamera.takePicture(null, null, new MyPictureCallback());
                            }
                        });
                    }
                }
            }
        });
    }

    // 照片回调
    private final class MyPictureCallback implements Camera.PictureCallback {
        // 照片生成后
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                btn_abcd.setVisibility(View.VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
//                matrix.setRotate(270);
                matrix.postScale(-1, 1); // 镜像水平翻转
                File jpgFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/camera111");
                if (!jpgFile.exists()) {
                    jpgFile.mkdirs();
                }
                File jpgFile1 = new File(jpgFile.getAbsoluteFile(), "abcd" + ".jpg");
//                File jpgFile1 = new File(jpgFile.getAbsoluteFile(), System.currentTimeMillis() + ".jpg");
                //存入数据库

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                FileOutputStream fos = new FileOutputStream(jpgFile1);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                Toast.makeText(getApplicationContext(), "成功了", Toast.LENGTH_SHORT).show();
                fos.close();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(jpgFile1);
                intent.setData(uri);
                sendBroadcast(intent);
            } catch (Exception e) {
                Log.e(TAG,e.toString() + "") ;
            } finally {
                if (Build.VERSION.SDK_INT >= 24) {
                    reset();
                }
                isTakingPhoto = false;
            }
        }
    }

    private void switchCamara() {
        //切换前后摄像头
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    if (surfaceView != null && surfaceView.getHolder() != null && previewCallBack != null) {
                        surfaceView.getHolder().removeCallback(previewCallBack);
                    }
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.lock();
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                   /* try {
                        if (null != mholder)
                            mCamera.setPreviewDisplay(mholder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览*/
                    if (null != mholder)
                        setParams(mholder, Camera.CameraInfo.CAMERA_FACING_BACK);
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    if (surfaceView != null && surfaceView.getHolder() != null && previewCallBack != null) {
                        surfaceView.getHolder().removeCallback(previewCallBack);
                    }
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.lock();
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    /*try {
                        if (null != mholder)
                            mCamera.setPreviewDisplay(mholder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览*/
                    if (null != mholder)
                        setParams(mholder, Camera.CameraInfo.CAMERA_FACING_FRONT);
                    cameraPosition = 1;
                    break;
                }
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {///音量减拍照
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Bitmap captureScreen() {
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
//        getWindow().getDecorView().setDrawingCacheEnabled(true);
//        Bitmap bmp = getWindow().getDecorView().getDrawingCache();
        return bmp;
    }

    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
