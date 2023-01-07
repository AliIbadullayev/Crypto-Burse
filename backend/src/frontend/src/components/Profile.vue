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
          <Toast/>
          <div class="main-info-img">
            <div class="main-info">
              <div class="main-info-login">
              <span class="info-title">
                Логин
              </span>
                <span class="info-content">
                {{ profile.userLogin }}
              </span>
              </div>
              <div class="main-info-name">
              <span class="info-title">
                Имя и фамилия
              </span>
                <span class="info-content">
                {{ profile.name }} {{ profile.surname }}
              </span>
              </div>
              <div class="main-info-fiat">
              <span class="info-title">
                Фиатный баланс
              </span>
                <span class="info-content">
                $ {{ profile.fiatBalance }}
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
        <template #header>
        <span class="header">
          Банковские карты
        </span>


        </template>
        <template #content>
<!--          <template v-for="bankCard in bankCards">-->
<!--            <BankCard :card="bankCard"/>-->
<!--          </template>-->
          <div class="cards-block" v-if="hasCards">
            <Carousel :value="bankCards" :numVisible="1" :numScroll="1" class="bank-card-carousel">
              <template #item="slotProps">
                <BankCard :card="slotProps.data"/>
                <!--              {{alert(JSON.stringify(slotProps))}}-->
              </template>
            </Carousel>
          </div>



          <div class="add-button">
            <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px"
                    @click="openAddBankCard">
              <font-awesome-icon icon="fa-solid fa-circle-plus" size="3x" style="color: #183153"/>
            </Button>
          </div>
          <form class="replenish-form">
            <div class="field">
          <span class="p-float-label">
            <InputNumber id="inputnumber2" v-model="replenishForm.amount" :maxFractionDigits="5" style="width: 30vw" placeholder=""/>
            <label for="inputnumber2">Количество фиата</label>
          </span>
            </div>
            <div class="replenish-button">
              <Button label="Пополнить" icon="pi pi-check" @click="replenishFiat"/>
            </div>
          </form>
        </template>
      </Card>
    </div>
    <div class="dialog-bank-card">
      <Dialog class="dialog-bank-card-main" :closable="false" v-model:visible="this.bankCardDialogVisible"
              :style="{width: '75vw'}" :modal="true" :contentStyle="{height: '75vh'}">
        <template #header>
          <div class="bank-card-dialog-header">
            <div class="header-block">
              <h3>
                Добавление банковской карты
              </h3>
            </div>
            <div class="exit-button">
              <Button icon="pi pi-times" class="p-button-rounded p-button-danger p-button-text" @click="close"/>
            </div>
          </div>
        </template>
        <div class="card">
          <form class="card-fields">
            <div class="field ">
              <label for="number">Номер карты</label>
              <InputMask id="number" mask="9999999999999999" v-model="card.cardNumber"
                         placeholder="1234 5678 8765 4321"/>
            </div>
            <div class="field ">
              <label for="name">Имя и фамилия владельца карты</label>
              <InputText id="name" v-model="card.nameOnCard" placeholder="Иван Иванов" type="text"/>
            </div>
            <div class="field ">
              <label for="monthpicker">Month Picker</label>
              <Calendar inputId="monthpicker" v-model="card.expireDate" view="month" dateFormat="mm/y"
                        placeholder="9/24"/>
            </div>
            <div class="field ">
              <label for="cvv">СVV</label>
              <InputNumber id="cvv" mode="decimal" :useGrouping="false" v-model="card.cvv" :min="100" :max="999"
                           placeholder="123"/>
            </div>
            <div class="bank-card-button">
              <Button label="Разместить предложение" icon="pi pi-check" @click="addBankCard"/>
            </div>
          </form>
        </div>
      </Dialog>
    </div>
  </div>
</template>

<script>
import CryptoService from '@/services/crypto.service';
import BankCard from "@/components/BankCard";

