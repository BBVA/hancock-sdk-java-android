## HancockEthereumTransactionClient

### Introduction

We can use this interface to manage transactions over blockchain

### Sign transaction locally and send it to the blockchain

```bash
EthereumRawTransaction rawTx = new EthereumRawTransaction(
    "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
    "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
    new BigInteger("523"),
    new BigInteger("100000000000000"),
    "data",
    new BigInteger("300000"),
    new BigInteger("50000")
);

String signed = client.getTransactionClient()
    .signTransaction(rawTx, "0xd06026d5b8664036bdec0a924b8c7360566e678a2291e9440156365b040a7b83");

TransactionConfig signedConfig = new TransactionConfig.Builder()
    .withCallbackOptions("https://callbackUrl/myEndpoint", "52348901480917420194790134")
    .build();

EthereumTransactionResponse sendSignedResponse = client.getTransactionClient().sendSignedTransaction(signed, signedConfig);

System.out.println("success:" + response.getSuccess());
```

Output:
```bash
success: true
```

### Subscribe to transactions

```bash
ArrayList<String> addresses = new ArrayList();
addresses.add("0x6c0a14f7561898b9ddc0c57652a53b2c6665443e");

Function txFunc = o -> {
    System.out.println(o);
    return null;
};

HancockSocket socket = client.getTransactionClient().subscribe(addresses, "");

socket.on("tx", txFunc);
```
