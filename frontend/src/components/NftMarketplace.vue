<template>
  <div class="market">
    <Card class="market-block-card">
      <template #content>
        <ScrollPanel class="market-scroll-panel" style="width: 100%; height: 90vh">
          <template v-for="item in nfts">
            <NftEntity :nft="item"/>
          </template>
        </ScrollPanel>
      </template>
    </Card>
  </div>
</template>

<script>
import NftEntity from "@/components/NftEntity";
export default {
  name: "NftMarketplace",
  components: {NftEntity},
  data(){
    return{
      nfts: [
        {name: 'Rosey', price: 15.732, rating: 0, placed: true, likes: 121, dislikes: 21, img: 'https://picsum.photos/360/528/?random'},
        {name: 'Yves', price: 127, rating: 12, placed: true, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Sophia', price: 829.3, rating: 142, placed: false, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Siren', price: 583, rating: 53, placed: true, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Liz', price: 5.42, rating: -12, placed: false, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Rosey', price: 15.732, rating: 0, placed: true, likes: 121, dislikes: 21, img: 'https://picsum.photos/360/528/?random'},
        {name: 'Yves', price: 127, rating: 12, placed: true, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Sophia', price: 829.3, rating: 142, placed: false, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Siren', price: 583, rating: 53, placed: true, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Liz', price: 5.42, rating: -12, placed: false, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Rosey', price: 15.732, rating: 0, placed: true, likes: 121, dislikes: 21, img: 'https://picsum.photos/360/528/?random'},
        {name: 'Yves', price: 127, rating: 12, placed: true, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Sophia', price: 829.3, rating: 142, placed: false, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Siren', price: 583, rating: 53, placed: true, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'},
        {name: 'Liz', price: 5.42, rating: -12, placed: false, likes: 121, dislikes: 21, img: 'https://picsum.photos/400/528/?random'}
      ]
    }
  }
}
</script>

<style scoped>
.market-block-card{
  border-radius: 15px;
  margin-left: 0.5rem;
  height: 98vh;
}

.market-scroll-panel::v-deep .p-scrollpanel-content{
  display: flex;
  flex-wrap: wrap;
}

.market{
  width: 94%;
  height: 100%;
}

</style>