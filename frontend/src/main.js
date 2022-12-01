import { createApp } from 'vue'
import App from './App.vue'
import PrimeVue from 'primevue/config';
import router from "@/router";

import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import Card from "primevue/card";

import 'primevue/resources/themes/saga-blue/theme.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';
import Button from "primevue/button";

import {
    faArrowDown,
    faCircleQuestion, faDollarSign,
    faRightFromBracket,
    faRotate,
    faStore,
    faUser,
    faUsers,
    faUserSecret
} from '@fortawesome/free-solid-svg-icons'
import Listbox from "primevue/listbox";
import InputNumber from "primevue/inputnumber";

const app = createApp(App)

library.add(faUserSecret, faUser, faUsers, faRotate, faStore, faCircleQuestion, faRightFromBracket, faDollarSign, faArrowDown)
app.use(PrimeVue).use(router)

app.component('Card', Card)
app.component('Button', Button)
app.component('Listbox', Listbox)
app.component('InputNumber', InputNumber)

app.component('font-awesome-icon', FontAwesomeIcon)

app.mount('#app')

