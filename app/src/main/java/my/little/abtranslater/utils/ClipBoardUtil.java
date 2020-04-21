package my.little.abtranslater.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import my.little.abtranslater.MyApplication;

public class ClipBoardUtil {
    public static String paste(){
        ClipboardManager manager = (ClipboardManager) MyApplication.getContextObject().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    /**
     * 清空剪切板
     */
    public static void clear(){
        ClipboardManager manager = (ClipboardManager) MyApplication.getContextObject().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            try {
                manager.setPrimaryClip(manager.getPrimaryClip());
                manager.setPrimaryClip(ClipData.newPlainText("",""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void write(String text){
        ClipboardManager manager = (ClipboardManager) MyApplication.getContextObject().getSystemService(Context.CLIPBOARD_SERVICE);
        if(manager != null){
            manager.setPrimaryClip(manager.getPrimaryClip());
            manager.setPrimaryClip(ClipData.newPlainText("",text));
        }
        return;
    }
}

