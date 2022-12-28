<template>
  <div class="p2p-offers">
    <div class="card">
      <DataTable :value="p2pOffers" responsiveLayout="scroll">
        <Column field="id" header="Id" :sortable="true"></Column>
        <Column field="walletOneAddress" header="Seller" :sortable="true"></Column>
        <Column field="cryptoName" header="Crypto" :sortable="true"></Column>
        <Column field="cryptoAmount" header="Amount" :sortable="true"></Column>
        <Column field="fiatAmount" header="Price" :sortable="true"></Column>
        <Column field="operationType" header="Тип" :sortable="true">
          <template #body="slotProps">
            {{ slotProps.data.operationType === "SELL_CRYPTO" ? "Продажа" : "Покупка" }}
          </template>
        </Column>
        <Column field="operationType" header="Операция" :sortable="true">
          <template #body="slotProps">
            <div class="buy-button" v-if="slotProps.data.operationType === 'SELL_CRYPTO'">
              <Button label="Купить" class="p-button-success" @click="respondToOffer(slotProps.data)" style="width: 100%"/>
            </div>
            <div class="sell-button" v-else>
              <Button label="Продать" class="p-button-danger" @click="respondToOffer(slotProps.data)" style="width: 100%"/>
            </div>
            <!--                {{ slotProps.data.operationType === "SELL_CRYPTO" ? "Продажа" : "Покупка" }}-->
          </template>
        </Column>
      </DataTable>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
import axios from 'axios'
import CryptoService from "@/services/crypto.service";


export default ({
  name: 'P2POffers',
  data() {
    return{
      p2pOffers: null,
    }
  },
  methods: {
    fetchP2POffers() {
      CryptoService.getAllOffers()
          .then((r) => {
            this.p2pOffers = r.data
          })
    },
    respondToOffer(data){
      CryptoService.respondToOffer({id : data.id })
          .then(() => {
            this.p2pOffers.splice(this.p2pOffers.findIndex(x => x.id === data.id), 1 )
          })
          .catch((err)=>{
            alert(err.response.data)
          })
    }
  },
  mounted() {
    this.fetchP2POffers()
  }
})
</script>

<style scoped>

</style>