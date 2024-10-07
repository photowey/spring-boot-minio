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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * {@code AsyncMinioTemplate}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/10/06
 */
public interface AsyncMinioTemplate extends StreamingTemplate, SilentCall {

    String MINIO_TEMPLATE_BEAN_NAME = "asyncMinioTemplate";

    // ----------------------------------------------------------------

    /**
     * Try acquire Sync template {@link SyncMinioTemplate}.
     *
     * @return {@link SyncMinioTemplate}
     */
    default SyncMinioTemplate sync() {
        return this.beanFactory().getBean(SyncMinioTemplate.class);
    }

    // ----------------------------------------------------------------

    CompletableFuture<Boolean> bucketExists(String bucket);

    CompletableFuture<Boolean> createBucket(String bucket);

    CompletableFuture<Boolean> removeBucket(String bucket);

    CompletableFuture<List<Bucket>> buckets();

    // ----------------------------------------------------------------

    default CompletableFuture<ObjectWriteResponse> putObject(
        String bucket, String object, InputStream in) {
        return this.call(() -> {
            return this.putObject(bucket, object, DEFAULT_STREAM_CONTENT_TYPE, in, in.available());
        });
    }

    default CompletableFuture<ObjectWriteResponse> putObject(
        String bucket, String object, String contextType, InputStream in) {
        return this.call(() -> {
            return this.putObject(bucket, object, contextType, in, in.available());
        });
    }

    CompletableFuture<ObjectWriteResponse> putObject(
        String bucket, String object, String contextType, InputStream in, long size);

    // ----------------------------------------------------------------

    <T extends InputStream> CompletableFuture<T> downloadObject(String bucket, String object);

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

    CompletableFuture<Boolean> downloadObject(DownloadObjectArgs args);

    // ----------------------------------------------------------------

    default CompletableFuture<StatObjectResponse> statObject(String bucket, String object) {
        return this.call(() -> {
            StatObjectArgs args = StatObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build();

            return this.statObject(args);
        });
    }

    CompletableFuture<StatObjectResponse> statObject(StatObjectArgs args);

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
