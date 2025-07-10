package com.kensftwr.shopping_cart.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kensftwr.shopping_cart.dtos.ImageResponse;
import com.kensftwr.shopping_cart.exceptions.ImageNotFoundException;
import com.kensftwr.shopping_cart.models.Image;
import com.kensftwr.shopping_cart.models.Product;
import com.kensftwr.shopping_cart.repository.ImageRepository;
import com.kensftwr.shopping_cart.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageFile implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image not found!"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ImageNotFoundException("Image not found!");
        });
    }

    @Override
    public List<ImageResponse> saveImages(List<MultipartFile> files, Long productId) {

        Product product = productService.getProductById(productId);
        List<ImageResponse> imageResponse = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();

                image.setFilename(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String downloadUrlBuilder = "/api/v1/images/image/download";
                String downloadUrl = downloadUrlBuilder + image.getId();

                image.setDownloadUrl(downloadUrl);

                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(downloadUrlBuilder + savedImage.getId());

                ImageResponse imageResponseDTO = new ImageResponse();
                imageResponseDTO.setImageId(savedImage.getId());
                imageResponseDTO.setImageName(savedImage.getFilename());
                imageResponseDTO.setDownloadUrl(savedImage.getDownloadUrl());

                imageResponse.add(imageResponseDTO);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return imageResponse;

    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);

        try {
            image.setFilename(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
           imageRepository.save(image); 
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
