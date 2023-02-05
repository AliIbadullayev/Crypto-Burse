<template>
  <DataTable :value="transactions" style="height: 70vh" responsiveLayout="scroll" scrollable="true" scrollHeight="flex">
    <Column field="walletFromAddress" header="Крипта" :sortable="true">
      <template #body="slotProps">
        {{getCryptoNameByAddress(wallets, slotProps.data.walletFromAddress)}}
      </template>
    </Column>
    <Column field="walletFromAddress" header="Адрес отправителя" :sortable="true"></Column>
    <Column field="walletToAddress" header="Адрес получателя" :sortable="true"></Column>
    <Column field="blockchainNetworkName" header="Блокчейн сеть" :sortable="true"></Column>
    <Column field="amount" header="Цена" :sortable="true"></Column>
    <Column field="timestamp" header="Время" :sortable="true">
      <template #body="slotProps">
        {{ getDateTime(slotProps.data.timestamp) }}
      </template>
    </Column>
  </DataTable>
</template>

<script>
import CryptoService from '@/services/crypto.service'

export default {
  name: "WalletHistoryTransactions",
  data(){
    return{
      transactions: null,
      wallets: null,
    }
  },
  methods:{
    fetchHistoryAndWallets(){
      CryptoService.getWallets().then(
          r => {
            this.wallets = r.data
          }
      )
      CryptoService.getClientTransactions().then(
          r => {
            this.transactions = r.data
          }
      )
    },
    getCryptoName(address){
      if (this.wallets !== null){
        return this.getCryptoNameByAddress(this.wallets, address)
      }else {
        return ""
      }
    }
  },
  mounted() {
    this.fetchHistoryAndWallets()
    }
}
</script>

<style scoped>

</style>