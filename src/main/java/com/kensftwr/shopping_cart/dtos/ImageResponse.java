package com.kensftwr.shopping_cart.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {

    private Long imageId;
    private String imageName;
    private String downloadUrl;

}
