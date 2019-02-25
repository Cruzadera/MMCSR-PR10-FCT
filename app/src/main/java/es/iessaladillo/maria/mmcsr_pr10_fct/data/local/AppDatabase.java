package es.iessaladillo.maria.mmcsr_pr10_fct.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

@Database(entities = {Company.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "fct.db";

    public abstract CompanyDao companyDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance =
                        Room.databaseBuilder(
                            context.getApplicationContext(), AppDatabase.class,
                            DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

}