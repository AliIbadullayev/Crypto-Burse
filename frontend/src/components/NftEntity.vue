<template>
  <Card class="entity-card">
    <template #header>
      <div class="image-block">
        <img :src="nft.img"/>
      </div>
    </template>
    <template #title>
      <div class="title-block">
        <div class="nft-name">
          {{nft.name}}
        </div>
        <div class="rating" >
          #{{nft.rating}}
        </div>
      </div>
    </template>
    <template #subtitle>
      <div class="price">
        ${{nft.price}}
      </div>
    </template>
    <template #content >
      <div class="block">
        <div class="like-dislike">
          <div class="like">
            <div class="button">
              <Button icon="pi pi-thumbs-up" class="p-button p-button-rounded p-button-success p-button-text" />
            </div>
            <div class="text">
              {{nft.likes}}
            </div>
          </div>
          <div class="dislike">
            <div class="button">
              <Button icon="pi pi-thumbs-down" class="p-button p-button-rounded p-button-danger p-button-text" />
            </div>
            <div class="text">
              {{nft.dislikes}}
            </div>
          </div>
        </div>
        <div class="foot-block">
          <div class="buy-button">
            <Button label="Купить" :class="nft.placed===true? 'p-button-success': 'p-button-secondary'" :disabled="nft.placed!==true" style="width: 100%"/>
          </div>
        </div>
      </div>
    </template>
  </Card>
</template>

<script>
export default {
  name: "NftEntity",
  data(){
    return{

    }
  },
  props:{
    nft: Object
  }
}
</script>

<style scoped>
.entity-card{
  width: 22.5%;
  height: 400px;
  margin: 1.25%;
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.4);
}
.image-block{
  height: 50%;
  text-align: center;
}

.image-block img{
  width: 200px;
  height: 200px;
  object-fit: contain;
}

.like-dislike{
  display: flex;
  justify-content: space-around;
  margin-bottom: 1rem;
}

.like, .dislike{
  display: flex;
  align-items: center;
}

.rating{
  margin-left: 1rem;
  font-size: 1.25rem;
  font-weight: 500;
}

.title-block{
  display: flex;
  align-items: baseline;
}

.entity-card::v-deep .p-card-content{
  padding: 0;
}


</style>