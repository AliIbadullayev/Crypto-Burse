<template>
  <div class="p2p">
    <Card class="custom-card p2p-card">
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
  name: "P2P",
  data(){
    return{
      active: 0,
      items: [
        {
          label: 'Пополнение',
          icon: 'pi pi-fw pi-cart-plus',
          to: '/main/p2p/offers'
        },
        {
          label: 'Перевод',
          icon: 'pi pi-fw pi-user',
          to: '/main/p2p/my-transactions'
        }
      ]
    }
  }
}
</script>

<style scoped>
.p2p-card {
  border-radius: 15px;
}
.p2p{
  width: 94%;
}
</style>