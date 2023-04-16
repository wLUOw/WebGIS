package com.njnu.picshare.service;

import com.njnu.picshare.domain.BoxAreaInfo;
import com.njnu.picshare.domain.FileResult;
import com.njnu.picshare.domain.PictureInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.List;

public interface PictureInfoService {
    // 上传图片文件处理
    FileResult uploadPictureFile(MultipartFile file, String location, String attachment);
    // 通过id选择获取图片信息
    String selectPictureInfoById(Integer id, boolean needCompress);
    // 通过图片名称选择获取图片信息
    String selectPictureInfoByName(String name, boolean needCompress);
    // 通过区域边界选择获取图片名称列表
    List<String> selectPictureListByBox(String boxArea);
    // 通过手机屏幕当前视角范围选择获取每个小区域内的图片信息
    List<BoxAreaInfo> selectBoxAreaPictureInfoByView(String boxArea);
    // 通过图片名称删除图片信息
    FileResult deletePictureFileByName(String pictureName);
    // 通过图片名称选择获取图片附言
    String selectPictureAttachmentByName(String pictureName);
    // 通过图片名称获取图片的缩略图
    FileInputStream getThumbnailPicture(String pictureName);
}
