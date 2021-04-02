package com.sij.todoapp.database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sij.todoapp.model.ETodo;

import java.util.Date;

@Database(entities = {ETodo.class},version=1,exportSchema=false)
public abstract class TodoRoomDatabase extends RoomDatabase {
    public abstract TodoDAO mTodoDAO();

    private static TodoRoomDatabase INSTANCE;
    public static TodoRoomDatabase getDatabase(Context context){
        if(INSTANCE==null){
            synchronized (TodoRoomDatabase.class){
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoRoomDatabase.class, "todo.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(callback)
                            .build();
                }
            }
        }return INSTANCE;
    }

    private static class populateDbAsynchTask extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDAO;
        private populateDbAsynchTask(TodoRoomDatabase db){
            mTodoDAO=db.mTodoDAO();
        }

        @Override
        protected Void doInBackground(ETodo... todos) {
            Date date = new Date();
            ETodo todo = new ETodo("Demo Title","Demo Description",date, 1, false);
            mTodoDAO.insert(todo);
            return null;
        }
    }
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDbAsynchTask(INSTANCE).execute();
        }
    };

}

