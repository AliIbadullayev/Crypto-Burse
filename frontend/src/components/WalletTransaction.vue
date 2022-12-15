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
                          <InputText id="addr" type="text" v-model="formTransaction.wallet2" style="width: 30vw" />
                          <label for="addr">Адрес получателя</label>
                        </span>
    </div>
    <div class="field-3 field">
      <Dropdown  :options="blockchains" v-model="formTransaction.blockchain_network " optionLabel="name" placeholder="Сеть блокчейна" style="width: 30vw">
        <template #value="slotProps">
          <div class="country-item country-item-value" v-if="formTransaction.blockchain_network.name != null" style="width: 30vw">
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
              {{slotProps.option.fee}}/{{slotProps.option.lead_time}}
            </div>
          </div>
        </template>
      </Dropdown>
    </div>
    <small v-if="formTransaction.blockchain_network.name != null" style="display: block">
      Комиссия - {{formTransaction.blockchain_network.fee}}%, Время перевода - {{formTransaction.blockchain_network.lead_time}} сек.
    </small>
    <div class="transaction-button" style="margin-top: 1rem;">
      <Button label="Перевести" icon="pi pi-check" />
    </div>
  </form>
</template>

<script>
export default {
  name: "WalletTransaction",
  data(){
    return{
      blockchains: [
        {name: 'Bn_1', fee: 0.8, lead_time: 50},
        {name: 'Bn_2', fee: 0.45, lead_time: 150},
        {name: 'Shangai_Bn_1', fee: 1, lead_time: 10},
        {name: 'German_Bn_1', fee: 0.3, lead_time: 180}
      ],
      formTransaction: {
        blockchain_network: {
          name: null,
          fee: null,
          lead_time: null
        },
        wallet1: null,
        wallet2: null,
        amount: null
      }
    }
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