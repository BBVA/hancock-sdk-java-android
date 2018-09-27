## HancockEthereumTokenClient

### Introduction

We can use this interface to manage operations related with ERC20 Ethereum tokens over blockchain

### Token transfer

```bash
TransactionConfig txconfig = new TransactionConfig.Builder()
    .withPrivateKey("0xd06026d5b8664036bdec0a924b8c7360566e678a2291e9440156365b040a7b83");
    .build();
      
EthereumTokenTransferRequest transferRequest = new EthereumTokenTransferRequest(
    "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
    "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
    "1",
    "token-alias"
);

EthereumTransactionResponse response = client.getTokenClient().transfer(transferRequest, txconfig);
        
System.out.println("success:" + response.getSuccess());
```

Console output:
```bash
success: true
```

### Token register

```bash
client.getTokenClient().register(
    "token-alias", 
    "0x547aaccaef70a7aef5a44fa173f46e9ea07bdce7"
);
```