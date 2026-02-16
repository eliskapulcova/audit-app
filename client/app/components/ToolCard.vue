<template>
  <div
    class="bg-slate-800 rounded-xl shadow-lg border border-slate-700 hover:border-slate-600 transition-all overflow-hidden"
  >
    <div class="p-6">
      <div class="flex items-start justify-between mb-4">
        <div class="flex items-center gap-3">
          <div class="p-2 bg-slate-700 rounded-lg">
            <Icon :name="icon" class="w-6 h-6 text-white" />
          </div>
          <div>
            <h3 class="text-lg font-bold text-white">{{ title }}</h3>
            <div class="flex items-center gap-1 text-xs text-slate-400 mt-1">
              <Icon name="lucide:clock" class="w-3 h-3" />
              <span>{{ formattedDate }}</span>
            </div>
          </div>
        </div>

        <div
          v-if="status"
          :class="[
            'flex items-center gap-2 px-3 py-1 rounded-full border',
            statusConfig.bg,
            statusConfig.border,
          ]"
        >
          <Icon
            :name="statusConfig.icon"
            :class="['w-4 h-4', statusConfig.color]"
          />
          <span :class="['text-sm font-medium', statusConfig.color]">{{
            status
          }}</span>
        </div>
      </div>

      <div class="space-y-4">
        <slot name="essentials" />
      </div>

      <button
        @click="isExpanded = !isExpanded"
        class="text-sm text-cyan-400 hover:text-cyan-300 mb-4 font-medium my-4"
      >
        {{ isExpanded ? 'Collapse' : 'Expand' }} Details
      </button>

      <div v-if="isExpanded" class="space-y-4">
        <slot name="details" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { TOOL_CARDS_EXPANDED_DEFAULT } from '~/config/general';
import { toolHealthStatuses } from '~/config/visuals'
import type { HealthStatus } from '~/domain/types'

const props = defineProps<{
  title: string
  icon: string
  status: HealthStatus | null
  lastRun: string
}>()

const isExpanded = ref<boolean>(TOOL_CARDS_EXPANDED_DEFAULT)

const formattedDate = computed(() => {
  return new Date(props.lastRun).toLocaleString('en-US', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
})

const statusConfig = computed(() => {
  if (!props.status) {
    return toolHealthStatuses['healthy']
  }
  return toolHealthStatuses[props.status]
})
</script>
