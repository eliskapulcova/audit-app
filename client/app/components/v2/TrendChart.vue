<template>
  <div class="bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700">
    <div
      class="flex flex-col sm:flex-row items-start sm:items-center justify-between mb-6 gap-4"
    >
      <h2 class="text-xl font-bold text-white">Trend Overview</h2>

      <div class="flex flex-wrap gap-3">
        <button
          v-for="tool in tools"
          :key="tool"
          @click="toggleTool(tool)"
          :class="[
            'flex items-center gap-2 px-3 py-1 rounded-lg transition-all',
            visibleTools[tool]
              ? 'bg-slate-700 opacity-100'
              : 'bg-slate-900 opacity-50',
          ]"
        >
          <div
            class="w-3 h-3 rounded-full"
            :style="{ backgroundColor: trendToolColors[tool] }"
          />
          <span class="text-sm text-slate-300">{{ tool }}</span>
        </button>
      </div>
    </div>

    <div class="w-full h-[300px]">
      <Chart
        type="line"
        :data="chartData"
        :options="chartOptions"
        class="w-full h-full"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
} from 'chart.js'
import type { Tool, TrendDataPoint } from '../../domain/types'
import { tools } from '../../domain/types'
import { trendToolColors } from '~/config/visuals';


ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement
)

const props = defineProps<{
  data: TrendDataPoint[]
}>()

const visibleTools = ref<Record<string, boolean>>(Object.fromEntries(tools.map((tool) => [tool, true])))

const toggleTool = (tool: Tool) => {
  visibleTools.value[tool] = !visibleTools.value[tool]
}

const chartData = computed(() => {
  return {
    labels: props.data.map((d) => d.date),
    datasets: tools.map((tool) => {
      return {
        label: tool,
        data: props.data.map((d) => d.tools[tool]),
        borderColor: trendToolColors[tool],
        backgroundColor: trendToolColors[tool],
        tension: 0.4,
        pointBackgroundColor: trendToolColors[tool],
        pointRadius: 4,
        pointHoverRadius: 6,
        hidden: !visibleTools.value[tool],
      }
    })
  }
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: true,
      labels: {
        color: '#94a3b8',
      },
    },
    tooltip: {
      backgroundColor: '#1e293b',
      titleColor: '#f8fafc',
      bodyColor: '#f8fafc',
      borderColor: '#334155',
      borderWidth: 1,
      padding: 10,
      displayColors: true,
    },
  },
  scales: {
    x: {
      grid: {
        color: '#334155',
      },
      ticks: {
        color: '#94a3b8',
      },
    },
    y: {
      grid: {
        color: '#334155',
      },
      ticks: {
        color: '#94a3b8',
      },
    },
  },
}
</script>
