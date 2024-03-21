/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{js,jsx,ts,tsx,html,css}'],
  theme: {
    extend: {},
  },
  plugins: [require('daisyui'),],
  daisyui: {
    themes: ["cupcake", "dark", "cmyk"],
    darkTheme: "dark",
    base: true,
    styled: true,
    utils: true,
    logs: true,
    themeRoot: ":root",
  },
};
