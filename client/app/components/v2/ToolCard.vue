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

      <button
        @click="isExpanded = !isExpanded"
        class="text-sm text-cyan-400 hover:text-cyan-300 mb-4 font-medium"
      >
        {{ isExpanded ? 'Collapse' : 'Expand' }} Details
      </button>

      <div v-if="isExpanded" class="space-y-4">
        <slot />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  title: string
  icon: string
  status: 'Healthy' | 'Warning' | 'Critical'
  lastRun: string
}>()

const isExpanded = ref(true)

const formattedDate = computed(() => {
  return new Date(props.lastRun).toLocaleString('en-US', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
})

const statusConfig = computed(() => {
  const configs = {
    Healthy: {
      icon: 'lucide:check-circle-2',
      color: 'text-green-400',
      bg: 'bg-green-500/10',
      border: 'border-green-500',
    },
    Warning: {
      icon: 'lucide:alert-triangle',
      color: 'text-yellow-400',
      bg: 'bg-yellow-500/10',
      border: 'border-yellow-500',
    },
    Critical: {
      icon: 'lucide:x-circle',
      color: 'text-red-400',
      bg: 'bg-red-500/10',
      border: 'border-red-500',
    },
  }
  return configs[props.status]
})
</script>
