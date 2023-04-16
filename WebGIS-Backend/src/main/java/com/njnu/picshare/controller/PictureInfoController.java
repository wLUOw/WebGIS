package com.njnu.picshare.controller;

import com.njnu.picshare.domain.BoxAreaInfo;
import com.njnu.picshare.domain.FileResult;
import com.njnu.picshare.domain.JsonResult;
import com.njnu.picshare.service.PictureInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Authod oruizn
 * @date 2021年11月2021/11/29 0029日下午 22:18
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/PictureInfo")
public class PictureInfoController extends BaseController {

    @Autowired
    private PictureInfoService pictureInfoService;

    /**
     * 接口测试
     * @return
     */
    @GetMapping("/serviceTest")
    public JsonResult serviceTest(){
        json.setData("后台服务已开启！");
        json.setMsg("这是一个测试接口！");
        json.setUrl("http://localhost:8888/PictureInfo/serviceTest");
        return json;
    }

    /**
     * 上传照片
     * @param file 用户上传的照片
     * @param location 用户当前所处的位置
     * @param attachment 照片的附言
     * @return
     */
    @PostMapping("/uploadPicture")
    public JsonResult uploadPictureFile(@RequestParam("file") MultipartFile file, @RequestParam("location") String location, @RequestParam(value = "attachment",defaultValue="") String attachment){
        FileResult message = pictureInfoService.uploadPictureFile(file, location, attachment);
        json.setData(message);
        return json;
    }

    /**
     *  通过前端的屏幕视角区域切割为多个小区域，并返回每个小区域里面的照片数量
     * @param viewBoundary 屏幕视角区域
     * @return [{中心经纬度、边界经纬度、区域内照片数量、第一张照片的URL}]
     */
    @GetMapping("/picturesInAreaByBox")
    public JsonResult getPicturesInAreaBoxByView(@RequestParam("viewBoundary")String viewBoundary){
        List<BoxAreaInfo> boxAreaInfoList = pictureInfoService.selectBoxAreaPictureInfoByView(viewBoundary);
        json.setData(boxAreaInfoList);
        return json;
    }
    /**
     * 通过边界经纬度查询该区域的所有照片，返回相关照片的URL
     * [照片1url，照片2url...]
     * @return
     */
    @GetMapping("/picturesByBox")
    public JsonResult getPictureListByBox(@RequestParam("boxExtent")String boxExtent){
        List<String> pictureNameList = pictureInfoService.selectPictureListByBox(boxExtent);
        json.setData(pictureNameList);
        return json;
    }

    /**
     * 查看照片
     * @param pictureName
     * @param needCompress
     * @throws FileNotFoundException
     * @throws IOException
     */
    @GetMapping("/viewPicture")
    public void viewPictureFile(@RequestParam("pictureName") String pictureName, @RequestParam("needCompress") Integer needCompress) throws FileNotFoundException, IOException{
        //-----------编码练习部分·开始---------------
//1、根据照片名称找到文件路径；
        String realPath = "";
        if (needCompress == 1){
            realPath = pictureInfoService.selectPictureInfoByName(pictureName, true);//查看缩略图
        }else {
            realPath = pictureInfoService.selectPictureInfoByName(pictureName, false);//查看原图
        }
        if (realPath == null) return;
        //2、根据文件设置正确的图片类型
        String fileType = realPath.substring(realPath.lastIndexOf(".") + 1);
        FileInputStream inputStream = new FileInputStream(realPath);
        int i = inputStream.available();
        //byte数组用于存放图片字节数据
        byte[] buff = new byte[i];
        inputStream.read(buff);
        //记得关闭输入流
        inputStream.close();
        //设置发送到客户端的响应内容类型
        response.setContentType("image/" + fileType);
        OutputStream out = response.getOutputStream();
        out.write(buff);
        //关闭响应输出流
        out.close();
        //-----------编码练习部分·结束---------------
    }

    /**
     * 查看缩略图
     * @return
     */
    @GetMapping("/viewThumbnailPicture")
    public void viewThumbnailPicture(@RequestParam("pictureName") String pictureName) throws IOException{
        //-----------编码练习部分·开始---------------
        String fileType = pictureName.substring(pictureName.lastIndexOf(".") + 1);
        FileInputStream inputStream = pictureInfoService.getThumbnailPicture(pictureName);
        int i = inputStream.available();
        //byte数组用于存放图片字节数据
        byte[] buff = new byte[i];
        inputStream.read(buff);
        //记得关闭输入流
        inputStream.close();
        //设置发送到客户端的响应内容类型
        response.setContentType("image/" + fileType);
        OutputStream out = response.getOutputStream();
        out.write(buff);
        //关闭响应输出流
        out.close();
        //-----------编码练习部分·结束---------------
    }

    /**
     * 查看照片附言
     * @return
     */
    @GetMapping("/viewAttachment")
    public JsonResult viewPictureAttachment(@RequestParam("pictureName") String pictureName){
        //-----------编码练习部分·开始---------------
        String attachment = pictureInfoService.selectPictureAttachmentByName(pictureName);
        if (attachment == null) attachment = "";
        json.setData(attachment);
        return json;
        //-----------编码练习部分·结束---------------
    }

    @PostMapping("/deletePicture")
    public JsonResult deletePictureFile(@RequestParam("pictureName") String pictureName){
        FileResult message = pictureInfoService.deletePictureFileByName(pictureName);
        json.setData(message);
        json.setMsg(message.getMessage());
        return json;
    }

}
