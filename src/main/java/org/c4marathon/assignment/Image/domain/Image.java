package org.c4marathon.assignment.Image.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.global.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String fileName;

    private String imageUrl;

    @Builder
    private Image(String fileName, String imageUrl) {
        this.fileName = fileName;
        this.imageUrl = imageUrl;
    }

    public static Image of(String fileName, String imageUrl) {
        return Image.builder()
                .fileName(fileName)
                .imageUrl(imageUrl)
                .build();
    }

}
