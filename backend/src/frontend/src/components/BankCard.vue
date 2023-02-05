<template>
  <div class="item">
    <div class="image-block">
      <div class="card-number">
        {{ getCardNumber(card.cardNumber)}}
      </div>
      <div class="card-footer">
        <div class="card-name">
          {{card.nameOnCard}}
        </div>
        <div class="card-date">
          {{card.expireDate}}
        </div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: "BankCard",
  props: {
    card: Object
  },
  data(){
    return{

    }
  },
  methods:{
    getCardNumber(number){
      let res = ""
      for (let i=0; i< number.length; i++){
        if (i % 4 === 0){
          res += ' '
        }
        res += number[i]
      }
      return res

    }
  }

}
</script>

<style scoped>
.image-block{
  background-image: url('../assets/img/bank-card.jpeg' );
  border-radius: 15px;
  height: 240px;
  display: flex;
  flex-direction: column;
  padding: 110px 1rem 1rem;
  justify-content: space-around;
  box-shadow: 0px 0px 24px #a8a1d9;
  margin: 1.8rem;
}

.image-block div{
  font-family: "Source Code Pro", monospace;
  color: white;
}

.card-number{
  font-size: x-large;
  text-align: center;
}

.card-footer{
  display: flex;
  justify-content: space-between;
  font-size: large;
}

</style>