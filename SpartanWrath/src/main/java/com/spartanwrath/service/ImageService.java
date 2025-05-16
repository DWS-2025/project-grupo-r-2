package com.spartanwrath.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path imagePath = uploadPath.resolve(filename).normalize();
        Files.write(imagePath, sanitizedImageBytes);

        if (!imagePath.startsWith(uploadPath)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid file path");
        }

        return filename;
    }

    public byte[] getDefault() throws IOException {
        String ImageName = getDefaultName();
        Path defaultImagePath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path target = defaultImagePath.resolve(ImageName).normalize();

        if (!target.startsWith(defaultImagePath) || !Files.exists(defaultImagePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Default image not found");
        }
        return Files.readAllBytes(defaultImagePath);


    }
    public String getDefaultName() {
        return "DefaultProduct.jpg";
    }



    public void deleteImage(String imageName) throws IOException {
       if (!"DefaultProduct.jpg".equals(imageName)){
           Path uploadPath = Paths.get(uploadDir)
                   .toAbsolutePath()
                   .normalize();
           Path target = uploadPath.resolve(imageName).normalize();
           if (!target.startsWith(uploadPath)) {
               throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid file path");
           }

           Files.deleteIfExists(target);
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
