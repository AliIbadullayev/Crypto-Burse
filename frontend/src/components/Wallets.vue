<!--TODO transactions history, stacking history, tabs-menu to route between transaction, stacking, withdraw, history-->
<template>
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
                    {{slot.data.value}}
                  </div>
                </template>
              </Column>
              <Column field="value" header="Рейтинг">
                <template #body="slot">
                  <div class="nft-balance">
                    {{ slot.data.rating !== 0 ? slot.data.rating : '-' }}
                  </div>
                </template>
              </Column>
            </DataTable>

            <div class="dialog-nft">
              <Dialog  v-model:visible="nftDialogVisible" :style="{width: '75vw'}"  :modal="true" :contentStyle="{height: '75vh'}" >
                <div class="nft-dialog-first-block">
                  <h2>
                    {{selectedNft.name}}
                  </h2>
                  <div class="nft-main-info">
                    <div class="nft-amount-address-block">
                      <div class="nft-amount-address-instance">
                        <h3>Цена</h3>
                        {{selectedNft.value}}
                      </div>
                    </div>
                    <div class="nft-rating-address-block">
                      <div class="nft-rating-address-instance">
                        <h3>Рейтинг</h3>
                        {{selectedNft.rating}}
                      </div>
                    </div>
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
        },
        {
          label: 'История',
          icon: 'pi pi-fw pi-file',
          to: '/main/wallets/history'
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
          }
      )
    },
    onWalletSelect(){
      this.$router.push('/main/wallets/replenish')
      this.walletDialogVisible = true;
    },
    onChange(value){
      if (value === true) {
        this.fetchProfile()
        this.fetchSelectedWallet()
      }else {
        console.log('nothing was changed!')
      }
    },
    onWalletClose() {
      this.walletDialogVisible = false;
    },
    onNftSelect(){
      this.nftDialogVisible = true;
    },
    onNftClose() {
      this.nftDialogVisible = false;
    },
    countPercent() {
    //  TODO realize logic of giving percent
    },
    close(){
      this.$router.push('/main/wallets')
      this.walletDialogVisible = false;
    },

  },
  mounted() {
    this.fetchWalletsAndExchangeRates()
    this.fetchProfile()
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

</style>