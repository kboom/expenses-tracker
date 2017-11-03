'use strict';

// Modules
var webpack = require('webpack');
var autoprefixer = require('autoprefixer');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

/**
 * Env
 */
var ENV = process.env.npm_lifecycle_event;

module.exports = function makeWebpackConfig() {

    var config = {};

    config.entry = {
        app: './src/app.js'
    };

    config.output = {
        path: __dirname + '/../../../target/classes/static',
        publicPath: '/',
        filename: '[name].[hash].js',
        chunkFilename: '[name].[hash].js'
    };

    config.devtool = 'cheap-source-map';

    config.module = {
        rules: [
            {
            test: /\.js$/,
            loader: 'babel-loader',
            exclude: /node_modules/
        },
        {
            test: /\.css$/,
            loader: ExtractTextPlugin.extract({
                fallbackLoader: 'style-loader',
                loader: [
                    {loader: 'css-loader', query: {sourceMap: true}},
                    {loader: 'postcss-loader'}
                ]
            })
        },
        {
            test: /\.(png|jpg|jpeg|gif|svg|woff|woff2|ttf|eot)$/,
            loader: 'file-loader'
        },
        {
            test: /\.html$/,
            loader: 'raw-loader'
        }]
    };

    config.plugins = [
        new webpack.LoaderOptionsPlugin({
            test: /\.scss$/i,
            options: {
                postcss: {
                    plugins: [autoprefixer]
                }
            }
        })
    ];

    config.plugins.push(
        new HtmlWebpackPlugin({
            template: './src/index.html',
            inject: 'body'
        }),
        new ExtractTextPlugin({filename: 'css/[name].css', allChunks: true})
    );

    config.plugins.push(
        new webpack.optimize.UglifyJsPlugin()
    );

    return config;

}();