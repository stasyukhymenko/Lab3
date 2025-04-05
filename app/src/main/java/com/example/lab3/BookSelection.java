package com.example.lab3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_selections")
public class BookSelection {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String bookName;
    public String selectionYear;

    public BookSelection(String bookName, String selectionYear) {
        this.bookName = bookName;
        this.selectionYear = selectionYear;
    }
}