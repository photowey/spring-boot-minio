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

import io.github.photowey.minio.spring.boot.core.async.Futures;
import io.minio.*;
import io.minio.messages.Bucket;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * {@code DefaultAsyncMinioTemplate}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/10/06
 */
public class DefaultAsyncMinioTemplate implements AsyncMinioTemplate {

    private final MinioAsyncClient minioClient;

    public DefaultAsyncMinioTemplate(MinioAsyncClient minioClient) {
        this.minioClient = minioClient;
    }

    // ----------------------------------------------------------------- bucket

    @Override
    public CompletableFuture<Boolean> bucketExists(String bucket) {
        return this.call(() -> {
            BucketExistsArgs args = BucketExistsArgs.builder()
                .bucket(bucket)
                .build();

            return this.minioClient.bucketExists(args);
        });
    }

    // -----------------------------------------------------------------

    @Override
    public CompletableFuture<Boolean> createBucket(String bucket) {
        return this.call(() -> {
            CompletableFuture<Boolean> fx = new CompletableFuture<>();
            if (Futures.tryGet(this.bucketExists(bucket), false)) {
                fx.complete(false);

                return fx;
            }

            MakeBucketArgs args = MakeBucketArgs.builder()
                .bucket(bucket)
                .build();

            CompletableFuture<Void> fv = this.minioClient.makeBucket(args);
            fv.whenComplete((x, ex) -> {
                if (ex != null) {
                    fx.completeExceptionally(ex);
                } else {
                    fx.complete(true);
                }
            });

            return fx;
        });
    }

    @Override
    public CompletableFuture<Boolean> removeBucket(String bucket) {
        return this.call(() -> {
            CompletableFuture<Boolean> fx = new CompletableFuture<>();

            RemoveBucketArgs args = RemoveBucketArgs.builder()
                .bucket(bucket)
                .build();

            CompletableFuture<Void> fv = this.minioClient.removeBucket(args);
            fv.whenComplete((x, ex) -> {
                if (ex != null) {
                    fx.completeExceptionally(ex);
                } else {
                    fx.complete(true);
                }
            });

            return fx;
        });
    }

    @Override
    public CompletableFuture<List<Bucket>> buckets() {
        return this.call(this.minioClient::listBuckets);
    }

    // -----------------------------------------------------------------

    @Override
    public CompletableFuture<ObjectWriteResponse> putObject(String bucket, String object, String contextType, InputStream in, long size) {
        return this.call(() -> {
            PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .stream(in, size, -1)
                .contentType(contextType)
                .build();

            return this.minioClient.putObject(args);
        });
    }

    // -----------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public <T extends InputStream> CompletableFuture<T> downloadObject(String bucket, String object) {
        return this.call(() -> {
            GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build();

            return (CompletableFuture<T>) this.minioClient.getObject(args);
        });
    }

    @Override
    public CompletableFuture<Boolean> downloadObject(DownloadObjectArgs args) {
        return this.call(() -> {
            CompletableFuture<Boolean> fx = new CompletableFuture<>();

            CompletableFuture<Void> fv = this.minioClient.downloadObject(args);
            fv.whenComplete((x, ex) -> {
                if (ex != null) {
                    fx.completeExceptionally(ex);
                } else {
                    fx.complete(true);
                }
            });

            return fx;
        });
    }

    // -----------------------------------------------------------------

    @Override
    public CompletableFuture<StatObjectResponse> statObject(StatObjectArgs args) {
        return this.call(() -> {
            return this.minioClient.statObject(args);
        });
    }

    // -----------------------------------------------------------------

    @Override
    public String url(GetPresignedObjectUrlArgs args) {
        return this.call(() -> {
            return this.minioClient.getPresignedObjectUrl(args);
        });
    }
}
