package com.sij.todoapp.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.sij.todoapp.model.ETodo;

import java.util.List;

public class Repository {
    private TodoDAO mTodoDAO;
    private LiveData<List<ETodo>> mAllTodoList;

    public Repository(Application application) {
        TodoRoomDatabase database = TodoRoomDatabase.getDatabase(application);
        mTodoDAO=database.mTodoDAO();
        mAllTodoList=mTodoDAO.getAllTodos();

    }

    public LiveData<List<ETodo>> getmAllTodoList() {
        return mAllTodoList;
    }

    public ETodo getTodoById(int id){
        return mTodoDAO.getTodoById(id);
    }

    public void insert(ETodo todo){
        new insertTodoAsynchTask(mTodoDAO).execute(todo);
    }

    public void deleteAll(){
        new deleteAllTodoAsynchTask(mTodoDAO).execute();
    }
    public void update(ETodo todo){
        new updateTodoAsynchTask(mTodoDAO).execute(todo);
    }

    public void deleteById(ETodo todo){
        new deleteByIdTodoAsynchTask(mTodoDAO).execute(todo);
    }
    public void deleteCompleted(){
        new deleteCompletedTodoAsynchTask(mTodoDAO).execute();
    }

    private static class deleteCompletedTodoAsynchTask extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDAO;
        private deleteCompletedTodoAsynchTask(TodoDAO todoDAO){
            mTodoDAO = todoDAO;
        }

        @Override
        protected Void doInBackground(ETodo... todos) {

            mTodoDAO.deleteCompleted();
            return null;
        }
    }

    private static class insertTodoAsynchTask extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDAO;
        private insertTodoAsynchTask(TodoDAO todoDAO){
            mTodoDAO = todoDAO;
        }

        @Override
        protected Void doInBackground(ETodo... todos) {
            mTodoDAO.insert(todos[0]);
            return null;
        }
    }
    private static class updateTodoAsynchTask extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDAO;
        private updateTodoAsynchTask(TodoDAO todoDAO){
            mTodoDAO = todoDAO;
        }

        @Override
        protected Void doInBackground(ETodo... todos) {
            mTodoDAO.update(todos[0]);
            return null;
        }
    }
    private static class deleteAllTodoAsynchTask extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDAO;
        private deleteAllTodoAsynchTask(TodoDAO todoDAO){
            mTodoDAO = todoDAO;
        }

        @Override
        protected Void doInBackground(ETodo... todos) {

            mTodoDAO.deleteAll();
            return null;
        }
    }
    private static class deleteByIdTodoAsynchTask extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDAO;
        private deleteByIdTodoAsynchTask(TodoDAO todoDAO){
            mTodoDAO = todoDAO;
        }

        @Override
        protected Void doInBackground(ETodo... todos) {
            mTodoDAO.deleteById(todos[0]);
            return null;
        }
    }
}

