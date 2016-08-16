//package net.muxi.huashiapp.common.util;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.os.Environment;
//import android.util.Log;
//
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Target;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
///**
// * Created by ybao on 16/8/16.
// */
//public class PicassoUtils {
//
//    //save image
//    public static void downloadImage(final Context context, final String url) {
////        Logger.d(url);
////        Target target = new Target(){
////
////            @Override
////            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
////                Logger.d("bitmap loaded");
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////
////                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + url);
////                        try {
////                            file.createNewFile();
////                            FileOutputStream ostream = new FileOutputStream(file);
////                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
////                            ostream.flush();
////                            ostream.close();
////                            Logger.d("create file" + file.getAbsolutePath());
////                        } catch (IOException e) {
////                            Log.e("IOException", e.getLocalizedMessage());
////                        }
////                    }
////                }).start();
////
////            }
////
////            @Override
////            public void onBitmapFailed(Drawable errorDrawable) {
////                return;
////            }
////
////            @Override
////            public void onPrepareLoad(Drawable placeHolderDrawable) {
////                return;
////            }
////        };
//        final Bitmap bitmap = null;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        bitmap = Picasso.with(context)
//                                .load(url)
//                                .get();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        if (bitmap == null) {
//            Logger.d("down bitmap fail");
//        } else {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + url);
//                    try {
//                        file.createNewFile();
//                        FileOutputStream ostream = new FileOutputStream(file);
//                        Bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
//                        ostream.flush();
//                        ostream.close();
//                        Logger.d("create file" + file.getAbsolutePath());
//                    } catch (IOException e) {
//                        Log.e("IOException", e.getLocalizedMessage());
//                    }
//                }
//            }).start();
//        }
//    }
//
//    //target to save
//    private static Target getTarget(final String url) {
//        Target target = new Target() {
//
//            @Override
//            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//                Logger.d("bitmap loaded");
//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + url);
//                        try {
//                            file.createNewFile();
//                            FileOutputStream ostream = new FileOutputStream(file);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
//                            ostream.flush();
//                            ostream.close();
//                            Logger.d("create file" + file.getAbsolutePath());
//                        } catch (IOException e) {
//                            Log.e("IOException", e.getLocalizedMessage());
//                        }
//                    }
//                }).start();
//
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        };
//        return target;
//    }
//}
