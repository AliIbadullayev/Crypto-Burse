<template>
  <form class="transaction">
    <div class="field-1 field">
      <span class="p-float-label">
        <InputNumber id="amount" v-model="formTransaction.amount" :maxFractionDigits="5" style="width: 30vw"/>
        <label for="amount">Количество крипты</label>
      </span>
    </div>
    <div class="field-2 field">
      <span class="p-float-label">
        <InputText id="addr" type="text" v-model="formTransaction.walletToAddress" style="width: 30vw" />
        <label for="addr">Адрес получателя</label>
      </span>
    </div>
    <div class="field-3 field">
      <Dropdown  :options="blockchains" v-model="selectedBlockchain" optionLabel="name" placeholder="Сеть блокчейна" style="width: 30vw">
        <template #value="slotProps">
          <div class="country-item country-item-value" v-if="selectedBlockchain != null" style="width: 30vw">
            <div>{{slotProps.value.name}}</div>
          </div>
          <span v-else>
            Сеть блокчейна
          </span>
        </template>
        <template #option="slotProps" style="width: 30vw">
          <div class="country-item" style="display: flex; justify-content: space-between">
            <div>{{slotProps.option.name}}</div>
            <div >
              {{slotProps.option.fee}}/{{slotProps.option.leadTime}}
            </div>
          </div>
        </template>
      </Dropdown>
    </div>
    <small v-if="selectedBlockchain != null" style="display: block">
      Комиссия - {{selectedBlockchain.fee}}%, Время перевода - {{selectedBlockchain.leadTime}} сек.
    </small>
    <div class="transaction-button" style="margin-top: 1rem;">
      <Button label="Перевести" icon="pi pi-check" @click="sendCrypto()"/>
    </div>
  </form>
</template>

<script>
import CryptoService from '@/services/crypto.service'

export default {
  name: "WalletTransaction",
  data(){
    return{
      blockchains: null,
      formTransaction: {
        walletFromAddress: this.wallet.address,
        walletToAddress: null,
        blockchainNetworkName: null,
        amount: null
      },
      selectedBlockchain: null
    }
  },
  methods:{
    fetchBN(){
      CryptoService.getBlockchainNetworks().then(
          r => {
            this.blockchains = r.data
          }
      )
    },
    sendCrypto(){
      this.formTransaction.blockchainNetworkName = this.selectedBlockchain.name
      CryptoService.sendCrypto(this.formTransaction).then(
          (r) => {
            this.$emit('changed', true)
          },
          (err) => {
            this.$emit('changed', false)
            alert(err.response.data)
          }
      )
    }
  },
  mounted() {
    this.fetchBN()
  },
  props:{
    wallet: Object,
  }
}
</script>

<style scoped>

form{
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  justify-content: center;
}

.transaction .field{
  margin-top: 1.5rem;
}

.transaction small{
  margin-top: 0.25rem;
}
</style>