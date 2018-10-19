## HancockEthereumSmartContractService

### Introduction

We can use this interface to manage operations related with Ethereum smart contracts over blockchain

### Smart contract invoke

```bash
TransactionConfig txconfig = new TransactionConfig.Builder()
    .withPrivateKey("0xd06026d5b8664036bdec0a924b8c7360566e678a2291e9440156365b040a7b83")
    .build();
      
ArrayList<String> params = new ArrayList();
params.add("0x6c0a14f7561898b9ddc0c57652a53b2c6665443e");

EthereumTransactionResponse invokeResponse = client.getSmartContractService().invoke(
    "token-contract-alias",
    "balanceOf", 
    params, 
    "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e", 
    txconfig
);

System.out.println("success: " + invokeResponse.getSuccess());
```

Output:
```bash
success: true
```

### Subscribe to smart contract events
```bash
ArrayList<String> contracts = new ArrayList<>();
contracts.add("0x547aaccaef70a7aef5a44fa173f46e9ea07bdce7");

HancockSocket socket = client.getSmartContractService().subscribe(contracts);

Function logsFunc = o -> {
    System.out.println(o);
    return null;
};

socket.on("log", logsFunc);
```
    
