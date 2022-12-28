<template>
  <Toast/>
  <div class="p2p">
    <Card class="custom-card p2p-card">
      <template #content>
        <div class="p2p-main">
          <div class="p2p-client">
            <div class="card">
              <DataTable :value="p2ps" style="height: 90vh" responsiveLayout="scroll" scrollable="true" scrollHeight="flex">
                <Column field="id" header="Id" :sortable="true"></Column>
<!--                <Column field="walletOneAddress" header="Seller" :sortable="true"></Column>-->
<!--                <Column field="walletTwoAddress" header="Seller" :sortable="true"></Column>-->
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
                    <div class="buttons">
                      <div class="approve-button" >
                        <Button label="Подтвердить" class="p-button-success" @click="makeDecision(slotProps.data, true)" style="width: 100%"/>
                      </div>
                      <div class="not-approve-button" >
                        <Button label="Отклонить" class="p-button-danger" @click="makeDecision(slotProps.data, false)" style="width: 100%"/>
                      </div>
                    </div>
                  </template>
                </Column>
              </DataTable>
            </div>
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>

<script>

import CryptoService from "@/services/crypto.service";

export default {
  name: "Admin",
  data() {
    return{
      p2ps: null,
    }
  },
  methods: {
    fetchP2P() {
      CryptoService.getAllTransactionsToCheck()
          .then((r) => {
            this.p2ps = r.data
          })
    },
    makeDecision(data, isApproved){
      CryptoService.makeDecision({
        p2pTransactionId: data.id,
        isApproved: isApproved
      })
          .then(() => {
            this.p2ps.splice(this.p2ps.findIndex(x => x.id === data.id), 1 )
            this.$toast.add({severity:'success', summary: 'Панель администратора', detail: "Сделка успешно оценена!", life: 3000});
          })
          .catch((err)=>{
            alert(err.response.data)
          })
    }
  },
  mounted() {
    this.fetchP2P()
  }
}
</script>

<style scoped>
.p2p-card {
  border-radius: 15px;
}
.p2p{
  width: 94%;
}
.buttons{
  display : flex;
  justify-content: space-between;
}

.buttons .approve-button{
  margin-right: 1.5rem;
}

.card{
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 1rem;
}
.card h4 {
  margin: 0 2rem 0 0;
}

.card > *{
  margin-bottom: 1.5rem;
}

.available-resources > div{
  margin-bottom: 1rem;
  display: flex;
}



.p2p-transaction-dialog-header{
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: space-between;
}

.header-block{
  display: flex;
  align-items: center;
}

.header-block h3 {
  margin: 0;
}

</style>