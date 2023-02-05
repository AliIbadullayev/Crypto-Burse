<template>
  <div class="history">
    <Card class="custom-card history-card">
      <template #content>
        <h2>История крипто-кошельков</h2>
        <div class="history-main">
          <TabMenu :model="items" v-model:activeIndex="active"/>
          <router-view/>
        </div>
      </template>
    </Card>
  </div>
</template>

<script>
export default {
  name: "WalletHistory",
  data(){
    return{
      active: 0,
      items: [
        {
          label: 'Транзакции',
          icon: 'pi pi-fw pi-cart-plus',
          to: '/main/history/transactions'
        },
        {
          label: 'Обмены',
          icon: 'pi pi-fw pi-user',
          to: '/main/history/exchanges'
        },
        {
          label: 'Пополнения',
          icon: 'pi pi-fw pi-user',
          to: '/main/history/replenishments'
        }
      ]
    }
  },

}
</script>

<style scoped>
.history-card {
  border-radius: 15px;
}
.history{
  width: 94%;
}


</style>