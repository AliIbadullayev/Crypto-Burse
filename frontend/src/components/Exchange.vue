<template>
  <Toast/>
  <div class="exchange">
    <div class="select-block">
      <Card class="select-block-card">
        <template #content>
          <div class="crypto-one">
            <h3>Криптовалюта №1</h3>
            <Listbox v-model="wallet1" :options="wallets" :filter="true" optionLabel="crypto_name" listStyle="max-height:25vh" style="width:15rem" filterPlaceholder="Search" @change="changeWallet1">
              <template #option="slotProps">
                <div class="crypto-item">
                  <div>{{slotProps.option.crypto_name}}</div>
                  <div>{{parseFloat(slotProps.option.amount.toFixed(5))}}</div>
                </div>
              </template>
            </Listbox>
          </div>
          <div class="rotate-icon">
            <font-awesome-icon icon="fa-solid fa-rotate" size="2x" style="color: #183153"/>
          </div>
          <div class="crypto-two">
            <h3>Криптовалюта №2</h3>
            <Listbox v-model="wallet2" :options="wallets" :filter="true"  optionLabel="crypto_name" listStyle="max-height:25vh" style="width:15rem" filterPlaceholder="Search" @change="changeWallet2">
              <template #option="slotProps">
                <div class="crypto-item">
                  <div>{{slotProps.option.crypto_name}}</div>
                  <div>{{parseFloat(slotProps.option.amount.toFixed(5))}}</div>
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
            {{ onAmountInput() }}
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
import CryptoService from '@/services/crypto.service'
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
      CryptoService.exchangeCryptos(this.exchangeForm)
          .then(() => {
            this.fetchWalletsAndExchanges()
            this.$toast.add({severity:'success', summary: 'Обмен', detail: "Успешно обменено!", life: 3000});
          })
          .catch((err)=>{
            this.$toast.add({severity:'error', summary: 'Обмен', detail: err.response.data, life: 3000});
          })
    },
    fetchWalletsAndExchanges(){
      CryptoService.getWallets().then(
          r => {
            this.wallets = r.data
          }
      )
      CryptoService.getExchangeRates().then(
          r => {
            this.exchangeRates = r.data
          }
      )
    },
    changeWallet1(){
      this.exchangeForm.walletFromAddress = this.wallet1.address
    },
    changeWallet2(){
      this.exchangeForm.walletToAddress = this.wallet2.address
    },
    onAmountInput(){
      return this.wallet1 != null && this.wallet2 != null? parseFloat((this.exchangeForm.amount*this.getExchange(this.exchangeRates, this.wallet1.crypto_name)/this.getExchange(this.exchangeRates, this.wallet2.crypto_name)).toFixed(5)): 0
    },
  },
  mounted(){
    this.fetchWalletsAndExchanges();
  }
}
</script>

<style scoped>
.field-2{
  text-align: center;
}
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