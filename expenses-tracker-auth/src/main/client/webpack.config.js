'use strict';

// Modules
const webpack = require('webpack');
const autoprefixer = require('autoprefixer');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

/**
 * Env
 */
const ENV = process.env.npm_lifecycle_event;

const env = process.env.NODE_ENV || 'prod';

const isProd = () => env === 'prod';


module.exports = function makeWebpackConfig() {

    const config = {};

    config.context = __dirname + "/app";

    config.entry = {
        'js/app': './app.js'
    };

    config.output = {
        path: __dirname + '/../../../target/classes/static/',
        publicPath: '/',
        filename: isProd() ? '[name].[hash].js' : '[name].js',
        sourceMapFilename: isProd() ? '[name].[hash].js.map' : '[name].js.map',
        chunkFilename: isProd() ? '[name].[hash].js' : '[name].js'
    };

    config.devtool = 'source-map';

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
            template: './index.html',
            inject: 'body'
        }),
        new ExtractTextPlugin({filename: isProd()
            ? 'css/[name].[hash].css' : 'css/[name].css', allChunks: true}
        )
    );

    config.plugins.push(
        new webpack.optimize.UglifyJsPlugin({
            sourceMap: true,
            mangle: false
        })
    );

    return config;

}();