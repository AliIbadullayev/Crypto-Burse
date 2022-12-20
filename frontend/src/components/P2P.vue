<template>
  <div class="p2p">
    <Card class="custom-card p2p-card">
      <template #content>
        <div class="add-button">
          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" @click="openP2PAdd">
            <font-awesome-icon icon="fa-solid fa-circle-plus" size="3x" style="color: #183153"/>
          </Button>
        </div>
        <div class="p2p-main">
          <TabMenu :model="items" v-model:activeIndex="active"/>
          <router-view/>
        </div>
      </template>
    </Card>
  </div>
  <div class="dialog-p2p-transaction">
    <Dialog class="dialog-p2p-transaction-main" :closable="false" v-model:visible="this.p2pTransactionDialogVisible" :style="{width: '75vw'}"  :modal="true" :contentStyle="{height: '75vh', display: 'flex', 'flex-direction': 'column'}">
      <template #header>
        <div class="p2p-transaction-dialog-header">
          <div class="header-block">
            <h3>
              Добавление P2P транзакции
            </h3>
          </div>
          <div class="exit-button">
            <Button icon="pi pi-times" class="p-button-rounded p-button-danger p-button-text" @click="close"/>
          </div>
        </div>
      </template>
      <div class="card">
        <div class="available-resources">
          <div v-if="userData != null" class="available-fiat">
            <h4>Доступно фиата:</h4>{{userData.fiatBalance}}
          </div>
          <div v-if="walletOne.crypto_name != null" class="available-crypto">
            <h4>Доступно крипты:</h4>{{walletOne.amount}}
          </div>
        </div>
        <Dropdown  :options="wallets" v-model="walletOne" optionLabel="name" placeholder="Сеть блокчейна" style="width: 30vw" @change="changeWallet">
          <template #value="slotProps">
            <div v-if="walletOne.crypto_name != null" style="width: 30vw">
              <div>{{walletOne.crypto_name}}</div>
            </div>
            <span v-else>
              Крипто-кошелек
            </span>
          </template>
          <template #option="slotProps" style="width: 30vw">
            <div style="display: flex; justify-content: space-between">
              <div>{{slotProps.option.crypto_name}}</div>
              <div >
                {{slotProps.option.amount}}
              </div>
            </div>
          </template>
        </Dropdown>
        <Dropdown  :options="operationType" v-model="selectedType" optionLabel="name" placeholder="Тип операции" style="width: 30vw" @change="changeType">
          <template #value="slotProps">
            <div v-if="selectedType.name != null" style="width: 30vw">
              <div>{{selectedType.name}}</div>
            </div>
            <span v-else>
              Тип операции
            </span>
          </template>
          <template #option="slotProps" style="width: 30vw">
            <div>{{slotProps.option.name}}</div>
          </template>
        </Dropdown>
        <div class="field">
          <span class="p-float-label">
            <InputNumber id="inputnumber1" v-model="formP2PTransaction.fiatAmount" :maxFractionDigits="5" style="width: 30vw"/>
            <label for="inputnumber1">Количество фиата</label>
          </span>
        </div>
        <div class="field">
          <span class="p-float-label">
            <InputNumber id="inputnumber2" v-model="formP2PTransaction.cryptoAmount" :maxFractionDigits="5" style="width: 30vw"/>
            <label for="inputnumber2">Количество крипты</label>
          </span>
        </div>
        <div class="p2p-transaction-button">
          <Button label="Разместить предложение" icon="pi pi-check" @click="postP2PTransaction"/>
        </div>
      </div>
    </Dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "P2P",
  data(){
    return{
      active: 0,
      items: [
        {
          label: 'Предложения',
          icon: 'pi pi-fw pi-cart-plus',
          to: '/main/p2p/offers'
        },
        {
          label: 'Мои сделки',
          icon: 'pi pi-fw pi-user',
          to: '/main/p2p/my-transactions'
        }
      ],
      p2pTransactionDialogVisible: false,
      formP2PTransaction: {
        "walletOneAddress":null,
        "cryptoName":null,
        "cryptoAmount":null,
        "fiatAmount":null,
        "operationType":""
      },
      wallets: null,
      walletOne:{
        "address": null,
        "crypto_name": null,
        "amount": null
      },

      selectedType:{
        name: null,
        value: null
      },
      userData: null,
      operationType: [
        {name:"Покупка", value: "BUY_CRYPTO"},
        {name:"Продажа", value: "SELL_CRYPTO"},
      ],

    }
  },
  methods: {
    async openP2PAdd(){
      this.p2pTransactionDialogVisible = true;
      const userResponse = await axios.get('/api/v1/users/getClientInfo')
      const walletsResponse = await axios.get('/api/v1/users/getAllClientWallets')
      this.userData = userResponse.data
      this.wallets = walletsResponse.data
    },
    close(){
      this.p2pTransactionDialogVisible = false;
    },
    changeWallet(){
      this.formP2PTransaction.cryptoName = this.walletOne.crypto_name
      this.formP2PTransaction.walletOneAddress = this.walletOne.address
      alert(JSON.stringify(this.formP2PTransaction))
    },
    changeType(){
      this.formP2PTransaction.operationType = this.selectedType.value
    },
    postP2PTransaction(){
      axios.post('/api/v1/users/postOffer', this.formP2PTransaction)
          .then(() => {

          })
          .catch((err)=>{
            alert(err.response.data)
          })
    }
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
.add-button{
  text-align: center;
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