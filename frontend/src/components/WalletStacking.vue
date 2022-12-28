<template>
  <form v-if="!hasStaking" class="stacking" >
    <div class="field-1 field">
      <span class="p-float-label">
        <InputNumber id="stacking-amount" v-model="formStaking.amount" :maxFractionDigits="5" style="width: 30vw"/>
        <label for="stacking-amount">Количество крипты</label>
      </span>
    </div>
    <h3>Период вложения: {{formStaking.years}} {{getYearsPostfix(formStaking.years)}} </h3>
    <div class="field-2 field">
      <span class="p-float-label">
        <Slider v-model="formStaking.years" :step="1" :min="1" :max="5" style="width: 30vw" />
      </span>
    </div>
    <div class="transaction-button" style="margin-top: 1rem;">
      <Button label="Вложить" icon="pi pi-check" @click="makeStaking"/>
    </div>
  </form>
  <div v-if="hasStaking" class="has-stacking">
    <h4 style="margin-left: 2rem">
      Имеется активный счет!
    </h4>
    <h4 style="margin-left: 2rem">
      Срок хранение истекает {{getExpireTime()}}
    </h4>
    <h4 style="margin-left: 2rem">
      Процент вклада: {{ staking.interestRate }}
    </h4>
    <div class="transaction-button" style="margin-top: 1rem;">
      <Button label="Снять" icon="pi pi-check" @click="freeStaking"/>
    </div>
  </div>

</template>

<script>
import CryptoService from '@/services/crypto.service'
import moment from "moment";

export default {
  name: "WalletStacking",
  data(){
    return{
      formStaking: {
        walletAddress: this.wallet.address,
        amount: null,
        years: null
      },
      hasStaking: false,
      staking: null
    }
  },
  methods:{
    makeStaking(){
      CryptoService.toStake(this.formStaking).then(
          (r) => {
            this.$emit('changed', true)
            this.hasStaking = true
            this.getStaking()
          },
          (err) => {
            this.$emit('changed', false)
            alert(err.response.data)
          }
      )
    },
    getStaking(){
      CryptoService.getStaking({"walletAddress":this.formStaking.walletAddress}).then(
          (r) => {
            this.hasStaking = true
            this.staking = r.data
          },
          //TODO make this as warning
          (err) => {
            this.hasStaking = false
            alert(err.response.data)
          }
      )
    },
    freeStaking(){
      CryptoService.freeStake({"walletAddress":this.formStaking.walletAddress}).then(
          (r) => {
            this.hasStaking = false
          },
          //TODO make this as warning
          (err) => {
            alert(err.response.data)
          }
      )
    },
    getExpireTime(){
      return moment(String(this.staking.expireDate)).format('DD/MM/YYYY')
    },
  },
  mounted(){
    this.getStaking()
  },
  props:{
    wallet: Object
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

.has-stacking{
  display: flex;
  align-items: center;
  flex-direction: column;
}
</style>