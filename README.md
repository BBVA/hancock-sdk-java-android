# Hancock SDK client (for java/android)

## Using this package
This package is distributed from [Maven repository](https://mvnrepository.com/artifact/com.bbva.ndb/hancock-sdk-java-android).

### Installation

Gradle
```text/plain
# build.gradle
dependencies {
	compile('com.bbva.ndb:hancock-sdk-java-android:1.0.0-alpha.49')
}
```

Maven
```text/plain
# pom.xml
<dependencies>
    <dependency>
        <groupId>com.bbva.ndb</groupId>
        <artifactId>hancock-sdk-java-android</artifactId>
        <version>1.0.0-alpha.49</version>
    </dependency>
</dependencies>
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
