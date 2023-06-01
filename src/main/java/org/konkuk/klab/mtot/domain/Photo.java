package org.konkuk.klab.mtot.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {

    @Id @GeneratedValue()
    @Column(name = "photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pin pin;

    @Column(name = "uploadDate")
    private LocalDate uploadDate;

    @Column(name = "filePath")
    private String filePath;

    public Photo(Pin pin, String filePath, LocalDate uploadDate) {
        this.pin = pin;
        pin.getPhotos().add(this);
        this.filePath = filePath;
        this.uploadDate = uploadDate;
    }
}
