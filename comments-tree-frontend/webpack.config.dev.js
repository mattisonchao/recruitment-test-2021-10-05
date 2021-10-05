const base = require('./webpack.config');


module.exports = Object.assign({}, base, {
    mode: "development",
    devServer: {
        port:8888,
        proxy: {
            '/api': {
                target: 'http://127.0.0.1:7000',
                changeOrigin: true,     // target是域名的话，需要这个参数，
                secure: false,          // 设置支持https协议的代理
            },
        },
        historyApiFallback: true
    }
});