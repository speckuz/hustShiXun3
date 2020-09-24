package com.feiyueve.snsdemo.EncodeBase64;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncodeBase64 {

    public static String bitmaptoString(Bitmap bitmap){
        //将Bitmap转换成字符串
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        String string = "";
        if(bitmap!=null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] bytes = bStream.toByteArray();
            string = new Base64().encodeToString(bytes);
        }
        return string;
    }

    public static String fileToString(String path) throws IOException {
        File  file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new Base64().encodeToString(buffer);
    }

    public static void stringToFile(String base64Code, String savePath) throws Exception {
        byte[] buffer = new Base64().decode(base64Code);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }


    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = new Base64().decode(string);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
