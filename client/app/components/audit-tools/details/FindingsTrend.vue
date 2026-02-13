<template>
  <div class="bg-slate-900 p-4 rounded-lg">
    <div class="text-sm text-slate-400 mb-3">
      Findings Trend (Last {{ trendData.length }} Scans)
    </div>
    <div class="w-full h-[120px]">
      <Chart
        type="line"
        :data="lineChartData"
        :options="lineChartOptions"
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
import { TREND_DATA_MAX_POINTS } from '../../../config/general'

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
  trendData: number[]
}>()

const trendData = computed(() => {
  return props.trendData.slice(props.trendData.length - TREND_DATA_MAX_POINTS)
})

const lineChartData = computed(() => {
  return {
    labels: trendData.value.map((_, i) => i + 1),
    datasets: [
      {
        data: trendData.value,
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
