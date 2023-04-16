package com.njnu.picshare.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.njnu.picshare.domain.BoxAreaInfo;
import com.njnu.picshare.domain.FileResult;
import com.njnu.picshare.domain.PictureInfo;
import com.njnu.picshare.mapper.PictureInfoMapper;
import com.njnu.picshare.service.PictureInfoService;
import com.njnu.picshare.util.GeoProcessUtil;
import com.njnu.picshare.util.PictureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Authod oruizn
 * @date 2021年11月2021/11/30 0030日下午 20:38
 */
@Service
public class PictureInfoServiceImpl  implements PictureInfoService {
    @Autowired
    private PictureInfoMapper pictureInfoMapper;
    // 以下为图片存储路径
    @Value("${file.pictureFileStore}")
    private String pictureFilePath;
    @Value("${file.compressPictureFileStore}")
    private String compressPictureFilePath;
    @Value("${file.thumbnailPictureFileStore}")
    private String thumbnailPictureFilePath;
    @Override
    public FileResult uploadPictureFile(MultipartFile pictureFile, String location, String attachment) {
        //-----------编码练习部分·开始---------------
        //获取文件在服务器的储存位置
        File filePath = new File(pictureFilePath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdirs();
        }
        //获取压缩文件存储位置
        File compressFilePath = new File(compressPictureFilePath);
        if (!compressFilePath.exists() && !compressFilePath.isDirectory()) {
            compressFilePath.mkdirs();
        }
        //获取原始文件名称(包含格式)
        String originalFileName = pictureFile.getOriginalFilename();
        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        //重新命名图片，避免重复
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + type;
        //在指定路径下创建一个文件
        File targetFile = new File(pictureFilePath, fileName);
        //将文件保存到服务器指定位置
        try {
            //将原图保存到指定文件中
            pictureFile.transferTo(targetFile);
            // 压缩大小是否超过1.0M的图片
            float fileSize = targetFile.length()/1024.0f/1024.0f;
            if (fileSize > 1.0f){
                //对超过1.0M的图片计算压缩因子
                float fileCompressScale = PictureUtil.calculateCompresScale(fileSize);
                fileName = targetFile.getName();
                //根据压缩因子对图片进行压缩，放置于压缩图片文件夹中
                PictureUtil.compressPictureByScale(pictureFilePath+fileName, fileCompressScale,0.8f, compressPictureFilePath+fileName);
            }else {
                //不超过1M，复制放置于压缩文件夹中备份
                PictureUtil.copyPictureFile(pictureFilePath+fileName, compressPictureFilePath+fileName);
            }
            // 构建图片信息实例
            PictureInfo pictureInfo = new PictureInfo();
            pictureInfo.setPicName(fileName);//文件名
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            Metadata metadata = ImageMetadataReader.readMetadata(targetFile);//读取图片元信息，以提取拍摄时间、拍照时的经纬度
            Iterator<Directory> it = metadata.getDirectories().iterator();
            while (it.hasNext()) {
                Directory exif = it.next();
                Iterator<Tag> tags = exif.getTags().iterator();
                while (tags.hasNext()) {
                    Tag tag = (Tag) tags.next();
                    switch (tag.getTagName()){
                        case "Date/Time":
                            Date date = simpleDateFormat.parse(tag.getDescription());
                            pictureInfo.setShootTime(date);
                            break;
                        case "Date/Time Original":
                            if (pictureInfo.getShootTime() == null){
                                pictureInfo.setShootTime(simpleDateFormat.parse(tag.getDescription()));
                            }
                            break;
                        case "GPS Latitude":
                            pictureInfo.setLatitude(GeoProcessUtil.tranformDMSToDegree(tag.getDescription()));
                            break;
                        case "GPS Longitude":
                            pictureInfo.setLongitude(GeoProcessUtil.tranformDMSToDegree(tag.getDescription()));
                            break;
                    }
                }
            }
            //若图片在上传之间已经经过压缩，则图片元信息中无经纬度，此时以当前上传图片的位置作为补充
            if (pictureInfo.getLongitude() == null || pictureInfo.getLatitude() == null && !"".equals(location)){
                String[] locations = location.split(",");
                pictureInfo.setLongitude(Double.parseDouble(locations[0]));
                pictureInfo.setLatitude(Double.parseDouble(locations[1]));
            }
            pictureInfo.setPicLocation(GeoProcessUtil.GeometryToString(pictureInfo.getLongitude(), pictureInfo.getLatitude()));
            pictureInfo.setUploadTime(new Date());//上传图片的时间
            pictureInfo.setPicAttachment(attachment);//图片附言
            // 将文件信息插入数据库
            pictureInfoMapper.insertPictureInfo(pictureInfo);
            //将文件在服务器的存储路径返回
            return new FileResult(true, "上传成功", pictureInfo.getPicName());
        } catch (IOException e) {
            //捕获处理相关的错误
            e.printStackTrace();
            return new FileResult(false, "上传失败","");
        }catch (ImageProcessingException e){
            e.printStackTrace();
            return new FileResult(false, "图片信息提取失败","");
        }catch (ParseException e){
            e.printStackTrace();
            return new FileResult(false, "图片日期信息转换失败","");
        }catch (NullPointerException e){
            e.printStackTrace();
            return new FileResult(false, "出现空指针","");
        }
        //-----------编码练习部分·结束---------------
    }

