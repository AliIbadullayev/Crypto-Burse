// const { defineConfig } = require('@vue/cli-service')
module.exports = {
  // https://cli.vuejs.org/config/#devserver-proxy
  devServer: {
    proxy: {
      '/api/v1': {
        target: 'http://localhost:8080',
        ws: true,
        changeOrigin: true
      }
    }
  }
}
