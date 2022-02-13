package com.geektech.noteapp3lesson2;

import androidx.room.Database;
import androidx.room.Query;
import androidx.room.RoomDatabase;

@Database(entities = {NotesModel.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NotesDao dao();

}
