package com.muxistudio.appcommon.utils;

import android.text.TextUtils;

import com.muxistudio.appcommon.data.AttentionBook;
import com.muxistudio.appcommon.data.BorrowedBook;
import com.muxistudio.common.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

/**
 * Created by ybao on 17/2/22.
 */

public class MyBooksUtils {

    public static void saveAttentionBooks(List<AttentionBook> attentionBooks){
        Observable.from(attentionBooks).map(attentionBook -> attentionBook.id)
                .toList()
                .subscribe(strings -> {
                    PreferenceUtil.saveString(PreferenceUtil.ATTENTION_BOOK_IDS, TextUtils.join(",",strings));
                });
    }

    public static void saveBorrowedBooks(List<BorrowedBook> borrowedBooks){
        Observable.from(borrowedBooks).map(personalBook -> personalBook.id)
                .toList()
                .subscribe(strings -> {
                    PreferenceUtil.saveString(PreferenceUtil.BORROW_BOOK_IDS, TextUtils.join(",",strings));
                });
    }

    public static List<String> getAttentionBooksId(){
        String s = PreferenceUtil.getString(PreferenceUtil.ATTENTION_BOOK_IDS);
        List<String> attentionBookList = new ArrayList<>();
        Collections.addAll(attentionBookList,s.split(","));
        return attentionBookList;
    }

    public static List<String> getBorrowedBooksId(){
        String s = PreferenceUtil.getString(PreferenceUtil.BORROW_BOOK_IDS);
        List<String> borrowedBookList = new ArrayList<>();
        Collections.addAll(borrowedBookList,s.split(","));
        return borrowedBookList;
    }
}
