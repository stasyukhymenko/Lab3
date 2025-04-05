package com.example.lab3;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BookSelectionDao {

    @Insert
    void insert(BookSelection bookSelection);

    @Query("SELECT * FROM book_selections ORDER BY id DESC")
    List<BookSelection> getAllSelections();

    @Query("DELETE FROM book_selections")
    void deleteAll();

    @Delete
    void delete(BookSelection bookSelection);
}