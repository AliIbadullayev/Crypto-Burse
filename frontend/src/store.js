
import { createStore } from 'vuex'
import router from "@/router";

// Create a new store instance.
const store = createStore({
    state: {
        user: {
            role: "",
            token: "",
            username: ""
        }
    },
    getters: {
        user: (state => {
            return state.user;
        })
    },
    actions: {
        user(context, user){
            context.commit('user', user)
        }
    },
    mutations: {
        user(state, user){
            state.user = user;
        }
    },

})

export default store