<template>
<div class="profile">
  <div class="main-info-block">
    <Card class="custom-card main-card">
      <template #header>
        <span class="header">
          Личная информация
        </span>
      </template>
      <template #content>
        <div class="main-info-img">
          <div class="main-info">
            <div class="main-info-login">
              <span class="info-title">
                Логин
              </span>
              <span class="info-content">
                ali_200257
              </span>
            </div>
            <div class="main-info-name">
              <span class="info-title">
                Имя и фамилия
              </span>
              <span class="info-content">
                Алибаба Ибадуллаев
              </span>
            </div>
            <div class="main-info-email">
              <span class="info-title">
                Email
              </span>
              <span class="info-content">
                ali.ibadullayev.02@gmail.com
              </span>
            </div>
            <div class="main-info-fiat">
              <span class="info-title">
                Фиатный баланс
              </span>
              <span class="info-content">
                $ 123.789
              </span>
            </div>
            <div class="main-info-all-amount">
              <span class="info-title">
                Общий баланс
              </span>
              <span class="info-content">
                $ 123 456.789
              </span>
            </div>


          </div>
          <div class="img">
            <font-awesome-icon icon="fa-solid fa-user" size="10x" style="color: #183153"/>
          </div>
        </div>
      </template>
    </Card>
  </div>
  <div class="bank-card-block">
    <Card class="custom-card bank-card">
      <template #content>
        <div class="card-fields">
          <div class="field ">
            <label for="number">Номер карты</label>
            <InputMask id="number" mask="9999 9999 9999 9999" v-model="card.cardNumber" placeholder="1234 5678 8765 4321"/>
          </div>
          <div class="field ">
            <label for="name">Имя и фамилия владельца карты</label>
            <InputText id="name" v-model="card.cardName" placeholder="Иван Иванов" type="text" />
          </div>
          <div class="field ">
            <label for="monthpicker">Month Picker</label>
            <Calendar inputId="monthpicker" v-model="card.cardDate" view="month" dateFormat="mm/y" placeholder="9/24"/>
          </div>
          <div class="field ">
            <label for="cvv">Номер карты</label>
            <InputNumber id="cvv" mode="decimal" :useGrouping="false" v-model="card.cardCvv" :min="0" :max="9999" placeholder="1234"/>
          </div>
        </div>
      </template>
    </Card>
  </div>
</div>
</template>

<script>

export default {
  name: "Profile",
  data(){
    return{
      card: {
        cardName: "",
        cardNumber: "",
        cardDate: "",
        cardCvv: ""
      },
    }
  }
}
</script>

<style scoped>
.profile{
  display: flex;
  width: 94%;
}

.main-info-block{
  width: 55%;
}
.bank-card-block{
  width: 45%;
}

.bank-card, .main-card{
  border-radius: 15px;
}

.info-title{
  color: grey;
  font-weight: 500;
  margin-bottom: 0.75rem
}
.info-content{
  border-bottom: solid;
  font-weight: bold;
  padding-bottom: 0.5rem;
  inline-size: fit-content;
}
.main-info > div{
  display: flex;
  flex-direction: column;
  margin-bottom: 1.25rem;
}

.header{
  font-size: larger;
  font-weight: bolder;
  margin-top: 30px;
}

.main-info-img{
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.main-card::v-deep .p-card-body {
  width: 100%;
}

.main-card::v-deep .p-card-content {
  padding: 1rem;
  width: 100%
}
.main-card::v-deep .p-card-header{
  margin-top: 1rem;
}

.img{
  margin-right: 2rem;
}

.card-fields{
  height: 50%;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  justify-content: space-around;
}

.card-fields .field{
  display: flex;
  flex-direction: column;
}

.bank-card::v-deep .p-card-body {
  height: 100%;
}

.bank-card::v-deep .p-card-content {
  height: 100%
}
</style>