/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.photowey.minio.spring.boot.autoconfigure.template;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * {@code SyncMinioTemplate}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/10/07
 */
public interface SyncMinioTemplate extends StreamingTemplate, SilentCall {

    String MINIO_TEMPLATE_BEAN_NAME = "minioTemplate";

    // ----------------------------------------------------------------

    /**
     * Try acquire Sync MinIO client {@link MinioClient}.
     *
     * @return {@link MinioClient}
     */

    default MinioClient client() {
        return this.beanFactory().getBean(MinioClient.class);
    }

    // ----------------------------------------------------------------

    /**
     * Try acquire Async template {@link AsyncMinioTemplate}.
     *
     * @return {@link AsyncMinioTemplate}
     */
    default AsyncMinioTemplate async() {
        return this.beanFactory().getBean(AsyncMinioTemplate.class);
    }

    // ----------------------------------------------------------------

    default boolean bucketNotExists(String bucket) {
        return !this.bucketExists(bucket);
    }

    boolean bucketExists(String bucket);

    boolean createBucket(String bucket);

    boolean removeBucket(String bucket);

    List<Bucket> buckets();

    // ----------------------------------------------------------------

    default ObjectWriteResponse putObject(String bucket, String object, InputStream in) {
        return this.call(() -> {
            return this.putObject(bucket, object, DEFAULT_STREAM_CONTENT_TYPE, in, in.available());
        });
    }

    default ObjectWriteResponse putObject(String bucket, String object, String contextType, InputStream in) {
        return this.call(() -> {
            return this.putObject(bucket, object, contextType, in, in.available());
        });
    }

    ObjectWriteResponse putObject(String bucket, String object, String contextType, InputStream in, long size);

    // ----------------------------------------------------------------

    InputStream downloadObject(String bucket, String object);

    default void downloadObject(String bucket, String object, String filename) {
        this.run(() -> {
            DownloadObjectArgs args = DownloadObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .filename(filename)
                .build();

            this.downloadObject(args);
        });
    }

    boolean downloadObject(DownloadObjectArgs args);

    // ----------------------------------------------------------------

    default StatObjectResponse statObject(String bucket, String object) {
        return this.call(() -> {
            StatObjectArgs args = StatObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build();

            return this.statObject(args);
        });
    }

    StatObjectResponse statObject(StatObjectArgs args);

    // -----------------------------------------------------------------

    default String url(String bucket, String object) {
        return this.call(() -> {
            return this.url(bucket, object, GetPresignedObjectUrlArgs.DEFAULT_EXPIRY_TIME);
        });
    }

    default String url(String bucket, String object, int expirySeconds) {
        return this.call(() -> {
            return this.url(bucket, object, expirySeconds, TimeUnit.SECONDS);
        });
    }

    default String url(String bucket, String object, int expiry, TimeUnit timeUnit) {
        return this.call(() -> {
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .object(object)
                .expiry(expiry, timeUnit).
                build();

            return this.url(args);
        });
    }

    String url(GetPresignedObjectUrlArgs args);
}
