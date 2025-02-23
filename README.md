# CloudBees Feature Management provider for OpenFeature

[![OpenFeature Specification](https://img.shields.io/static/v1?label=OpenFeature%20Specification&message=v0.5.1&color=yellow)](https://github.com/open-feature/spec/tree/v0.5.1)
[![OpenFeature SDK](https://img.shields.io/static/v1?label=OpenFeature%20Java%20SDK&message=v1.0.0&color=green)](https://github.com/open-feature/java-sdk/tree/1.0.0)
[![CloudBees Rox SDK](https://img.shields.io/static/v1?label=Rox%20SDK&message=v6.0.0&color=green)](https://mvnrepository.com/artifact/io.rollout.rox/rox-java-server)
[![Known Vulnerabilities](https://snyk.io/test/github/rollout/cloudbees-openfeature-provider-java/badge.svg)](https://snyk.io/test/github/rollout/cloudbees-openfeature-provider-java)

This is the [CloudBees](https://www.cloudbees.com/products/feature-management) provider implementation for [OpenFeature](https://openfeature.dev/) for the [Java SDK](https://github.com/open-feature/java-sdk).

OpenFeature provides a vendor-agnostic abstraction layer on Feature Flag management.

This provider allows the use of CloudBees Feature Management as a backend for Feature Flag configurations.

## Requirements
- Java 8+

## Installation

### Add it to your build

Maven:
```xml
<dependency>
    <groupId>com.cloudbees.openfeature</groupId>
    <artifactId>cloudbees-openfeature-provider</artifactId>
    <version>1.0.1</version>
</dependency>
```

Gradle:
```groovy
dependencies {
    implementation 'com.cloudbees.openfeature:cloudbees-openfeature-provider:1.0.1'
}
```

### Configuration

Follow the instructions on the [Java SDK project](https://github.com/open-feature/java-sdk) for how to use the Java SDK.

You can configure the CloudBees provider by doing the following:

```java
import com.cloudbees.openfeature.provider.CloudbeesProvider;
...

OpenFeatureAPI api = OpenFeatureAPI.getInstance();
String appKey = "INSERT_APP_KEY_HERE" 
// see Installation Instructions in your dashboard for this key (it is also your environment ID) 
api.setProvider(new CloudbeesProvider(appKey));
```
