package net.muxi.huashiapp.common.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 16/5/5.
 * 某本图书的详情
 */
public class Book implements Parcelable {

    public String bid;
    public String book;
    public String intro;
    public String author;
    public List<BooksBean> books;

    public static class BooksBean {
        public String status;
        public String tid;
        public String room;
        public String bid;
        public String date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bid);
        dest.writeString(this.book);
        dest.writeString(this.intro);
        dest.writeString(this.author);
        dest.writeList(this.books);
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.bid = in.readString();
        this.book = in.readString();
        this.intro = in.readString();
        this.author = in.readString();
        this.books = new ArrayList<BooksBean>();
        in.readList(this.books, BooksBean.class.getClassLoader());
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
