import { createApp } from 'vue'
import App from './App.vue'
import PrimeVue from 'primevue/config';
import router from "@/router";
import { library } from '@fortawesome/fontawesome-svg-core'

import './assets/styles/style.css'
import 'primevue/resources/themes/saga-blue/theme.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';

import Button from "primevue/button";

import {
    faArrowDown, faCirclePlus,
    faCircleQuestion, faClockRotateLeft, faDollarSign,
    faRightFromBracket,
    faRotate,
    faStore,
    faUser,
    faUsers,
    faUserSecret
} from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import Carousel from "primevue/carousel";
import Card from "primevue/card";
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
import Badge from "primevue/badge";
import Password from "primevue/password";


import store from "@/store";
import {Mixin} from "@/mixin";
const app = createApp(App)

library.add(faUserSecret, faUser, faUsers, faRotate, faStore, faCircleQuestion, faRightFromBracket, faDollarSign, faArrowDown, faCirclePlus, faClockRotateLeft)
app.use(PrimeVue).use(router).use(store)

app.component('Carousel', Carousel)
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
app.component('Password', Password)
app.component('font-awesome-icon', FontAwesomeIcon)


app.directive('tooltip', Tooltip)
app.mixin(Mixin)
app.mount('#app')