    @Override
    public List<BoxAreaInfo> selectBoxAreaPictureInfoByView(String boxArea) {
        //-----------编码练习部分·开始---------------
        String[] boxAreaLoc = boxArea.split(",");
        double rightUpLongitude = Double.parseDouble(boxAreaLoc[0]);//右上角经度
        double rightUpLatitude = Double.parseDouble(boxAreaLoc[1]);//右上角纬度
        double leftDownLongitude = Double.parseDouble(boxAreaLoc[2]);//左下角经度
        double leftDownLatitude = Double.parseDouble(boxAreaLoc[3]);//左下角纬度​
        double leftUpLatitude = rightUpLatitude;
        double rightDownLongitude = rightUpLongitude;
        //查询要求是四个角的坐标都要有，默认纵轴分4个，横轴分3个，即将手机屏幕分割为12个小区域逐一查询
        int rowCount = 4;
        int columnCount = 3;
        double intervalLongitude = (rightDownLongitude - leftDownLongitude) / columnCount;//每格的经度间隔
        double intervalLatitude = (leftUpLatitude - leftDownLatitude) / rowCount;//每格的纬度间隔
        List<BoxAreaInfo> boxAreaInfoList = new ArrayList<>();
        for (int row = 0; row < rowCount; row++){
            for (int column = 0; column < columnCount; column++){
                String leftDown = String.valueOf(leftDownLongitude + column * intervalLongitude) + " " + String.valueOf(leftDownLatitude + row * intervalLatitude);
                String leftUp = String.valueOf(leftDownLongitude + column * intervalLongitude) + " " + String.valueOf(leftDownLatitude + (row+1) * intervalLatitude);
                String rightUp = String.valueOf(leftDownLongitude + (column+1) * intervalLongitude) + " " + String.valueOf(leftDownLatitude + (row+1) * intervalLatitude);
                String rightDown = String.valueOf(leftDownLongitude + (column+1) * intervalLongitude) + " " + String.valueOf(leftDownLatitude + row * intervalLatitude);
                String geometryBox = "(" + String.join(",", leftDown, leftUp, rightUp, rightDown, leftDown) + ")";//拼接形式符合PostGIS表达的空间区域
                List<PictureInfo> pictureInfoList = pictureInfoMapper.findPictureListByBox(geometryBox);
                if (pictureInfoList != null && pictureInfoList.size() > 0){
                    BoxAreaInfo boxAreaInfo = new BoxAreaInfo();
                    boxAreaInfo.setBoxExtent(geometryBox);
                    boxAreaInfo.setPictureCount(pictureInfoList.size());
                    boxAreaInfo.setLongitude(pictureInfoList.get(0).getLongitude());
                    boxAreaInfo.setLatitude(pictureInfoList.get(0).getLatitude());
                    String firstPictureName = pictureInfoList.get(0).getPicName();//返回缩略图URL
                    boxAreaInfo.setFirstPictureName(firstPictureName);
                    boxAreaInfoList.add(boxAreaInfo);
                }
            }
        }
        return boxAreaInfoList;
        //-----------编码练习部分·结束---------------
    }

