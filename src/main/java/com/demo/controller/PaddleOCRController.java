package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.dao.ChemMapper;
import com.demo.entity.Chem;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/paddleocr")
@MapperScan("com.demo.dao")
public class PaddleOCRController {
    @Autowired
    private ChemMapper chemMapper;

    @GetMapping("/")
    public String uploladPage(){
        return "upload";
    }
    @PostMapping("/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest req){
        try {
            //接收上传文件
            String fileName = System.currentTimeMillis()+file.getOriginalFilename();
            String destFileName=req.getServletContext().getRealPath("")+"uploaded"+ File.separator+fileName;
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            System.out.println(destFile);
            file.transferTo(destFile);
            //开始准备请求API
            //创建请求头
            HttpHeaders headers = new HttpHeaders();
            //设置请求头格式
            headers.setContentType(MediaType.APPLICATION_JSON);
            //构建请求参数
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            //读入静态资源文件
            InputStream imagePath = new FileInputStream(destFile);
            //添加请求参数images，并将Base64编码的图片传入
            map.add("images", ImageToBase64(imagePath));
            //构建请求
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            RestTemplate restTemplate = new RestTemplate();
            //发送请求

            Map json = restTemplate.postForEntity("http://127.0.0.1:8868/predict/ocr_system", request, Map.class).getBody();
            System.out.println(json);
            //解析Json返回值
            List<List<Map>> json1 = (List<List<Map>>) json.get("results");
            //循环遍历出所有内容
            /*for (int i = 0; i < json1.get(0).size(); i++) {
                System.out.println(json1.get(0).get(i).get("text"));
                System.out.println("置信度：" + json1.get(0).get(i).get("confidence"));
                List<List<Integer>> json2 = (List<List<Integer>>) json1.get(0).get(i).get("text_region");
                System.out.println("文字的坐标" + json2);
            }*/

            for (int i = 2; (i*5-1) < json1.get(0).size(); i++){
                Chem chem = new Chem();
                int j = (i-1)*5;

                chem.setCode(json1.get(0).get(j).get("text").toString());
                System.out.print(json1.get(0).get((j)).get("text")+" ");

                chem.setItem(json1.get(0).get(j+1).get("text").toString());
                System.out.print(json1.get(0).get(j+1).get("text")+" ");

                chem.setResult(json1.get(0).get(j+2).get("text").toString());
                System.out.print(json1.get(0).get(j+2).get("text")+" ");


                chem.setRefer(json1.get(0).get(j+3).get("text").toString());
                System.out.print(json1.get(0).get(j+3).get("text")+" ");

                chem.setUnit(json1.get(0).get(j+4).get("text").toString());
                System.out.println(json1.get(0).get(j+4).get("text")+" ");

                int result = chemMapper.insert(chem);
                //System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败," + e.getMessage();
        }
        return "OK";
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
