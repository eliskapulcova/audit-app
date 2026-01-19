import Aura from "@primeuix/themes/aura";

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2025-07-15",
  devtools: { enabled: true },

  modules: [
    "@nuxt/eslint",
    "@nuxt/fonts",
    "@nuxtjs/tailwindcss",
    "@primevue/nuxt-module",
  ],
  css: ["primeicons/primeicons.css"],
  primevue: {
    options: {
      theme: {
        preset: Aura,
      },
    },
  },
  runtimeConfig: {
    public: {
      apiBase: "http://localhost:8080", // Default value, overridden by NUXT_PUBLIC_API_BASE
    },
  },
});
