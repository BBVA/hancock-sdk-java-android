# Hancock SDK client (for java/android)

## Before use this package
This package is distributed under our private [Maven repository](s3://microservices-java-repository/maven/release).
You have to configure your project build.gradle file in order to have access to it:

```text/plain
# build.gradle
repositories {
    maven {
        url "s3://microservices-java-repository/maven/release"
        credentials(AwsCredentials) {
            accessKey "AKIAIM5BHCDAPR2HJ6GQ"
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
	compile('bbva.ndb:hancock-sdk-client-android:1.0.0-alpha.14')
}
```

### Using all together

The main client is the [[EthereumClient]] class. You have to instantiate it passing an [[HancockConfig]] configuration object
to indicate the client which hancock service it has to use.

Configuration object example:
```text/plain
import com.bbva.hancock.sdk.dlt.ethereum.EthereumClient;
import com.bbva.hancock.sdk.config.HancockConfig;

HancockConfig config = new HancockConfig.Builder()
    .withAdapter("https://hancock-url.es", "/dlt-adapter", 443)
    .withBroker("ws://hancock-url", "", 80)
    .withWallet("https://hancock-url.es", "/wallet-hub", 443)
    .withEnv("pro")
    .build();
    
EthereumClient client = new EthereumClient(config);
```

### Introduction and examples

[[EthereumClient]] provides interfaces to interact with the blockchain 
allowing common operation like transfers, balance consulting or smart contract interactions. Take a look at the diferent sections of the [docs](https://docs.kickstartteam.es/blockchainhub/kst-hancock-sdk-client-android/docs/index.html) to see examples of use:

- [[EthereumWalletService]]
- [[EthereumTransferService]]
- [[EthereumTransactionService]]
- [[EthereumSmartContractService]]
- [[EthereumTokenService]]
- [[ProtocolService]]