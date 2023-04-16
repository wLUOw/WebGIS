package com.njnu.picshare.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Authod oruizn
 * @date 2021年11月2021/11/29 0029日下午 22:33
 */
@Data
public class PictureInfo  implements Serializable {
    private Integer id;
    private String picName;//图片名称
    private Double longitude;//图片经度
    private Double latitude;//图片纬度
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;//图片上传时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date shootTime;//图片拍摄时间
    private String picAttachment;//图片附言
    private String picLocation;//图片位置

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Date getShootTime() {
        return shootTime;
    }

    public void setShootTime(Date shootTime) {
        this.shootTime = shootTime;
    }

    public String getPicAttachment() {
        return picAttachment;
    }

    public void setPicAttachment(String picAttachment) {
        this.picAttachment = picAttachment;
    }

    public String getPicLocation() {
        return picLocation;
    }

    public void setPicLocation(String picLocation) {
        this.picLocation = picLocation;
    }
}
