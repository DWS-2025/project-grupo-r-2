package com.spartanwrath.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import com.spartanwrath.model.Product;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${image.upload.dir}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg","jpeg","png","gif");

    public String saveImage(byte[] imageBytes, String originalFileName) throws IOException {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image is empty");
        }
        if (imageBytes.length > MAX_FILE_SIZE_BYTES){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tamaño de imagen no permitido");
        }
        String fileExtension = getFileExtension(originalFileName);
        if (!imageExtensionAllowed(fileExtension)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid image");
        }
        byte[] sanitizedImageBytes = sanitizeImage(imageBytes);
        String sanitizedFileName = sanitizeFileName(originalFileName);
        String filename = StringUtils.cleanPath(sanitizedFileName);

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path imagePath = uploadPath.resolve(filename);
        Files.write(imagePath, sanitizedImageBytes);

        return filename;
    }

    public byte[] getDefault() throws IOException {
        String ImageName = getDefaultName();
        Path defaultImagePath = Paths.get(uploadDir).resolve(ImageName);
        if (!Files.exists(defaultImagePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Default image not found");
        }
        return Files.readAllBytes(defaultImagePath);


    }
    public String getDefaultName() {
        return "DefaultProduct.jpg";
    }

    public ResponseEntity<Resource> createResponseFromImage(String imageName) throws MalformedURLException {
        Path imagePath = Paths.get(uploadDir).resolve(imageName);
        Resource resource = new UrlResource(imagePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    public void deleteImage(String imageName) throws IOException {
       if (!"DefaultProduct.jpg".equals(imageName)){
           Path imagePath = Paths.get(uploadDir).resolve(imageName);
           Files.deleteIfExists(imagePath);
       }
    }

    private String getFileExtension(String fileName){
        int dot = fileName.lastIndexOf(".");
        if (dot == -1){
            return "";
        }
        return fileName.substring(dot + 1).toLowerCase();
    }

    private boolean imageExtensionAllowed (String extension){
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    public String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }


    public byte[] sanitizeImage (byte[] imageBytes) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        BufferedImage sanitizedImage = new BufferedImage(originalImage.getWidth(),originalImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        sanitizedImage.createGraphics().drawImage(originalImage,0,0,null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(sanitizedImage,"jpg",outputStream);
        System.out.println("imagen sanitizada");

        byte [] sanitizedImageBytes = outputStream.toByteArray();
        outputStream.close();

        return sanitizedImageBytes;
    }

}
