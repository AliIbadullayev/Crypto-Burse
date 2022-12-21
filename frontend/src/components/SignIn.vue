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
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "SignIn",
  data(){
    return{
      signinForm: {
        username: null,
        password: null
      },
      user: {
        "role": "",
        "username": "",
        "token": ""
      }
    }
  },
  methods:{
     async signIn(){
      const response =  await axios.post('/api/v1/auth/login', this.signinForm)
      this.user = response.data
      localStorage.setItem('jwt', JSON.stringify(this.user.token))
      localStorage.setItem('role', this.user.role)
      this.$store.dispatch('user', this.user)
      if (this.user.role === "ROLE_CLIENT")
        this.$router.push('/main/profile')
      else
        this.$router.push('/main/admin')
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
</style>