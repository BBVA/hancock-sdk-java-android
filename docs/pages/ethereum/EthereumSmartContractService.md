---
pageId: 'EthereumSmartContractService'
title: 'SmartContract Service'
location: 'EthereumSmartContractService'
parent: 'location=Ethereum'
---

## EthereumSmartContractService

### Introduction

We can use this interface to manage operations related with Ethereum smart contracts over blockchain

### Smart contract deployment

```javascript
  TransactionConfig txconfig = new TransactionConfig.Builder()
      .withPrivateKey("0xd06026d5b8664036bdec0a924b8c7360566e678a2291e9440156365b040a7b83")
      .build();
        
  List<String> params = new ArrayList<>();
          params.add("1000");
          params.add("HancockTest1");
          params.add("18");
          params.add("HT1");
  
  final Future<HancockSocketTransactionBody> response = ethereumSmartContractService
  .deploy("0x6c0a14f7561898b9ddc0c57652a53b2c6665443e", "https://s3-eu-west-1.amazonaws.com/ethereum/eip20/EIP20", "EIP20", params, txConfig);
          
  
  System.out.println(response.get());

```

Console output:
```bash
{
  blockHash: '0x4c5dae42c9ea1b90912b6f7128a9d1213d14f70ca12b06f417b4d519b8cfe543';
  blockNumber: 4460598;
  transactionId: '0x241dbc176195e58c7505812e7c9a04bd83c5dd59b0684e39911f88bedce5c6bf';
  from: '0x8a37b79e54d69e833d79cac3647c877ef72830e1';
  to: null;
  value: IHancockSocketCurrency;
  data: '';
  fee: IHancockSocketCurrency;
  newContractAddress: '0x54a298ee9fccbf0ad8e55bc641d3086b81a48c41';
  timestamp: 1559038793;
}
```

### Smart contract invoke

```bash
TransactionConfig txconfig = new TransactionConfig.Builder()
    .withPrivateKey("0xd06026d5b8664036bdec0a924b8c7360566e678a2291e9440156365b040a7b83")
    .build();
      
ArrayList<String> params = new ArrayList();
params.add("0x6c0a14f7561898b9ddc0c57652a53b2c6665443e");

EthereumTransactionResponse invokeResponse = ethereumSmartContractService.invoke(
    "token-contract-alias",
    "balanceOf", 
    params, 
    "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e", 
    txconfig
);

System.out.println("success: " + invokeResponse.getSuccess());
```

Console output:
```bash
success: true
```

### Smart contract register

```bash
TransactionConfig txconfig = new TransactionConfig.Builder()
    .withPrivateKey("0xd06026d5b8664036bdec0a924b8c7360566e678a2291e9440156365b040a7b83")
    .build();
      
ArrayList<String> abiDefinitions = new ArrayList();
params.add(new AbiDefinition());

EthereumRegisterResponse invokeResponse = ethereumSmartContractService.register(
"MyAlias", 
"0x6c0a14f7561898b9ddc0c57652a53b2c6665443e", 
abiDefinitions)
System.out.println("success: " + invokeResponse.getSuccess());
```

Console output:
```bash
success: true
```

### Subscribe to smart contract events
```bash
ArrayList<String> contracts = new ArrayList<>();
contracts.add("0x547aaccaef70a7aef5a44fa173f46e9ea07bdce7");

HancockSocket socket = ethereumSmartContractService.subscribeToEvents(contracts);

Function logsFunc = o -> {
    System.out.println(o);
    return null;
};

socket.on("contract-event", logsFunc);
```
    
### Subscribe to smart contract deployment

```bash
ArrayList<String> contracts = new ArrayList<>();
contracts.add("0x547aaccaef70a7aef5a44fa173f46e9ea07bdce7");

Function readyFunc = o -> {
    System.out.println(o);
    return null;
};
HancockSocket socket = ethereumSmartContractService.subscribeToContractsDeployments(contracts, "consumer", readyFunc);

Function logsFunc = o -> {
    System.out.println(o);
    return null;
};

socket.on("contract-deployment", logsFunc);
```

### Subscribe to smart contract transactions

```bash
ArrayList<String> contracts = new ArrayList<>();
contracts.add("0x547aaccaef70a7aef5a44fa173f46e9ea07bdce7");

HancockSocket socket = ethereumSmartContractService.subscribeToTransactions(contracts);

Function logsFunc = o -> {
    System.out.println(o);
    return null;
};

socket.on("transaction", logsFunc);
```
