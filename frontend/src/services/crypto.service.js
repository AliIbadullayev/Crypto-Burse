import moment from "moment";
import axios from "axios";
import authHeader from "@/services/auth-header";


class CryptoService{
    addBankCard(card){
        card.expireDate = moment(String(card.expireDate)).format('MM/YY')
        return axios.post('/api/v1/users/addCard', card, {headers: authHeader()})
    }
    getProfileInfo(){
        return axios.get('/api/v1/users/getClientInfo', {headers: authHeader()})
    }
    replenishFiat(replenishForm) {
        return axios.post('/api/v1/users/depositFiat', replenishForm, {headers: authHeader()})
    }
    getBankCards(){
        return axios.get('/api/v1/users/getAllClientBankCards', {headers: authHeader()})
    }
    getWallets(){
        return axios.get('/api/v1/users/getAllClientWallets', {headers: authHeader()})
    }
    getExchangeRates(){
        return axios.get('/api/v1/users/getCryptoExchangeRates', {headers: authHeader()})
    }
    exchangeCryptos(exchangeForm){
        return axios.post('/api/v1/users/exchangeCrypto', exchangeForm, {headers: authHeader()})
    }
    replenishCrypto(replenishForm){
        return axios.post('/api/v1/users/fiatToCrypto', replenishForm, {headers: authHeader()})
    }
    sendCrypto(sendCryptoForm){
        return axios.post('/api/v1/users/sendCrypto', sendCryptoForm, {headers: authHeader()})
    }
    getStaking(wallet){
        return axios.get('/api/v1/users/getStackingByWallet',  {headers: authHeader(), params: wallet})
    }
    toStake(stakeForm){
        return axios.post('/api/v1/users/toStake', stakeForm, {headers: authHeader()})
    }
    freeStake(wallet){
        return axios.post('/api/v1/users/freeStake', wallet, {headers: authHeader()})
    }
    getClientWallet(wallet){
        return axios.get('/api/v1/users/getClientWallet',  {headers: authHeader(), params: wallet})
    }
    getBlockchainNetworks(){
        return axios.get('/api/v1/users/getBlockchainNetworks',  {headers: authHeader()})
    }
    getClientTransactions(){
        return axios.get('/api/v1/users/getClientTransactions',  {headers: authHeader()})
    }
    getClientExchanges(){
        return axios.get('/api/v1/users/getClientExchanges',  {headers: authHeader()})
    }
    getClientFiatToCryptos(){
        return axios.get('/api/v1/users/getClientFiatToCryptos',  {headers: authHeader()})
    }
    postOffer(formP2PTransaction){
        return axios.post('/api/v1/users/postOffer', formP2PTransaction,  {headers: authHeader()})
    }
    respondToOffer(toSend){
        return axios.post('/api/v1/users/respondToOffer', toSend, {headers: authHeader()})
    }
    getAllOffers(){
        return axios.get('/api/v1/users/getAllOffers', {headers: authHeader()})
    }
    getAllMyP2P(){
        return axios.get('/api/v1/users/getAllMyP2P', {headers: authHeader()})
    }
    buyNft(toSend){
        return axios.post('/api/v1/users/buyNft', toSend, {headers: authHeader()})
    }
    scoreNft(toSend){
        return axios.post('/api/v1/users/scoreNft', toSend, {headers: authHeader()})
    }
    getAllNfts(){
        return axios.get('/api/v1/users/getAllNfts', {headers: authHeader()})
    }
    getAllClientNfts(){
        return axios.get('/api/v1/users/getAllClientNfts', {headers: authHeader()})
    }
    sellNft(toSend){
        return axios.post('/api/v1/users/sellNft', toSend, {headers: authHeader()})
    }
    returnNft(toSend){
        return axios.post('/api/v1/users/returnNft', toSend, {headers: authHeader()})
    }
    getAllTransactionsToCheck(){
        return axios.get('/api/v1/admin/getAllTransactionsToCheck', {headers: authHeader()})
    }
    makeDecision(formP2P){
        return axios.post('/api/v1/admin/makeDecision', formP2P,  {headers: authHeader()})
    }

}

export default new CryptoService()