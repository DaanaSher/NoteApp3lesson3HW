package com.geektech.noteapp3lesson2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotesModel {
    @PrimaryKey(autoGenerate = true)
    public int  id;
    private String title, description, date;

    public NotesModel(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
