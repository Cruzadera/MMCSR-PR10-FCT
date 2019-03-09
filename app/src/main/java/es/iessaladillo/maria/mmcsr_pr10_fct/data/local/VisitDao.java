package es.iessaladillo.maria.mmcsr_pr10_fct.data.local;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.BaseDao;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Visit;

@Dao
public interface VisitDao extends BaseDao<Visit> {
    @Query("SELECT * FROM visit")
    LiveData<List<Visit>> queryVisits();

    @Query("SELECT * FROM visit WHERE idVisit = :visitId")
    LiveData<Visit> queryVisit(long visitId);

    @Query("SELECT * FROM visit WHERE nameStu = :nameStudent")
    LiveData<Visit> queryVisitByIdStudent(String nameStudent);
}
