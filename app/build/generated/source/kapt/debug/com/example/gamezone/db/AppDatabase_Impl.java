package com.example.gamezone.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile GameDao _gameDao;

  private volatile CartDao _cartDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fullName` TEXT NOT NULL, `email` TEXT NOT NULL, `password` TEXT NOT NULL, `phone` TEXT, `genres` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `games` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `genre` TEXT NOT NULL, `thumbnail` TEXT NOT NULL, `short_description` TEXT NOT NULL, `description` TEXT NOT NULL, `platform` TEXT NOT NULL, `developer` TEXT NOT NULL, `release_date` TEXT NOT NULL, `price` INTEGER NOT NULL, `stock` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cart_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `gameId` INTEGER NOT NULL, `title` TEXT NOT NULL, `price` INTEGER NOT NULL, `quantity` INTEGER NOT NULL, `thumbnail` TEXT NOT NULL, `isLocalGame` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f3fc71fca962ef351284121b21c89fa3')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `games`");
        db.execSQL("DROP TABLE IF EXISTS `cart_items`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(6);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fullName", new TableInfo.Column("fullName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("genres", new TableInfo.Column("genres", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.example.gamezone.db.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsGames = new HashMap<String, TableInfo.Column>(11);
        _columnsGames.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("genre", new TableInfo.Column("genre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("thumbnail", new TableInfo.Column("thumbnail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("short_description", new TableInfo.Column("short_description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("platform", new TableInfo.Column("platform", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("developer", new TableInfo.Column("developer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("release_date", new TableInfo.Column("release_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("price", new TableInfo.Column("price", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGames.put("stock", new TableInfo.Column("stock", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGames = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGames = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGames = new TableInfo("games", _columnsGames, _foreignKeysGames, _indicesGames);
        final TableInfo _existingGames = TableInfo.read(db, "games");
        if (!_infoGames.equals(_existingGames)) {
          return new RoomOpenHelper.ValidationResult(false, "games(com.example.gamezone.db.LocalGame).\n"
                  + " Expected:\n" + _infoGames + "\n"
                  + " Found:\n" + _existingGames);
        }
        final HashMap<String, TableInfo.Column> _columnsCartItems = new HashMap<String, TableInfo.Column>(8);
        _columnsCartItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCartItems.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCartItems.put("gameId", new TableInfo.Column("gameId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCartItems.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCartItems.put("price", new TableInfo.Column("price", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCartItems.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCartItems.put("thumbnail", new TableInfo.Column("thumbnail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCartItems.put("isLocalGame", new TableInfo.Column("isLocalGame", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCartItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCartItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCartItems = new TableInfo("cart_items", _columnsCartItems, _foreignKeysCartItems, _indicesCartItems);
        final TableInfo _existingCartItems = TableInfo.read(db, "cart_items");
        if (!_infoCartItems.equals(_existingCartItems)) {
          return new RoomOpenHelper.ValidationResult(false, "cart_items(com.example.gamezone.db.CartItem).\n"
                  + " Expected:\n" + _infoCartItems + "\n"
                  + " Found:\n" + _existingCartItems);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f3fc71fca962ef351284121b21c89fa3", "199e831542fd7e65d1a3053e32752b4e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","games","cart_items");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `games`");
      _db.execSQL("DELETE FROM `cart_items`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GameDao.class, GameDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CartDao.class, CartDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public GameDao gameDao() {
    if (_gameDao != null) {
      return _gameDao;
    } else {
      synchronized(this) {
        if(_gameDao == null) {
          _gameDao = new GameDao_Impl(this);
        }
        return _gameDao;
      }
    }
  }

  @Override
  public CartDao cartDao() {
    if (_cartDao != null) {
      return _cartDao;
    } else {
      synchronized(this) {
        if(_cartDao == null) {
          _cartDao = new CartDao_Impl(this);
        }
        return _cartDao;
      }
    }
  }
}
