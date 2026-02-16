<template>
  <ToolCard
    title="PHPCS"
    icon="lucide:code"
    :status="data.healthStatus"
    :last-run="data.lastRun"
  >
  <template #essentials>
    <TotalFindings :total-violations="data.totalViolations" title="Total Violations">
      <template #additionalInfo>
        <div class="mt-2 text-xs text-cyan-400">
          Standard: {{ data.standard }}
        </div>
      </template>
    </TotalFindings>

    <div class="flex flex-row flex-wrap gap-4">
      <div class="flex-1 bg-slate-900 p-3 rounded-lg border-red-500">
        <div class="text-2xl font-bold text-red-400">{{ data.errors }}</div>
        <div class="text-xs text-slate-400">Error</div>
      </div>
      <div class="flex-1 bg-slate-900 p-3 rounded-lg border-yellow-500">
        <div class="text-2xl font-bold text-yellow-400">{{ data.warnings }}</div>
        <div class="text-xs text-slate-400">Warning</div>
      </div>
    </div>
  </template>

  <template #details>
    <FindingsTrend v-if="data.trendData.length >= TREND_DATA_MIN_POINTS" :trend-data="data.trendData" />

    <ViolationCategories title="Top Violated Standards" :data="data.topSniffs" />

    <TopFiles v-if="data.topFiles.length > 0" title="Files with Most Violations" :data="data.topFiles" />
  </template>
  </ToolCard>
</template>

<script setup lang="ts">
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  DoughnutController,
} from 'chart.js'
import ToolCard from '../ToolCard.vue'
import TotalFindings from './details/TotalFindings.vue'
import FindingsTrend from './details/FindingsTrend.vue'
import ViolationCategories from './details/ViolationCategories.vue'
import TopFiles from './details/TopFiles.vue'
import type { PHPCSData } from '../../domain/types'
import { TREND_DATA_MIN_POINTS } from '../../config/general'

ChartJS.register(Title, Tooltip, Legend, ArcElement, DoughnutController)

const props = defineProps<{
  data: PHPCSData
}>()

const pieChartData = computed(() => {
  return {
    labels: ['Errors', 'Warnings'],
    datasets: [
      {
        data: [props.data.errors, props.data.warnings],
        backgroundColor: ['#ef4444', '#f59e0b'],
        borderWidth: 0,
      },
    ],
  }
})

const pieChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'bottom' as const,
      labels: {
        color: '#94a3b8',
        usePointStyle: true,
        padding: 20,
      },
    },
    tooltip: {
      backgroundColor: '#1e293b',
      titleColor: '#94a3b8',
      bodyColor: '#f8fafc',
      padding: 10,
      displayColors: true,
    },
  },
  cutout: '60%',
}
</script>
