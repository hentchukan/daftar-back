package prv.ferchichi.daftar.api.media;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BucketService {

    @Getter
    private final Storage storage;
    @Getter
    private final String bucketName;
    private final String subdirectory;

    public BucketService(final Storage storage, @Value("daftar-articles") final String bucketName, @Value("images") final String subdirectory) {
        this.storage = storage;
        this.bucketName = bucketName;
        this.subdirectory = subdirectory;
    }

    public Mono<String> uploadImage(final FilePart image, final String filename) {
        return Mono.just(constructBlobId(filename))
            //Create the blobInfo
            .map(bId -> BlobInfo.newBuilder(bId).setContentType("image/jpeg").build())
            //Upload the blob to GCS
            .doOnNext(blobInfo -> {
                try {
                    storage.create(blobInfo, convertToByteArray(image));
                } catch (IOException e) {
                    log.error("Error while uploading photo " + filename);
                }
            })

            //Create a Signed "Path Style" URL to access the newly created Blob
            //Set the URL expiry to 10 Minutes
            .map(blobInfo -> URI.create("https://storage.googleapis.com/"+bucketName+"/"+filename).toString())
            .doOnError(throwable -> log.error(throwable.getMessage()));
    }

    private byte[] convertToByteArray(FilePart filePart) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            filePart.content()
                    .subscribe(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        log.trace("readable byte count:" + dataBuffer.readableByteCount());
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);
                        try {
                            bos.write(bytes);
                        } catch (IOException e) {
                            log.error("read request body error...", e);
                        }
                    });

            return bos.toByteArray();
        }
    }

    /**
     * Construct Blob ID
     *
     * @param fileName
     * @return
     */
    public BlobId constructBlobId(String fileName) {
        return Optional.ofNullable(subdirectory)
                .map(s -> BlobId.of(bucketName, fileName))
                .orElse(BlobId.of(bucketName, fileName));
    }

    public URL createPath(BlobInfo blobInfo, int duration, TimeUnit timeUnit) {
        return getStorage()
                .signUrl(blobInfo, duration, timeUnit, Storage.SignUrlOption.withPathStyle());
    }
}
