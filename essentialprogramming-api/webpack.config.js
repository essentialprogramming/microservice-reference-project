const path = require('path');

module.exports = [{
    entry: {
        app: [
            'babel-polyfill',
            './src/main/resources/webapp/resources/app/quiz.js'


        ]
    },
    output: {
        path: path.resolve(__dirname, './src/main/resources/webapp/resources/app'),
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {
                // Only run `.js` and `.jsx` files through Babel
                test: /\.js?$/,
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        "presets": [
                            ["@babel/preset-env", {
                                "targets": {
                                    "ie": "11"
                                },
                                "useBuiltIns": false
                            }],
                            "@babel/preset-react"
                        ]
                    }
                }]
            },
            {
                test: /\.(js|jsx)$/,
                loader: 'i18next-loader!babel-loader'
            }
        ]

    }
},

{
    entry: {
        app: [
            'regenerator-runtime',
            './src/main/resources/webapp/resources/app/quiz.js'


        ]
    },
    output: {
        filename: 'bundle.es6.js',
        path: path.resolve(__dirname, './src/main/resources/webapp/resources/app'),
    },
    module: {
        rules: [
            {
                // Only run `.js` and `.jsx` files through Babel
                test: /\.js?$/,
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        "presets": [
                            ["@babel/preset-env", {
                                "targets": {
                                    "esmodules": true,
                                }
                            }],
                            "@babel/preset-react"
                        ]
                    }
                }]
            }
        ],
    }
}
];