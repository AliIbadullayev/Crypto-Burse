const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 7766,
    proxy: {
      '/api/v1': {
        target: 'http://localhost:8091/',
        changeOrigin: true
      },
    }
  }
})
