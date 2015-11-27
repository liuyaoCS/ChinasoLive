//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utls {
    public MD5Utls() {
    }

    public static String stringToMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var7) {
            var7.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException var8) {
            var8.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] var6 = hash;
        int var5 = hash.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            byte b = var6[var4];
            if((b & 255) < 16) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(b & 255));
        }

        return hex.toString();
    }

    public static String encodeByMD5(String str) {
        if(str == null) {
            return null;
        } else {
            try {
                MessageDigest e = MessageDigest.getInstance("MD5");
                e.update(str.getBytes());
                byte[] b = e.digest();
                String md5 = getString(b);
                return md5;
            } catch (Exception var4) {
                var4.printStackTrace();
                return "";
            }
        }
    }

    private static String getString(byte[] b) {
        StringBuffer buf = new StringBuffer("");

        for(int offset = 0; offset < b.length; ++offset) {
            int i = b[offset];
            if(i < 0) {
                i += 256;
            }

            if(i < 16) {
                buf.append("0");
            }

            buf.append(Integer.toHexString(i));
        }

        return buf.toString();
    }
}
