package com.geektech.noteapp3lesson2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notesmodel")
    List<NotesModel> getALL();

    @Insert
    void addNote(NotesModel notesModel);
}
