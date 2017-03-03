package a00971903.comp3717.ca.bcit.ca.explorenewwest.database.schema;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "LOCATIONS".
 */
@Entity
public class Locations {

    @Id
    private Long id;

    @NotNull
    private String name;
    private long latitude;
    private long longitude;
    private long categoryID;

    @Generated
    public Locations() {
    }

    public Locations(Long id) {
        this.id = id;
    }

    @Generated
    public Locations(Long id, String name, long latitude, long longitude, long categoryID) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categoryID = categoryID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

}