<template>
  {{ role }}
</template>

<script>

export default {
  name: "Admin",
  computed:{
    role(){
      return this.$store.state.user.role
    }
  }
}
</script>

<style scoped>

</style>