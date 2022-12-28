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
            alert(err.response.data)
          }
      );
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