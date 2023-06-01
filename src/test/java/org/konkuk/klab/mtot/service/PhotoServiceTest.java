package org.konkuk.klab.mtot.service;

import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.PhotoRepository;
import org.konkuk.klab.mtot.s3.AwsS3FileSupporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PhotoServiceTest {

    @MockBean
    private AwsS3FileSupporter awsS3FileSupporter;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private JourneyRepository journeyRepository;

//    @Test
//    @DisplayName("여정에 사진을 등록한다")
//    public void uploadPhotos() throws Exception{
//        //given
//
//        //when
//
//        //then
//
//    }


}
