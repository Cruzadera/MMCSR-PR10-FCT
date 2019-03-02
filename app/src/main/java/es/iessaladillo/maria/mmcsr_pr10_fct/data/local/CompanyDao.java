package es.iessaladillo.maria.mmcsr_pr10_fct.data.local;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.BaseDao;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

@Dao
public interface CompanyDao extends BaseDao<Company> {

    @Query("SELECT * FROM company")
    LiveData<List<Company>> queryAllCompany();

    @Query("SELECT * FROM company WHERE idCompany = :companyId")
    LiveData<Company> queryCompany(long companyId);

    @Query("SELECT name FROM company")
    LiveData<List<String>> queryAllCompanyNames();
}
