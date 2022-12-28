import moment from "moment";

export const Mixin = {
    data() {
        return {
            title: 'Mixins are cool',
            copyright: 'All rights reserved. Product of super awesome people'
        };
    },
    methods: {
        getFloatToFixed(string, pos){
            return parseFloat(string.toFixed(pos))
        },
        getExchange(exchangeRates, crypto_name){
            return exchangeRates.find(x => x.name === crypto_name).exchange_rate
        },
        getYearsPostfix(years){
            return years === 1 ? 'год': years > 1 && years <=4 ? 'года': 'лет'
        },
        getDateTime(timestamp){
            return moment(String(timestamp)).format('MMMM Do YYYY, HH:mm:ss')
        },
        getCryptoNameByAddress(wallets, walletAddress){
            if (this.wallets !== null) {
                return wallets.find(x => x.address === walletAddress).crypto_name
            }else {
                return ""
            }
        }

    }
};