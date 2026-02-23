<template>
  <div class="bg-slate-900 p-4 rounded-lg">
    <div class="text-3xl font-bold text-white mb-1">
      {{ totalViolations.toLocaleString() }}
    </div>
    <div class="text-sm text-slate-400">{{ title }}</div>
    <slot name="additionalInfo" />
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  title: string
  totalViolations: number
}>()
</script>
