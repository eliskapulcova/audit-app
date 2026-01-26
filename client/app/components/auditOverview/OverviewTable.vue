<template>
  <h3 class="text-xl font-bold mx-5 my-3">{{ title }}</h3>
  <table class="w-full px-5 my-3 border-separate border-spacing-x-4">
    <tbody>
      <tr v-for="item in data" :key="item.key">
        <td class="font-bold">{{ item.key }}</td>
        <td class="pl-2 min-w-[40%]">{{ item.value }}</td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true,
  },
  data: {
    type: Array,
    required: true,
  },
});
</script>
