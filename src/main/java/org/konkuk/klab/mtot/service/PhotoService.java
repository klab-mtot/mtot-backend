package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Photo;
import org.konkuk.klab.mtot.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    @Transactional
    public Photo uploadPhoto(MultipartFile file) throws IOException {
        Photo photo = new Photo();
        photo.setPicture(file.getBytes()); // MultipartFile을 byte[]로 변환하여 저장

        return photoRepository.save(photo);
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean deletePhotoById(Long id) {
        if (photoRepository.existsById(id)) {
            photoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
