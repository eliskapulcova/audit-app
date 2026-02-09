<template>
  <div
    class="bg-gradient-to-br from-slate-800 via-slate-800 to-slate-900 rounded-xl p-8 shadow-xl border border-slate-700"
  >
    <div class="flex flex-col lg:flex-row items-center justify-between gap-8">
      <div class="flex-1 text-center lg:text-left">
        <h2 class="text-2xl font-bold text-white mb-2">Overall Health Score</h2>
        <p class="text-slate-400 mb-6">
          Aggregated across {{ projectSummary.tools.length }} tools,
          {{ projectSummary.repositoryCount }} repositories
        </p>

        <div class="flex items-center justify-center lg:justify-start gap-4">
          <div
            :class="[
              'inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-gradient-to-r text-white font-bold text-lg',
              gradeColor,
            ]"
          >
            Grade: {{ healthScore.grade }}
          </div>

          <div
            :class="[
              'flex items-center gap-1 px-3 py-2 rounded-lg',
              healthScore.trend > 0
                ? 'bg-green-500/10 text-green-400'
                : 'bg-red-500/10 text-red-400',
            ]"
          >
            <Icon
              v-if="healthScore.trend > 0"
              name="lucide:trending-up"
              class="w-4 h-4"
            />
            <Icon v-else name="lucide:trending-down" class="w-4 h-4" />
            <span class="font-semibold"
              >{{ Math.abs(healthScore.trend) }}%</span
            >
          </div>
        </div>

        <p class="mt-4 text-sm text-slate-500">
          Previous score: {{ healthScore.previousScore }}/100
        </p>
      </div>

      <div class="relative flex items-center justify-center">
        <div class="relative w-48 h-48">
          <Chart
            type="doughnut"
            :data="chartData"
            :options="chartOptions"
            class="w-full h-full"
          />
          <div
            class="absolute inset-0 flex items-center justify-center pointer-events-none"
          >
            <div class="text-center">
              <div class="text-5xl font-bold text-white">
                {{ healthScore.score }}
              </div>
              <div class="text-sm text-slate-400">/ 100</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { HealthScore, ProjectSummary } from '../../domain/types'
import { healthGradeChartColors, healthGradeGradientColors } from '../../config/visuals'

const props = defineProps<{
  healthScore: HealthScore
  projectSummary: ProjectSummary
}>()

const gradeColor = computed(() => {
  return healthGradeGradientColors[props.healthScore.grade] || healthGradeGradientColors.default
})

const chartData = computed(() => {
  const grade = props.healthScore.grade
  const color = healthGradeChartColors[grade]

  return {
    labels: ['Score', 'Remaining'],
    datasets: [
      {
        data: [props.healthScore.score, 100 - props.healthScore.score],
        backgroundColor: [color, '#1e293b'],
        borderWidth: 0,
        hoverBackgroundColor: [color, '#1e293b'],
      },
    ],
  }
})

const chartOptions = {
  cutout: '75%',
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      enabled: false,
    },
  },
  maintainAspectRatio: false,
  responsive: true,
}

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  CategoryScale,
  DoughnutController,
} from 'chart.js'

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  ArcElement,
  CategoryScale,
  DoughnutController
)
</script>
