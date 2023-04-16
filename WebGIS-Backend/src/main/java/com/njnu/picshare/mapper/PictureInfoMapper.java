package com.njnu.picshare.mapper;

import com.njnu.picshare.domain.PictureInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureInfoMapper {
    // 往数据库中插入图片信息
    Integer insertPictureInfo(PictureInfo pictureInfo);
    // 通过id查找图片具体信息
    PictureInfo findPictureById(Integer id);
    // 通过图片名称查找图片具体信息
    PictureInfo findPictureByName(String pictureName);
    // 通过区域边界查找区域中的图片信息列表
    List<PictureInfo> findPictureListByBox(String geometryBox);
    // 通过图片名称删除图片信息
    Integer deletePictureFileByName(String pictureName);
    // 通过图片名称查找图片附言
    String findPictureAttachmet(String pictureName);
}
