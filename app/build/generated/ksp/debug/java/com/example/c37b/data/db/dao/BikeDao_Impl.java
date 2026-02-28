package com.example.c37b.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.c37b.data.db.Bike;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BikeDao_Impl implements BikeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Bike> __insertionAdapterOfBike;

  private final EntityDeletionOrUpdateAdapter<Bike> __deletionAdapterOfBike;

  private final EntityDeletionOrUpdateAdapter<Bike> __updateAdapterOfBike;

  public BikeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBike = new EntityInsertionAdapter<Bike>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bikes` (`id`,`name`,`lastServiceDate`,`currentMileage`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Bike entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getLastServiceDate());
        statement.bindLong(4, entity.getCurrentMileage());
      }
    };
    this.__deletionAdapterOfBike = new EntityDeletionOrUpdateAdapter<Bike>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `bikes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Bike entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBike = new EntityDeletionOrUpdateAdapter<Bike>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `bikes` SET `id` = ?,`name` = ?,`lastServiceDate` = ?,`currentMileage` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Bike entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getLastServiceDate());
        statement.bindLong(4, entity.getCurrentMileage());
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public Object insertBike(final Bike bike, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBike.insert(bike);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBike(final Bike bike, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBike.handle(bike);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBike(final Bike bike, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBike.handle(bike);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllBikes(final Continuation<? super List<Bike>> $completion) {
    final String _sql = "SELECT * FROM bikes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Bike>>() {
      @Override
      @NonNull
      public List<Bike> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLastServiceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastServiceDate");
          final int _cursorIndexOfCurrentMileage = CursorUtil.getColumnIndexOrThrow(_cursor, "currentMileage");
          final List<Bike> _result = new ArrayList<Bike>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Bike _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLastServiceDate;
            _tmpLastServiceDate = _cursor.getString(_cursorIndexOfLastServiceDate);
            final int _tmpCurrentMileage;
            _tmpCurrentMileage = _cursor.getInt(_cursorIndexOfCurrentMileage);
            _item = new Bike(_tmpId,_tmpName,_tmpLastServiceDate,_tmpCurrentMileage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
