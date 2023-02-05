<template>
  <div class="signin">
    <form class="signin-form">
      <div class="field">
        <span class="p-float-label">
          <InputText id="username" v-model="signinForm.username" style="width: 30vw"/>
          <label for="username">Ваш логин</label>
        </span>
      </div>
      <div class="field">
            <span class="p-float-label">
              <Password id="password" v-model="signinForm.password" style="width: 30vw"/>
              <label for="password">Пароль</label>
            </span>
      </div>
      <div class="signin-button">
        <Button label="Войти" icon="pi pi-check" @click="signIn"/>
      </div>
    </form>
    <div class="signin-oauth">
      <span>Также можете войти при помощи</span>
      <div class="button">
          <Button class="google p-0 p-button-outlined" aria-label="Google" @click="signInOauth">
            <i class="pi pi-google px-2"></i>
            <span class="px-3">Google</span>
          </Button>
      </div>
    </div>
  </div>
</template>

<script>

import {AppProps} from "../../config";

export default {
  name: "SignIn",
  data(){
    return{
      signinForm: {
        username: null,
        password: null
      },
      message: ''
    }
  },
  computed:{
    loggedIn() {
      return this.$store.state.status.loggedIn;
    }
  },
  methods:{
    signIn(){
      this.$store.dispatch('login', this.signinForm).then(
          () => {
            if ( this.$store.state.user.role === "ROLE_ADMIN")
              this.$router.push('/main/admin');
            else
              this.$router.push('/main/profile');
          })
          .catch((err)=>{
            this.$toast.add({severity:'error', summary: 'Вход', detail: err.response.data, life: 3000});
          }
      );
    },
    signInOauth(){
      window.location.href = AppProps.GOOGLE_OAUTH;
    },
  },
  mounted(){
    const user = this.$route.query;
    if (JSON.stringify(user) !== "{}"){
      this.$store.dispatch('loginOAuth', user);
      this.$router.push('/main/profile');
    }
    else{
      this.$store.dispatch('logout');
    }
  }
}
</script>

<style scoped>

.p-password::v-deep input {
  width: 30vw
}
.signin-form > *{
  margin-bottom: 1.5rem;
}

.signin-button{
  text-align: center;
}

.signin-form{
  margin-top: 2rem;
}

.google{
  width: 100%;
}

.google span{
  width: 100%;
  display: flex;
  justify-content: center;
}

.signin-oauth > span{
  color: #909090;
  display: block;
  text-align: center;
}

.signin-oauth > .button {
  margin-top: 1rem;
}

</style>