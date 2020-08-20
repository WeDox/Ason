package com.onedream.json.ason;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jdallen
 * @since 2020/8/19
 */
public class Ason {

    public static <T> List<T> parseObjectList(String jsonDataStr, Class<T> clazz) throws Exception {
        JSONArray jsonArray = new JSONArray(jsonDataStr);
        ArrayList<T> dataList = new ArrayList<>();
        for (int i = 0, j = jsonArray.length(); i < j; i++) {

            if (clazz.getSimpleName().equals("int")) {
                Integer b = jsonArray.getInt(i);
                dataList.add((T) b);
                break;
            }
            if (clazz.getSimpleName().equals("long")) {
                Long b = jsonArray.getLong(i);
                dataList.add((T) b);
                break;
            }
            if (clazz.getSimpleName().equals("double")) {
                Double b = jsonArray.getDouble(i);
                dataList.add((T) b);
                break;
            }
            if (clazz.getSimpleName().equals("String")) {
                String b = jsonArray.getString(i);
                dataList.add((T) b);
                break;
            }
            if (clazz.getSimpleName().equals("boolean")) {
                Boolean b = (Boolean) (jsonArray.getBoolean(i));
                dataList.add((T) b);
                break;
            }
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T object = parseObject(jsonObject.toString(), clazz);
            dataList.add(object);
        }
        return dataList;
    }

    public static <T> T parseObject(String jsonDataStr, Class<T> clazz) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonDataStr);
        T obj = (T) clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            Object value = getValueByKey(field, field.getType(), jsonObject, key);
            Log.e("ATU", "==" + field.getName() + "===>" + key + "===>" + value);
            //获取是否可访问
            boolean flag = field.isAccessible();
            //设置该属性总是可访问
            field.setAccessible(true);
            field.set(obj, value);
            //还原可访问权限
            field.setAccessible(flag);
        }
        return obj;
    }


    private static Object getValueByKey(Field field, Class<?> clazz, JSONObject jsonObject, String key) throws Exception {
        if (clazz.getSimpleName().equals("int")) {
            return OriginJsonParseUtils.getInt(jsonObject, key);
        }
        if (clazz.getSimpleName().equals("long")) {
            return OriginJsonParseUtils.getLong(jsonObject, key);
        }
        if (clazz.getSimpleName().equals("double")) {
            return OriginJsonParseUtils.getDouble(jsonObject, key);
        }
        if (clazz.getSimpleName().equals("String")) {
            return OriginJsonParseUtils.getString(jsonObject, key);
        }
        if (clazz.getSimpleName().equals("boolean")) {
            return OriginJsonParseUtils.getBoolean(jsonObject, key);
        }
        String jsonObjStr = OriginJsonParseUtils.getString(jsonObject, key);
        //泛型
        if (field.getType() == java.util.List.class) {
            return parseObjectList(jsonObjStr, getTrueClassByList(field, clazz));
        }
        if (jsonObjStr.startsWith("{")) {
            return parseObject(jsonObjStr, clazz);
        }
        if (TextUtils.isEmpty(jsonObjStr) || jsonObjStr.equals("null")) {
            return null;
        }
        throw new Exception("暂不支持该类型数据的解析" + clazz.getSimpleName());
    }

    private static Class<?> getTrueClassByList(Field field, Class<?> defaultClazz) {
        if (field.getType() == java.util.List.class) {
            // 如果是List类型，得到其Generic的类型
            Type genericType = field.getGenericType();
            // 如果是泛型参数的类型
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                //得到泛型里的class类型对象
                Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                return genericClazz;
            }
        }
        return defaultClazz;
    }

}
