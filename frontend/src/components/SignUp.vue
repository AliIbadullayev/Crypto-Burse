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
              <InputNumber id="password" v-model="signupForm.password" :maxFractionDigits="5" style="width: 30vw"/>
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
</style>