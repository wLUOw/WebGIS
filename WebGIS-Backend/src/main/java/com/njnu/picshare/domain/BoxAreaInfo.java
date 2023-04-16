package com.njnu.picshare.domain;

import lombok.Data;

/**
 * @Authod oruizn
 * @date 2021年11月2021/11/30 0030日下午 21:52
 */
@Data
public class BoxAreaInfo {
    private Double longitude;//中心经度
    private Double latitude;//中心纬度
    private Integer pictureCount;//该区域的照片数量
    private String firstPictureName;//该区域第一张照片的名字
    private String boxExtent;//区域范围t

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

    public Integer getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(Integer pictureCount) {
        this.pictureCount = pictureCount;
    }

    public String getFirstPictureName() {
        return firstPictureName;
    }

    public void setFirstPictureName(String firstPictureName) {
        this.firstPictureName = firstPictureName;
    }

    public String getBoxExtent() {
        return boxExtent;
    }

    public void setBoxExtent(String boxExtent) {
        this.boxExtent = boxExtent;
    }
}
