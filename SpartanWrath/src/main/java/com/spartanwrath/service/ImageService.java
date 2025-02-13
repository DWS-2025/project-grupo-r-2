package com.spartanwrath.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {


    public static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/images");

   /* public String createImage(MultipartFile multiPartFile) {

        String originalName = multiPartFile.getOriginalFilename();

        if (!originalName.matches(".*\\.(jpg|jpeg|gif|png)")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url is not an image resource");
        }

        String fileName = "image_" + UUID.randomUUID() + "_" + originalName;

        Path imagePath = FILES_FOLDER.resolve(fileName);
        try {
            multiPartFile.transferTo(imagePath);
        } catch (Exception ex) {
            System.err.println(ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save image locally", ex);
        }

        return fileName;
    }
*/


    public void saveImage(String relativeImagePath, MultipartFile image) throws IOException {
        Path imagePath = FILES_FOLDER.resolve(relativeImagePath.substring(13));
        Files.createDirectories(imagePath.getParent());
        Path newFile = imagePath;
        image.transferTo(newFile);
        System.out.println(imagePath);
        System.out.println(newFile);

    }


    public ResponseEntity<Object> createResponseFromImage(String folderName, String relativePath) throws MalformedURLException{
        Path folder = FILES_FOLDER.resolve(folderName);
        System.out.println(folder);
        Path imagePath = folder.resolve(relativePath.substring(13));
        System.out.println(imagePath);
        Resource file = new UrlResource(imagePath.toUri());
        System.out.println(file);

        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }else {
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
        }
    }

    public void deleteImage(String relativeImagePath) throws IOException {
        Path imagePath = FILES_FOLDER.resolve(relativeImagePath.substring(13));
        Files.deleteIfExists(imagePath);
    }
}