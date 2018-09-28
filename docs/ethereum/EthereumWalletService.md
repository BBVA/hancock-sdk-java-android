## HancockEthereumWalletService

### Introduction

We can use this interface to do operations related with blockchain wallets

### Generate new wallet

```bash
EthereumWallet wallet = client.getWalletService().generateWallet();

System.out.println("Address -> " + wallet.getAddress());
System.out.println("PrivateKey -> " + wallet.getPrivateKey());
System.out.println("PublicKey -> " + wallet.getPublicKey());
```

Output:
```bash
Address -> 0x1319e2f5f0f54f3f41e111ccc1b70d9903277c9d
PrivateKey -> 0xa93e62b7689acd797e9b486597041c6616175577d52b7beb098877e98b4916f9
PublicKey -> 0xa84a6cb46ad29c898062b9b9ee570cff30fa3dc29e05c20500d6efeb5905969f7800a143832a1e868ecccf843f9e1cc9f0000dffde78f3432ca5fc412cfa5305
```

### Consult wallet balance

```bash
BigInteger balance = client.getWalletService()
    .getBalance("0x6c0a14f7561898b9ddc0c57652a53b2c6665443e");

System.out.println("Balance -> " + balance);
```

Output:
```bash
# Balance is given in weis
Balance -> 99996219999999999991
```