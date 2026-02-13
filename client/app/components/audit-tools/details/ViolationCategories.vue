<template>
    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="text-sm text-slate-400 mb-3">{{ title }}</div>
      <div class="space-y-2">
        <div
          v-for="category in data"
          :key="category.name"
          class="flex items-center justify-between"
        >
          <span class="text-sm text-slate-300 font-mono">{{
            category.name
          }}</span>
          <span class="text-sm font-bold text-cyan-400">{{
            category.count
          }}</span>
        </div>
      </div>
    </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  title: string
  data: { name: string; count: number }[]
}>()
</script>