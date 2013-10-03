package com.eaglesakura.aautil;

import android.app.Fragment;

/**
 * EActivity/EFragment系Util
 */
public class EUtil {

    /**
     * Annotationのコンバートクラスに変換する
     * @param clazz
     * @return
     */
    public static Class<?> annotation(Class<?> clazz) {
        try {
            return Class.forName(clazz.getName() + "_");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Annotation付きのフラグメントへ変換する
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Fragment> T newFragment(Class<T> clazz) {
        try {
            return (T) (annotation(clazz).newInstance());
        } catch (Exception e) {
            return null;
        }
    }

}
