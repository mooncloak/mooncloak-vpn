config.optimization = config.optimization || {};
const TerserPlugin = require("terser-webpack-plugin");
config.optimization.minimizer = [
    new TerserPlugin({
        terserOptions: {
            compress: {
                sequences: 10
            },
        },
    }),
];
