package net.muxi.huashiapp.login;

//import android.support.annotation.NonNull;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;


/**
 * @author messi-wpy
 * 2019-05-19
 * <p>
 * 仿照android-async-http的SerializableCookie实现
 * 这其实是okhttp很坑的一点，不知道为什么okhttp的Cookie类没有序列化且是个final类,导致没办法本地化存储
 * <p>
 * 所以需要自定义一个可序列化的cookie，它包含了okhttp下的cookie
 * 这里用到了自定义序列化过程,通过重写writeobject()和readObject()来实现，底部ObjectOutputStream通过反射来调用用户自定义的这两个方法
 * 调用栈：writeObject ---> writeObject0 --->writeOrdinaryObject--->writeSerialData--->invokeWriteObject
 * 虽然看不到源码的具体实现,通过官方给出的解释class-defined writeObject method, or null if none也可看出是反射
 * <p>
 * transient关键字修饰的变量不会被序列化
 */
public class SerializableCookie implements Serializable {

    private static final long serialVersionUID = 5902009990929715767L;

    //final类必须显示初始化,即在定义时初始化或在构造器了里初始化，且每个构造器都必须初始化它
    private transient final Cookie cookie;
    private transient Cookie clientCookie;

    public SerializableCookie(@NonNull Cookie cookie) {
        this.cookie = cookie;
    }

    @NonNull
    public Cookie getCookie() {
        if (clientCookie == null) {
            return cookie;
        } else
            return clientCookie;
    }

    //严格书写方法签名,没有override注解检查,如果写错就无法反射调用
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.cookie.name());
        out.writeObject(this.cookie.value());
        out.writeLong(this.cookie.expiresAt());
        out.writeObject(this.cookie.domain());
        out.writeObject(this.cookie.path());
        out.writeBoolean(this.cookie.secure());
        out.writeBoolean(this.cookie.httpOnly());
        out.writeBoolean(this.cookie.hostOnly());
        out.writeBoolean(this.cookie.persistent());


    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        long expiresAt = in.readLong();
        String domain = (String) in.readObject();
        String path = (String) in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();

        //这个值在expiresAt(expiresAt)负了，其实不需要我们管
        boolean persistent = in.readBoolean();

        Cookie.Builder builder = new Cookie.Builder()
                .name(name)
                .value(value)
                .expiresAt(expiresAt)
                .path(path);

        builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
        builder = secure ? builder.secure() : builder;
        builder = httpOnly ? builder.httpOnly() : builder;
        this.clientCookie = builder.build();


    }


}

