<!--TODO transactions history, stacking history, tabs-menu to route between transaction, stacking, withdraw, history-->
<template>
  <Toast/>
<div class="wallet">
    <div class="wallets-block">
      <Card class="wallets-block-card custom-card">
        <template #content>
          <div class="wallet-table">
            <h2 class="wallet-header">Крипто-кошельки</h2>
            <DataTable :value="wallets" v-model:selection="selectedWallet" selectionMode="single"  @rowSelect="onWalletSelect"  :scrollable="true" scrollHeight="70vh">
              <Column field="name" header="Криптовалюта">
                <template #body="slot">
                  <div class="wallet-name">
                    {{ slot.data.crypto_name }}
                  </div>
                </template>
              </Column>
              <Column field="value" header="Баланс">
                <template #body="slot">
                  <div class="wallet-balance">
                    {{getFloatToFixed(slot.data.amount, 5)}}
                  </div>
                </template>
              </Column>
            </DataTable>
            <div class="dialog-wallet">
              <Dialog class="dialog-wallet-main" :closable="false" v-model:visible="walletDialogVisible" :style="{width: '75vw'}"  :modal="true" :contentStyle="{height: '75vh', display: 'flex', 'flex-direction': 'column'}">
                <template #header>
                  <div class="wallet-dialog-header">
                    <div class="header-block">
                      <h3>
                        {{selectedWallet.crypto_name}}
                      </h3>
                      <h4 style="margin-left: 2rem">
                        {{selectedWallet.address}}
                      </h4>
                    </div>
                    <div class="exit-button">
                      <Button icon="pi pi-times" class="p-button-rounded p-button-danger p-button-text" @click="close"/>
                    </div>
                  </div>
                </template>
                <div class="card">
                  <TabMenu :model="items" v-model:activeIndex="active"/>
                  <div class="card-text" style="text-align: center; margin-top: 2rem">
                    <h4 style="margin-left: 2rem">
                      Доступно: {{getFloatToFixed(selectedWallet.amount,5)}}
                    </h4>
                    <h4 style="margin-left: 2rem">
                      Доступно фиата: {{getFloatToFixed(profile.fiatBalance, 5)}}
                    </h4>
                  </div>
                  <router-view :wallet="selectedWallet" :exchange-rate="getExchange(exchangeRates, selectedWallet.crypto_name)" @changed="onChange"/>
                </div>
              </Dialog>
            </div>
          </div>
        </template>
      </Card>
    </div>
    <div class="nft-block" >
      <Card class="nft-block-card custom-card">
        <template #content>
          <div class="wallet-table">
            <h2 class="wallet-header">NFT кошельки</h2>
            <DataTable :value="nfts" v-model:selection="selectedNft" selectionMode="single"  @rowSelect="onNftSelect"  :scrollable="true"  responsiveLayout="scroll" scrollHeight="70vh">
              <Column field="name" header="Название">
                <template #body="slot">
                  <div class="nft-name">
                    {{slot.data.name}}
                  </div>
                </template>
              </Column>
              <Column field="value" header="Цена(USD)">
                <template #body="slot">
                  <div class="nft-balance">
                    {{slot.data.price}}
                  </div>
                </template>
              </Column>
              <Column field="value" header="Статус">
                <template #body="slot">
                  <div class="nft-balance">
                    {{ slot.data.placed ? "Размещен": "Не размещен"}}
                  </div>
                </template>
              </Column>
            </DataTable>

            <div class="dialog-nft">
              <Dialog  v-model:visible="nftDialogVisible" :closable="false"  :style="{width: '45vw'}"  :modal="true" :contentStyle="{height: '75vh'}" >
                <template #header>
                  <div class="wallet-dialog-header">
                    <div class="header-block">
                      <h2>
                        {{selectedNft.name}}
                      </h2>
                      <h4 style="margin-left: 2rem">
                        {{selectedNft.price}}$
                      </h4>
                    </div>
                    <div class="exit-button">
                      <Button icon="pi pi-times" class="p-button-rounded p-button-danger p-button-text" @click="close"/>
                    </div>
                  </div>
                </template>
                <div class="nft-dialog-first-block">
                  <div class="image-block">
                    <img :src="selectedNft.url"/>
                  </div>
                  <div class="block">
                    <div class="like-dislike">
                      <div class="like">
                        <div class="button">
                          <Button icon="pi pi-thumbs-up" class="p-button p-button-rounded p-button-success p-button-text"/>
                        </div>
                        <div class="text">
                          {{ selectedNft.likes }}
                        </div>
                      </div>
                      <div class="dislike">
                        <div class="button">
                          <Button icon="pi pi-thumbs-down" class="p-button p-button-rounded p-button-danger p-button-text"/>
                        </div>
                        <div class="text">
                          {{ selectedNft.dislikes }}
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="toggle-button-placed">
                    <ToggleButton v-model="selectedNft.placed" onLabel="Отозвать NFT" offLabel="Разместить NFT" onIcon="pi pi-times" offIcon="pi pi-check" class="w-full sm:w-10rem" aria-label="do you confirm" @click="placeOrReturnNft" />
                  </div>
                </div>
                <div class="nft-dialog-second-block">
                </div>
              </Dialog>
            </div>
          </div>
        </template>
      </Card>
    </div>
