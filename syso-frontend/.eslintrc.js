module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    "plugin:vue/vue3-essential",
    "eslint:recommended",
    "@vue/typescript/recommended",
    "plugin:prettier/recommended",
  ],
  parserOptions: {
    ecmaVersion: 2020,
  },
  rules: {
    "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off",
    "prettier/prettier": "off", // 禁用 Prettier 格式化规则
    // eslint-disable-next-line no-dupe-keys
    "no-console": "off", // 禁用 no-console 报错
    // eslint-disable-next-line no-dupe-keys
    "no-debugger": "off", // 禁用 no-debugger 报错
  },
};
