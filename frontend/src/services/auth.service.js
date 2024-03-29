import axios from "axios";

class AuthService{
    signIn(signinForm){
        return axios.post('/api/v1/auth/login', signinForm)
            .then((resp) => {
                if (resp.data.token != null) localStorage.setItem('user', JSON.stringify(resp.data))
                return resp.data
            })
    }

    logout(){
        localStorage.removeItem('user')
    }

    signUp(signupForm){
        return axios.post('/api/v1/auth/register', signupForm)
    }

    loginOAuth(user){
        if (user != null) localStorage.setItem('user', JSON.stringify(user))
        return user
    }
}

export default new AuthService()