## HancockProtocolClient

### Introduction

We can use this interface to manage Hancock's protocol operations

### Encode transaction with Hancock's protocol

```bash
EthereumClient client = new EthereumClient(config);

HancockProtocolEncodeResponse encode = client.getProtocolClient()
    .encodeProtocol(
            HancockProtocolAction.transfer,
            new BigInteger("1"),
            "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
            "data",
            HancockProtocolDlt.ethereum
    );
                
System.out.println(encode.getCode());
```

Output:
```bash
hancock://qr?code=%7B%22action%22%3A%22transfer%22%2C%22body%22%3A%7B%22value%22%3A%221%22%2C%22data%22%3A%22data%22%2C%22to%22%3A%220xde8e772f0350e992ddef81bf8f51d94a8ea9216d%22%7D%2C%22dlt%22%3A%22ethereum%22%7D
```

### Decode transaction with Hancock's protocol
```bash
String encode = "hancock://qr?code=%7B%22action%22%3A%22transfer%22%2C%22body%22%3A%7B%22value%22%3A%221%22%2C%22data%22%3A%22data%22%2C%22to%22%3A%220xde8e772f0350e992ddef81bf8f51d94a8ea9216d%22%7D%2C%22dlt%22%3A%22ethereum%22%7D"

HancockProtocolDecodeResponse decode = client.getProtocolClient()
    .decodeProtocol(encode);

System.out.println("Decode Dlt response -> " + decode.getDlt());
System.out.println("Decode Data response -> " + decode.getBodyData());
System.out.println("Decode To response -> " + decode.getTo());
System.out.println("Decode Action response -> " + decode.getAction());
System.out.println("Decode Value response -> " + decode.getValue());
```
Output:
```bash
Decode Dlt response -> ethereum
Decode Data response -> data
Decode To response -> 0xde8e772f0350e992ddef81bf8f51d94a8ea9216d
Decode Action response -> transfer
Decode Value response -> 1
```
