<template>
  <div class="exchange">
    <div class="select-block">
      <Card class="select-block-card">
        <template #content>
          <div class="crypto-one">
            <h3>Криптовалюта №1</h3>
            <Listbox v-model="exchangeForm.wallet1" :options="cryptos" :filter="true" optionLabel="name" listStyle="max-height:250px" style="width:15rem" filterPlaceholder="Search">
              <template #option="slotProps">
                <div class="crypto-item">
                  <div>{{slotProps.option.name}}</div>
                  <div>{{slotProps.option.value}}</div>
                </div>
              </template>
            </Listbox>
          </div>
          <div class="rotate-icon">
            <font-awesome-icon icon="fa-solid fa-rotate" size="2x" style="color: #183153"/>
          </div>
          <div class="crypto-two">
            <h3>Криптовалюта №2</h3>
            <Listbox v-model="exchangeForm.wallet2" :options="cryptos" :filter="true"  optionLabel="name" listStyle="max-height:250px" style="width:15rem" filterPlaceholder="Search">
              <template #option="slotProps">
                <div class="crypto-item">
                  <div>{{slotProps.option.name}}</div>
                  <div>{{slotProps.option.value}}</div>
                </div>
              </template>
            </Listbox>
          </div>
        </template>
      </Card>
    </div>
    <div class="exchange-block" @click="onClickNew($event)">
      <Card class="exchange-block-card">
        <template #content>
          <div class="field-1">
            <span class="p-float-label">
              <InputNumber id="inputcrypto1" v-model="exchangeForm.amount" :maxFractionDigits="5" />
              <label for="inputcrypto1">Количество крипты №1 </label>
            </span>
          </div>
          <div class="arrow-down-icon">
            <font-awesome-icon icon="fa-solid fa-arrow-down" size="2x" style="color: #183153"/>
          </div>
          <div class="field-2">
            <span class="p-float-label">
              <InputNumber id="inputnumber" v-model="amountCrypto2" :maxFractionDigits="5" disabled="true" />
              <label for="inputnumber">Количество крипты №2 </label>
            </span>
          </div>
          <div class="exchange-button">
            <Button label="Обменять" icon="pi pi-check" />
          </div>
        </template>
      </Card>
    </div>
  </div>
</template>

<script>
import Nav from "@/components/Nav";
export default {
  name: "Exchange",
  components: {Nav},
  data(){
    return{
      exchangeForm:{
        wallet1: null,
        wallet2: null,
        amount: null
      },

      amountCrypto1: 6.23,
      amountCrypto2: 1.98232,

      cryptos: [
        {name: 'Bitcoin', value: 2.843},
        {name: 'ShibaCoin', value: 1000129},
        {name: 'Ethereum', value: 10.383},
        {name: 'DentCoin', value: 1002},
        {name: 'LiteCoin', value: 0.837},
      ],
    }
  },
  methods: {
    onClickNew($event){
      this.exchangeForm.wallet1 = this.exchangeForm.wallet1.name
      alert(JSON.stringify((this.exchangeForm)))
    }
  }
}
</script>

<style scoped>
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