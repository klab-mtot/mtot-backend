package org.konkuk.klab.mtot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Pin pin;

    @Column(name = "uploadDate")
    private LocalDate uploadDate;

    @Column(name = "imageUrl")
    private String imageUrl;

    public Photo(Pin pin, String imageUrl, LocalDate uploadDate) {
        this.pin = pin;
        pin.getPhotos().add(this);
        this.imageUrl = imageUrl;
        this.uploadDate = uploadDate;
    }
}
