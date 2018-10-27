package com.prj666_183a06.xbudget.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.prj666_183a06.xbudget.database.entity.UserEntity;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user_table WHERE user_name=:userName AND user_password=:userPassword")
    LiveData<UserEntity> getUserAuthenticate(String userName, String userPassword);

    @Insert
    void registerUser(UserEntity userEntity);

    @Update
    void updateUser(UserEntity userEntity);

    @Delete
    void deleteUser(UserEntity userEntity);

    @Query("DELETE FROM user_table")
    void deleteAllUsers();
}
