package com.demo.controller;

import com.demo.dao.ChemMapper;
import com.demo.entity.Chem;
import com.demo.utils.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Wrapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import static com.demo.controller.PaddleOCRController.ImageToBase64;


@RestController
@RequestMapping("/tableocr")
@MapperScan("com.demo.dao")
public class TableController {
    @Autowired
    private ChemMapper chemMapper;
    @PostMapping("/upload")
    public String table(@RequestParam("file") MultipartFile file, HttpServletRequest req) {

        try {
            //接收上传文件
            String fileName = System.currentTimeMillis()+file.getOriginalFilename();
            String destFileName=req.getServletContext().getRealPath("")+"uploaded"+ File.separator+fileName;
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            System.out.println(destFile);
            file.transferTo(destFile);

            InputStream imagePath = new FileInputStream(destFile);
            String imgParam = URLEncoder.encode(ImageToBase64(imagePath), "UTF-8");

            // 请求url
            String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/table";
            String accessToken = "24.743ab4369d8fb69528134de22b339496.2592000.1692854960.282335-24683982";
            String param = "image=" + imgParam;

            String result = HttpUtil.post(url, accessToken, param);

            Map json = (Map) JSON.parse(result);

            //解析Json返回值
            List<Map> json0 = (List<Map>) json.get("tables_result");
            List<Map>json1 = (List<Map>) json0.get(0).get("body");

            int allRow = Integer.parseInt(json1.get(json1.size()-1).get("row_end").toString())-1;
            Chem[] chemList = new Chem[allRow];

            for (int i=0;i<allRow;i++){
                chemList[i]=new Chem();
            }
            for (int i=5; i<json1.size(); i++){
                String col = json1.get(i).get("col_end").toString();
                int row = Integer.parseInt(json1.get(i).get("row_start").toString())-1;
                switch(col){
                    case "1":
                        chemList[row].setCode(json1.get(i).get("words").toString());
                        System.out.print(json1.get((i)).get("words")+" ");
                        break;
                    case "2":
                        chemList[row].setItem(json1.get(i).get("words").toString());
                        System.out.print(json1.get(i).get("words")+" ");
                        break;
                    case "3":
                        chemList[row].setResult(json1.get(i).get("words").toString());
                        System.out.print(json1.get(i).get("words")+" ");
                        break;
                    case "4":
                        chemList[row].setRefer(json1.get(i).get("words").toString());
                        System.out.print(json1.get(i).get("words")+" ");
                        break;
                    case "5":
                        chemList[row].setUnit(json1.get(i).get("words").toString());
                        System.out.println(json1.get(i).get("words")+" ");
                        break;
                    default:
                        return null;
                }
            }


            for (int i=0;i<allRow;i++){
                int sql = chemMapper.insert(chemList[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Done";
    }

}