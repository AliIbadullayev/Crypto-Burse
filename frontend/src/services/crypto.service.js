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
        return axios.get('/api/v1/users/getStackingByWallet',  {headers: authHeader(), data: wallet})
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

}

export default new CryptoService()