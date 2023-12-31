package com.mushroomapp.app.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.mushroomapp.app.model.interaction.Comment;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Media;
import lombok.Setter;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@Setter
public class AwsService {


    private final String BUCKET_NAME = "mushroom-app-v1";

    @Value("${aws.secret-key}")
    private String awsSecretKey;

    @Value("${aws.access-key}")
    private String awsAccessKey;

    private AWSCredentials credentials;
    private AmazonS3 s3;

    @PostConstruct
    public void postConstruct() {
        this.credentials = new BasicAWSCredentials(
                this.awsAccessKey,
                this.awsSecretKey
        );

        this.s3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_WEST_2)
                .build();
    }

    public String getSignedUrlForProfilePicture(User user) {
        return getSignedUrlForObjectKey(
                user.getProfilePicture().getFilename()
        );
    }

    public String getSignedUrlForCommentProfilePicture(Comment comment) {
        return getSignedUrlForProfilePicture(
                comment.getUser()
        );
    }

    public String getSignedUrlForMedia(Media media) {
        return getSignedUrlForObjectKey(
                media.getFilename()
        );
    }

    public String getSignedUrlForObjectKey(String objectKey) {
        Date expiration = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS)
        );

        GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(
                BUCKET_NAME,
                objectKey
        )
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        URL url = s3.generatePresignedUrl(presignedUrlRequest);
        return url.toString();
    }

    public PutObjectResult uploadFile(MultipartFile file, String filename) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        return this.s3.putObject(BUCKET_NAME, filename, file.getInputStream(), metadata);
    }
}
