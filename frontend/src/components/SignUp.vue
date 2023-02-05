<template>
  <div class="signup">
    <form class="signup-form">
      <div class="field">
        <span class="p-float-label">
          <InputText id="name" v-model="signupForm.name" style="width: 30vw"/>
          <label for="name">Имя</label>
        </span>
      </div>
      <div class="field">
        <span class="p-float-label">
          <InputText id="surname" v-model="signupForm.surname" style="width: 30vw"/>
          <label for="surname">Фамилия</label>
        </span>
      </div>

      <div class="field">
        <span class="p-float-label">
          <InputText id="username" v-model="signupForm.username" style="width: 30vw"/>
          <label for="username">Логин</label>
        </span>
      </div>
      <div class="field">
            <span class="p-float-label">
              <Password id="password" v-model="signupForm.password" style="width: 30vw"/>
              <label for="password">Пароль</label>
            </span>
      </div>
      <div class="signup-button">
        <Button label="Регистрация" icon="pi pi-check" @click="signUp"/>
      </div>
    </form>
    <div class="signin-oauth">
      <span>Также можете зарегистрироваться при помощи</span>
      <div class="button">
        <Button class="google p-0 p-button-outlined" aria-label="Google" @click="signUpOauth">
          <i class="pi pi-google px-2"></i>
          <span class="px-3">Google</span>
        </Button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {AppProps} from "../../config";

export default {
  name: "SignUp",
  data(){
    return{
      signupForm: {
        name: null,
        surname: null,
        username: null,
        password: null
      }
    }
  },
  methods:{
    signUp(){
      this.$store.dispatch('register', this.signupForm)
          .then(() => {
            this.$router.push('/login/signIn')
            this.$toast.add({severity:'success', summary: 'Регистрация', detail: "Успешно зарегистрирован!", life: 3000});

          })
          .catch((err)=>{
            this.$toast.add({severity:'error', summary: 'Регистрация', detail: err.response.data, life: 3000});
          })
    },
    signUpOauth(){
      window.location.href = AppProps.GOOGLE_OAUTH;
    },
  }
}
</script>

<style scoped>
  .signup-form > *{
    margin-bottom: 1.5rem;
  }

  .signup-button{
    text-align: center;
  }

  .signup-form{
    margin-top: 2rem;
  }

  .p-password::v-deep input {
    width: 30vw
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