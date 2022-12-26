import axios from "axios";

class AuthService{
    signIn(signinForm){
        return axios.post('/api/v1/auth/login', signinForm)
            .then((resp) => {
                if (resp.data.token != null) localStorage.setItem('user', JSON.stringify(resp.data))
                return resp.data
            })
        // this.user = response.data
        // localStorage.setItem('jwt', this.user.token)
        // localStorage.setItem('role', this.user.role)
        // // this.$store.dispatch('user', this.user)
        // if (this.user.role === "ROLE_CLIENT")
        //     this.$router.push('/main/profile')
        // else
        //     this.$router.push('/main/admin')
    }

    logout(){
        localStorage.removeItem('user')
    }

    signUp(signupForm){
        return axios.post('/api/v1/auth/register', signupForm)
    }
}

export default new AuthService()