</div>

</template>

<script>
import CryptoService from "@/services/crypto.service";

export default {
  name: "Wallets",
  data() {
    return{
      items: [
        {
          label: 'Пополнение',
          icon: 'pi pi-fw pi-home',
          to: '/main/wallets/replenish'
        },
        {
          label: 'Перевод',
          icon: 'pi pi-fw pi-calendar',
          to: '/main/wallets/transaction'
        },
        {
          label: 'Вложение',
          icon: 'pi pi-fw pi-pencil',
          to: '/main/wallets/stacking'
        }
      ],

      wallets: null,
      nfts: null,
      profile: null,

      selectedWallet: null,
      selectedNft: null,
      walletDialogVisible: false,
      nftDialogVisible: false,
      exchangeRates: null,
    }
  },
  methods:{
    fetchWalletsAndExchangeRates(){
      CryptoService.getWallets().then(
          r => {
            this.wallets = r.data
          }
      )
      CryptoService.getExchangeRates().then(
          r => {
            this.exchangeRates = r.data
          }
      )
    },
    fetchProfile(){
      CryptoService.getProfileInfo().then(
          r => {
            this.profile = r.data
          }
      )
    },
    fetchSelectedWallet(){
      CryptoService.getClientWallet({address: this.selectedWallet.address}).then(
          r => {
            this.selectedWallet = r.data
            this.wallets.find(x => x.address === this.selectedWallet.address).amount = this.selectedWallet.amount
          }
      )

    },
    fetchNfts(){
      CryptoService.getAllClientNfts().then(
          r => {
            this.nfts = r.data
          }
      )
    },
    placeOrReturnNft() {
      if (this.selectedNft.placed) {
        CryptoService.sellNft({id: this.selectedNft.id}).then(
            r => {
              this.selectedNft = r.data
              this.$toast.add({severity:'success', summary: 'NFT-cущность', detail: "Успешно размещена!", life: 3000});
            }
        )
      }
      else {
        CryptoService.returnNft({id: this.selectedNft.id}).then(
            r => {
              this.selectedNft = r.data
              this.$toast.add({severity:'success', summary: 'NFT-cущность', detail: "Успешно отозвана!", life: 3000});
            }
        )
      }
      this.nfts.find(x => x.id === this.selectedNft.id).placed = this.selectedNft.placed
    },
    onChange(value){
      if (value === true) {
        this.fetchProfile()
        this.fetchSelectedWallet()
      }else {
        console.log('nothing was changed!')
      }
    },
    onWalletSelect(){
      this.$router.push('/main/wallets/replenish')
      this.walletDialogVisible = true;
    },
    onNftSelect(){
      this.nftDialogVisible = true;
    },
    close(){
      this.$router.push('/main/wallets')
      this.nftDialogVisible = false;
      this.walletDialogVisible = false;
    }
  },
  mounted() {
    this.fetchWalletsAndExchangeRates()
    this.fetchProfile()
    this.fetchNfts()
  }
}
</script>

<style scoped>
.wallet-dialog-header{
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: space-between;
}

.header-block{
  display: flex;
  align-items: center;
}

.header-block h3,h4 {
  margin: 0;
}

.header-block h4{
  margin-left: 2rem
}

.card{
  height: 80%;
}

.wallet-header{
text-align:center;
}

.wallet-table{
width: 75vh;
}

.wallet-balance{
text-align: right;
}

.nft-block, .wallets-block{
width: 100%;
}

.wallet{
display:flex;
width: 94%;
}

.wallets-block-card, .nft-block-card{
border-radius: 15px;
}

.image-block {
  height: 50%;
  text-align: center;
}

.image-block img {
  width: 40vw;
  height: 40vh;
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

.toggle-button-placed{
  text-align:center;
}
</style>