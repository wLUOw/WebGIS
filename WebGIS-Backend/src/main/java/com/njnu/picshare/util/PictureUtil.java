package com.njnu.picshare.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Authod oruizn
 * @date 2021年11月2021/11/29 0029日下午 22:43
 * 用于图片处理的工具类，如图片压缩、图片复制
 */
public class PictureUtil {

    /**
     * fileSize 图片文件的存储空间大小
     * @param fileSize
     * @return 图片压缩比例因子
     */
    public static float calculateCompresScale(float fileSize){
        float fileCompressScale = 1.0f;
        if (fileSize < 1.5f) fileCompressScale = 0.6f;
        else if (fileSize < 2.0f) fileCompressScale = 0.5f;
        else if (fileSize < 3.0f) fileCompressScale = 0.4f;
        else if (fileSize < 4.0f) fileCompressScale = 0.3f;
        else fileCompressScale = 0.2f;
        return fileCompressScale;
    }

    /**
     * 按照比例进行缩放
     * scale 图片的压缩比例 值在0-1之间，1f就是原图，0.5就是原图的一半长宽大小
     * outputQuality 图片压缩的质量 值在0-1 之间，越接近1质量越好，越接近0 质量越差
     * @throws IOException
     */
    public static void compressPictureByScale(String inputPath, float scale, float outputQuality, String ouputPath) throws IOException{
        Thumbnails.of(inputPath).scale(scale).outputQuality(outputQuality).toFile(ouputPath);
    }

    /*
     * size(width,height) 若图片横比width小，高比height小，不变
     * 若图片横比width小，高比height大，高缩小到height，图片比例不变 若图片横比width大，高比height小，横缩小到width，图片比例不变
     * 若图片横比width大，高比height大，图片按比例缩小，横为width或高为height
     */
    public static void compressPictureBySizeAndScale(String inputPath, int width, int height, String outputPath) throws IOException{
        Thumbnails.of(inputPath).size(width, height).toFile(outputPath);
    }

    /**
     * 不按照比例，按照图片指定大小进行缩放
     * @param inputPath
     * @param width
     * @param height
     * @param outputPath
     * @throws IOException
     */
    public static void compressPictureBySize(String inputPath, int width, int height, String outputPath) throws IOException{
        Thumbnails.of(inputPath).size(width, height).keepAspectRatio(false).toFile(outputPath);
    }

    public static void copyPictureFile(String srcPath, String destPath) throws IOException{
        // 打开输入流
        FileInputStream fis = new FileInputStream(srcPath);
        // 打开输出流
        FileOutputStream fos = new FileOutputStream(destPath);
        // 读取和写入信息
        int len = 0;
        // 创建一个字节数组，当做缓冲区
        byte[] b = new byte[1024];
        while ((len = fis.read(b)) != -1) {
            fos.write(b, 0, len);
        }
        // 关闭流  先开后关  后开先关
        fos.close(); // 后开先关
        fis.close(); // 先开后关
    }
}
