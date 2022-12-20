<template>
  <div class="exchange">
    <div class="select-block">
      <Card class="select-block-card">
        <template #content>
          <div class="crypto-one">
            <h3>Криптовалюта №1</h3>
            <Listbox v-model="wallet1" :options="wallets" :filter="true" optionLabel="crypto_name" listStyle="max-height:250px" style="width:15rem" filterPlaceholder="Search" @change="changeWallet1">
              <template #option="slotProps">
                <div class="crypto-item">
                  <div>{{slotProps.option.crypto_name}}</div>
                  <div>{{slotProps.option.amount.toFixed(5)}}</div>
                </div>
              </template>
            </Listbox>
          </div>
          <div class="rotate-icon">
            <font-awesome-icon icon="fa-solid fa-rotate" size="2x" style="color: #183153"/>
          </div>
          <div class="crypto-two">
            <h3>Криптовалюта №2</h3>
            <Listbox v-model="wallet2" :options="wallets" :filter="true"  optionLabel="crypto_name" listStyle="max-height:250px" style="width:15rem" filterPlaceholder="Search" @change="changeWallet2">
              <template #option="slotProps">
                <div class="crypto-item">
                  <div>{{slotProps.option.crypto_name}}</div>
                  <div>{{slotProps.option.amount.toFixed(5)}}</div>
                </div>
              </template>
            </Listbox>
          </div>
        </template>
      </Card>
    </div>
    <div class="exchange-block">
      <Card class="exchange-block-card">
        <template #content>
          <div class="field-1">
            <span class="p-float-label">
              <InputNumber id="inputcrypto1" v-model="exchangeForm.amount" :maxFractionDigits="5"  />
              <label for="inputcrypto1">Количество крипты №1 </label>
            </span>
          </div>
          <div class="arrow-down-icon">
            <font-awesome-icon icon="fa-solid fa-arrow-down" size="2x" style="color: #183153"/>
          </div>
          <div class="field-2">
<!--            <span class="p-float-label">-->
<!--              <InputNumber id="inputnumber" v-model="onAmountInput" :maxFractionDigits="5" disabled="disabled" />-->
              <small style="display: block">Количество крипты №2 </small>
<!--            </span>-->
            {{wallet1 != null && wallet2 != null? (exchangeForm.amount*getExchange(wallet1.crypto_name)/getExchange(wallet2.crypto_name)).toFixed(5): 0}}
          </div>
          <div class="exchange-button">
            <Button label="Обменять" icon="pi pi-check" @click="onExchange" />
          </div>
        </template>
      </Card>
    </div>
  </div>
</template>

<script>
import Nav from "@/components/Nav";
import axios from "axios";
export default {
  name: "Exchange",
  components: {Nav},
  data(){
    return{
      exchangeForm:{
        "walletToAddress": null,
        "walletFromAddress": null,
        "amount": null
      },

      amountCrypto1: 6.23,
      amountCrypto2: 1.98232,

      wallets: [],
      exchangeRates: [],
      wallet1: null,
      wallet2: null,
    }
  },
  methods: {
    onExchange(){
      axios.post('/api/v1/users/exchangeCrypto', this.exchangeForm)
          .then(() => {
            this.fetchWalletsAndExchanges()
          })
          .catch((err)=>{
            alert(err.response.data)
          })
    },
    async fetchWalletsAndExchanges(){
      const walletsResponse = await axios.get('/api/v1/users/getAllClientWallets')
      const exchangeRates = await axios.get('/api/v1/users/getCryptoExchangeRates')
      this.wallets = walletsResponse.data
      this.exchange = exchangeRates.data
    },
    changeWallet1(){
      this.exchangeForm.walletFromAddress = this.wallet1.address
    },
    changeWallet2(){
      this.exchangeForm.walletToAddress = this.wallet2.address
    },
    onAmountInput(){
      alert(this.exchangeForm.amount/this.getExchange(this.wallet1.crypto_name)*this.getExchange(this.wallet2.crypto_name))
       this.amountCrypto2 = this.exchangeForm.amount/this.getExchange(this.wallet1.crypto_name)*this.getExchange(this.wallet2.crypto_name)

      // this.exchangeRates[this.wallets.findIndex(x => x.crypto_name === this.wallet1.crypto_name)].exchange_rate
      // alert(this.exchange.find(x => x.name === crypto_name).exchange_rate)
    },
    getExchange(crypto_name){
      return this.exchange.find(x => x.name === crypto_name).exchange_rate
    }
  },
  mounted(){
    this.fetchWalletsAndExchanges();
  }
}
</script>

<style scoped>
.select-block-card, .exchange-block-card{
  height: 98vh;
  background: #FFFFFF;
  border-radius: 15px;
  margin-left: 0.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.select-block, .exchange-block{
  width: 100%;
}

.exchange{
  display:flex;
  width: 94%;
}

.nav-main-block::v-deep .p-card-content {
  display: flex;
  flex-direction: column;
  align-items: center;/* Rectangle 1 */
  height: 100%;
}

.nav-main-block::v-deep .p-card-body {
  height: 100%;
}

.crypto-item{
  display: flex;
  justify-content: space-between;
}

.rotate-icon{
  text-align: center;
  margin-top: 1rem;
}

h3{
  text-align: center;
}
.arrow-down-icon{
  margin: 1.5rem;
  margin-bottom: 2rem;
  text-align: center;
}
.exchange-button{
  margin-top: 2rem;
  text-align: center;
}

</style>