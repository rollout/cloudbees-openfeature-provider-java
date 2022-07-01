# cloudbees-openfeature-provider
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
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Gradle:
```groovy
dependencies {
    implementation 'com.cloudbees.openfeature:cloudbees-openfeature-provider:0.0.1-SNAPSHOT'
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
