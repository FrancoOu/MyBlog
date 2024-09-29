package org.franco.service.impl;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.franco.domain.ResponseResult;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.exception.SystemException;
import org.franco.service.UploadService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;



@Service
@Data
@ConfigurationProperties(prefix = "google-cloud")
public class UploadServiceImpl implements UploadService {

    String projectId;
    String bucketName;

    @Override
    public ResponseResult uploadImg(MultipartFile imgFile) {

        return ResponseResult.okResult(googleCloudUpload(imgFile));
    }

    private String googleCloudUpload(MultipartFile imgFile){
        // The ID of your GCS object
        String objectName = "2024/" + imgFile.getOriginalFilename();
        String fileType = FilenameUtils.getExtension(objectName);

        if (!fileType.equals("jpg") && !fileType.equals("png") && !fileType.equals("jpeg")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        // The contents you wish to upload
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        // Initialise input stream
        InputStream content;

        try {
            content = imgFile.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Set access control list for the object (no longer needed because have applied a default acl on google cloud.
        // refer to: https://cloud.google.com/storage/docs/access-control/create-manage-lists#command-line_3 )
//        Acl acl = Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER);
//        List<Acl> acls = new ArrayList<>();
//        acls.add(acl);

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo
                .newBuilder(blobId)
                .setContentType(imgFile.getContentType())
                .build();

        // Write the object to the cloud storage
        try (WriteChannel writer = storage.writer(blobInfo)) {
            writer.write(ByteBuffer.wrap(IOUtils.toByteArray(content)));
            System.out.println(
                    "Wrote to " + objectName + " in bucket " + bucketName + " using a WriteChannel.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buildFileUrl(objectName);
    }

    private String buildFileUrl(String fileName) {
        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
    }
}
