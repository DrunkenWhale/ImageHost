package com.example.imagehost.controller;

import com.example.imagehost.model.Image;
import com.example.imagehost.model.User;
import com.example.imagehost.repository.ImageRepository;
import com.example.imagehost.repository.UserRepository;
import com.example.imagehost.util.EncodeHex;
import com.example.imagehost.util.ImageHostUrl;
import com.example.imagehost.util.response.DataResponse;

import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

@RequestMapping("/image")
@RestController
public class ImageController {

    final UserRepository userRepository;

    final ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @PostMapping("/upload")
    @ResponseBody
    public DataResponse<String> imageUpload(
            @RequestAttribute("mailbox") String mailbox,
            @RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return new DataResponse<String>(0, "EmptyFile", null);
        }
        String imageName = image.getOriginalFilename();  // 图片名称 包括后缀
        if (imageName == null || !imageName.contains(".")) {
            return new DataResponse<String>(0, "EmptyFile", null);
        }
        String[] imageNameSplitWithPoint = imageName.split("\\.");
        String imagePostfix = imageNameSplitWithPoint[imageNameSplitWithPoint.length - 1];
        if (
                        imagePostfix.equals("png") ||
                        imagePostfix.contains("jpg") ||
                        imagePostfix.contains("webp") ||
                        imagePostfix.contains("gif") ||
                        imagePostfix.contains("bmp")) {
            // 是图片文件 (后缀合理)
            long imageSize = image.getSize();
            String path = System.getProperty("user.dir") + File.separatorChar + "image";
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");  // 这里是用了单例吧?
            messageDigest.update(imageName.getBytes(StandardCharsets.UTF_8));
            String encodeImageName = EncodeHex.encode(messageDigest.digest());
            String imageSavePath = path + File.separatorChar + encodeImageName + '.' + imagePostfix;
            File file = new File(imageSavePath);
            if (!file.exists()){
                file.mkdirs();
                file.createNewFile();
                image.transferTo(file);
                if (userRepository.findById(mailbox).isPresent()) {
                    User imageHost = userRepository.findById(mailbox).get();
                    imageHost.setCounts(imageHost.getCounts() + 1);  // 更新持有的数量
                    userRepository.save(imageHost);
                    imageRepository.save(new Image(imageName, imageSavePath, imageSize, imageHost));
                    HashMap<String, String> map = new HashMap<>();
                    map.put("ImageUrl", ImageHostUrl.prefixUrl+"/image/download/" + encodeImageName + '.' + imagePostfix);
                    return new DataResponse<>(1, "Succeed", map);
                } else {
                    return new DataResponse<>(0, "UserUnExist", null);
                }
            }else{
                return new DataResponse<>(0,"ImageExist",null);
            }

        } else {
            return new DataResponse<>(0, "IllegalImage", null);
        }
    }

    @SneakyThrows
    @GetMapping("/download/{image}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> imageDownload(
            @PathVariable String image
           ) {
        String path = System.getProperty("user.dir") + File.separatorChar + "image" + File.separatorChar + image;
        File file = new File(path);
        if (file.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", image));
            FileSystemResource fileSystemResource = new FileSystemResource(path);
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(fileSystemResource.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(fileSystemResource.getInputStream()));
        }
        return null;
    }

}
