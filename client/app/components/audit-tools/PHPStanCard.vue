<template>
  <ToolCard
    title="PHPStan"
    icon="lucide:bug"
    :status="data.healthStatus"
    :last-run="data.lastRun"
  >
    <template #essentials>
      <div class="bg-slate-900 p-4 rounded-lg">
        <div class="text-3xl font-bold text-white mb-1">
          {{ data.totalErrors }}
        </div>
        <div class="text-sm text-slate-400">Total Errors</div>
        <div class="mt-2 flex items-center gap-2">
          <span class="text-xs text-slate-500">Analysis Level:</span>
          <span
            class="px-2 py-1 bg-cyan-500/20 text-cyan-400 text-xs font-bold rounded"
          >
            Level {{ data.level }}
          </span>
        </div>
      </div>
    </template>

    <template #details>
      <div class="bg-slate-900 p-4 rounded-lg">
        <div class="text-sm text-slate-400 mb-3">Errors by Level</div>
        <div class="w-full h-[180px]">
          <Chart
            type="bar"
            :data="barChartData"
            :options="barChartOptions"
            class="w-full h-full"
          />
        </div>
      </div>

      <FindingsTrend
        v-if="data.trendData.length >= TREND_DATA_MIN_POINTS"
        :trend-data="data.trendData"
      />

      <ViolationCategories
        title="Top Error Categories"
        :data="data.topCategories"
      />

      <TopFiles
        v-if="data.topFiles.length > 0"
        title="Files with Most Violations"
        :data="data.topFiles"
      />
    </template>
  </ToolCard>
</template>

<script setup lang="ts">
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
} from 'chart.js'
import ToolCard from '../ToolCard.vue'
import TotalFindings from './details/TotalFindings.vue'
import ViolationCategories from './details/ViolationCategories.vue'
import FindingsTrend from './details/FindingsTrend.vue'
import TopFiles from './details/TopFiles.vue'
import type { PHPStanData } from '../../domain/types'
import { TREND_DATA_MIN_POINTS } from '../../config/general'

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement
)

const props = defineProps<{
  data: PHPStanData
}>()

const barChartData = computed(() => {
  return {
    labels: props.data.errorsByLevel.map((d) => d.level),
    datasets: [
      {
        data: props.data.errorsByLevel.map((d) => d.count),
        backgroundColor: '#06b6d4',
        borderRadius: 4,
        barThickness: 20,
      },
    ],
  }
})

const barChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: '#1e293b',
      titleColor: '#94a3b8',
      bodyColor: '#f8fafc',
      padding: 10,
      displayColors: false,
    },
  },
  scales: {
    x: {
      grid: { display: false },
      ticks: { color: '#64748b' },
    },
    y: {
      grid: { color: '#334155' },
      ticks: { color: '#64748b' },
    },
  },
}

const lineChartData = computed(() => {
  return {
    labels: props.data.trendData.map((_, i) => i + 1),
    datasets: [
      {
        data: props.data.trendData,
        borderColor: '#ef4444',
        borderWidth: 2,
        pointBackgroundColor: '#ef4444',
        pointRadius: 3,
        tension: 0.4,
      },
    ],
  }
})

const lineChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: '#1e293b',
      titleColor: '#94a3b8',
      bodyColor: '#f8fafc',
      padding: 10,
      displayColors: false,
    },
  },
  scales: {
    x: {
      grid: { display: false },
      ticks: { color: '#64748b' },
    },
    y: {
      grid: { color: '#334155' },
      ticks: { color: '#64748b' },
    },
  },
}
</script>
