<template>
  <ToolCard
    title="PHPCS"
    icon="lucide:code"
    :status="status"
    :last-run="data.lastRun"
  >
    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="text-3xl font-bold text-white mb-1">{{ data.totalViolations.toLocaleString() }}</div>
      <div class="text-sm text-slate-400">Total Violations</div>
      <div class="mt-2 text-xs text-cyan-400">Standard: {{ data.standard }}</div>
    </div>

    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="text-sm text-slate-400 mb-3">Errors vs Warnings</div>
      <div class="w-full h-[180px]">
        <Chart type="doughnut" :data="pieChartData" :options="pieChartOptions" class="w-full h-full" />
      </div>
    </div>

    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="text-sm text-slate-400 mb-3">Top Violated Standards</div>
      <div class="space-y-2">
        <div v-for="(sniff, index) in data.topSniffs" :key="sniff.name" class="flex items-center gap-3">
          <div class="flex-shrink-0 w-6 h-6 bg-cyan-500/20 rounded-full flex items-center justify-center text-xs font-bold text-cyan-400">
            {{ index + 1 }}
          </div>
          <div class="flex-1 min-w-0">
            <div class="text-sm text-slate-300 truncate font-mono">{{ sniff.name }}</div>
          </div>
          <div class="text-sm font-bold text-red-400">{{ sniff.count }}</div>
        </div>
      </div>
    </div>

    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="text-sm text-slate-400 mb-3">Files with Most Violations</div>
      <div class="space-y-2">
        <div v-for="file in data.topFiles" :key="file.file" class="flex items-start justify-between gap-2">
          <span class="text-xs text-slate-400 flex-1 truncate font-mono">{{ file.file }}</span>
          <span class="text-sm font-bold text-orange-400 flex-shrink-0">{{ file.violations }}</span>
        </div>
      </div>
    </div>
  </ToolCard>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  DoughnutController
} from 'chart.js';
import ToolCard from '../ToolCard.vue';
import type { PHPCSData } from '../../../domain/types';

ChartJS.register(Title, Tooltip, Legend, ArcElement, DoughnutController);

const props = defineProps<{
  data: PHPCSData;
}>();

const status = computed(() => {
  if (props.data.errors > 1000) return 'Critical';
  if (props.data.errors > 500) return 'Warning';
  return 'Healthy';
});

const pieChartData = computed(() => {
  return {
    labels: ['Errors', 'Warnings'],
    datasets: [
      {
        data: [props.data.errors, props.data.warnings],
        backgroundColor: ['#ef4444', '#f59e0b'],
        borderWidth: 0,
      }
    ]
  };
});

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
      }
    },
    tooltip: {
      backgroundColor: '#1e293b',
      titleColor: '#94a3b8',
      bodyColor: '#f8fafc',
      padding: 10,
      displayColors: true,
    }
  },
  cutout: '60%',
};
</script>
