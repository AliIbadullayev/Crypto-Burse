# Crypto-Burse
Service that interact with crypto
![Alt Text](assets/preview.gif)
```
◦ Service secured using JWT token.
◦ Normalized database structure.
◦ Optimized code ieararchy according to SOLID coding principles
◦ UI/UX models and frontend app for implementing business processes.
```
In `assets/openapi3_0.yaml` you can find documentation for SwaggerEditor.\
Also `assets/Crypto Burse.postman_collection.json` you can find documentation for Postman.\

## Запросы к серверу
### 1)Регистрация клиента
requestType : POST   
url : http://localhost:8080/api/v1/auth/register
request :  
{
    "username": "ali",
    "password": "test",
    "name":"Alibaba",
    "surname":"Ibadulaev"
}
### 2)Авторизация пользователя
requestType : POST   
url : http://localhost:8080/api/v1/auth/login
request :  
{
    "username": "lwbeamer",
    "password": "test"
}
### 3)Отправка криптовалюты другому клиенту (транзакция)
requestType : POST   
url : http://localhost:8080/api/v1/users/sendCrypto
header : Authorization Bearer + token  
request :  
{
    "walletToAddress":"HJCwkR80Q8Mbwu9FV339",
    "walletFromAddress":"XJRpzUpg7VMRxAom2Xop",
    "blockchainNetworkName":"Bitcoin_network",
    "amount":"10"
}
### 4)Обмен криптовалюты внутри своих кошельков (exchange)
requestType : POST  
url : http://localhost:8080/api/v1/users/exchangeCrypto
header : Authorization Bearer + token  
request :  
{
    "walletToAddress":"QwhqGiGJt9t5C6WOjMFh",
    "walletFromAddress":"HJCwkR80Q8Mbwu9FV339",
    "amount":"88"
}
### 5)Добавление банковской карты пользователю
requestType : POST  
url : http://localhost:8080/api/v1/users/addCard
header : Authorization Bearer + token  
{
    "cardNumber": "1234123412343328",
    "nameOnCard": "Ali Ibadullaev",
    "expireDate": "12/23",
    "cvv": "666"
}
### 6)Покупка крипты за фиатные деньги
requestType : POST  
url : http://localhost:8080/api/v1/users/fiatToCrypto
header : Authorization Bearer + token  
request :  
{
    "walletAddress":"XJRpzUpg7VMRxAom2Xop",
    "amount":"2900"
}
### 7)Пополнение фиатного счёта
requestType : POST  
url : http://localhost:8080/api/v1/users/depositFiat
header : Authorization Bearer + token  
request :  
{
    "amount": 100000
}
### 7)Оценить NFT (поставить лайк или дизлайк). True - лайк. False - дизлайк
requestType : POST  
url : http://localhost:8080/api/v1/users/scoreNft
header : Authorization Bearer + token  
request :  
{
    "nftId":"4",
    "isLiked":false
}
### 8)Получить информацию о клиенте
requestType : GET 
url : http://localhost:8080/api/v1/users/getClientInfo
header : Authorization Bearer + token  

### 9)Получить все кошельки текущего авторизированного пользователя
requestType : GET
url : http://localhost:8080/api/v1/users/getAllWallets
header : Authorization Bearer + token  

### 10)Получить все кошельки текущего авторизированного пользователя
requestType : GET
url : http://localhost:8080/api/v1/users/getAllClientWallets
header : Authorization Bearer + token  

### 11)Получить все банковские карты текущего авторизированного пользователя
requestType : GET
url : http://localhost:8080/api/v1/users/getAllClientBankCards
header : Authorization Bearer + token  

### 12)Получить все NFT текущего авторизированного пользователя
requestType : GET
url : http://localhost:8080/api/v1/users/getAllClientNfts
header : Authorization Bearer + token  

### 13)Получить все NFT (для маркетплейса, доступно авторизированным юзерам только)
requestType : GET
url : http://localhost:8080/api/v1/users/getAllNfts
header : Authorization Bearer + token  

### 14)Получить все транзакции текущего авторизированного пользователя (transaction)
requestType : GET
url : http://localhost:8080/api/v1/users/getClientTransactions
header : Authorization Bearer + token  

### 15)Получить все обмены текущего авторизированного пользователя (crypto_exchange) 
requestType : GET
url : http://localhost:8080/api/v1/users/getClientExchangesnsactions
header : Authorization Bearer + token  

### 16)Получить все покупки крипты за фиат текущего авторизированного пользователя (fiat_to_crypto) 
requestType : GET
url : http://localhost:8080/api/v1/users/getClientFiatToCryptos
header : Authorization Bearer + token  





 


