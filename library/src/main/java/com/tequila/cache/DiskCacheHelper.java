package com.tequila.cache;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import com.tequila.base.BaseApplication;
import com.tequila.utils.CompatUtil;
import com.tequila.model.BaseResult;
import com.tequila.utils.CheckUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Disk Cache Helper
 */
public class DiskCacheHelper {

	@TargetApi(8)
	public static File getExternalFilesDir(Context context) {
		if (CompatUtil.hasFroyo()) {
			return context.getExternalFilesDir(null);
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String dir = "/Android/data/" + context.getPackageName() + "/files/";
		return new File(Environment.getExternalStorageDirectory().getPath() + dir);
	}

	public static BaseResult getDataWithCacheKey(String cacheKey, Class<? extends BaseResult> type) throws Exception {
		String dir = getDiskCacheDir();
		if (dir != null) {
			File cacheFile = new File(join(dir, cacheKey));
			if (!cacheFile.exists()) {
				return null;
			}

			BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
			StringBuilder sb = new StringBuilder();
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				sb.append(line);
			}

			String json = sb.toString();
			BaseResult result = JSONObject.parseObject(json, type);
			return result;
		}
		return null;
	}

	public static BaseResult getDataWithCacheKeySafe(String cacheKey, Class<? extends BaseResult> type) {
		try {
			return getDataWithCacheKey(cacheKey, type);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean putDataToDiskSafe(String cacheKey, BaseResult result) {
		try {
			return putDataToDisk(cacheKey, result);
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean putDataToDisk(String cacheKey, BaseResult result) throws IOException {
		String dir = getDiskCacheDir();
		if (dir != null) {
			File cacheFile = new File(join(dir, cacheKey));
			if (cacheFile.exists()) {
				boolean success = cacheFile.delete();
				if (!success) {
					return false;
				}
			}
			String data = JSONObject.toJSONString(result);
			//TODO 加密
			FileWriter writer = new FileWriter(cacheFile, false);
			writer.write(data);
			writer.flush();
			writer.close();
		}
		return false;
	}

	public static void delAllCache() {
		try {
			String dir = getDiskCacheDir();
			if (!TextUtils.isEmpty(dir)) {
				File dirFile = new File(dir);
				if (dirFile.exists()) {
					File[] files = dirFile.listFiles();
					if (!CheckUtils.isEmpty(files)) {
						for (File file : files) {
							file.delete();
						}
					}
				}
			}
		} catch (Exception e) {

		}
	}

	public static String getDiskCacheDir() {
		try {
			File rootFile = getExternalFilesDir(BaseApplication.getContext());
			if (!rootFile.exists()) {
				rootFile.mkdirs();
			}
			String diskDir = join(rootFile.getAbsolutePath(), "rcache");
			if (!CheckUtils.isEmpty(diskDir)) {
				File diskFile = new File(diskDir);
				if (!diskFile.exists()) {
					diskFile.mkdirs();
				}
				return diskDir;
			}
		} catch (Exception e) {
			Log.e("DiskCacheHelper", "create file error " + e.getMessage());
		}
		return null;
	}

	public static String join(String root, String dir) {
		if (root == null || dir == null) {
			throw new IllegalArgumentException("root or dir is null...");
		}

		boolean endSp = root.endsWith("/");
		boolean strSp = dir.startsWith("/");

		if (endSp && strSp) {
			return root + dir.substring(1, dir.length());
		} else if (!endSp && !strSp) {
			return root + "/" + dir;
		} else {
			return root + dir;
		}

	}

	/**
	 * A hashing method that changes a string (like a URL) into a hash suitable for using as a disk filename.
	 */
	public static String hashKeyForDisk(String key) {
		if (key == null) {
			return null;
		}
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static boolean remove(String cacheKey) {

		if (TextUtils.isEmpty(cacheKey)) {
			return false;
		}

		String dir = getDiskCacheDir();
		if (!TextUtils.isEmpty(dir)) {
			File cacheFile = new File(join(dir, cacheKey));
			if (cacheFile.exists()) {
				boolean success = cacheFile.delete();
				return success;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
