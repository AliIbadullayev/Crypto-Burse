<template>
  <Card class="nav-main-block">

    <template #content>
      <div class="nav-block">
        <div v-if="isClient" class="first-group">
<!--          <Button icon="pi pi-user" class="p-button-rounded p-button-info" />-->
<!--          <button class="plus-button"  routerLink="/add-target" routerLinkActive="active-link" mat-fab>-->
<!--            <span class="material-icons">add</span>-->
<!--          </button>-->
          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" @click="$router.push('/main/profile')">
            <font-awesome-icon icon="fa-solid fa-user" size="2x" style="color: #183153"/>
          </Button>
        </div>
        <div v-if="isClient" class="second-group">
          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" @click="$router.push('/main/wallets')">
            <font-awesome-icon icon="fa-solid fa-dollar-sign" size="2x" style="color: #183153"/>
          </Button>
          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" @click="$router.push('/main/p2p/offers')">
            <font-awesome-icon icon="fa-solid fa-users" size="2x" style="color: #183153"/>
          </Button>
          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" @click="$router.push('/main/exchange')">
            <font-awesome-icon icon="fa-solid fa-rotate" size="2x" style="color: #183153"/>
          </Button>
          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" @click="$router.push('/main/nft-marketplace')">
            <font-awesome-icon icon="fa-solid fa-store" size="2x" style="color: #183153"/>
          </Button>
          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" @click="$router.push('/main/history/transactions')">
            <font-awesome-icon icon="fa-solid fa-clock-rotate-left" size="2x" style="color: #183153"/>
          </Button>

        </div>
        <div v-if="!isClient" class="first-group">
          <!--          <Button icon="pi pi-user" class="p-button-rounded p-button-info" />-->
          <!--          <button class="plus-button"  routerLink="/add-target" routerLinkActive="active-link" mat-fab>-->
          <!--            <span class="material-icons">add</span>-->
          <!--          </button>-->
          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" >
            <font-awesome-icon icon="fa-solid fa-users" size="2x" style="color: #183153"/>
          </Button>
        </div>
        <div class="third-group">
<!--          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" @click="$router.push('/main/tutorial')">-->
<!--            <font-awesome-icon icon="fa-solid fa-circle-question" size="2x" style="color: #183153"/>-->
<!--          </Button>-->
          <Button icon="pi " class="p-button-rounded p-button-info p-button-text" style="width:50px; height:50px" @click="logout">
            <font-awesome-icon icon="fa-solid fa-right-from-bracket" size="2x" style="color: #183153"/>
          </Button>
        </div>
      </div>
    </template>
  </Card>
</template>

<script>
import Card from "primevue/card";
import { mapGetters } from 'vuex';

export default {
  components: {
    Card
  },
  name: "Nav",
  data(){
    return{
      isClient: null,
    }
  },
  methods: {
    logout(){
      this.$store.dispatch('logout')
      this.$router.push('/login/signIn')
    }
  },
  computed:{

  },
  mounted() {
    this.isClient = this.$store.state.user.role === "ROLE_CLIENT"
    if (!this.isClient) {
      this.$router.push('/main/admin')
    }
  }
}
</script>

<style lang="scss" scoped >

.nav-main-block{
  width: 6%;
  height: 98vh;
  background: #FFFFFF;
  border-radius: 15px;
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

.nav-block{
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.second-group button {
  margin-bottom: 15px;
}


</style>