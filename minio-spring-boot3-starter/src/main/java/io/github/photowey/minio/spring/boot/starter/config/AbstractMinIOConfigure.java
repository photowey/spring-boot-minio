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
package io.github.photowey.minio.spring.boot.starter.config;

import io.github.photowey.minio.spring.boot.autoconfigure.property.MinIOProperties;
import io.github.photowey.minio.spring.boot.autoconfigure.template.AsyncMinioTemplate;
import io.github.photowey.minio.spring.boot.autoconfigure.template.DefaultAsyncMinioTemplate;
import io.github.photowey.minio.spring.boot.autoconfigure.template.DefaultSyncMinioTemplate;
import io.github.photowey.minio.spring.boot.autoconfigure.template.SyncMinioTemplate;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * {@code AbstractMinIOConfigure}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/10/06
 */
public abstract class AbstractMinIOConfigure {

    @Autowired
    private MinIOProperties properties;

    /**
     * {@link MinioClient }
     * <p>
     * {@literal @}ConditionalOnExpression("#{'true'.equals(environment['spring.minio.sync.enabled'])}")
     *
     * @return {@link MinioClient}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("${spring.minio.sync.enabled:true}") // ?
    public MinioClient minioSyncClient() {
        MinioClient.Builder builder = MinioClient.builder()
            .endpoint(properties.getEndpoint())
            .credentials(properties.getAccessKey(), properties.getSecretKey());

        if (StringUtils.hasText(properties.getRegion())) {
            builder.region(properties.getRegion());
        }

        return builder.build();
    }

    /**
     * {@link MinioAsyncClient}
     *
     * @return {@link MinioAsyncClient}
     * {@literal @}ConditionalOnExpression("#{'true'.equals(environment['spring.minio.async.enabled'])}")
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("${spring.minio.async.enabled:false}")
    public MinioAsyncClient minioAsyncClient() {
        MinioAsyncClient.Builder builder = MinioAsyncClient.builder()
            .endpoint(properties.getEndpoint())
            .credentials(properties.getAccessKey(), properties.getSecretKey());

        if (StringUtils.hasText(properties.getRegion())) {
            builder.region(properties.getRegion());
        }

        return builder.build();
    }

    /**
     * {@link SyncMinioTemplate}
     *
     * @param minioClient {@link MinioClient}
     * @return {@link SyncMinioTemplate}
     */
    @Bean(SyncMinioTemplate.MINIO_TEMPLATE_BEAN_NAME)
    @ConditionalOnMissingBean
    @ConditionalOnExpression("${spring.minio.sync.enabled:true}")
    public SyncMinioTemplate minioTemplate(MinioClient minioClient) {
        return new DefaultSyncMinioTemplate(minioClient);
    }

    /**
     * {@link AsyncMinioTemplate}
     *
     * @param minioClient {@link MinioAsyncClient}
     * @return {@link AsyncMinioTemplate}
     */
    @Bean(AsyncMinioTemplate.MINIO_TEMPLATE_BEAN_NAME)
    @ConditionalOnMissingBean
    @ConditionalOnExpression("${spring.minio.async.enabled:false}")
    public AsyncMinioTemplate asyncMinioTemplate(MinioAsyncClient minioClient) {
        return new DefaultAsyncMinioTemplate(minioClient);
    }
}
