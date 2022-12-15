<template>
  <div class="form-crypto-fiat">
    <form>
      <div class="inputs">
        <div class="field-1">
          <span class="p-float-label">
            <InputNumber id="inputnumber1" v-model="formCryptoFiat.amount" :maxFractionDigits="5" style="width: 30vw"/>
            <label for="inputnumber1">Количество крипты</label>
          </span>
        </div>
      </div>
      <h4>
        <!-- TODO instead of 100 must be actual crypto course -->
        Необходимый фиат: {{formCryptoFiat.amount * 100 }}
      </h4>
      <div class="crypto-fiat-button">
        <Button label="Пополнить" icon="pi pi-check" />
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: "WalletReplenish",
  data(){
    return {

      formCryptoFiat:{
        wallet: null,
        amount: null
      }
    }

  }
}
</script>

<style scoped>


.transaction .field{
  margin-top: 1.5rem;
}

.transaction small{
  margin-top: 0.25rem;
}

form{
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  justify-content: center;
}
.form-crypto-fiat{
  display: flex;
  flex-direction: column;
  height: 100%;
  align-items: center;
}





</style>