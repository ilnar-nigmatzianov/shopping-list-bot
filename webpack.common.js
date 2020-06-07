const path = require('path');

module.exports = {
    entry: path.join(__dirname, 'src', 'main', 'resources', 'frontend', 'js', 'main.js'),
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            }
        ]
    },
    plugins: [
    ],
    resolve: {
        modules: [
            path.join(__dirname, 'src', 'main', 'resources', 'frontend', 'js'),
            path.join(__dirname, 'node_modules'),
        ],
    }
}