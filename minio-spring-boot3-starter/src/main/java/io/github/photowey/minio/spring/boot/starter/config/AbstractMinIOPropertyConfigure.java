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
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * {@code AbstractMinIOPropertyConfigure}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/10/06
 */
public abstract class AbstractMinIOPropertyConfigure {

    @Configuration
    static class PropertyConfigure {

        @Bean
        public MinIOProperties minioProperties(Environment environment) {
            return bind(environment, MinIOProperties.getPrefix(), MinIOProperties.class);
        }
    }

    static <T> T bind(Environment environment, String prefix, Class<T> clazz) {
        Binder binder = Binder.get(environment);

        return binder.bind(prefix, clazz).get();
    }
}
