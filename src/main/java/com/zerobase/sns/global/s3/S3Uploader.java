package com.zerobase.sns.global.s3;

import static com.zerobase.sns.global.exception.ErrorCode.S3_UPLOAD_ERROR;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.zerobase.sns.global.exception.CustomException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3Uploader {

  private final AmazonS3 amazonS3;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  @Value("${cloud.aws.region.static}")
  private String region;

  public String fileUpload(MultipartFile multipartFile) {
    String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

    ObjectMetadata objMeta = new ObjectMetadata();
    objMeta.setContentLength(multipartFile.getSize());
    objMeta.setContentType(multipartFile.getContentType());

    try {
      amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objMeta);
    } catch (Exception e) {
      throw new CustomException(S3_UPLOAD_ERROR);
    }

    return amazonS3.getUrl(bucket, fileName).toString();
  }

  //파일 삭제
  public void delete(String key) {
    amazonS3.deleteObject(bucket, key);
  }
}