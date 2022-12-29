<template>
  <div class="my-transactions">
    <div class="card">
      <DataTable :value="myTransactions" style="height: 65vh" responsiveLayout="scroll" scrollable="true" scrollHeight="flex">
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
        <Column field="p2pTransactionStatus" header="Статус" :sortable="true" style="width:25%">
          <template #body="slotProps">
<!--            <span class="customer-badge ">{{slotProps.data.p2pTransactionStatus}}</span>-->
            <Badge :value="getStatus(slotProps.data.p2pTransactionStatus)" :severity="getStatusColor(slotProps.data.p2pTransactionStatus)" size="medium" class="status-badge"></Badge>
          </template>
        </Column>
      </DataTable>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import CryptoService from "@/services/crypto.service";

export default {
  name: "P2PMyTransactions",
  data(){
    return{
      myTransactions: []
    }
  },
  methods: {
    fetchMyP2P() {
      CryptoService.getAllMyP2P()
          .then((r) => {
            this.myTransactions = r.data
            this.myTransactions.sort((a,b)=>a.id-b.id)
          })
    },
    getStatus(p2pTransactionStatus){
      switch (p2pTransactionStatus){
        case "PARTNER_WAITING":
          return "Ожидание партнера"
        case "ADMIN_WAITING":
          return "Ожидание админа"
        case "APPROVED":
          return "Подтвержден"
        case "REJECTED":
          return "Отклонен"
      }
    },
    getStatusColor(p2pTransactionStatus){
      switch (p2pTransactionStatus){
        case "PARTNER_WAITING":
          return "warning"
        case "ADMIN_WAITING":
          return "warning"
        case "APPROVED":
          return "success"
        case "REJECTED":
          return "danger"
      }
    }
  },
  mounted() {
    this.fetchMyP2P()
  }
}
</script>


<style scoped>
.status-badge{
  white-space: nowrap;
}
</style>