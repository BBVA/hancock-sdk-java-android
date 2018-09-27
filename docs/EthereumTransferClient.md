## HancockEthereumTransferClient

### Introduction

We can use this interface to manage transfers of ether over blockchain

### Send transfer

```bash
EthereumTransferRequest transfer = new EthereumTransferRequest(
    "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e", 
    "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d", 
    "1"
);

TransactionConfig txconfig = new TransactionConfig.Builder()
    .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
    .build();
    
EthereumTransactionResponse transferResponse = client.getTransferClient().send(transfer, txconfig);
System.out.println("success: " + transferResponse.getSuccess());
```

Console output:
```bash
success: true
```

### Subscribe to transfers

```bash
ArrayList<String> addresses = new ArrayList();
addresses.add("0x6c0a14f7561898b9ddc0c57652a53b2c6665443e");

Function txFunc = o -> {
    System.out.println(o);
    return null;
};

HancockSocket socket = client.getTransferClient().subscribe(addresses, "");

socket.on("tx", txFunc);
```
