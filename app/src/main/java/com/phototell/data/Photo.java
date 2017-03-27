package com.phototell.data;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.phototell.util.PhotoUtility;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Photo information object saved to the database.
 * based on code example.
 */
@DatabaseTable
public class Photo implements Serializable {

    private static final long serialVersionUID = -6582623980712135028L;

    public static final String DATE_FIELD_NAME = "creationDate";

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(columnName = DATE_FIELD_NAME, dataType = DataType.DATE_STRING,
            format = "mm-dd-yyyy HH:mm:ss")
    private Date creationDate;

    @DatabaseField
    private String name;

    @DatabaseField
    private String description;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] imageBytes;

    private Bitmap imageBitMap;

    public Bitmap getImageBitMap() {
        if(imageBitMap == null){
            imageBitMap = PhotoUtility.getImage(imageBytes);
        }
        return imageBitMap;
    }

    public void setImageBitMap(Bitmap imageBitMap) {
        this.imageBitMap = imageBitMap;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " " + description;
    }
}
