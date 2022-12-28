<template>
  <Card class="entity-card">
    <template #header>
      <div class="image-block">
        <img :src="nft.url"/>
      </div>
    </template>
    <template #title>
      <div class="title-block">
        <div class="nft-name">
          {{ nft.name }}
        </div>
        <div class="owner">
          #{{ nft.owner }}
        </div>
      </div>
    </template>
    <template #subtitle>
      <div class="price">
        ${{ nft.price }}
      </div>
    </template>
    <template #content>
      <div class="block">
        <div class="like-dislike">
          <div class="like">
            <div class="button">
              <Button icon="pi pi-thumbs-up" class="p-button p-button-rounded p-button-success p-button-text" @click="scoreNft(nft, true)"/>
            </div>
            <div class="text">
              {{ nft.likes }}
            </div>
          </div>
          <div class="dislike">
            <div class="button">
              <Button icon="pi pi-thumbs-down" class="p-button p-button-rounded p-button-danger p-button-text" @click="scoreNft(nft, false)"/>
            </div>
            <div class="text">
              {{ nft.dislikes }}
            </div>
          </div>
        </div>
        <div class="foot-block">
          <div class="buy-button">
            <Button label="Купить" :class="nft.placed===true? 'p-button-success': 'p-button-secondary'"
                    @click="buyNft(nft)" style="width: 100%"/>
          </div>
        </div>
      </div>
    </template>
  </Card>
</template>

<script>
import axios from "axios";
import CryptoService from "@/services/crypto.service";

export default {
  name: "NftEntity",
  data() {
    return {}
  },
  methods: {
    buyNft(nft) {
      const toSend = {id: nft.id}
      CryptoService.buyNft(toSend)
          .then(() => {
            this.$emit('changed', true)
          })
          .catch((err) => {
            this.$emit('changed', false)
            alert(err.response.data)
          })
    },

    scoreNft(nft, like){
      let toSend
      if (like === true){
        toSend = {
          nftId: nft.id,
          isLiked: true
        }
      }else {
        toSend = {
          nftId: nft.id,
          isLiked: false
        }
      }
      CryptoService.scoreNft(toSend)
          .then((r) => {
            this.$emit('changed', true)
          })
          .catch((err)=>{
            this.$emit('changed', false)
            alert(err.response.data)
          })
    }
  },
  props: {
    nft: null
  }
}
</script>

<style scoped>
.entity-card {
  width: 22.5%;
  height: 400px;
  margin: 1.25%;
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.4);
}

.image-block {
  height: 50%;
  text-align: center;
}

.image-block img {
  width: 200px;
  height: 200px;
  object-fit: contain;
}

.like-dislike {
  display: flex;
  justify-content: space-around;
  margin-bottom: 1rem;
}

.like, .dislike {
  display: flex;
  align-items: center;
}

.owner {
  margin-left: 1rem;
  font-size: 1.25rem;
  font-weight: 500;
}

.title-block {
  display: flex;
  align-items: baseline;
}

.entity-card::v-deep .p-card-content {
  padding: 0;
}


</style>