export default {
  name: "Profile",
  components: {BankCard},
  data() {
    return {
      card: {
        "cardNumber": null,
        "nameOnCard": null,
        "expireDate": null,
        "cvv": null
      },
      profile: Object,
      bankCardDialogVisible: false,
      replenishForm: {
        amount: null
      },
      bankCards: null,
      responsiveOptions: [
        {
          breakpoint: '1024px',
          numVisible: 3,
          numScroll: 3
        },
        {
          breakpoint: '600px',
          numVisible: 2,
          numScroll: 2
        },
        {
          breakpoint: '480px',
          numVisible: 1,
          numScroll: 1
        }
      ],
      hasCards: false,
    }
  },
  methods: {
    getProfileInfo() {
      this.$store.dispatch('checkIsLoggedIn').then(
          (resp) => {
            this.profile = resp
          },
          () => {
            this.$toast.add({severity:'error', summary: 'Личный кабинет', detail: err.response.data, life: 3000});
            this.$router.push('/login/signIn')
          }
      )
    },
    getBankCards(){
      CryptoService.getBankCards().then(
          (resp) => {
            this.bankCards = resp.data
            this.hasCards = Array.isArray(this.bankCards) && this.bankCards.length
          }
      )
    },
    addBankCard() {
      CryptoService.addBankCard(this.card)
          .then(() => {
                this.$toast.add({severity:'success', summary: 'Банковская карта', detail: "Успешно добавлена!", life: 3000});
                this.bankCardDialogVisible = false;
                this.getBankCards()
              },
              (err) => {
                this.$toast.add({severity:'error', summary: 'Банковская карта', detail: err.response.data, life: 3000});
              })
    },
    replenishFiat() {
      CryptoService.replenishFiat(this.replenishForm)
          .then(() => {
                this.getProfileInfo()
                this.$toast.add({severity:'success', summary: 'Личный счет', detail: "Успешно пополнен!", life: 3000});
              },
              (err) => {
                this.$toast.add({severity:'error', summary: 'Личный счет', detail: err.response.data, life: 3000});
              })
    },
    openAddBankCard() {
      this.bankCardDialogVisible = true
    },
    close() {
      this.bankCardDialogVisible = false
    }
  },
  mounted() {
    this.getProfileInfo()
    this.getBankCards()
  }
}
</script>

<style scoped>
.bank-card-carousel::v-deep .p-carousel-items-container {
  width: 30vw;
}


.replenish-form {
  margin-top: 1rem;
}

.replenish-form .field{
  display: flex;
  flex-direction: column;
  align-items: center;
}


.replenish-form > div {
  margin-bottom: 1.5rem;
  text-align: center;
}

.profile {
  display: flex;
  width: 94%;
}

.main-info-block {
  width: 55%;
}

.bank-card-block {
  width: 45%;
}

.field label {
  margin-bottom: 8px;
}

.bank-card, .main-card {
  border-radius: 15px;
}

.card-fields > * {
  margin-bottom: 1rem;
}

.info-title {
  color: grey;
  font-weight: 500;
  margin-bottom: 0.75rem
}

.info-content {
  border-bottom: solid;
  font-weight: bold;
  padding-bottom: 0.5rem;
  inline-size: fit-content;
}

.main-info > div {
  display: flex;
  flex-direction: column;
  margin-bottom: 1.25rem;
}

.header {
  font-size: larger;
  font-weight: bolder;
  margin-top: 30px;
}

.main-info-img {
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

.main-card::v-deep .p-card-header {
  margin-top: 1rem;
}

.bank-card::v-deep .p-card-header {
  margin-top: 1rem;
}

.img {
  margin-right: 2rem;
}

.card-fields {
  height: 50%;
  display: flex;
  flex-direction: column;
  flex-wrap: nowrap;
  justify-content: space-around;
}

.card-fields .field {
  display: flex;
  flex-direction: column;
}

.bank-card::v-deep .p-card-body {
  height: 100%;
}

.bank-card::v-deep .p-card-content {
  height: 100%
}

.add-button {
  text-align: center;
  margin-top: 0;
}

.card {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 1rem;
}

.card h4 {
  margin: 0 2rem 0 0;
}

.card > * {
  margin-bottom: 1.5rem;
}

.available-resources > div {
  margin-bottom: 1rem;
  display: flex;
}


.bank-card-dialog-header {
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: space-between;
}

.header-block {
  display: flex;
  align-items: center;
}

.header-block h3 {
  margin: 0;
}

</style>