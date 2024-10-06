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
package io.github.photowey.minio.spring.boot.autoconfigure.property;

import io.github.photowey.minio.spring.boot.core.exception.MinioException;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * {@code MinIOProperties}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/10/06
 */
@Data
public class MinIOProperties implements InitializingBean {

    public static final String SPRING_BOOT_MINIO_PREFIX = "spring.minio";

    private String region;
    private String endpoint;
    private String accessKey;
    private String secretKey;

    private Sync sync = new Sync();
    private Async async = new Async();

    @Data
    public static class Sync {
        private boolean enabled = true;
    }

    @Data
    public static class Async {
        private boolean enabled = false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.hasText(this.getEndpoint())) {
            throw new MinioException("spring.minio.endpoint can't be blank");
        }
        if (!StringUtils.hasText(this.getAccessKey())) {
            throw new MinioException("spring.minio.accessKey can't be blank");
        }
        if (!StringUtils.hasText(this.getSecretKey())) {
            throw new MinioException("spring.minio.secretKey can't be blank");
        }
    }

    public static String getPrefix() {
        return SPRING_BOOT_MINIO_PREFIX;
    }
}

