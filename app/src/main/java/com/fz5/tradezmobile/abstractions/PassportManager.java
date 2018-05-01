package com.fz5.tradezmobile.abstractions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


/**
 * Created by Francis Ilechukwu 05/08/2017.
 */
public class PassportManager {

    private static final char[] symbols;
    private final Random random = new Random();
    private Context context;
    private boolean ready = false;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ch++) {
            tmp.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            tmp.append(ch);
        }
        symbols = tmp.toString().toCharArray();
    }

    public Object[] createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            return new Object[] {image, image.getAbsolutePath()};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PassportManager(Context context) {
        this.context = context;
        File file = new File(context.getFilesDir(), "passports");
        if (!file.exists()) {
            if (file.mkdirs()) {
                ready = true;
            }
        } else {
            ready = true;
        }
    }

    public boolean savePassport(String fileName, Bitmap passport) {
        File passportFile = getFileObject(fileName);
        boolean success = false;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(passportFile);
            passport.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
            success = true;
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
            Toast.makeText(context, "Permission to Save Passport Denied.", Toast.LENGTH_LONG).show();
        } catch (IOException ex2) {
            ex2.printStackTrace();
            Toast.makeText(context, "Error Saving Passport.", Toast.LENGTH_LONG).show();
        }
        return success;
    }

    public Bitmap getPassport(String name) {
        File passportFile = getFileObject(name);
        if (passportFile.exists()) {
            try {
                return BitmapFactory.decodeStream(new FileInputStream(passportFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bitmap getCircularBitmap(Bitmap bmp, double radiusPercentage) {
        if (radiusPercentage <= 100) {
            int w = bmp.getWidth();
            int h = bmp.getHeight();
            int radius = (w < h) ? w : h;
            radiusPercentage = radiusPercentage / 100;
            radius = (int) (radius * radiusPercentage);
            w = radius;
            h = radius;
            Bitmap bmOut = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmOut);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(0xff424242);
            Rect rect = new Rect(0, 0, w, h);
            RectF rectF = new RectF(rect);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawCircle(rectF.left + (rectF.width() / 2), rectF.top + (rectF.height() / 2), radius / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bmp, rect, rect, paint);
            return bmOut;
        } else {
            return null;
        }
    }

    public InputStream getPassportInputStream(String name) {
        File passportFile = getFileObject(name);
        if (passportFile.exists()) {
            try {
                return new FileInputStream(passportFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public OutputStream getPassportOutputStream(String name) {
        File passportFile = getFileObject(name);
        try {
            if (passportFile.exists()) {
                return new FileOutputStream(passportFile);
            } else {
                if (passportFile.createNewFile()) {
                    return new FileOutputStream(passportFile);
                }
            }
            /*if (!passportFile.exists()) {
                return context.openFileOutput(name, MODE_PRIVATE);
            } else {
                if (passportFile.delete()) {
                    return context.openFileOutput("passports/" + name, MODE_PRIVATE);
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> resolvePassportsToDownload(ArrayList<String> allPassports) {
        ArrayList<String> toDownload = new ArrayList<>();
        if (allPassports != null) {
            for (String passport : allPassports) {
                File file = getFileObject(passport);
                if (!file.exists()) {
                    toDownload.add(passport);
                }
            }
        }
        if (toDownload.size() > 0) {
            return toDownload;
        }
        return null;
    }

    public String generateAlphaNum(int length) {
        char[] buff = new char[length];
        for (int i = 0; i < buff.length; i++) {
            buff[i] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buff);
    }

    private File getFileObject(String fileName) {
        return new File(new File(context.getFilesDir(), "passports"), fileName);
    }

    public long getUsedSpace() {
        File files = new File(context.getFilesDir(), "passports");
        long length = 0;
        for (File file : files.listFiles()) {
            length += file.length();
        }
        return length / 1024;
    }

    public boolean clearPassports() {
        File files = new File(context.getFilesDir(), "passports");
        for (File file : files.listFiles()) {
            if (!file.delete()) {
                return false;
            }
        }
        return true;
    }

    public int getPassportsCount() {
        File files = new File(context.getFilesDir(), "passports");
        return files.listFiles().length;
    }

    public long getPassportFileSize(String name) {
        return getFileObject(name).length();
    }

}
