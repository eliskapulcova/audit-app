<template>
  <div class="bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700">
    <div
      class="flex flex-col sm:flex-row items-start sm:items-center justify-between mb-6 gap-4"
    >
      <h2 class="text-xl font-bold text-white">Trend Overview</h2>

      <div class="flex flex-wrap gap-3">
        <button
          v-for="tool in tools"
          :key="tool.key"
          @click="toggleTool(tool.key)"
          :class="[
            'flex items-center gap-2 px-3 py-1 rounded-lg transition-all',
            visibleTools[tool.key]
              ? 'bg-slate-700 opacity-100'
              : 'bg-slate-900 opacity-50',
          ]"
        >
          <div
            class="w-3 h-3 rounded-full"
            :style="{ backgroundColor: tool.color }"
          />
          <span class="text-sm text-slate-300">{{ tool.name }}</span>
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
import { ref, computed } from 'vue'
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
import type { TrendDataPoint } from '../../domain/types'

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

const tools = [
  { key: 'sonarqube', name: 'SonarQube', color: '#06b6d4' },
  { key: 'semgrep', name: 'Semgrep', color: '#10b981' },
  { key: 'phpcs', name: 'PHPCS', color: '#f59e0b' },
  { key: 'phpstan', name: 'PHPStan', color: '#ef4444' },
] as const

const visibleTools = ref<Record<string, boolean>>({
  sonarqube: true,
  semgrep: true,
  phpcs: true,
  phpstan: true,
})

const toggleTool = (key: string) => {
  visibleTools.value[key] = !visibleTools.value[key]
}

const chartData = computed(() => {
  return {
    labels: props.data.map((d) => d.date),
    datasets: [
      {
        label: 'SonarQube',
        data: props.data.map((d) => d.sonarqube),
        borderColor: '#06b6d4',
        backgroundColor: '#06b6d4',
        tension: 0.4,
        pointBackgroundColor: '#06b6d4',
        pointRadius: 4,
        pointHoverRadius: 6,
        hidden: !visibleTools.value.sonarqube,
      },
      {
        label: 'Semgrep',
        data: props.data.map((d) => d.semgrep),
        borderColor: '#10b981',
        backgroundColor: '#10b981',
        tension: 0.4,
        pointBackgroundColor: '#10b981',
        pointRadius: 4,
        pointHoverRadius: 6,
        hidden: !visibleTools.value.semgrep,
      },
      {
        label: 'PHPCS',
        data: props.data.map((d) => d.phpcs),
        borderColor: '#f59e0b',
        backgroundColor: '#f59e0b',
        tension: 0.4,
        pointBackgroundColor: '#f59e0b',
        pointRadius: 4,
        pointHoverRadius: 6,
        hidden: !visibleTools.value.phpcs,
      },
      {
        label: 'PHPStan',
        data: props.data.map((d) => d.phpstan),
        borderColor: '#ef4444',
        backgroundColor: '#ef4444',
        tension: 0.4,
        pointBackgroundColor: '#ef4444',
        pointRadius: 4,
        pointHoverRadius: 6,
        hidden: !visibleTools.value.phpstan,
      },
    ],
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
