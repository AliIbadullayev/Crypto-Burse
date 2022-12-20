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
    faArrowDown, faCirclePlus,
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
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import Tooltip from "primevue/tooltip";
import Dropdown from "primevue/dropdown";
import Slider from "primevue/slider";
import TabMenu from "primevue/tabmenu";
import ScrollPanel from "primevue/scrollpanel";
import InputMask from "primevue/inputmask";
import Calendar from "primevue/calendar";
import axios from "axios";
import Badge from "primevue/badge";

const app = createApp(App)

library.add(faUserSecret, faUser, faUsers, faRotate, faStore, faCircleQuestion, faRightFromBracket, faDollarSign, faArrowDown, faCirclePlus)
app.use(PrimeVue).use(router)

app.component('Card', Card)
app.component('Button', Button)
app.component('Listbox', Listbox)
app.component('InputNumber', InputNumber)
app.component('InputText', InputText)
app.component('DataTable', DataTable)
app.component('Column', Column)
app.component('Dialog', Dialog)
app.component('Dropdown', Dropdown)
app.component('Slider', Slider)
app.component('TabMenu', TabMenu)
app.component('ScrollPanel', ScrollPanel)
app.component('InputMask', InputMask)
app.component('Calendar', Calendar)
app.component('Badge', Badge)
app.directive('tooltip', Tooltip)

// axios.defaults.baseURL = 'http://localhost:8080/api/v1';
axios.defaults.headers.common['Authorization'] = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2YXNzaWxpeSIsInJvbGVzIjoiUk9MRV9DTElFTlQiLCJpYXQiOjE2NzE1MTU0OTcsImV4cCI6MTY3MTU1MTQ5N30.5cQga0cIKVuyXpMXR5QQZ6qtinvNvlj5BRHKhAoTCLY'
axios.defaults.headers.common['Access-Control-Allow-Origin']= '*'
axios.defaults.headers.common['Access-Control-Allow-Methods'] = 'GET, POST, PATCH, PUT, DELETE, OPTIONS'

axios.defaults.headers.common['Access-Control-Allow-Headers'] = 'Origin, Content-Type, X-Auth-Token'

app.component('font-awesome-icon', FontAwesomeIcon)

app.mount('#app')

