package com.onriderentals.util;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

import java.io.InputStream;
import java.util.Properties;

public class S3Service {
    private static S3Client s3Client;
    private static String bucketName;

    static {
        try {
            Properties props = new Properties();
            InputStream input = S3Service.class.getClassLoader().getResourceAsStream("aws.properties");
            if (input != null) {
                props.load(input);
                String accessKey = props.getProperty("aws.access.key");
                String secretKey = props.getProperty("aws.secret.key");
                String regionStr = props.getProperty("aws.region", "us-east-1");
                bucketName = props.getProperty("aws.s3.bucket");

                s3Client = S3Client.builder()
                        .region(Region.of(regionStr))
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)))
                        .crossRegionAccessEnabled(true)
                        .build();
            } else {
                System.err.println("aws.properties not found. S3 service will not be available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getImageUrl(String key) {
        if (s3Client == null || key == null || key.isEmpty()) {
            return null;
        }
        try {
            GetUrlRequest request = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            return s3Client.utilities().getUrl(request).toExternalForm();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String uploadImage(java.io.File file) {
        if (s3Client == null || file == null || !file.exists()) {
            return null;
        }
        String key = "vehicles/" + System.currentTimeMillis() + "_" + file.getName();
        try {
            software.amazon.awssdk.services.s3.model.PutObjectRequest request = 
                software.amazon.awssdk.services.s3.model.PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.putObject(request, software.amazon.awssdk.core.sync.RequestBody.fromFile(file));
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
