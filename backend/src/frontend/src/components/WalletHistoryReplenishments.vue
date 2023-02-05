<template>
  <DataTable :value="replenishments" style="height: 70vh" responsiveLayout="scroll" scrollable="true" scrollHeight="flex">
    <Column field="walletAddress" header="Крипта 1" :sortable="true">
      <template #body="slotProps">
        {{ getCryptoNameByAddress(wallets, slotProps.data.walletAddress)}}
      </template>
    </Column>
    <Column field="walletAddress" header="Адрес " :sortable="true"></Column>
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
  name: "WalletHistoryReplenishments",
  data(){
    return{
      replenishments: null,
      wallets: null
    }
  },
  methods:{
    fetchHistoryAndWallets(){
      CryptoService.getClientFiatToCryptos().then(
          r => {
            this.replenishments = r.data
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