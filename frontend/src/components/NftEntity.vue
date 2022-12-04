<template>
  <Card style="width: 336px; height: 472px">
    <template #header>
      <div class="image-block" style="height: 264px">
        <img src="https://www.primefaces.org/wp-content/uploads/2020/02/primefacesorg-primevue-2020.png" style="max-height: 264px; margin: auto" />
      </div>
    </template>
    <template #title>
      Advanced Card
    </template>
    <template #subtitle>
      Card subtitle
    </template>
    <template #content>
      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Inventore sed consequuntur error repudiandae numquam deserunt
        quisquam repellat libero asperiores earum nam nobis, culpa ratione quam perferendis esse, cupiditate neque quas!</p>
    </template>
    <template #footer>
      <Button icon="pi pi-check" label="Save" />
      <Button icon="pi pi-times" label="Cancel" class="p-button-secondary" style="margin-left: .5em" />
    </template>
  </Card>
</template>

<script>
export default {
  name: "NftEntity"
}
</script>

<style scoped>
.image-block{
  height: 264px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}


</style>