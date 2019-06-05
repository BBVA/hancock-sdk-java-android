---
title: 'Hancock'
location: 'Hancock'
---
# Hancock (for java/android)

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

### Configuring a client

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

EthereumClient provides interfaces to interact with the blockchain 
allowing common operation like transfers, balance consulting or smart contract interactions. 
Take a look at the examples:

- [Ethereum]({{ link('pageId=Ethereum') }})
  - [EthereumWalletService]({{ link('pageId=EthereumWalletService') }})
  - [EthereumTransferService]({{ link('pageId=EthereumTransferService') }})
  - [EthereumTransactionService]({{ link('pageId=EthereumTransactionService') }})
  - [EthereumSmartContractService]({{ link('pageId=EthereumSmartContractService') }})
  - [EthereumTokenService]({{ link('pageId=EthereumTokenService') }})
- [ProtocolService]({{ link('pageId=ProtocolService') }})

### Javadoc

You can find API definitions [here](https://bbva.github.io/hancock-sdk-java-android/api).
