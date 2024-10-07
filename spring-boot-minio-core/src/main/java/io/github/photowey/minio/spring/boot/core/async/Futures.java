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
package io.github.photowey.minio.spring.boot.core.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * {@code Futures}
 *
 * @author photowey
 * @version 1.1.0
 * @since 2024/10/07
 */
public final class Futures {

    private Futures() {
        // utility class; can't create
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    public static <T> T get(Future<T> future) {
        return get(future, (e) -> {
            throw new RuntimeException(e);
        });
    }

    public static <T> T tryGet(Future<T> future, T defaultValue) {
        return tryGet(future, (e) -> {
            throw new RuntimeException(e);
        }, defaultValue);
    }

    public static <T> T get(Future<T> future, Consumer<Exception> fx) {
        return tryGet(future, fx, null);
    }

    public static <T> T tryGet(Future<T> future, Consumer<Exception> fx, T defaultValue) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fx.accept(e);
        } catch (ExecutionException e) {
            fx.accept(e);
        }

        return defaultValue;
    }
}
