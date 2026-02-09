<template>
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
    <div
      v-for="kpi in kpis"
      :key="kpi.id"
      class="bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700 hover:border-cyan-500 transition-all hover:shadow-cyan-500/10"
    >
      <div class="flex items-start justify-between mb-4">
        <div class="p-2 bg-slate-700 rounded-lg">
          <Icon
            :name="`lucide:${kpiIconName(kpi.icon)}`"
            class="w-5 h-5 text-cyan-400"
          />
        </div>
        <div
          :class="[
            'flex items-center gap-1 text-sm',
            kpi.trend > 0 ? 'text-red-400' : 'text-green-400',
          ]"
        >
          <Icon
            v-if="kpi.trend > 0"
            name="lucide:trending-up"
            class="w-4 h-4"
          />
          <Icon v-else name="lucide:trending-down" class="w-4 h-4" />
          <span class="font-semibold">{{ Math.abs(kpi.trend) }}%</span>
        </div>
      </div>

      <div class="mb-2">
        <div class="text-3xl font-bold text-white mb-1">{{ kpi.value }}</div>
        <div class="text-sm text-slate-400">{{ kpi.label }}</div>
      </div>

      <div class="h-12 -mx-2">
        <Chart
          type="line"
          :data="getSparklineData(kpi.sparklineData)"
          :options="sparklineOptions"
          class="w-full h-full"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { KPI } from '../../domain/types'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js'

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
)

defineProps<{
  kpis: KPI[]
}>()

// Helper to convert PascalCase icon names (from React) to kebab-case (for Iconify) if necessary
// Or just handle the mapping. The React version imported specific icons.
// Assuming the backend provides icon names like "AlertTriangle", "Clock", etc.
const kpiIconName = (icon: string) => {
  // Simple conversion or mapping if needed.
  // Lucide icons in Iconify are typically kebab-case, e.g. "alert-triangle".
  // The React component used specific imports, so `kpi.icon` is likely a string like "AlertTriangle".
  return icon.replace(/([a-z0-9])([A-Z])/g, '$1-$2').toLowerCase()
}

const getSparklineData = (data: number[]) => {
  return {
    labels: data.map((_, i) => i), // Dummy labels
    datasets: [
      {
        data: data,
        borderColor: '#06b6d4', // cyan-500
        borderWidth: 2,
        pointRadius: 0,
        fill: false,
        tension: 0.4, // 'monotone' equivalent
      },
    ],
  }
}

const sparklineOptions = {
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      enabled: false,
    },
  },
  scales: {
    x: {
      display: false,
    },
    y: {
      display: false,
    },
  },
  maintainAspectRatio: false,
  responsive: true,
}
</script>
