<template>
<div class="login">

  <Card class="custom-card login-card">
    <template #content>
      <div class="p2p-main">
        <TabMenu :model="items" v-model:activeIndex="active"/>
        <router-view/>
      </div>
    </template>
  </Card>

</div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return{
      items: [
        {
          label: 'Вход',
          icon: 'pi pi-fw pi-user',
          to: '/login/signIn'
        },
        {
          label: 'Регистрация',
          icon: 'pi pi-fw pi-plus',
          to: '/login/signUp'
        }
      ],
    }
  },
  created() {
    if (this.$store.state.status.loggedIn) {
      this.$router.push('/main/profile');
    }
  }
}
</script>

<style scoped>
  .login{
    margin: 1px;
    padding: 0;
  }
  .login-card {

    margin: 0;
    width: 100%
  }
</style>