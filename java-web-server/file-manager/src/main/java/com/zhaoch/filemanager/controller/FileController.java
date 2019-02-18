package com.zhaoch.filemanager.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @GetMapping("/list")
    public Mono<List> list() {
        String path = this.getClass().getResource("/").getPath().replace("target/classes/", "fileDir");
        File file = new File(path);

        File[] fileList = file.listFiles();

        List<String> fileListStr = new ArrayList<>();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String fileName = fileList[i].getName();
                System.out.println("文件：" + fileName);
                fileListStr.add(fileName);
            }

            if (fileList[i].isDirectory()) {
                String fileName = fileList[i].getName();
                System.out.println("目录：" + fileName);
                fileListStr.add(fileName);
            }
        }

        return Mono.justOrEmpty(fileListStr);
    }

    @GetMapping("/download")
    public Mono<Void> download(ServerHttpResponse response, String fileName) throws UnsupportedEncodingException {
        String path = this.getClass().getResource("/").getPath().replace("target/classes/", "fileDir") + File.separator + fileName;
        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"));
        response.getHeaders().setContentType(MediaType.MULTIPART_FORM_DATA);

        File file = new File(path);
        return zeroCopyResponse.writeWith(file, 0, file.length());

    }
}
