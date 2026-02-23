<template>
  <ToolCard
    title="Semgrep"
    icon="lucide:shield"
    :status="data.healthStatus"
    :last-run="data.lastRun"
  >
    <template #essentials>
      <TotalFindings
        :total-violations="data.totalFindings"
        title="Total Findings"
      />

      <div class="flex flex-row flex-wrap gap-4">
        <div class="flex-1 bg-slate-900 p-3 rounded-lg border-red-500">
          <div class="text-2xl font-bold text-red-400">{{ data.error }}</div>
          <div class="text-xs text-slate-400">Error</div>
        </div>
        <div class="flex-1 bg-slate-900 p-3 rounded-lg border-yellow-500">
          <div class="text-2xl font-bold text-yellow-400">
            {{ data.warning }}
          </div>
          <div class="text-xs text-slate-400">Warning</div>
        </div>
        <div class="flex-1 bg-slate-900 p-3 rounded-lg border-blue-500">
          <div class="text-2xl font-bold text-blue-400">{{ data.info }}</div>
          <div class="text-xs text-slate-400">Info</div>
        </div>
      </div>
    </template>

    <template #details>
      <FindingsTrend
        v-if="data.trendData.length >= TREND_DATA_MIN_POINTS"
        :trend-data="data.trendData"
      />

      <ViolationCategories
        title="Top Rule Categories"
        :data="data.topCategories"
      />

      <TopFiles
        v-if="data.topFiles.length > 0"
        title="Files with Most Violations"
        :data="data.topFiles"
      />

      <div
        v-if="data.owaspCoverage != null"
        class="bg-slate-900 p-4 rounded-lg"
      >
        <div class="flex items-center justify-between">
          <span class="text-sm text-slate-400">OWASP Top 10 Coverage</span>
          <span class="text-xl font-bold text-green-400"
            >{{ data.owaspCoverage }}%</span
          >
        </div>
        <div class="mt-2 h-2 bg-slate-700 rounded-full overflow-hidden">
          <div
            class="h-full bg-gradient-to-r from-green-500 to-cyan-500"
            :style="{ width: `${data.owaspCoverage}%` }"
          />
        </div>
      </div>
    </template>
  </ToolCard>
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
import ToolCard from '../ToolCard.vue'
import TotalFindings from './details/TotalFindings.vue'
import ViolationCategories from './details/ViolationCategories.vue'
import FindingsTrend from './details/FindingsTrend.vue'
import TopFiles from './details/TopFiles.vue'
import { TREND_DATA_MIN_POINTS } from '../../config/general'
import type { SemgrepData } from '../../domain/types'

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
  data: SemgrepData
}>()

const lineChartData = computed(() => {
  return {
    labels: props.data.trendData.map((_, i) => i + 1),
    datasets: [
      {
        data: props.data.trendData,
        borderColor: '#06b6d4',
        borderWidth: 2,
        pointBackgroundColor: '#06b6d4',
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
