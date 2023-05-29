package org.konkuk.klab.mtot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.konkuk.klab.mtot.domain.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PhotoServiceTest {

    @Autowired
    private PhotoService photoService;

    @Test
    public void testUploadPhoto() throws IOException {
        // Create a sample MultipartFile for testing
        byte[] fileData = {1, 2, 3}; // Replace with actual file data
        MultipartFile file = new MockMultipartFile("test.jpg", fileData);

        // Upload the photo and verify the result
        Photo uploadedPhoto = photoService.uploadPhoto(file);
        assertNotNull(uploadedPhoto);
        assertNotNull(uploadedPhoto.getId());
    }

    @Test
    public void testGetAllPhotos() {
        // Call the service method to get all photos
        List<Photo> photos = photoService.getAllPhotos();

        // Perform assertions
        assertNotNull(photos);
        assertFalse(photos.isEmpty());
    }

    @Test
    public void testGetPhotoById() throws IOException {
        // Create a sample MultipartFile for testing
        byte[] fileData = {1, 2, 3}; // Replace with actual file data
        MultipartFile file = new MockMultipartFile("test.jpg", fileData);

        // Upload the photo
        Photo uploadedPhoto = photoService.uploadPhoto(file);

        // Get the photo by ID
        Photo retrievedPhoto = photoService.getPhotoById(uploadedPhoto.getId());

        // Perform assertions
        assertNotNull(retrievedPhoto);
        assertEquals(uploadedPhoto.getId(), retrievedPhoto.getId());
    }

    @Test
    public void testDeletePhotoById() throws IOException {
        // Create a sample MultipartFile for testing
        byte[] fileData = {1, 2, 3}; // Replace with actual file data
        MultipartFile file = new MockMultipartFile("test.jpg", fileData);

        // Upload the photo
        Photo uploadedPhoto = photoService.uploadPhoto(file);

        // Delete the photo by ID
        boolean isDeleted = photoService.deletePhotoById(uploadedPhoto.getId());

        // Perform assertions
        assertTrue(isDeleted);

        // Verify that the photo no longer exists
        Photo deletedPhoto = photoService.getPhotoById(uploadedPhoto.getId());
        assertNull(deletedPhoto);
    }
}
