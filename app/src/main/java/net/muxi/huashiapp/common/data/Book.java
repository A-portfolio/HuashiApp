package net.muxi.huashiapp.common.data;

import java.util.List;

/**
 * Created by ybao on 16/5/5.
 * 某本图书的详情
 */
public class Book {

    public String bid;
    public String book;
    public String intro;
    public String author;

    public List<BooksBean> books;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<BooksBean> getBooks() {
        return books;
    }

    public void setBooks(List<BooksBean> books) {
        this.books = books;
    }

    public static class BooksBean {
        public String status;
        public String tid;
        public String date;
        public String room;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }
}
