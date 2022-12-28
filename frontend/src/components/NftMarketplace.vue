<template>
  <Toast/>
  <div class="market">
    <Card class="market-block-card">
      <template #content>
        <ScrollPanel class="market-scroll-panel" style="width: 100%; height: 90vh">
          <template v-for="item in nfts">
            <NftEntity :nft="item" @changed="onChanged"/>
          </template>
        </ScrollPanel>
      </template>
    </Card>
  </div>
</template>

<script>
import NftEntity from "@/components/NftEntity";
import axios from "axios";
import CryptoService from "@/services/crypto.service";
export default {
  name: "NftMarketplace",
  components: {NftEntity},
  data(){
    return{
      nfts: []
    }
  },
  methods:{
    fetchAllNfts(){
      CryptoService.getAllNfts()
          .then((r) => {
            this.nfts = r.data
          })
    },
    onChanged(value){
      if (value === true) {
        this.fetchAllNfts()
      }else {
        console.log('nothing was changed!')
      }
    }
  },
  mounted() {
    this.fetchAllNfts()
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