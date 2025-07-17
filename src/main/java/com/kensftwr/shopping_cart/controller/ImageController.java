package com.kensftwr.shopping_cart.controller;

import org.springframework.http.HttpHeaders;
import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kensftwr.shopping_cart.dtos.ApiResponse;
import com.kensftwr.shopping_cart.dtos.ImageResponse;
import com.kensftwr.shopping_cart.exceptions.ImageNotFoundException;
import com.kensftwr.shopping_cart.models.Image;
import com.kensftwr.shopping_cart.service.image.IImageService;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestBody List<MultipartFile> files,
            @RequestParam Long productId) {
        try {
            List<ImageResponse> imageResponse = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload files!", imageResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload failed!", e.getMessage()));
        }
    }

    // This method declares throws SQLException, meaning it’s letting Spring’s
    // exception handler catch it.
    // If getImageById(imageId) throws a ImageNotFoundException, and it’s not caught
    // here, you must have:
    // A global exception handler (@ControllerAdvice) that handles this exception
    // Or it defaults to a 500 Internal Server Error

    @GetMapping("/image/download/{imageId}")
    @Transactional
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        // Use the BLOB data directly, as it was loaded in the service
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,
            @RequestParam MultipartFile file) {
        try {
            imageService.getImageById(imageId); // Validate existence
            imageService.updateImage(file, imageId);
            return ResponseEntity.ok(new ApiResponse("Update success", null));
        } catch (ImageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update failed!", e.getMessage()));
        }
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            imageService.getImageById(imageId); // Validate existence
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("Deletion success!", null));
        } catch (ImageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Delete failed!", e.getMessage()));
        }
    }
}
