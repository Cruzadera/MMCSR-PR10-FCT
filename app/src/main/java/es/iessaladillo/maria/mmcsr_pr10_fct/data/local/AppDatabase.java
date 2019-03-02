package es.iessaladillo.maria.mmcsr_pr10_fct.data.local;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Student;

@Database(entities = {Company.class, Student.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "fct.db";

    public abstract CompanyDao companyDao();

    public abstract StudentDao studentDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            insertInitialData();
                        }
                    }).build();
                }
            }
        }
        return instance;
    }

    private static void insertInitialData() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> instance.studentDao()
                .insert(new Student(1, "Baldo", "66666666", "baldomer@jdjd.es", "2º DAM",
                        new Company(1, "company name", "63737373A", "Calle Segovia", "777777777", "company@name.es", "url.es", "pepe"),
                        "Juan", "837833333", "12:00-23:00")));
    }
}