package es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "company",
        indices = {@Index(value = {"name"},
                unique = true)})
public class Company {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="idCompany")
    private long idCompany;
    @ColumnInfo(name="name")
    private String name;
    @ColumnInfo(name="CIF")
    private String CIF;
    @ColumnInfo(name="address")
    private String address;
    @ColumnInfo(name="phone")
    private String phone;
    @ColumnInfo(name="email")
    private String email;
    @ColumnInfo(name="urlLogo")
    private String urlLogo;
    @ColumnInfo(name="nameContactPerson")
    private String nameContactPerson;

    public Company(long idCompany, String name, String CIF, String address, String phone, String email, String urlLogo, String nameContactPerson) {
        this.idCompany = idCompany;
        this.name = name;
        this.CIF = CIF;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.urlLogo = urlLogo;
        this.nameContactPerson = nameContactPerson;
    }
    @Ignore
    public Company() {

    }
    @Ignore
    public Company(String name, String CIF, String address, String phone, String email, String urlLogo, String nameContactPerson) {
        this.name = name;
        this.CIF = CIF;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.urlLogo = urlLogo;
        this.nameContactPerson = nameContactPerson;
    }

    public long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(long idCompany) {
        this.idCompany = idCompany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) {
        this.CIF = CIF;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getNameContactPerson() {
        return nameContactPerson;
    }

    public void setNameContactPerson(String nameContactPerson) {
        this.nameContactPerson = nameContactPerson;
    }
}
