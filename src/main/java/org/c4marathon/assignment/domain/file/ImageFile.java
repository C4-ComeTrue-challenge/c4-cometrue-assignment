package org.c4marathon.assignment.domain.file;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.domain.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long fileId;

    @Column(nullable = false)
    private String uploadFileName; // 회원이 업로드하는 파일명
    @Column(nullable = false)
    private String storeFileName; // 서버에 저장되는 파일명

    public ImageFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
