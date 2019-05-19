package net.muxi.huashiapp.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Cookie;


/**
 * @author messi-wpy
 * 参考 android-async-http这个开源库 的Cookie管理机制方案，PersistentCookieStore 进行改造 ccnu定制
 * 主要进行okhttp cookie的本地持久化存储
 */
public class CookieManager {

    private final SharedPreferences cookiePrefs;
    private static final String HOST_PRE = "HOST_";
    private HashMap<String, List<Cookie>> cookies;
    private HashMap<String, List<Cookie>> newCookies;
    private static final String COOKIE_FILE = "CookiePreFile";

    /**
     * @param context 必须是application的context,用app类里的，千万别用当前activity的Context否则造成内存泄漏
     *                关于在构造函数里进行很多操作是否ok的讨论:  https://stackoverflow.com/questions/7048515/is-doing-a-lot-in-constructors-bad
     */
    public CookieManager(Context context) {
        this.cookiePrefs = context.getSharedPreferences(COOKIE_FILE, 0);
    }


    /**
     * 从sharePreference中获取数据
     * cookie的存储格式,整体是一个map,根据其host为cookie分组
     * 而每个cookie存在sharePreference里的key是  host___index  这样一个字符串  (index是这个cookie在其分组的list的序号)
     * HashMap<String , List<Cookie>>
     */

    public void getDataFromPre() {
        if (cookies != null) {
            return;
        }
        cookies = new HashMap<>();
        Map tempCookieMap = new HashMap<String, Object>(cookiePrefs.getAll());

        for (Object key : tempCookieMap.keySet()) {
            String host = ((String) key).split("___")[0];
            if (!cookies.containsKey(host)) {
                cookies.put(host, new ArrayList<>());
            }
            cookies.get(host).add(decodeCookie((String) tempCookieMap.get(key)));

        }

    }

    /**
     * 请求成功服务器不会返回cookie，所以也不需要添加，更新
     * 如果请求了,说明登录失败，之前的cookie过期了需要把这个替换上去
     *
     * @param host
     * @param cookie
     */
    public void addCookie(String host, Cookie cookie) {
        if (this.newCookies == null) {
            newCookies = new HashMap<>();
        }
        if (newCookies.get(host) == null) {
            newCookies.put(host, new ArrayList<>());
        }
        newCookies.get(host).add(cookie);


    }

    public void addAll(String host,List<Cookie>list){
        if (newCookies==null){
            newCookies=new HashMap<>();
        }
        if (newCookies.get(host)==null){
            newCookies.put(host,new ArrayList<>());
        }

        newCookies.get(host).addAll(list);


    }

    /**
     * 这个方法其实是比较新cookie和旧cookie的差别,
     * 如果不同说明更新了，要更换
     * <p>
     * 最终存储的实现写在{@link #performSave(Map)} 方法里
     */
    public void saveToPre() {
        if (newCookies == null) {
            return;
        } else if (cookies.keySet().size() == 0) {
            performSave(newCookies);
        } else {
            for (String oldKey : cookies.keySet()) {
                if (newCookies.get(oldKey) == null) {
                    newCookies.put(oldKey, new ArrayList<>());
                    newCookies.get(oldKey).addAll(cookies.get(oldKey));
                }

            }
            performSave(newCookies);
        }

    }

    private void performSave(Map<String, List<Cookie>> res) {
        SharedPreferences.Editor writer = cookiePrefs.edit();

        for (String key : res.keySet()) {
            List<Cookie> list = res.get(key);
            for (int i = 0; i < list.size(); i++) {
                String preKey = key + "___" + i;
                writer.putString(preKey, encodeCookie(new SerializableCookie(list.get(i))));
            }

        }
        writer.apply();

    }

    @NotNull
    public List<Cookie> provideCookies(String host) {
        getDataFromPre();
        if (newCookies!=null&&newCookies.get(host)!=null){
            return newCookies.get(host);
        }
        else if (cookies!=null&&cookies.get(host)!=null){
            return cookies.get(host);
        }
        else
            return Collections.emptyList();

    }


    @Nullable
    protected String encodeCookie(SerializableCookie cookie) {
        if (cookie == null) return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(os);
            out.writeObject(cookie);
            bytes = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                out.close();
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArrayToHexString(bytes);
    }


    @Nullable
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        ObjectInputStream objectIn = null;
        try {
            objectIn = new ObjectInputStream(in);
            cookie = ((SerializableCookie) objectIn.readObject()).getCookie();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                objectIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cookie;

    }


    /**
     * 这是一个将字符数组转换为16进制表示的string的算法
     * 一个字节可以表示两个16进制的数-128~127--->255（128-255会溢出变成负数）
     * elem & 0xff这是为了让有符号的byte转换为int,
     * 因为当byte是负数时他的int值会变11111...（反码）
     * <p>
     * 最后把变成大写，是为了方便之后的base64编码,
     * (16进制 a b c d e f 大小写对其值也没有影响嘛)
     *
     * @return
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte elem : bytes) {
            int value = elem & 0xff;
            if (value < 16) {
                sb.append(0);
            }
            sb.append(Integer.toHexString(value));


        }
        return sb.toString().toUpperCase(Locale.US);
    }

    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }


}
