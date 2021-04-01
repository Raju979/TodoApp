package com.rajuthapa.todoapp.data.category;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
   private String cat_name;

   private String cat_des;


    public int getId() {
        return id;
    }

    @NonNull
    public String getCat_name() {
        return cat_name;
    }

    public String getCat_des() {
        return cat_des;
    }

    public Category(@NonNull String cat_name, String cat_des) {
        this.cat_name = cat_name;
        this.cat_des = cat_des;
    }

    public void setId(int id) {
        this.id = id;
    }
}
