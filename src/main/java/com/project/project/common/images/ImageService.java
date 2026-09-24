package com.project.project.common.images;

import com.project.project.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ImageService {

    private final Path storagePath = Paths.get("storage", "images");

    public ImageService() {
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage folder", e);
        }
    }

    public List<String> saveImages(List<MultipartFile> files) {

        List<String> paths = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            return paths;
        }

        for (MultipartFile file : files) {
            paths.add(saveImage(file));
        }

        return paths;
    }

    public String saveImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Image is empty"
            );
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());

            if (image == null) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid image file"
                );
            }

            String fileName = UUID.randomUUID() + ".webp";

            Path outputPath = storagePath.resolve(fileName);

            ImageWriter writer = ImageIO
                    .getImageWritersByFormatName("webp")
                    .next();

            ImageWriteParam param = writer.getDefaultWriteParam();

            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.75f);
            }

            try (ImageOutputStream output =
                         ImageIO.createImageOutputStream(outputPath.toFile())) {

                writer.setOutput(output);

                writer.write(
                        null,
                        new IIOImage(image, null, null),
                        param
                );
            } finally {
                writer.dispose();
            }

            return "storage/images/" + fileName;

        } catch (IOException e) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save image"
            );
        }
    }
}