<a href="https://casino-wallet.herokuapp.com/swagger-ui.html#/"><img src="https://nyspins.casino/wp-content/uploads/sites/131/2019/07/22ff69cc7a.png" title="CasinoWallet" alt="CasinoWallet"></a>

# Wallet

> This project contains a brief POC for a Casino Wallet.


[![Build Status](http://img.shields.io/travis/badges/badgerbadgerbadger.svg?style=flat-square)](https://travis-ci.org/badges/badgerbadgerbadger)  [![Coverage Status](http://img.shields.io/coveralls/badges/badgerbadgerbadger.svg?style=flat-square)](https://coveralls.io/r/badges/badgerbadgerbadger)

---

## Table of Contents


- [Installation](#installation)
- [Features](#features)
- [People](#people)
- [FAQ](#faq)
- [Support](#support)


---

## Installation

- To run this application you spring-boot 2.3.1 and Java 14 is required.

### Clone

- Clone this repo to your local machine using `https://github.com/Fredrik-Hall/Wallet.git`

### Setup

- To run using spring-boot

> Navigate to project root folder where the pom.xml file is located.
>Then run the following command.

```shell
$ mvn spring-boot:run
```

> The application should now be running and waiting for traffic at localhost:8080.

---

## Features
- The application is automatically deployed and hosted at [Heroku](https://www.heroku.com/) when changes are merged into master.
    - The application is reachable here [https://casino-wallet.herokuapp.com/swagger-ui.html#](https://casino-wallet.herokuapp.com/swagger-ui.html#)
- 
## Usage
- All endpoints in application use basic authentication.
    - User `walletuser` and Password `password123` must be used with basic Auth.
    -  `Authorization: Basic d2FsbGV0dXNlcjpwYXNzd29yZDEyMw==`
    
## Documentation

### How to use

- First register a user with a call to `RegisterUser`(/v1/player).

- Update the account balance using `UpdateAccount` with the PlayerId returned in the `RegisterUser` call (/v1/account/{playerId}).

- Check your balance with `GetAccount` (/v1/account/{playerId}).

- Start sending Bet `Withdraw` and Win `Deposit` transactions to alter the players balance (/v1/transaction/withdraw/{playerId}) (/v1/transaction/deposit/{playerId}).

- The transactions are Idempotent, if you send the same transaction more than once it will not be processed.

- It is possible to `Cancel` any existing `Withdraw` transaction (/v1/transaction/cancel/{playerId}).

- If a `Cancel` is sent in for a non existing transaction it will be inserted as Cancelled to prevent issues with packages being delvered out of order.

- It's possible to get the transaction history based on  different criteria.

- All transactions `GetTransactions` (/v1/transaction/history).

- All transactions for a specific player `GetTransactions` (/v1/transaction/history/player/{playerId}).

- All transactions in a specific round `GetTransactionsByRound` (/v1/transaction/history/round/{roundId}).

- A specific transaction `GetTransaction` (/v1/transaction/history/transaction/{transactionId}).

#### Additional calls

- `GetPlayers` (/v1/player)
- `GetPlayer` (/v1/player/{playerId})
- `UpdatePlayer` (/v1/player/{playerId})
- `DeletePlayer` (/v1/player/{playerId})

## Tests

- All Controllers and Services covered by unit tests.
- The tests are automatically ran when pushing changes to GitHub. 
---

## People

| <a href="https://github.com/Fredrik-Hall" target="_blank">**Fredrik Hall**</a> |
| :---: |
| [![Fredrik Hall](https://avatars2.githubusercontent.com/u/67291305?s=460&u=dc7d0ab164ad93fb065b96e6141d5dbc1283bfd2&v=4s=200)](http://.com)   |
| <a href="https://github.com/Fredrik-Hall" target="_blank">`github.com/Fredrik-Hall`</a> |

---

## FAQ

- **How do I do *specifically* so and so?**
    - No problem! Just do this.

---

## Support

I can be reached here

- <a href="https://www.linkedin.com/in/fredrik-hall-a85b43155/" target="_blank">`LinkedIn`</a>