    @Override
    public String selectPictureInfoById(Integer id, boolean needCompress) {
        // 根据图片id查找图片信息
        PictureInfo pictureInfo = pictureInfoMapper.findPictureById(id);
        // 返回查看图片的URL
        return getPictureInfoURL(pictureInfo, needCompress);
    }

    @Override
    public String selectPictureInfoByName(String name, boolean needCompress) {
        // 根据图片名称查找图片信息
        PictureInfo pictureInfo = pictureInfoMapper.findPictureByName(name);
        // 返回查看图片的URL
        return getPictureInfoURL(pictureInfo, needCompress);
    }

    @Override
    public List<String> selectPictureListByBox(String boxArea) {
        // 查询指定小区域中的图片信息
        List<PictureInfo> pictureInfoList = pictureInfoMapper.findPictureListByBox(boxArea);
        List<String> pictureUrlList = new ArrayList<>();
        if (pictureInfoList != null){
            for (int i = 0; i < pictureInfoList.size(); i++){
                String pictureUrl = pictureInfoList.get(i).getPicName();//仅返回缩略图名字
                pictureUrlList.add(pictureUrl);
            }
        }
        return pictureUrlList;
    }

    @Override
    public FileResult deletePictureFileByName(String pictureName) {
        pictureInfoMapper.deletePictureFileByName(pictureName);
        String deleteCompressPath = compressPictureFilePath + pictureName;
        String deleteOriginPath = pictureFilePath + pictureName;
        try {
            File compressFile = new File(deleteCompressPath);//删除压缩图
            if (compressFile.exists())//图片是否存在
                compressFile.delete();

            File originFile = new File(deleteOriginPath);//删除原图
            if (originFile.exists())
                originFile.delete();
            return new FileResult(true, "删除成功");
        }catch (Exception e){
            return new FileResult(false, "删除失败");
        }
    }

    @Override
    public String selectPictureAttachmentByName(String pictureName) {
        return pictureInfoMapper.findPictureAttachmet(pictureName);
    }

    @Override
    public FileInputStream getThumbnailPicture(String pictureName){
        //获取缩略文件存储位置
        File thumbnailFilePath = new File(thumbnailPictureFilePath);
        if (!thumbnailFilePath.exists() && !thumbnailFilePath.isDirectory()) {
            thumbnailFilePath.mkdirs();
        }
        FileInputStream inputStream = null;
        try {
            File file = new File(thumbnailPictureFilePath + pictureName);
            if (!file.exists()){
                // 将压缩图全部转换为指定大小的缩略图
                PictureUtil.compressPictureBySizeAndScale(compressPictureFilePath + pictureName, 200, 200, thumbnailPictureFilePath + pictureName);
            }
            inputStream = new FileInputStream(thumbnailPictureFilePath + pictureName);
        }catch (IOException e){
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 返回查看图片的URL
     * @param pictureInfo
     * @param needCompress
     * @return
     */
    public String getPictureInfoURL(PictureInfo pictureInfo, boolean needCompress){
        if (pictureInfo != null){
            if (needCompress){
                return compressPictureFilePath + pictureInfo.getPicName();//返回缩略图
            } else {
                return pictureFilePath + pictureInfo.getPicName();//返回原图
            }
        }else {
            return "";
        }
    }
}
