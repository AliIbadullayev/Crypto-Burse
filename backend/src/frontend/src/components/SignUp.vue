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
  </div>
</template>

<script>
import axios from "axios";

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
    }
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
</style>