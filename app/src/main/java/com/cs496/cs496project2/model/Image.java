package com.cs496.cs496project2.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Image implements Serializable {
    private String name;
    //private 결론은 외부저장소!
    private Date date;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy '@' hh:mm aaa", Locale.ENGLISH);
    private String description;
}
