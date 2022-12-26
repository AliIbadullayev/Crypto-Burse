import {createRouter, createWebHistory} from 'vue-router'
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
import WalletReplenish from "@/components/WalletReplenish";
import WalletTransaction from "@/components/WalletTransaction";
import WalletStacking from "@/components/WalletStacking";
import WalletHistory from "@/components/WalletHistory";
import SignIn from "@/components/SignIn";
import SignUp from "@/components/SignUp";
import Admin from "@/components/Admin";
import store from "@/store";
import NotFound from "@/components/NotFound";

const router = createRouter({
    history: createWebHistory(),
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
                    component: Wallets,
                    children: [
                        {
                            path: 'replenish',
                            component: WalletReplenish,
                        },
                        {
                            path: 'transaction',
                            component: WalletTransaction,
                        },
                        {
                            path: 'stacking',
                            component: WalletStacking,
                        },
                        {
                            path: 'history',
                            component: WalletHistory,
                        }
                    ]
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
                },
                {
                    path: 'admin',
                    component: Admin,
                }
            ],
        },
        { path: '/login', component: Login, children:
            [
                {
                    path: 'signIn',
                    component: SignIn,
                },
                {
                    path: 'signUp',
                    component: SignUp,
                }
            ]
        },
        { path: '/notfound', component: NotFound}
    ]
})

router.beforeEach((to, from, next) => {
    const publicPages = ['/login/signIn', '/login/signUp'];
    const authRequired = !publicPages.includes(to.path);
    const loggedIn = store.state.status.loggedIn;

    // trying to access a restricted page + not logged in
    // redirect to login page

    if (authRequired && !loggedIn) {
        next('/login/signIn');
    } else {
        next();
    }
});
export default router