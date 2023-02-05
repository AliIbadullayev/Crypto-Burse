<template>

  <div class="form-crypto-fiat">
    <form>
      <div class="inputs">
        <div class="field-1">
          <span class="p-float-label">
            <InputNumber id="inputnumber1" v-model="formCryptoFiat.amount" :maxFractionDigits="5" style="width: 30vw"/>
            <label for="inputnumber1">Количество фиата</label>
          </span>
        </div>
      </div>
      <h4>
        <!-- TODO instead of 100 must be actual crypto course -->
        Составляет в данной криптовалюте: {{getFloatToFixed(formCryptoFiat.amount / exchangeRate, 5)}}
      </h4>
      <div class="crypto-fiat-button">
        <Button label="Пополнить" icon="pi pi-check" @click="replenishCrypto()" />
      </div>
    </form>
  </div>
</template>

<script>
import CryptoService from '@/services/crypto.service'

export default {
  name: "WalletReplenish",
  data(){
    return {
      formCryptoFiat:{
        walletAddress: this.wallet.address,
        amount: null
      }
    }
  },
  props:{
    wallet: Object,
    exchangeRate: Number
  },
  methods:{
    replenishCrypto(){
      CryptoService.replenishCrypto(this.formCryptoFiat).then(
          (r) => {
            this.$emit('changed', true)
            this.$toast.add({severity:'success', summary: 'Пополнение', detail: "Успешно пополнено!", life: 3000});
          },
          (err) => {
            this.$emit('changed', false)
            this.$toast.add({severity:'error', summary: 'Пополнение', detail: err.response.data, life: 3000});
          }
      )
    },

  }
}
</script>

<style scoped>


.transaction .field{
  margin-top: 1.5rem;
}

.transaction small{
  margin-top: 0.25rem;
}

form{
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  justify-content: center;
}
.form-crypto-fiat{
  display: flex;
  flex-direction: column;
  height: 100%;
  align-items: center;
}





</style>