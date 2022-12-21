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
import axios from "axios";
export default {
  name: "NftMarketplace",
  components: {NftEntity},
  data(){
    return{
      nfts: []
    }
  },
  methods:{
    async fetchAllNfts(){
      const nftsResponse = await axios.get('/api/v1/users/getAllNfts')
      this.nfts = nftsResponse.data
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