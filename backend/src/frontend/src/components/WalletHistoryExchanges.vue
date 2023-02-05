<template>
  <DataTable :value="exchanges" style="height: 70vh" responsiveLayout="scroll" scrollable="true" scrollHeight="flex">
    <Column field="walletFromAddress" header="Крипта 1" :sortable="true">
      <template #body="slotProps">
        {{ getCryptoNameByAddress(wallets, slotProps.data.walletFromAddress)}}
      </template>
    </Column>
    <Column field="walletToAddress" header="Крипта 2" :sortable="true">
      <template #body="slotProps">
        {{ getCryptoNameByAddress(wallets, slotProps.data.walletToAddress)}}
      </template>
    </Column>
    <Column field="walletFromAddress" header="Адрес 1" :sortable="true"></Column>
    <Column field="walletToAddress" header="Адрес 2" :sortable="true"></Column>
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
  name: "WalletHistoryExchanges",
  data(){
    return{
      exchanges: null,
      wallets: null
    }
  },
  methods:{
    fetchHistoryAndWallets(){
      CryptoService.getClientExchanges().then(
          r => {
            this.exchanges = r.data
          }
      )
      CryptoService.getWallets().then(
          r => {
            this.wallets = r.data
          }
      )
    }
  },
  mounted() {
    this.fetchHistoryAndWallets()
  }

}
</script>

<style scoped>

</style>