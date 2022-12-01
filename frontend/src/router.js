import Vue from 'vue'
import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import Profile from "@/components/Profile";
import Main from "@/components/Main";
import Exchange from "@/components/Exchange";
import Login from "@/components/Login";
import Wallets from "@/components/Wallets";
import P2P from "@/components/P2P";
import NftMarketplace from "@/components/NftMarketplace";
import P2POffers from "@/components/P2POffers";
import P2PMyTransactions from "@/components/P2PMyTransactions";
import Tutorial from "@/components/Tutorial";

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        { path: '/main', component: Main,
            children: [
                {
                    path: 'profile',
                    component: Profile,
                },
                {
                    path: 'exchange',
                    component: Exchange,
                },
                {
                    path: 'wallets',
                    component: Wallets
                },
                {
                    path: 'p2p',
                    component: P2P,
                    children: [
                        {
                            path: 'offers',
                            component: P2POffers,
                        },
                        {
                            path: 'my-transactions',
                            component: P2PMyTransactions
                        }
                    ]
                },
                {
                    path: 'nft-marketplace',
                    component: NftMarketplace,
                },
                {
                    path: 'tutorial',
                    component: Tutorial,
                }
            ],
        },
        { path: '/login', component: Login},
        {
            path: "/:catchAll(.*)",
            redirect: '/login',
        }

    ]
})

export default router