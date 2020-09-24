package com.feiyueve.snsdemo.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.alibaba.fastjson.JSONObject;
import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.MyApplication;
import com.feiyueve.snsdemo.dataBase.DBHelper;
import com.feiyueve.snsdemo.webservice.WebService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BitmapCache {
    private static BitmapCache cache;
    private LruCache<String, Bitmap> mMemoryCache;

    public static BitmapCache getInstance() {
        if (cache == null) {
            cache = new BitmapCache();
        }
        return cache;
    }

    public BitmapCache() {
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = (450 * 16 * 1024);

        if (cacheSize > (maxMemory / 3))
            cacheSize = (maxMemory / 3);

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getRowBytes() * bitmap.getHeight();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (!oldValue.isRecycled()) {
                    oldValue.recycle();
                }
            }
        };

    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromDataBase(SQLiteDatabase db,String userName){
        Bitmap bitmap = null;
        Cursor cursor = db.query("loginData", null, "thumbnail" +" = ?", new String[]{userName}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                bitmap = EncodeBase64.stringtoBitmap(cursor.getString(cursor.getColumnIndex("thumbnail")));
            }
        }
        return bitmap;
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
    /**
     * 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
     * @param myApplication 图片url
     * @return Bitmap
     */
    public Bitmap getBitmap(final SQLiteDatabase db, final MyApplication myApplication, final String userName1) throws IOException {
        final String userName = myApplication.getPersonalInf().getUsername();
        Bitmap bmp = getBitmapFromMemCache(userName1);
        if (bmp == null) {
            bmp = getBitmapFromDataBase(db, userName1);
            if (bmp == null) {
                final Map data = new HashMap<String, String>();
                data.put("userName", userName1);
                String bmpString = null;
                try {
                    bmpString = WebService.getByURL("http://218.244.151.221:8080/user/getUserByUserName", data, myApplication.getLoginData().getUserKey());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject json = JSONObject.parseObject(bmpString);
                JSONObject jsonObject = json.getJSONObject("result");
                String head = jsonObject.getString("thumbnail");

                if (head != null) {
                    bmp = EncodeBase64.stringtoBitmap(head);
                }

                if (bmp != null) {
                    addBitmapToMemoryCache(userName1, bmp);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("userName", userName1);
                    contentValues.put("loginStatus", "true");
                    contentValues.put("thumbnail", head);
                    db.replace("loginData", null, contentValues);
                }
            }

        }
        return bmp;
    }
}
