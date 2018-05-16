# Hancock SDK client (for java)

## Before use this package
This package is distributed under our private [Maven repository](s3://microservices-java-repository/maven/release).
You have to configure your project build.gradle file in order to have access to it:

```text/plain
# build.gradle
repositories {
    maven {
        url "s3://microservices-java-repository/maven/release"
        credentials(AwsCredentials) {
            accessKey "AKIAJFKJEBF2EHAIW5OQ"
            secretKey "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"
        }
    }
}
```

## Using this package

### Installation

Once you have access to the repository:

```text/plain
# build.gradle
dependencies {
	compile('bbva.ndb:hancock-sdk-client-android:1.0.0-alpha.8')
}
```

### Using all together

In code:

```java
import com.bbva.hancock.sdk.HancockEthereumClient;
import com.bbva.hancock.sdk.config.HancockConfig;

HancockConfig config = new HancockConfig.Builder("dev").withNode("http://localhost", 8545).build();
new HancockEthereumClient(config);
```
