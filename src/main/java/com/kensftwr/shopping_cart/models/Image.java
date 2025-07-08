package com.kensftwr.shopping_cart.models;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private String fileType;
    private Blob image;
    private String downloadUrl;

    public Image(String filename, String fileType, Blob image, String downloadUrl) {
        this.filename = filename;
        this.fileType = fileType;
        this.image = image;
        this.downloadUrl = downloadUrl;
    }

    //many images belong to one product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
