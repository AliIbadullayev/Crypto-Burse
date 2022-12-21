<template>
  {{ user.role }}
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "Admin",
  computed:{
    ...mapGetters(['user'])
  }
}
</script>

<style scoped>

</style>