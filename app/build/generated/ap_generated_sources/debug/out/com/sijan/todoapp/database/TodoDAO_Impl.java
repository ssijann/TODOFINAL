package com.sij.todoapp.database;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sij.todoapp.model.ETodo;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TodoDAO_Impl implements TodoDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ETodo> __insertionAdapterOfETodo;

  private final EntityDeletionOrUpdateAdapter<ETodo> __deletionAdapterOfETodo;

  private final EntityDeletionOrUpdateAdapter<ETodo> __updateAdapterOfETodo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCompleted;

  public TodoDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfETodo = new EntityInsertionAdapter<ETodo>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `todo_table` (`id`,`title`,`description`,`todo_date`,`priority`,`is_completed`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ETodo value) {
        stmt.bindLong(1, value.getId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        final Long _tmp;
        _tmp = DateConverter.toTimeStamp(value.getTodo_date());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        stmt.bindLong(5, value.getPriority());
        final int _tmp_1;
        _tmp_1 = value.isIs_completed() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
      }
    };
    this.__deletionAdapterOfETodo = new EntityDeletionOrUpdateAdapter<ETodo>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `todo_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ETodo value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfETodo = new EntityDeletionOrUpdateAdapter<ETodo>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `todo_table` SET `id` = ?,`title` = ?,`description` = ?,`todo_date` = ?,`priority` = ?,`is_completed` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ETodo value) {
        stmt.bindLong(1, value.getId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        final Long _tmp;
        _tmp = DateConverter.toTimeStamp(value.getTodo_date());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        stmt.bindLong(5, value.getPriority());
        final int _tmp_1;
        _tmp_1 = value.isIs_completed() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
        stmt.bindLong(7, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM todo_table";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCompleted = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete From todo_table where is_completed=1";
        return _query;
      }
    };
  }

  @Override
  public void insert(final ETodo todo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfETodo.insert(todo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final ETodo todo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfETodo.handle(todo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ETodo... todos) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfETodo.handleMultiple(todos);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public void deleteCompleted() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCompleted.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteCompleted.release(_stmt);
    }
  }

  @Override
  public LiveData<List<ETodo>> getAllTodos() {
    final String _sql = "SELECT * FROM todo_table ORDER BY todo_date, priority desc";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"todo_table"}, false, new Callable<List<ETodo>>() {
      @Override
      public List<ETodo> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfTodoDate = CursorUtil.getColumnIndexOrThrow(_cursor, "todo_date");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
          final List<ETodo> _result = new ArrayList<ETodo>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final ETodo _item;
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Date _tmpTodo_date;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTodoDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTodoDate);
            }
            _tmpTodo_date = DateConverter.toDate(_tmp);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final boolean _tmpIs_completed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIs_completed = _tmp_1 != 0;
            _item = new ETodo(_tmpTitle,_tmpDescription,_tmpTodo_date,_tmpPriority,_tmpIs_completed);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public ETodo getTodoById(final int id) {
    final String _sql = "SELECT * FROM todo_table WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfTodoDate = CursorUtil.getColumnIndexOrThrow(_cursor, "todo_date");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
      final ETodo _result;
      if(_cursor.moveToFirst()) {
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Date _tmpTodo_date;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfTodoDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfTodoDate);
        }
        _tmpTodo_date = DateConverter.toDate(_tmp);
        final int _tmpPriority;
        _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
        final boolean _tmpIs_completed;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsCompleted);
        _tmpIs_completed = _tmp_1 != 0;
        _result = new ETodo(_tmpTitle,_tmpDescription,_tmpTodo_date,_tmpPriority,_tmpIs_completed);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
