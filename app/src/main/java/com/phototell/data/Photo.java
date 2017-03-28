package com.phototell.data;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.phototell.util.PhotoUtility;
import java.io.Serializable;
import java.util.Date;

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

    @DatabaseField(columnName = DATE_FIELD_NAME)
    private Date creationDate;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] thumbnailBytes;

    @DatabaseField
    private String description;

    @DatabaseField
    private String path;

    private Bitmap imageBitMap;

    private Bitmap thumbnailBBitMap;

    public Bitmap getThumbnailBBitMap() {
        if(thumbnailBBitMap ==null){
            thumbnailBBitMap = PhotoUtility.getImage(thumbnailBytes);
        }
        return thumbnailBBitMap;
    }

    public void setThumbnailBBitMap(Bitmap thumbnailBBitMap) {
        this.thumbnailBBitMap = thumbnailBBitMap;
    }

    public void setThumbnailBytes(byte[] thumbnailBytes) {
        this.thumbnailBytes = thumbnailBytes;
    }

    public byte[] getThumbnailBytes() {
        return thumbnailBytes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getImageBitMap() {
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

    @Override
    public String toString() {
        return creationDate + " " + description;
    }
}
