package com.kensftwr.shopping_cart.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kensftwr.shopping_cart.dtos.ImageResponse;
import com.kensftwr.shopping_cart.models.Image;

public interface IImageService {

    Image getImageById(Long id);

    void deleteImageById(Long id);

    List<ImageResponse> saveImages(List<MultipartFile> files,Long productId);

    void updateImage(MultipartFile file, Long imageId);

}
