package com.chinaso.cl.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.chinaso.cl.R;
import com.letv.recorder.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件的缓存与读取
 */
public class FileUtil {

	/**
	 * 获取缓存路径
	 * @param context
	 * @return
	 */
	public static String getCachePath(Context context) {
		String cachePath = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {

				cachePath =context.getExternalCacheDir().getPath() ;

		} else{
			cachePath = context.getCacheDir().getPath();
		}
		return cachePath;
	}

	/**
	 * 获取sd路径
	 * @param context
	 * @return
	 */
	public static String getSDPath(Context context) {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			// Toast.makeText(this,sdDir.toString(),Toast.LENGTH_LONG).show();
			return sdDir.toString();
		} else {
			Toast.makeText(context, "没有SD卡", Toast.LENGTH_LONG).show();
		}

		return null;
	}

	/**
	 * 保存bitmap到文件
	 * @param bmp
	 * @param filename
	 * @return
	 */
	public static boolean saveBitmap2File(Bitmap bmp, String filename) {
		CompressFormat format = CompressFormat.JPEG;
		int quality = 60;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bmp.compress(format, quality, stream);
	}
	public static File compressFile(Context context,String fileSrc){
		//just read size
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap img = BitmapFactory.decodeFile(fileSrc, options);

		//scale size to read
		options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max((double) options.outWidth / 1024f, (double) options.outHeight / 1024f)));
		options.inJustDecodeBounds = false;
		Log.i("ly", "options.inSampleSize-->" + options.inSampleSize);
		img = BitmapFactory.decodeFile(fileSrc, options);
		Log.i("ly", "file size after compress-->" + img.getByteCount() / 1024);

		String filename=getSDPath(context)+File.separator+"video-"+img.hashCode()+".jpg";
		saveBitmap2File(img, filename);

		return new File(filename);
	}
	public static boolean deleteFile(File file) {
		if (file.exists() && file.isFile()) {
			return	file.delete();
		}
		return false;
	}
}