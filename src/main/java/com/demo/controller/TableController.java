package com.demo.controller;

import com.demo.dao.ChemMapper;
import com.demo.entity.Chem;
import com.demo.utils.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/tableocr")
@MapperScan("com.demo.dao")
public class TableController {
    @Autowired
    private ChemMapper chemMapper;

    public static String table() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/table";
        try {
            // 本地文件路径
            String filePath = "[本地文件路径]";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "25.340e56fb0aa61d3a8b85a68156773b1e.315360000.2005610845.282335-24683982";

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String ImageToBase64(InputStream imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        //Read the image byte array
        try {
            InputStream in = imgPath;
            System.out.println(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        //Base64 encoding of byte array
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        //Returns a Base64 encoded byte array string
        //System.out.println("图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));
        return encoder.encode(Objects.requireNonNull(data));
    }

}