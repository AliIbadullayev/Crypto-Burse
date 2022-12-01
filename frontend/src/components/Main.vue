<template>
  <div class="main-block">
    <Nav></Nav>
    <router-view></router-view>
  </div>
</template>

<script>
import Nav from "@/components/Nav";
export default {
  name: "Main",
  components: {Nav}
}
</script>

<style scoped>
.main-block{
  display:flex;
}
</style>