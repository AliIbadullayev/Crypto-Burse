<template>
  <form class="stacking" >
    <div class="field-1 field">
      <span class="p-float-label">
        <InputNumber id="stacking-amount" v-model="formStacking.amount" :maxFractionDigits="5" style="width: 30vw"/>
        <label for="stacking-amount">Количество крипты</label>
      </span>
    </div>
    <h3>Период вложения: {{formStacking.expire_date}} {{formStacking.expire_date === 1 ? 'год': formStacking.expire_date > 1 && formStacking.expire_date <=4 ? 'года': 'лет'}} </h3>
    <div class="field-2 field">
      <span class="p-float-label">
        <Slider v-model="formStacking.expire_date" :step="1" :min="1" :max="5" style="width: 30vw" />
      </span>
    </div>
    <div class="transaction-button" style="margin-top: 1rem;">
      <Button label="Вложить" icon="pi pi-check" />
    </div>
  </form>
</template>

<script>
export default {
  name: "WalletStacking",
  data(){
    return{
      formStacking: {
        amount: null,
        expire_date: 1
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


.stacking{
  margin-top: 30px;
}
.stacking .field-2{
  margin-bottom: 20px;
}
</style>