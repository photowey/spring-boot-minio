# `spring-boot-minio`

An extender of `MinIO` (the "`MinIO`") for `Spring Boot`. Supports two modes: `MinioTemplate`(`SyncMinioTemplate`) and `AsyncMinioTemplate`.



## 1.`Examples`

### 1.1.`Spring Boot`

- [minio-spring-boot-starter-example](https://github.com/photowey/spring-boot-minio-examples)

### 1.2.`Spring Boot v3`

- [minio-spring-boot3-starter-example](https://github.com/photowey/spring-boot-minio-examples)



## 2.`Usage`

Add this to your `pom.xml`

### 2.1.`Spring Boot`

```http
https://central.sonatype.com/search?q=minio-spring-boot-starter&namespace=io.github.photowey
```

```xml
<minio-spring-boot-starter.version>${latest.version}</minio-spring-boot-starter.version>
```

```xml
<dependency>
    <groupId>io.github.photowey</groupId>
    <artifactId>minio-spring-boot-starter</artifactId>
    <version>${lastest.version}</version>
</dependency>
```



### 2.2.`Spring Boot v3`

```xml
<dependency>
    <groupId>io.github.photowey</groupId>
    <artifactId>minio-spring-boot3-starter</artifactId>
    <version>${lastest.version}</version>
</dependency>
```



## 3.`APIs`

### 3.1.`MiniTemplate`

`SyncMinioTemplate` as same as `MiniTemplate`.

#### 3.1.1.`Bucket`

- `bucketExists`
- `createBucket`
- `removeBucket`
- `buckets`

#### 3.1.2.`Put`

- `putObject`

#### 3.1.3.`Download`

- `getObject`
- `downloadObject`

#### 3.1.4.`Stat`

- `statObject`

#### 3.1.5.`URL`

- `getPresignedObjectUrl`



### 3.2.`AsyncMinioTemplate`

⭐⭐⭐Unsupported now.