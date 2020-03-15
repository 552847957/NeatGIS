package com.moyear.neatgis.Common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableConvert {

    /**
     * 将byte数组转化为object类
     *
     * @param bytes
     * @return
     */
    public static Object ByteToObject(byte[] bytes) {
        java.lang.Object obj = null;
        try{
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * 将object类转为byte数组
     *
     * @param obj
     * @return
     */
    public static byte[] ObjectToByte(Object obj) {
        byte[] bytes = null;
        try {
            //object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
