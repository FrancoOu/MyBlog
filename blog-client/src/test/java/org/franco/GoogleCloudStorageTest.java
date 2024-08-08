//package org.franco;
//
//
//import com.google.cloud.WriteChannel;
//import com.google.cloud.storage.BlobId;
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.apache.commons.compress.compressors.FileNameUtil;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.poi.util.IOUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.ByteBuffer;
//
//@SpringBootTest
//public class GoogleCloudStorageTest {
//
//    @Test
//    public void test() throws IOException {
//        // The ID of your GCP project
//         String projectId = "inner-strategy-431510-d7";
//
//        // The ID of your GCS bucket
//         String bucketName = "franco-s-blog-bucket";
//
//        // The ID of your GCS object
//        String objectName = "2024/" + FilenameUtils.getName("/Users/franco/Desktop/download.jpeg");
//
//        // Type of file
//
//        // The contents you wish to upload
//        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
//        FileInputStream content = new
//                FileInputStream("/Users/franco/Desktop/download.jpeg");
//
//        BlobId blobId = BlobId.of(bucketName, objectName);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpg").build();
//
//
//        try (WriteChannel writer = storage.writer(blobInfo)) {
//            writer.write(ByteBuffer.wrap(IOUtils.toByteArray(content)));
//            System.out.println(
//                    "Wrote to " + objectName + " in bucket " + bucketName + " using a WriteChannel.");
//        }
//    }
//
//}
