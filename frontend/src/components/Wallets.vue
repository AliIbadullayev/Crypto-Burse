<!--TODO transactions history, stacking history, tabs-menu to route between transaction, stacking, withdraw, history-->
<template>
<div class="wallet">
    <div class="wallets-block">
      <Card class="wallets-block-card custom-card">
        <template #content>
          <div class="wallet-table">
            <h2 class="wallet-header">Крипто-кошельки</h2>
            <DataTable :value="wallets" v-model:selection="selectedWallet" selectionMode="single"  @rowSelect="onWalletSelect"  :scrollable="true" scrollHeight="80vh">
              <Column field="name" header="Криптовалюта">
                <template #body="slot">
                  <div class="wallet-name">
                    {{slot.data.name}}
                  </div>
                </template>
              </Column>
              <Column field="value" header="Баланс">
                <template #body="slot">
                  <div class="wallet-balance">
                    {{slot.data.value}}
                  </div>
                </template>
              </Column>
            </DataTable>
            <div class="dialog-wallet">
              <Dialog class="dialog-wallet-main" v-model:visible="walletDialogVisible" :style="{width: '75vw'}"  :modal="true" :contentStyle="{height: '75vh', display: 'flex', 'flex-direction': 'column'}">
                <div class="main-crypto-name" >
                  <span>
                    {{selectedWallet.name}}
                  </span>
                </div>
                <div class="wallet-dialog-main-block">
                  <div class="wallet-dialog-first-block">
                    <div class="wallet-main-info">
                      <div class="wallet-balance-address-block">
                        <div class="wallet-balance-address-instance">
                          <h3>Баланс: {{selectedWallet.value}} шт.</h3>
                        </div>
                      </div>
                      <div class="wallet-balance-address-block">
                        <div class="wallet-balance-address-instance">
                          <h3>Адрес: {{selectedWallet.address }}</h3>
                        </div>
                      </div>
                      <div class="form-crypto-fiat">
                        <form >
                          <h3>
                            Пополнить счет
                          </h3>
                          <div class="inputs">
                            <div class="field-1">
                              <span class="p-float-label">
                                <InputNumber id="inputnumber1" v-model="formCryptoFiat.amount" :maxFractionDigits="5"/>
                                <label for="inputnumber1">Количество крипты</label>
                              </span>
                            </div>
                            <div class="field-2">
                              <h4>
                                <!--                            TODO instead of 100 must be actual crypto course -->
                                Необходимый фиат: {{formCryptoFiat.amount * 100 }}
                              </h4>
                            </div>
                          </div>
                          <div class="crypto-fiat-button">
                            <Button label="Пополнить" icon="pi pi-check" />
                          </div>
                        </form>
                      </div>
                    </div>
                  </div>
                  <div class="wallet-dialog-second-block">
                    <form class="transaction">
                      <div class="field-1 field">
                        <span class="p-float-label">
                          <InputNumber id="amount" v-model="formTransaction.amount" :maxFractionDigits="5" style="width: 20vw"/>
                          <label for="amount">Количество крипты</label>
                        </span>
                      </div>
                      <div class="field-2 field">
                        <span class="p-float-label">
                          <InputText id="addr" type="text" v-model="formTransaction.wallet2" style="width: 20vw" />
                          <label for="addr">Адрес получателя</label>
                        </span>
                      </div>
                      <div class="field-3 field">
                        <Dropdown  :options="blockchains" v-model="formTransaction.blockchain_network " optionLabel="name" placeholder="Сеть блокчейна" style="width: 20vw">
                          <template #value="slotProps">
                            <div class="country-item country-item-value" v-if="formTransaction.blockchain_network.name != null" style="width: 20vw">
                              <div>{{slotProps.value.name}}</div>
                            </div>
                            <span v-else>
                              Сеть блокчейна
                            </span>
                          </template>
                          <template #option="slotProps" style="width: 20vw">
                            <div class="country-item" style="display: flex; justify-content: space-between">
                              <div>{{slotProps.option.name}}</div>
                              <div >
                                {{slotProps.option.fee}}/{{slotProps.option.lead_time}}
                              </div>
                            </div>
                          </template>
                        </Dropdown>
                        <small v-if="formTransaction.blockchain_network.name != null" style="display: block">
                          Комиссия - {{formTransaction.blockchain_network.fee}}%, Время перевода - {{formTransaction.blockchain_network.lead_time}} сек.
                        </small>
                      </div>
                      <div class="transaction-button" style="margin-top: 1rem;">
                        <Button label="Перевести" icon="pi pi-check" />
                      </div>
                    </form>
                    <form class="stacking" >
                      <div class="field-1 field">
                        <span class="p-float-label">
                          <InputNumber id="stacking-amount" v-model="formStacking.amount" :maxFractionDigits="5" style="width: 20vw"/>
                          <label for="stacking-amount">Количество крипты</label>
                        </span>
                      </div>
                      <div class="field-2 field">
                        <span class="p-float-label">
                          <h5>Период вложения: {{formStacking.expire_date}} {{formStacking.expire_date === 1 ? 'год': formStacking.expire_date > 1 && formStacking.expire_date <=4 ? 'года': 'лет'}} </h5>
                          <Slider v-model="formStacking.expire_date" :step="1" :min="1" :max="5" />
                        </span>
                      </div>
                      <div class="transaction-button" style="margin-top: 1rem;">
                        <Button label="Вложить" icon="pi pi-check" />
                      </div>
                    </form>
                  </div>
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
            <DataTable :value="nfts" v-model:selection="selectedNft" selectionMode="single"  @rowSelect="onNftSelect"  :scrollable="true"  responsiveLayout="scroll" scrollHeight="80vh">
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
export default {
  name: "Wallets",
  data() {
    return{
      wallets: [
        {name: 'Bitcoin', value: 2.843, address: 'jfEa134O93cL1kdma04a'},
        {name: 'ShibaCoin', value: 1000129, address: 'jfEa134O93cL1kdma04a'},
        {name: 'Ethereum', value: 10.383, address: 'jfEa134O93cL1kdma04a'},
        {name: 'DentCoin', value: 1002, address: 'jfEa134O93cL1kdma04a'},
        {name: 'LiteCoin', value: 0.837, address: 'jfEa134O93cL1kdma04a'},
        {name: 'Bitcoin', value: 2.843, address: 'jfEa134O93cL1kdma04a'},
        {name: 'ShibaCoin', value: 1000129, address: 'jfEa134O93cL1kdma04a'},
        {name: 'Ethereum', value: 10.383, address: 'jfEa134O93cL1kdma04a'},
        {name: 'DentCoin', value: 1002, address: 'jfEa134O93cL1kdma04a'},
        {name: 'LiteCoin', value: 0.837, address: 'jfEa134O93cL1kdma04a'},
        {name: 'Bitcoin', value: 2.843, address: 'jfEa134O93cL1kdma04a'},
        {name: 'ShibaCoin', value: 1000129, address: 'jfEa134O93cL1kdma04a'},
        {name: 'Ethereum', value: 10.383, address: 'jfEa134O93cL1kdma04a'},
        {name: 'DentCoin', value: 1002, address: 'jfEa134O93cL1kdma04a'},
        {name: 'LiteCoin', value: 0.837, address: 'jfEa134O93cL1kdma04a'},
        {name: 'Bitcoin', value: 2.843, address: 'jfEa134O93cL1kdma04a'},
        {name: 'ShibaCoin', value: 1000129, address: 'jfEa134O93cL1kdma04a'},
        {name: 'Ethereum', value: 10.383, address: 'jfEa134O93cL1kdma04a'},
        {name: 'DentCoin', value: 1002, address: 'jfEa134O93cL1kdma04a'},
        {name: 'LiteCoin', value: 0.837, address: 'jfEa134O93cL1kdma04a'},
        {name: 'Bitcoin', value: 2.843, address: 'jfEa134O93cL1kdma04a'},
        {name: 'ShibaCoin', value: 1000129, address: 'jfEa134O93cL1kdma04a'},
        {name: 'Ethereum', value: 10.383, address: 'jfEa134O93cL1kdma04a'},
        {name: 'DentCoin', value: 1002, address: 'jfEa134O93cL1kdma04a'},
        {name: 'LiteCoin', value: 0.837, address: 'jfEa134O93cL1kdma04a'},
      ],
      nfts: [
        {name: 'Rosey', value: 15.732, rating: 0},
        {name: 'Yves', value: 127, rating: 12},
        {name: 'Sophia', value: 829.3, rating: 142},
        {name: 'Siren', value: 583, rating: 53},
        {name: 'Liz', value: 5.42, rating: -12},
      ],
      blockchains: [
        {name: 'Bn_1', fee: 0.8, lead_time: 50},
        {name: 'Bn_2', fee: 0.45, lead_time: 150},
        {name: 'Shangai_Bn_1', fee: 1, lead_time: 10},
        {name: 'German_Bn_1', fee: 0.3, lead_time: 180}
      ],
      formStacking: {
        amount: null,
        expire_date: 1
      },

      formTransaction: {
        blockchain_network: {
          name: null,
          fee: null,
          lead_time: null
        },
        wallet1: null,
        wallet2: null,
        amount: null
      },
      selectedWallet: null,
      selectedNft: null,
      walletDialogVisible: false,
      nftDialogVisible: false,
      formCryptoFiat:{
        wallet: null,
        amount: null
      }
    }
  },
  methods:{
    onWalletSelect(){
      this.walletDialogVisible = true;
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
    }
  }
}
</script>

<style scoped>
.stacking{
  margin-top: 30px;
}
.stacking .field-2{
  margin-bottom: 20px;
}

.form-crypto-fiat{
  margin-top: 70px;
}

.transaction .field{
  margin-top: 1.5rem;
}

.transaction small{
  margin-top: 0.25rem;
}

.main-crypto-name{
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
}

.wallet-dialog-main-block{
  display: flex;
  justify-content: center;
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

.wallet-dialog-first-block{
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.wallet-dialog-second-block{
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.dialog-wallet-main::v-deep .p-dialog-content {
  display: flex;
  flex-direction: column;
  /*align-items: center;!* Rectangle 1 *!*/
  height: 100%;
}


</style>