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
              <InputNumber id="password" v-model="signinForm.password" :maxFractionDigits="5" style="width: 30vw"/>
              <label for="password">Ваш пароль</label>
            </span>
      </div>
      <div class="signin-button">
        <Button label="Войти" icon="pi pi-check" @click="signIn"/>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: "SignIn",
  data(){
    return{
      signinForm: {
        username: null,
        password: null
      }
    }
  }
}
</script>

<style scoped>
.signin-form > *{
  margin-bottom: 1.5rem;
}

.signin-button{
  text-align: center;
}

.signin-form{
  margin-top: 2rem;
}
</style>