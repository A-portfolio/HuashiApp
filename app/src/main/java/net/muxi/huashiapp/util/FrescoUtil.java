package net.muxi.huashiapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import net.muxi.huashiapp.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import net.muxi.huashiapp.Constants;

/**
 * Created by ybao on 16/7/24.
 * 用 Fresco 缓存图片到本地的工具类
 */
public class FrescoUtil {

    public static final String IMAGE_CACHE_DIR = Constants.CACHE_DIR != null ? Constants.CACHE_DIR.getAbsolutePath() : App.getContext().getCacheDir().getAbsolutePath();

    public static void savePicture(String picUrl, Context context, String fileName) {
        File picDir = new File(IMAGE_CACHE_DIR);
        if (!picDir.exists()) {
            picDir.mkdir();
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(picUrl),context);
        File cacheFile = getCacheImageOnDisk(cacheKey);
        if (cacheFile == null) {
            downloadImage(Uri.parse(picUrl), fileName, context);
            Log.d("frescoUtil","download successful");
        } else {
            copyTo(cacheFile, picDir, fileName);
            Log.d("frescoUtil","copy successful");
        }
    }

    public static File getCacheImageOnDisk(CacheKey cacheKey) {
        Logger.d(IMAGE_CACHE_DIR);
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    public static boolean copyTo(File src, File dir, String fileName) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(src);
            in = fi.getChannel();
            File dst = new File(dir, fileName + ".jpg");
            fo = new FileOutputStream(dst);
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void downloadImage(Uri uri, final String fileName, Context context) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                if (bitmap == null) {
                    Log.d("FrescoUtil", "save image failed");
                }
                File appDir = new File(IMAGE_CACHE_DIR);
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                File file = new File(appDir, fileName + ".jpg");
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

            }
        }, CallerThreadExecutor.getInstance());
    }


}
