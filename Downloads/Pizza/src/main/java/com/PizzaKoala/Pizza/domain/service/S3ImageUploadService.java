package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.S3DTO;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PublicAccessBlockConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.UUID;

@Slf4j
@Service
public class S3ImageUploadService {

    private final AmazonS3Client s3Client;

    @Autowired
    public S3ImageUploadService(AmazonS3Client s3Client) {
        this.s3Client = s3Client;
    }
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    /**
     * S3에 파일 올리기
     * **/
    public String upload(MultipartFile image) throws IOException {


        //파일 이름이 존재하는지 확인.
        if (image.getOriginalFilename() == null){
            throw new PizzaAppException(ErrorCode.FILE_NAME_NOT_FOUND);
        }
        /* 업로드할 파일 이름 변경 */
        String originalFileName = image.getOriginalFilename();
        String fileName = changeFileName(originalFileName);

        /* S3에 업로드할 파일의 메다테이터 생성 */
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        /* s3에 파일 업로드 */
        try {
            s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);
        } catch (IOException e) {
            throw new PizzaAppException(ErrorCode.S3_UPLOAD_FAILED);
        }


        /* 업로드한 파일의 s3 url 주소 변환 */
        return s3Client.getUrl(bucket, fileName).toString();
    }
    private String changeFileName(String originalFileName) {
        // UUID를 사용하여 고유한 파일명 생성
        String uuid = UUID.randomUUID().toString();
        String extension = "";

        // 파일 확장자를 추출
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }
        // 고유한 파일명 생성
        return uuid + extension;
    }

    /**
     * S3에 파일 삭제하기 with url주소
     * **/
    public void deleteFiles(String fileName) throws IOException {
        String key = getKeyFromImageAddress(fileName);
        try{
            s3Client.deleteObject(new DeleteObjectRequest(bucket, key));
        }catch (Exception e){
            throw new PizzaAppException(ErrorCode.S3_UPLOAD_FAILED);
        }
    }
    private String getKeyFromImageAddress(String fileName){
        try {
            // URL 객체 생성
            URL url = new URL(fileName);
            // URL 경로 추출
            String path = url.getPath();
            // 경로에서 첫 4개 부분 제거
            return path.substring(path.indexOf('/', 1) + 1);

        } catch (MalformedURLException e) {
            throw new PizzaAppException(ErrorCode.S3_UPLOAD_FAILED);
        }
    }
}
