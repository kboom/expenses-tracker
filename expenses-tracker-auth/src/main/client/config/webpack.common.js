var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var helpers = require('./helpers');

module.exports = {

    entry: {
        'js/polyfills': './src/polyfills.ts',
        'js/vendor': './src/vendor.ts',
        'js/app': './src/main.ts'
    },

    resolve: {
        extensions: ['.ts', '.js'],
        modules: [helpers.root('src'), helpers.root('node_modules')],
    },

    module: {
        rules: [
            {
                test: /\.ts$/,
                loaders: [
                    {
                        loader: 'awesome-typescript-loader',
                        options: {configFileName: helpers.root('src', 'tsconfig.json')}
                    }, 'angular2-template-loader'
                ]
            },
            {
                test: /\.html$/,
                loader: 'html-loader'
            },
            {
                test: /\.(png|jpe?g|gif|svg|woff|woff2|ttf|eot|ico)$/,
                loader: 'file-loader?name=assets/[name].[hash].[ext]'
            },

            /**
             * To string and css loader support for *.css files (from Angular components)
             * Returns file content as string
             *
             */
            {
                test: /\.css$/,
                use: ['to-string-loader', 'css-loader'],
                exclude: [helpers.root('src', 'styles')]
            },

            /**
             * To string and sass loader support for *.scss files (from Angular components)
             * Returns compiled css content as string
             *
             */
            {
                test: /\.scss$/,
                use: ['to-string-loader', 'css-loader', 'sass-loader'],
                exclude: [helpers.root('src', 'styles')]
            },

            /**
             * Extract CSS files from .src/styles directory to external CSS file.
             * Useful for extracting themes.
             */
            {
                test: /\.css$/,
                loader: ExtractTextPlugin.extract({
                    fallback: 'style-loader',
                    use: 'css-loader'
                }),
                include: [helpers.root('src', 'styles')]
            },

            /**
             * Extract and transpile SCSS files from .src/styles directory to external CSS file.
             * Useful for extracting themes.
             */
            {
                test: /\.scss$/,
                loader: ExtractTextPlugin.extract({
                    fallback: 'style-loader',
                    use: 'css-loader!sass-loader'
                }),
                include: [helpers.root('src', 'styles')]
            },

        ]
    },

    plugins: [
        // Workaround for angular/angular#11580
        new webpack.ContextReplacementPlugin(
            // The (\\|\/) piece accounts for path separators in *nix and Windows
            /angular(\\|\/)core(\\|\/)@angular/,
            helpers.root('./src'), // location of your src
            {} // a map of your routes
        ),

        new webpack.optimize.CommonsChunkPlugin({
            name: ['js/app', 'js/vendor', 'js/polyfills']
        }),

        new HtmlWebpackPlugin({
            template: 'src/index.html'
        }),

        new CopyWebpackPlugin([
            {from: 'src/assets/i18n/', to: 'assets/i18n'}
        ]),
    ]
};