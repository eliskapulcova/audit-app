<template>
    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="text-sm text-slate-400 mb-3">{{ title }}</div>
      <div class="space-y-2">
        <div
          v-for="file in data"
          :key="file.file"
          class="flex items-start justify-between gap-2"
        >
          <span class="text-xs text-slate-400 flex-1 truncate font-mono">{{
            file.file
          }}</span>
          <span class="text-sm font-bold text-orange-400 flex-shrink-0">{{
            file.violations
          }}</span>
        </div>
      </div>
    </div>
</template>

<script setup lang="ts">
const props = defineProps<{
    title: string
    data: { file: string; violations: number }[]
}>()
</script>