package com.kensftwr.shopping_cart.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import com.kensftwr.shopping_cart.dtos.ImageResponse;
import com.kensftwr.shopping_cart.exceptions.ImageNotFoundException;
import com.kensftwr.shopping_cart.exceptions.ProductNotFoundException;
import com.kensftwr.shopping_cart.models.Image;
import com.kensftwr.shopping_cart.models.Product;
import com.kensftwr.shopping_cart.repository.ImageRepository;
import com.kensftwr.shopping_cart.repository.ProductRepository;
import com.kensftwr.shopping_cart.service.product.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageFile implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Image getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image not found!"));
        // Force BLOB data to be loaded within the transaction
        if (image.getImage() != null) {
            try {
                image.getImage().length(); // Trigger BLOB access
            } catch (SQLException e) {
                throw new RuntimeException("Error accessing image data", e);
            }
        }
        return image;
    }
    @Override
    @Transactional
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ImageNotFoundException("Image not found!");
        });
    }

    @Override
    @Transactional
    public List<ImageResponse> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        List<ImageResponse> imageResponses = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFilename(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                // Save first to generate the ID
                Image savedImage = imageRepository.save(image);

                // Now build and set the download URL using savedImage.getId()
                String downloadUrl = "/api/v1/images/image/download/" + savedImage.getId();
                savedImage.setDownloadUrl(downloadUrl);
                imageRepository.save(savedImage); // Save again with download URL if you store it in DB

                // Prepare response DTO
                ImageResponse dto = new ImageResponse();
                dto.setImageId(savedImage.getId());
                dto.setImageName(savedImage.getFilename());
                dto.setDownloadUrl(savedImage.getDownloadUrl());

                imageResponses.add(dto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
            }
        }

        return imageResponses;
    }

    @Override
    @Transactional
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);

        try {
            image.setFilename(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));

            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to update image: " + e.getMessage(), e);
        }
    }
}
