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

}

export default new CryptoService()