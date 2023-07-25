package com.demo.controller;

import com.demo.dao.ChemMapper;
import com.demo.entity.Chem;
import com.demo.utils.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

            JSONArray jsonObject = JSONArray.fromObject(result);

            //解析Json返回值
            List<List<Map>> json1 = (List<List<Map>>) json.get("tables_result");

            for (int i=1; i<json1.size(); i++){
                String col = json1.get(1).get(i).get("col_end").toString();
                Chem chem = new Chem();
                switch(col){
                    case "1":
                        chem.setCode(json1.get(1).get(i).get("words").toString());
                        System.out.print(json1.get(1).get((i)).get("words")+" ");
                        break;
                    case "2":
                        chem.setItem(json1.get(1).get(i).get("words").toString());
                        System.out.print(json1.get(1).get(i).get("words")+" ");
                        break;
                    case "3":
                        chem.setResult(json1.get(1).get(i).get("words").toString());
                        System.out.print(json1.get(1).get(i).get("words")+" ");
                        break;
                    case "4":
                        chem.setRefer(json1.get(1).get(i).get("words").toString());
                        System.out.print(json1.get(1).get(i).get("words")+" ");
                        break;
                    case "5":
                        chem.setUnit(json1.get(1).get(i).get("words").toString());
                        System.out.println(json1.get(1).get(i).get("words")+" ");
                        break;
                    default:
                        return null;
                }
                int sql = chemMapper.insert(chem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}