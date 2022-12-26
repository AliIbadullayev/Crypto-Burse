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

    }
};