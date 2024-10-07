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

import io.github.photowey.minio.spring.boot.core.exception.MinioException;

import java.util.concurrent.Callable;

/**
 * {@code SilentCall}
 *
 * @author photowey
 * @version 1.1.0
 * @since 2024/10/07
 */
public interface SilentCall {

    default void run(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            throw new MinioException(e);
        }
    }

    default <T> T call(Callable<T> task) {
        try {
            return task.call();
        } catch (Exception e) {
            throw new MinioException(e);
        }
    }
}
