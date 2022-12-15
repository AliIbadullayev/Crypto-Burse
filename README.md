# Crypto-Burse
Service that interact with crypto

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
url : http://localhost:8080/api/v1/users/getAllBankCards
header : Authorization Bearer + token  

 


