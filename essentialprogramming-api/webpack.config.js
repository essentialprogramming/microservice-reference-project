const path = require('path');

module.exports = [
    /*
    {
        experiments:
            {
                backCompat: true
            }
    },
    */
    {
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
        devtool: 'source-map',
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
                            ],
                            "plugins": [
                                ["@babel/plugin-proposal-class-properties", {"loose": true}],
                                ['@babel/plugin-proposal-private-methods', {loose: true}],
                                ['@babel/plugin-proposal-private-property-in-object', {loose: true}]
                            ]
                        }
                    }]
                },
                {
                    test: /\.(js|jsx)$/,
                    resolve: {
                        fullySpecified: false
                    },
                    use: ['i18next-loader', 'babel-loader']
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
        devtool: 'source-map',
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
                            ],
                            "plugins": [
                                ["@babel/plugin-proposal-class-properties", {"loose": true}],
                                ['@babel/plugin-proposal-private-methods', {loose: true}],
                                ['@babel/plugin-proposal-private-property-in-object', {loose: true}],
                                ['@babel/plugin-transform-runtime', {useESModules: true}]
                            ]
                        }
                    }]
                },
                {
                    test: /\.(js|jsx)$/,
                    resolve: {
                        fullySpecified: false
                    },
                    use: ['i18next-loader', 'babel-loader']
                }
            ],

        }
    }
];